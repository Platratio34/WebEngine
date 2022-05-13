package webEngine.pages;

import java.util.HashMap;
import java.util.List;

import nanoHTTPD.NanoHTTPD.CookieHandler;
import nanoHTTPD.NanoHTTPD.Method;
import nanoHTTPD.NanoHTTPD.Response;
import userEngine.User;
import webEngine.URL;
import webEngine.WebAction;
import webEngine.WebPage;

public class ProfilePage extends WebPage {
	
	public static String VIEW_OTHERS_PERM = "user.veiw.others";

	public ProfilePage() {
		super("user/profile", true);
		idPath = 3;
	}

	@Override
	protected WebAction validate(URL path, User u) {
		if(path.path.length == 2) {
			return WebAction.RedirectT("/user/profile/"+u.getUUID());
		}
		if(path.path[2].equals(u.getUUID()+"")) {
			return WebAction.Ok();
		}
		if(!u.hasPerm(VIEW_OTHERS_PERM)) {
			return WebAction.Block("you must have permsion '"+VIEW_OTHERS_PERM+"' to see other peoples profiles");
		}
		return WebAction.Ok();
	}

	@Override
	public Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u, CookieHandler cookies) {
		if(!params.containsKey("data")) {
			User u2 = server.users.getUserByUUID(Long.parseLong(path.path[2]));
			if(u2 != null) {
				return returnPage("user/profile");
			} else {
				return defaultPage("No user with UUID");
			}
		} else {
			User u2 = server.users.getUserByUUID(Long.parseLong(path.path[2]));
			if(u2 != null) {
				return returnOkJSON(u2.getVeiwData());
			} else {
				return badRequest("No User with UUID");
			}
		}
	}

}
