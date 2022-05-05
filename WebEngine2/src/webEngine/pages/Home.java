package webEngine.pages;

import java.util.HashMap;
import java.util.List;

import nanoHTTPD.NanoHTTPD.Method;
import nanoHTTPD.NanoHTTPD.Response;
import userEngine.User;
import webEngine.URL;
import webEngine.WebAction;
import webEngine.WebPage;

public class Home extends WebPage {

	public Home() {
		super(new URL("home"), false);
	}

	@Override
	protected WebAction validate(URL path, User u) {
		return WebAction.Ok();
	}

	@Override
	public Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u) {
		return returnPage("home.html");
	}

}
