package webEngine.pages;

import java.util.HashMap;
import java.util.List;

import org.nanohttpd.protocols.http.content.CookieHandler;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import userEngine.User;
import webEngine.URL;
import webEngine.WebAction;
import webEngine.WebPage;

public class AdminPanelPage extends WebPage {

	public AdminPanelPage() {
		super("admin", true);
	}

	@Override
	protected WebAction validate(URL path, User u) {
//		System.out.println(u.serialize());
		if(!u.hasPerm("admin.panel")) {
			return WebAction.Block("must have permission \"admin\"");
		}
		return WebAction.Ok();
	}

	@Override
	public Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u, CookieHandler cookies) {
		return returnPage("admin/admin.html");
	}

}
