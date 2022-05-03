package userEngine;

import java.util.ArrayList;
import java.util.HashMap;

import security.Key;
import security.KeyManager;

public class UserDirectory {

	private HashMap<Long, User> users;
	
	private KeyManager<User> keyManager;
	
	public UserDirectory() {
		users = new HashMap<Long, User>();
		keyManager = new KeyManager<User>();
	}
	
	public boolean addUser(User u) {
		if(users.containsKey(u.getUUID()))
			return false;
		users.put(u.getUUID(), u);
		return true;
	}
	
	public User getUserByUUID(long uuid) {
		if(users.containsKey(uuid))
			return users.get(uuid);
		return null;
	}
	
	public ArrayList<User> getUserByName(String name) {
		ArrayList<User> us = new ArrayList<User>();
		for(User u : users.values()) {
			if(u.getName().equals(name)) {
				us.add(u);
			}
		}
		return us;
	}

	public Key<User> tryLoginByName(String name, String pass) {
		ArrayList<User> us = getUserByName(name);
		
		if(us.size() > 0) {
			for(User u : us) {
				if(u.checkPass(pass)) {
					u.login();
					return keyManager.newKey(u);
				}
			}
		}
		return null;
	}
	
	public User getUserByKey(String key) {
		Key<User> k = keyManager.checkKey(key);
		if(k != null) {
			return k.getValue();
		}
		return null;
	}
}
