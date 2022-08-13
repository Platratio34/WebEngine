package webEngine;

import java.util.HashMap;
import java.util.List;

import dataManagment.JsonObj;
import dataManagment.JsonSerializable;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.content.CookieHandler;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;
import userEngine.User;

public abstract class WebPage {
	
	protected static final String PLAINTEXT = NanoHTTPD.MIME_PLAINTEXT;
	protected static final String HTML = NanoHTTPD.MIME_HTML;
	protected static final String CSS = "text/css";
	protected static final String JS = "text/javascript";
	private URL path;
	protected WebServer server;
	
	protected boolean blockOnNullUser;
	protected WebAction onNullAction = WebAction.Ok();
	protected int idPath = -1;
	
	public WebPage(URL path, boolean nullBlock) {
		blockOnNullUser = nullBlock;
		this.path = path.clone();
	}
	public WebPage(String path, boolean nullBlock) {
		this(new URL(path), nullBlock);
	}
	public WebPage(URL path, WebAction nullAction) {
		onNullAction = nullAction;
		this.path = path.clone();
	}
	public WebPage(String path, WebAction nullAction) {
		this(new URL(path), nullAction);
	}
	
	public void setWebServer(WebServer server) {
		this.server = server;
	}
	
	public WebAction canGo(URL path, User u) {
		if(u == null) {
			if(blockOnNullUser) {
				return WebAction.RedirectLog();
			}
			return onNullAction;
		}
		return validate(path, u);
	}
	
	protected abstract WebAction validate(URL path, User u);
	
	public abstract Response serve(URL path, Method method, HashMap<String, List<String>> params, String body, User u, CookieHandler cookies);
	
	protected static Response returnPage(String path) {
		return Response.newFixedLengthResponse(Status.OK, HTML, PageLoader.getPage(path));
	}
	protected static Response newFixedLengthResponse(Status status, String mimeType, String data) {
		return Response.newFixedLengthResponse(status, mimeType, data);
	}
	protected static Response badRequest(String msg) {
		return Response.newFixedLengthResponse(Status.BAD_REQUEST, PLAINTEXT, msg);
	}
	protected static Response returnOkPlain(String msg) {
		return Response.newFixedLengthResponse(Status.OK, PLAINTEXT, msg);
	}
	protected static Response returnOkJSON(String msg) {
		return Response.newFixedLengthResponse(Status.OK, "test/json", msg);
	}
	protected static Response returnOkJSON(JsonObj obj) {
		return returnOkJSON(obj.toString());
	}
	protected static Response returnOkJSON(JsonSerializable obj) {
		return returnOkJSON(obj.serialize().toString());
	}
	protected static Response defaultPage(String msg) {
		return Response.newFixedLengthResponse(Status.OK, HTML, PageLoader.getDefaultPage(msg));
	}

	public URL getURL() {
		return path;
	}
	public int getIdPath() {
		return idPath;
	}
	
}
