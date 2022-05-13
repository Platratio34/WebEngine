package webEngine.pages;

import java.util.HashMap;
import java.util.List;

import dataManagment.JsonObj;
import nanoHTTPD.NanoHTTPD.CookieHandler;
import nanoHTTPD.NanoHTTPD.Method;
import nanoHTTPD.NanoHTTPD.Response;
import userEngine.User;
import webEngine.URL;
import webEngine.WebAction;
import webEngine.WebPage;

public class PassChangePage extends WebPage {

	public PassChangePage() {
		super("user/changePass", true);
	}

	@Override
	protected WebAction validate(URL path, User u) {
		return WebAction.Ok();
	}

	@Override
	public Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u, CookieHandler cookies) {
		if(method == Method.GET) {
			return returnPage("user/changePass.html");
		} else if(method == Method.POST) {
			JsonObj obj = JsonObj.parseD(body);
			if(!obj.hasKey("oPass") || !obj.hasKey("nPass")) {
				return badRequest("Must have parameters \"oPass\" and \"nPass\"");
			}
			String oPass = obj.getKey("oPass").string();
			String nPass = obj.getKey("nPass").string();
			if(server.users.changePass(u, oPass, nPass)) {
				return returnOkPlain("Password changed");
			}
			return newFixedLengthResponse(Response.Status.UNAUTHORIZED, PLAINTEXT, "Incorrect password");
		}
		return badRequest("Invalid method");
	}

}
