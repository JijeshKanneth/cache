package cache;

public interface ICachManager {
	public void put(String key, Object value);
	public Object get(String key);
	public void clearAll();
}
