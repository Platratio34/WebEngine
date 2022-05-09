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

	public ProfilePage() {
		super("user/profile", true);
		idPath = 2;
	}

	@Override
	protected WebAction validate(URL path, User u) {
		if(path.path.length == 2) {
			return WebAction.RedirectT("/user/profile/"+u.getUUID());
		}
		if(path.path[2].equals(u.getUUID()+"")) {
			return WebAction.Ok();
		}
		if(!u.hasPerm("user.veiw.others")) {
			return WebAction.Block("you must have permsion 'user.veiw.others' to see other peoples profiles");
		}
		return WebAction.Ok();
	}

	@Override
	public Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u, CookieHandler cookies) {
		if(path.path.length == 3) {
			return returnPage("user/profile");
		} else {
			if(path.equals("data",3)) {
				User u2 = server.users.getUserByUUID(Long.parseLong(path.path[2]));
				return returnOkJSON(u2.getData());
			}
			return badRequest("Path may only be user/profile/uuid or user/profile/uuid/data");
		}
	}

}
