package security;

import java.time.Instant;

public class Key<T> {

	private String key;
	private T value;
	private long lastUsed;
	private long expires;
	private long timeout;
	
	public Key(String key, T value, long experation) {
		this.key = key;
		this.value = value;
		timeout = experation;
		lastUsed = Instant.now().getEpochSecond();
		expires = lastUsed + timeout;
	}
	
	public String getKey() {
		return key;
	}
	
	public T getValue() {
		return value;
	}
	
	public boolean check() {
		long now = Instant.now().getEpochSecond();
		if(now > expires) {
			return false;
		}
		lastUsed = now;
		expires = lastUsed + timeout;
		return true;
	}
}
