package webEngine.pages;

import java.util.HashMap;
import java.util.List;

import dataManagment.JsonObj;
import org.nanohttpd.protocols.http.content.CookieHandler;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import userEngine.User;
import webEngine.URL;
import webEngine.WebAction;
import webEngine.WebPage;

public class PermsPage extends WebPage {

	public PermsPage() {
		super("perms", false);
	}

	@Override
	protected WebAction validate(URL path, User u) {
		return WebAction.Ok();
	}

	@Override
	public Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u, CookieHandler cookies) {
		if(u != null) {
			return returnOkJSON(u.getPermsJSON());
		} else {
			return returnOkJSON(new JsonObj());
		}
	}

}
