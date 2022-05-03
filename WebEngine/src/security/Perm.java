package security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;

public class Perm {
	
	private HashMap<String, Perm> subPerms;
	protected String[] permArr;
	protected boolean has;
	
	public Perm(String perm) {
		this(perm.split("."), 0);
	}
	public Perm(String[] perm, int i) {
		subPerms = new HashMap<String, Perm>();
		permArr = Arrays.copyOfRange(perm, 0, i + 1);;
		if(permArr.length > permArr.length) {
			setPerm(permArr, i+1, true);
		}
	}
	
	public void setPerm(String perm, boolean has) {
		setPerm(perm.split("."), 0, has);
	}
	public void setPerm(String[] arr, boolean has) {
		setPerm(arr, 0, has);
	}
	protected void setPerm(String[] arr, int i, boolean has) {
		if(i == permArr.length-1) {
			this.has = has;
			return;
		}
		if(subPerms.containsKey(arr[permArr.length])) {
			subPerms.get(arr[permArr.length]).setPerm(arr, i+1, has);;
		} else {
			subPerms.put(arr[permArr.length], new Perm(arr, i+1));
		}
	}
	
	public boolean hasPerm(String p) {
		return hasPerm(p.split("."));
	}
	public boolean hasPerm(String[] pA) {
		if(pA.length == permArr.length) 
			return has;
		else {
			if(!subPerms.containsKey(pA[permArr.length])) {
				return false;
			}
			return subPerms.get(pA[permArr.length]).hasPerm(pA);
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Perm) {
			Perm p2 = (Perm)o;
			if(p2.permArr.length == permArr.length) {
				return IntStream.range(0, permArr.length).allMatch(i -> permArr[i].equals(p2.permArr[i]));
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		String str = permArr[0];
		for(int i = 0; i < permArr.length; i++) {
			str += "." + permArr[i];
		}
		return str;
	}
}
