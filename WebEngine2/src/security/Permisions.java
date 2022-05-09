package security;

import java.util.HashMap;

import dataManagment.JsonObj;
import dataManagment.JsonSerializable;

public class Permisions implements JsonSerializable {
	
	private HashMap<String, Perm> perms;
	
	public Permisions() {
		perms = new HashMap<String, Perm>();
	}
	public Permisions(JsonObj obj) {
		perms = new HashMap<String, Perm>();
		deserialize(obj);
	}
	
	public void setPerm(String perm, boolean has) {
		setPerm(perm.split("."), has);
	}
	public void setPerm(String[] perm, boolean has) {
		if(perms.containsKey(perm[0])) {
			perms.get(perm[0]).setPerm(perm, has);
		} else {
			perms.put(perm[0], new Perm(perm, 0));
		}
	}
	
	public boolean hasPerm(String perm) {
		return hasPerm(perm.split("."));
	}
	public boolean hasPerm(String[] perm) {
		if(perms.containsKey(perm[0])) {
			return perms.get(perm[0]).hasPerm(perm);
		}
		return false;
	}

	@Override
	public JsonObj serialize() {
		return new JsonObj();
	}

	@Override
	public void deserialize(JsonObj obj) {
		
	}
	
}
