package webEngine;

import java.util.HashMap;
import java.util.List;

import nanoHTTPD.NanoHTTPD;
import nanoHTTPD.NanoHTTPD.Response;
import userEngine.User;

public abstract class WebPage {
	
	private static final String PLAINTEXT = NanoHTTPD.MIME_PLAINTEXT;
	private static final String HTML = NanoHTTPD.MIME_HTML;
	private static final String CSS = "text/css";
	private static final String JS = "text/javascript";
	private URL path;
	
	protected boolean blockOnNullUser;
	
	public WebPage(URL path, boolean nullBlock) {
		blockOnNullUser = nullBlock;
		this.path = path.clone();
	}
	
	public WebAction canGo(URL path, User u) {
		if(u == null) {
			if(blockOnNullUser) {
				return WebAction.RedirectLog();
			}
			return WebAction.Ok();
		}
		return validate(path, u);
	}
	
	protected abstract WebAction validate(URL path, User u);
	
	public abstract Response serve(URL path, HashMap<String, List<String>> params, String body, User u);

	public URL getURL() {
		return path;
	}
	
}
