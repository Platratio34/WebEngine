package webEngine.pages;

import java.util.HashMap;
import java.util.List;

import dataManagment.JsonObj;
import org.nanohttpd.protocols.http.content.CookieHandler;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import security.Key;
import userEngine.User;
import webEngine.URL;
import webEngine.WebAction;
import webEngine.WebPage;

public class Login extends WebPage {

	public Login() {
		super("login", false);
	}

	@Override
	protected WebAction validate(URL path, User u) {
		return WebAction.Ok();
	}

	@Override
	public Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u, CookieHandler cookies) {
		if(method == Method.GET) {
			return returnPage("login.html");
		}
//		System.out.println("\""+body+"\"");
		JsonObj obj = JsonObj.parseD(body);
		if( !(obj.hasKey("user") && obj.hasKey("pass")) ) {
			if(obj.hasKey("logout")) {
				cookies.delete("userKey");
				cookies.delete("user");
				return returnOkPlain("Logout Successful");
			}
			return badRequest("Must have JSON body with elements 'user' and 'pass' or 'logout'");
		}
		String user = obj.getKey("user").string();
		String pass = obj.getKey("pass").string();
		Key<User> key = server.users.tryLoginByName(user, pass);
		if(key == null) {
			return returnOkPlain("Invalid Username or Password");
		}
		cookies.set("userKey",key.getKey(),1);
//		System.out.println(key.getKey());
//		System.out.println(cookies.read("userKey"));
//		cookies.set("user",key.getValue().getName(),1);
//		System.out.println(cookies.read("user"));
		return returnOkPlain("Login Successful");
	}

}
