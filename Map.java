import java.util.Map.Entry;

public interface Map<K, V> 
{
	public V get(K key);
	public V remove(K key);
	public V put(K key, V value);
	
	public int size();
	public boolean isEmpty();
	
	//Methods to allow iterating over keys, values or entries
	public Iterable<K> keySet();
	public Iterable<V> values();
	public Iterable<Entry<K, V>> entrySet();

}
