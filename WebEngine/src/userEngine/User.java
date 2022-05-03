package userEngine;

import dataManagment.JsonObj;
import dataManagment.JsonSerializable;
import security.Hasher;
import security.Permisions;

public class User implements JsonSerializable {
	
	private String displayName;
	private long uuid;
	private Permisions perms;
	private String passHash;
	
	public User(String name, long uuid, String pass) {
		passHash = Hasher.Hash1(pass);
		displayName = name;
		this.uuid = uuid;
		perms = new Permisions();
	}
	public User(JsonObj obj) {
		deserialize(obj);
	}
	
	public String getName() {
		return displayName;
	}
	public void setName(String name) {
		displayName = name;
	}
	public long getUUID() {
		return uuid;
	}
	public boolean hasPerm(String perm) {
		return perms.hasPerm(perm);
	}
	public void setPerm(String perm, boolean has) {
		perms.setPerm(perm, has);
	}
	
	public void setPass(String pass) {
		passHash = Hasher.Hash1(pass);
	}
	
	public boolean checkPass(String pass) {
		return passHash.equals(Hasher.Hash1(pass));
	}
	public boolean checkPassHash(String passHash) {
		return this.passHash.equals(passHash);
	}

	@Override
	public JsonObj serialize() {
		JsonObj obj = new JsonObj();
		obj.setKey("dsp", displayName);
		obj.setKey("uuid", uuid);
		obj.setKey("pass", passHash);
		obj.setKey("perms", perms);
		return obj;
	}

	@Override
	public void deserialize(JsonObj obj) {
		displayName = obj.getKey("dsp").string();
		uuid = obj.getKey("uuid").longInt();
		passHash = obj.getKey("pass").string();
		perms = new Permisions(obj.getKey("perms"));
	}
}
