package webEngine;

import java.util.HashMap;

import nanoHTTPD.NanoHTTPD;
import nanoHTTPD.NanoHTTPD.Response;
import userEngine.User;

import userEngine.User;

public abstract class WebPage {
	
	private static final String PLAINTEXT = NanoHTTPD.MIME_PLAINTEXT;
	private static final String HTML = NanoHTTPD.MIME_HTML;
	private static final String CSS = "text/css";
	private static final String JS = "text/javascript";
	
	private HashMap<String, WebPage> subPages;
	private String[] path;
	
	public WebPage(String[] path) {
		this.path = path.clone();
		subPages = new HashMap<String, WebPage>();
	}
	
	public abstract WebAction validate(User u);
	
	public abstract Response serve(String path, HashMap<String, String> params);
	
	private Response redirectResponse(String dest) {
		Response r = NanoHTTPD.newFixedLengthResponse(Response.Status.TEMPORARY_REDIRECT, PLAINTEXT, "");
		r.addHeader("Location", "/login?target=\"/" + dest);
		return r;
	}
	
}
