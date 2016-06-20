package com.jec.utils.lang;

import java.util.Collection;
import java.util.Iterator;

public class CollectionUtil {
	
	public static <T> boolean doCompare(Collection<T> c1, Collection<T> c2) {
		
		if(c1 == null && c2 == null) {
			return true;
		}
		
		if(c1 == null || c2 == null) {
			return false;
		}
		
		if(c1.size() != c2.size()) {
			return false;
		}
		
		Iterator<T> it = c1.iterator();
		while(it.hasNext()) {
			if(!c2.contains(it.next())) {
				return false;
			}
		}

		return true;
	}
	
	

	private CollectionUtil() {
	}
	
}
