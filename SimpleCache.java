package cache;

import java.util.Hashtable;

public class SimpleCache implements ICachManager{
	private Hashtable<String, Object> table = new Hashtable<String, Object>();
	
	@Override
	public void put(String key, Object value) {
		table.put(key, value);
	}

	@Override
	public Object get(String key) {
		return table.get(key);
	}

	@Override
	public void clearAll() {
		table = new Hashtable<String, Object>();
	}

}
