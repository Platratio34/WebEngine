package webEngine;

import java.util.HashMap;
import java.util.List;

import dataManagment.JsonObj;
import dataManagment.JsonSerializable;
import nanoHTTPD.NanoHTTPD;
import nanoHTTPD.NanoHTTPD.CookieHandler;
import nanoHTTPD.NanoHTTPD.Method;
import nanoHTTPD.NanoHTTPD.Response;
import userEngine.User;

public abstract class WebPage {
	
	protected static final String PLAINTEXT = NanoHTTPD.MIME_PLAINTEXT;
	protected static final String HTML = NanoHTTPD.MIME_HTML;
	protected static final String CSS = "text/css";
	protected static final String JS = "text/javascript";
	private URL path;
	protected WebServer server;
	
	protected boolean blockOnNullUser;
	protected int idPath = -1;
	
	public WebPage(URL path, boolean nullBlock) {
		blockOnNullUser = nullBlock;
		this.path = path.clone();
	}
	public WebPage(String path, boolean nullBlock) {
		this(new URL(path), nullBlock);
	}
	
	public void setWebServer(WebServer server) {
		this.server = server;
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
	
	public abstract Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u, CookieHandler cookies);
	
	protected static Response returnPage(String path) {
		return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, HTML, PageLoader.getPage(path));
	}
	protected static Response newFixedLengthResponse(Response.Status status, String mimeType, String data) {
		return NanoHTTPD.newFixedLengthResponse(status, mimeType, data);
	}
	protected static Response badRequest(String msg) {
		return NanoHTTPD.newFixedLengthResponse(Response.Status.BAD_REQUEST, PLAINTEXT, msg);
	}
	protected static Response returnOkPlain(String msg) {
		return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, PLAINTEXT, msg);
	}
	protected static Response returnOkJSON(String msg) {
		return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, "test/json", msg);
	}
	protected static Response returnOkJSON(JsonObj obj) {
		return returnOkJSON(obj.toString());
	}
	protected static Response returnOkJSON(JsonSerializable obj) {
		return returnOkJSON(obj.serialize().toString());
	}

	public URL getURL() {
		return path;
	}
	public int getIdPath() {
		return idPath;
	}
	
}
