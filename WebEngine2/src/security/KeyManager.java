package security;

import java.util.HashMap;
import java.util.Random;

public class KeyManager<T> {
	
	private HashMap<String, Key<T>> keys;
	
	public long keyExpiration = 1000 * 60 * 60 * 1;
	
	public KeyManager() {
		keys = new HashMap<String, Key<T>>();
	}
	
	public Key<T> newKey(T val) {
		String keyS = getSaltString(32);
		while(keys.containsKey(keyS)) {
			keyS = getSaltString(32);
		}
		Key<T> k = new Key<T>(keyS, val, keyExpiration);
		keys.put(keyS, k);
		return k;
	}
	
	public Key<T> checkKey(String key) {
		if(keys.containsKey(key)) {
			Key<T> k = keys.get(key);
			if(k.check()) {
				return k;
			} else {
				keys.remove(key);
			}
		}
		return null;
	}
	
	protected String getSaltString(int l) {
        String SALTCHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < l) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
