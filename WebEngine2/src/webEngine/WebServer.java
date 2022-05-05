package webEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nanoHTTPD.NanoHTTPD;
import nanoHTTPD.util.ServerRunner;
import userEngine.User;
import userEngine.UserDirectory;
import webEngine.pages.Home;

public class WebServer extends NanoHTTPD {

//	private HashMap<URL, WebPage> pages;
	private ArrayList<WebPage> pages;
	
	private UserDirectory users;
	
	public WebServer() {
		super(8080);
//		pages = new HashMap<URL, WebPage>();
		pages = new ArrayList<WebPage>();
		WebPage p = new WebPage(new URL(""), false) {

			@Override
			public WebAction validate(URL path, User u) {
				return WebAction.RedirectT("/home");
			}

			@Override
			public Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u) {
				return null;
			}
			
		};
		addPage(p);
		addPage(new Home());
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		HashMap<String, List<String>> params = (HashMap<String, List<String>>) decodeParameters(session.getQueryParameterString());
		URL url = new URL(session.getUri().substring(1));
		CookieHandler cookies = session.getCookies();
		Method method = session.getMethod();
		String postData = "";
		
//		System.out.println("http://localhost:1080/" + printPath(path) + ";\t Method=" + method);
		
		User user = null;
		String userKeyCookie = cookies.read("userKey");
		if(userKeyCookie != null) {
			user = users.getUserByKey(userKeyCookie);
			if(user == null) {
				cookies.set("userKey", "-Delete-", 1);
				cookies.set("user", "-Delete-", 1);
			}
		}
		WebPage page = getPage(url);
		if(page != null) {
			WebAction act = page.canGo(url, user);
			if(act.act == WebAction.Act.OK) {
				try {
					return page.serve(url, method, params, postData, user);
				} catch(Exception e) {
					e.printStackTrace();
					return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_HTML, PageLoader.getDefaultPage("Failed to server page, internal error"));
				}
			} else if(act.act == WebAction.Act.BLOCK) {
				return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_HTML, PageLoader.getDefaultPage("You can not acces this page because " + act.data));
			} else if(act.act == WebAction.Act.REDIRECT_TEMP) {
				return redirectResponse(act.data);
			} else if(act.act == WebAction.Act.REDIRECT_LOGIN) {
				return redirectLogin(url);
			}
		}
		return missingPage(url);
	}
	
	public WebPage getPage(URL url) {
		boolean first = true;
		for(int i = url.path.length-1; i >= 0; i--) {
			for(WebPage p : pages) {
				if(p.getURL().equals(url,i)) {
					if(first || p.getIdPath() == i+1) {
						return p;
					}
				}
			}
			first = false;
		}
		return null;
	}
	
	public static Response missingPage(URL url) {
		String page = PageLoader.getPage("404.html");
		page = page.replace("%Path%", url.toString());
		return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML, page);
	}
	
	public static Response redirectResponse(String dest) {
		Response r = NanoHTTPD.newFixedLengthResponse(Response.Status.TEMPORARY_REDIRECT, "text/plain", "");
		r.addHeader("Location", dest);
		return r;
	}
	public static Response redirectLogin(URL path) {
		Response r = NanoHTTPD.newFixedLengthResponse(Response.Status.TEMPORARY_REDIRECT, "text/plain", "");
		r.addHeader("Location", "/login?target=\"/" + path + "\"");
		return r;
	}
	
	public boolean addPage(WebPage p) {
		if(getPage(p.getURL()) == null) {
			pages.add(p);
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		ServerRunner.run(WebServer.class);
	}
	
}
