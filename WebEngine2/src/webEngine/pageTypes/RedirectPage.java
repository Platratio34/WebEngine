package webEngine.pageTypes;

import java.util.HashMap;
import java.util.List;

import org.nanohttpd.protocols.http.content.CookieHandler;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import userEngine.User;
import webEngine.URL;
import webEngine.WebAction;
import webEngine.WebPage;

public class RedirectPage extends WebPage {
	
	public String dest;

	public RedirectPage(String path, String dest) {
		super(path, WebAction.RedirectT(dest));
		this.dest = dest;
	}

	@Override
	protected WebAction validate(URL path, User u) {
		return WebAction.RedirectT(dest);
	}

	@Override
	public Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u,
			CookieHandler cookies) {
		return defaultPage("How did you get here?");
	}

}
