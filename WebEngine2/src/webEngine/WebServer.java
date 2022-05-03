package webEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nanoHTTPD.NanoHTTPD;
import nanoHTTPD.NanoHTTPD.Response;
import userEngine.User;
import userEngine.UserDirectory;

public class WebServer extends NanoHTTPD {

	private HashMap<URL, WebPage> pages;
	
	private UserDirectory users;
	
	public WebServer() {
		super(8080);
		pages = new HashMap<URL, WebPage>();
		WebPage p = new WebPage(new URL(""), true) {

			@Override
			public WebAction validate(URL path, User u) {
				if(u.hasPerm("pages.veiw")) {
					return WebAction.Ok();
				} else {
					return WebAction.Block("Must have permision pages.veiw");
				}
			}

			@Override
			public Response serve(URL path, HashMap<String, List<String>> params, String body, User u) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		HashMap<String, List<String>> params = (HashMap<String, List<String>>) decodeParameters(session.getQueryParameterString());
		URL url = new URL(session.getUri().substring(1));
		CookieHandler cookies = session.getCookies();
		Method method = session.getMethod();
		Map<String, List<String>> postBody = new HashMap<String, List<String>>();
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
		
		if(pages.containsKey(url)) {
			WebPage page = pages.get(url);
			WebAction act = page.canGo(url, user);
			if(act.act == WebAction.Act.OK) {
				try {
					return page.serve(url, params, postData, user);
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
		r.addHeader("Location", "/login?target=\"/" + dest + "\"");
		return r;
	}
	
	public boolean addPage(WebPage p) {
		if(!pages.containsKey(p)) {
			pages.put(p.getURL(), p);
			return true;
		}
		return false;
	}
	
}
