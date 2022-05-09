package webEngine;

public class WebAction {
	
	public Act act;
	public String data;
	
	public WebAction(Act action, String data) {
		act = action;
		this.data = data;
	}
	
	public static WebAction Ok() {
		return new WebAction(Act.OK, "");
	}
	public static WebAction Block(String msg) {
		return new WebAction(Act.BLOCK, msg);
	}
	public static WebAction RedirectT(String dst) {
		return new WebAction(Act.REDIRECT_TEMP, dst);
	}
	public static WebAction RedirectLog() {
		return new WebAction(Act.REDIRECT_LOGIN, "");
	}
	
	@Override
	public String toString() {
		return "{"+act + ", " + data + "}";
	}
	
	public enum Act {
		OK,
		BLOCK,
		REDIRECT_TEMP,
		REDIRECT_LOGIN
	}
}
