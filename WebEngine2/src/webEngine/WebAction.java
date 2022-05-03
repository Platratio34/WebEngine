package webEngine;

public class WebAction {
	
	public Act act;
	public String dst;
	public String msg;
	
	public WebAction(Act action, String destination, String message) {
		act = action;
		dst = destination;
		msg = message;
	}
	
	public static WebAction Ok() {
		return new WebAction(Act.OK, "", "");
	}
	public static WebAction Block(String msg) {
		return new WebAction(Act.BLOCK, "", msg);
	}
	public static WebAction RedirectT(String dst) {
		return new WebAction(Act.REDIRECT_TEMP, dst, "");
	}
	public static WebAction RedirectLog() {
		return new WebAction(Act.REDIRECT_LOGIN, "", "");
	}
	
	public enum Act {
		OK,
		BLOCK,
		REDIRECT_TEMP,
		REDIRECT_LOGIN
	}
}
