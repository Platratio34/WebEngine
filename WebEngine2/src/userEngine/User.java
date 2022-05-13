package userEngine;

import java.time.Instant;

import dataManagment.JsonObj;
import dataManagment.JsonSerializable;
import security.Hasher;
import security.Permisions;

public class User implements JsonSerializable {
	
	private String displayName;
	private long uuid;
	private Permisions perms;
	private String passHash;
	private long lastLogin;
	
	private JsonObj data;
	
	public User(String name, long uuid, String pass) {
		passHash = Hasher.Hash1(pass);
		displayName = name;
		this.uuid = uuid;
		perms = new Permisions();
		lastLogin = -1;
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
	
	public void login() {
		lastLogin = Instant.now().getEpochSecond();
	}
	
	public long getLastLogin() {
		return lastLogin;
	}

	@Override
	public JsonObj serialize() {
		JsonObj obj = new JsonObj();
		obj.setKey("dsp", displayName);
		obj.setKey("uuid", uuid);
		obj.setKey("pass", passHash);
		obj.setKey("perms", perms);
		obj.setKey("last", lastLogin);
		obj.setKey("data", data);
		return obj;
	}

	@Override
	public void deserialize(JsonObj obj) {
		displayName = obj.getKey("dsp").string();
		uuid = obj.getKey("uuid").longInt();
		passHash = obj.getKey("pass").string();
		lastLogin = obj.getKey("last").longInt();
		perms = new Permisions(obj.getKey("perms"));
		data = obj.getKey("data");
	}
	public JsonObj getPermsJSON() {
		return perms.serialize();
	}
	
	public JsonObj getData() {
		return new JsonObj(data);
	}
	
	public JsonObj getVeiwData() {
		JsonObj obj = new JsonObj();
		obj.setKey("dsp", displayName);
		obj.setKey("uuid", uuid);
		obj.setKey("last", lastLogin);
		obj.setKey("data", data);
		return obj;
	}
	public boolean changePass(String oPass, String nPass) {
		if(!checkPass(oPass)) return false;
		setPass(nPass);
		return true;
	}
}
