package webEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nanoHTTPD.NanoHTTPD;
import nanoHTTPD.util.ServerRunner;
import userEngine.User;
import userEngine.UserDirectory;
import webEngine.pages.Home;
import webEngine.pages.Login;
import webEngine.pages.PermsPage;

public class WebServer extends NanoHTTPD {

//	private HashMap<URL, WebPage> pages;
	private ArrayList<WebPage> pages;
	
	public UserDirectory users;
	
	public WebServer() {
		super(80);
//		pages = new HashMap<URL, WebPage>();
		pages = new ArrayList<WebPage>();
		users = new UserDirectory();
		WebPage p = new WebPage("", false) {

			@Override
			public WebAction validate(URL path, User u) {
				return WebAction.RedirectT("/home");
			}

			@Override
			public Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u, CookieHandler cookies) {
				return null;
			}
			
		};
		addPage(p);
		addPage(new Home());
		addPage(new Login());
		addPage(new PermsPage());
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		HashMap<String, List<String>> params = (HashMap<String, List<String>>) decodeParameters(session.getQueryParameterString());
		URL url = new URL(session.getUri().substring(1));
		CookieHandler cookies = session.getCookies();
		Method method = session.getMethod();
		String postData = "";
		if(method == Method.POST) {
			try {
				final HashMap<String, String> map = new HashMap<String, String>();
				session.parseBody(map);
				postData = map.get("postData");
			} catch(IOException e) {
				return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOExceptoin: " + e.getMessage());
			} catch(ResponseException e) {
				return newFixedLengthResponse(e.getStatus(), MIME_PLAINTEXT, e.getMessage());
			}
		}
		
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
//		System.out.println(url);
		if(url.equals("favicon.ico")) {
			try {
				InputStream is = new FileInputStream(new File("base/favicon.ico"));
				return newChunkedResponse(Response.Status.OK, "image/x-icon", is);
			} catch (IOException e) {
				System.out.println("Faild to load favicon.ico");
				e.printStackTrace();
				return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Failed to load icon");
			}
		}
		if(url.equalsAt("script", 0)) {
			return newFixedLengthResponse(Response.Status.OK, "text/javascript", PageLoader.getJS(url.toString(1)));
		}
		if(url.equalsAt("style", 0)) {
			return newFixedLengthResponse(Response.Status.OK, "text/css", PageLoader.getCSS(url.toString(1)));
		}
		WebPage page = getPage(url);
		if(page != null) {
			WebAction act = page.canGo(url, user);
//			System.out.println(act.toString());
			if(act.act == WebAction.Act.OK) {
				try {
//					System.out.println("Severing page " + url);
					return page.serve(url, method, params, postData, user, cookies);
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
		for(int i = url.path.length; i >= 1; i--) {
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
		Response r = NanoHTTPD.newFixedLengthResponse(Response.Status.TEMPORARY_REDIRECT, "text/plain", "Redirecting to " + dest);
		r.addHeader("Location", dest);
		return r;
	}
	public static Response redirectLogin(URL path) {
		Response r = NanoHTTPD.newFixedLengthResponse(Response.Status.TEMPORARY_REDIRECT, "text/plain", "Redirecting to login");
		r.addHeader("Location", "/login?target=\"/" + path + "\"");
		return r;
	}
	
	public boolean addPage(WebPage p) {
		if(getPage(p.getURL()) == null) {
			pages.add(p);
			p.setWebServer(this);
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
//		ServerRunner.run(WebServer.class);
		WebServer server = new WebServer();
		server.users.addUser(new User("Peter",0,"PeterPass1"));
		ServerRunner.executeInstance(server);
	}
	
}
