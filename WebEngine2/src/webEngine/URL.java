package webEngine;

public class URL {
	
	public String[] path;
	
	public URL() {
		path = new String[0];
	}
	public URL(String path) {
		this.path = path.split("/");
	}
	public URL(String[] path) {
		this.path = path;
	}
	
	public URL clone() {
		return new URL(this.toString());
	}
	
	@Override
	public String toString() {
		String str = "";
		for(int i = 0; i < path.length; i++) {
			if(i > 0) str += "/";
			str += path[i];
		}
		return str;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof URL) {
			URL u = (URL)o;
			if(u.path.length != path.length) {
				return false;
			}
			for(int i = 0; i < path.length; i++) {
				if(!path[i].equals(u.path[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
