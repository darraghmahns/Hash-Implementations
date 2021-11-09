import java.util.Map.Entry;
import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;

public class HashMap<K, V> implements Map<K, V>
{
	//List of primes to be used for array size as well as p value in the MAD compression method
	private final int[] primes = {100663319, 201326611, 402653189,805306457, 1610612741};
	
	//Data members
	LinkedList<Entry<K, V>>[] buckets; //Arraylist of linked lists of entries
	
	int numEntries; //This is what the method size() should return since it's the number of entries in the map
	
	//MAD method parameters
	private int p;
	private int a;
	private int b;
	
	
	public HashMap()
	{
		this.numEntries = 0;		
		initMap(16);
		
	}

	private void expandIfNeeded()
	{
		if(((double) numEntries / buckets.length) > 0.75 )
		{
			Iterable<Entry<K, V>> entries = this.entrySet();
			initMap(buckets.length * 2);
			
			numEntries = 0;
			
			for(Entry<K, V> e : entries)
			{
				this.put(e.getKey(), e.getValue());
			}
		}
	}
	
	private void initMap(int bc)
	{
		Random random = new Random();
		this.p = primes[(int)(random.nextDouble() * primes.length)];
		this.a = random.nextInt(p-1) + 1; 
		this.b = random.nextInt(p); 
		
		this.buckets = new LinkedList[bc];

		for(int i=0; i < this.buckets.length; i++)
		{
			this.buckets[i] = new LinkedList<>();
		}
	}

	private int hashFunction(K k)
	{
		int abs = k.hashCode();
		int mult = abs * a;
		int add = mult + b;
		return (Math.abs(add) % p) % this.buckets.length;
	}
	
	@Override
	public V put(K k, V v) 
	{
		expandIfNeeded();
		int index = hashFunction(k);
		LinkedList<Entry<K,V>> bucket = this.buckets[index];
		
		for(Entry<K,V> e : bucket)
		{
			if(e.getKey().equals(k))
			{
				return e.setValue(v);
			}
		}
		
		//Entry not in map
		bucket.add(new MapEntry(k, v));
		this.numEntries++;
		return null;
	}

	@Override
	public V get(K k) 
	{
		int index = hashFunction(k);
		LinkedList<Entry<K,V>> bucket = this.buckets[index];

		for(Entry<K,V> e : bucket)
		{
			if(e.getKey().equals(k))
			{
				return e.getValue();
			}
		}
		return null;
	}

	@Override
	public V remove(K key) 
	{
		int index = hashFunction(key);
		LinkedList<Entry<K,V>> bucket = this.buckets[index];
		
		V returnValue = null;
		
		for(Entry<K,V> e : bucket)
		{
			if(e.getKey().equals(key))
			{
				returnValue = e.getValue();
				bucket.remove(e);
				return returnValue;
			}
		}
		return returnValue;
	}

	@Override
	public boolean isEmpty() 
	{
		return this.numEntries == 0;
	}

	@Override
	public int size() 
	{
		return this.numEntries;
	}

	@Override
	public Iterable<K> keySet() 
	{
		ArrayList<K> keys = new ArrayList<>();
		for(int i=0; i < buckets.length; i++)
		{
			for(Entry<K, V> e : buckets[i])
			{
				keys.add(e.getKey());
			}
		}
		
		return keys;
	}

	@Override
	public Iterable<V> values() 
	{
		ArrayList<V> values = new ArrayList<>();
		for(int i=0; i < buckets.length; i++)
		{
			for(Entry<K, V> e : buckets[i])
			{
				values.add(e.getValue());
			}
		}
		
		return values;
	}

	@Override
	public Iterable<Entry<K, V>> entrySet() 
	{
		ArrayList<Entry<K, V>> entries = new ArrayList<>();
		for(int i=0; i < buckets.length; i++)
		{
			for(Entry<K, V> e : buckets[i])
			{
				entries.add(e);
			}
		}
		
		return entries;
	}

	public String toString()
	{
		String r = "";
		int largestBucket = 0;
		
		for(int i=0; i < buckets.length; i++)
		{
			if(this.buckets[i].size() > largestBucket)
			{
				largestBucket = this.buckets[i].size();
			}
			r += "Bucket " + i + "( " + this.buckets[i].size() + " ) - ";
			for(Entry<K, V> e : this.buckets[i])
			{
				r += e + " ";
			}
			r += "\n";
		}
		r += "\nNumber of Entries: " + this.size() + "\nLargest Bucket: " + largestBucket + "\nLambda = " + (double)this.size()/this.buckets.length;
		
		return r;
	}
	/**
	 * MapEntry class implements the public Entry Interface. 
	 * Supports getKey, setValue and getValue
	 *
	 */
	private class MapEntry implements Entry<K, V>
	{
		private K key;
		private V value;
		
		public MapEntry(K k, V v)
		{
			this.key = k;
			this.value = v;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V val) 
		{
			V oldValue = this.value;
			this.value = val;
			return oldValue;
		}
		
		public String toString()
		{
			return "(" + this.key.toString() + " : " + this.value.toString() + ")";
		}
		
	}
}
