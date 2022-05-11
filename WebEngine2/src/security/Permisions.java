package security;

import java.util.HashMap;

import dataManagment.JsonObj;
import dataManagment.JsonSerializable;

public class Permisions implements JsonSerializable {
	
	private HashMap<String, Boolean> perms;
	
	public Permisions() {
		perms = new HashMap<String, Boolean>();
	}
	public Permisions(JsonObj obj) {
		perms = new HashMap<String, Boolean>();
		deserialize(obj);
	}
	
	public void setPerm(String perm, boolean has) {
		perms.put(perm, has);
	}
	
	public boolean hasPerm(String perm) {
		if(perms.containsKey(perm)) {
			return perms.get(perm);
		}
		String[] p = perm.split("\\.");
		boolean o = false;
		for(int i = 0 ; i < p.length; i++) {
			String P = permArrToString(p, i) + ".*";
			if(perms.containsKey(P)) {
				o = perms.get(P);
			}
			P = permArrToString(p, i+1);
			if(perms.containsKey(P)) {
				o = perms.get(P);
			}
		}
		return o;
	}
	
	private String permArrToString(String[] arr, int m) {
		String perm = "";
		for(int i = 0; i < m && i < arr.length; i++) {
			if(i>0) perm += ".";
			perm += arr[i];
		}
		return perm;
	}

	@Override
	public JsonObj serialize() {
		JsonObj obj = new JsonObj();
		for(HashMap.Entry<String, Boolean> kv : perms.entrySet()) {
			obj.setKey(kv.getKey(), kv.getValue());
		}
		return obj;
	}

	@Override
	public void deserialize(JsonObj obj) {
		perms = new HashMap<String, Boolean>();
		HashMap<String, JsonObj> map = obj.getMap();
		for(HashMap.Entry<String, JsonObj> ent : map.entrySet()) {
			setPerm(ent.getKey(), ent.getValue().bool());
		}
	}
	
}
