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
	public String toString(int start) {
		String str = "";
		for(int i = start; i < path.length; i++) {
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
			return equals(u, path.length);
		}
		return false;
	}
	public boolean equals(URL url, int l) {
		int m = Math.min(Math.min(l, path.length), url.path.length);
		for(int i = 0; i < m; i++) {
			if(!path[i].equals(url.path[i])) {
				return false;
			}
		}
		return true;
	}
	public boolean equals(String path, int l) {
		return equals(new URL(path));
	}
	public boolean equals(String path) {
		return equals(new URL(path));
	}
	
	public boolean equalsAt(String elm, int i) {
		if(path.length > i) {
			return path[i].equals(elm);
		}
		return false;
	}
}
