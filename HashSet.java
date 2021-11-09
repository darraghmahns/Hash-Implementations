
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class HashSet<E> implements Set<E> 
{
	private LinkedList<E>[] theSet;
	private int P;
	private int a;
	private int b;
	private int numElements;
	private int numBuckets;
	
	private final int[] primes = {100663319, 201326611, 402653189,805306457, 1610612741};
	
	public HashSet()
	{
		initSet(16);
		this.numElements = 0;
		this.numBuckets = 16;
	}
	
	private void initSet(int N)
	{
		Random random = new Random();
		this.P = primes[(int)(random.nextDouble() * primes.length)];
		this.a = random.nextInt(P-1) + 1; 
		this.b = random.nextInt(P); 
		
		this.theSet = new LinkedList[N];

		for(int i = 0; i < N; i++)
		{
			theSet[i] = new LinkedList<E>();
		}
	}
	
	private int hashFunction(E e)
	{
		int abs = e.hashCode();
		int mult = abs * a;
		int add = mult + b;
		return (Math.abs(add) % P) % this.numBuckets;
	}
	
	public int getBucket(E e)
	{
		return  hashFunction(e);
	}
	
	private void expandIfNeeded()
	{
		if(((double) numElements / numBuckets) > 0.75)
		{
			Iterable<E> values = this.toArrayList();
			initSet(numBuckets * 2);
			
			for(E value : values)
			{
				this.add(value);
			}
			this.numBuckets *=2;
		}
	}
	
	private ArrayList<E> toArrayList()
	{
		ArrayList<E> values = new ArrayList<>();
		
		for(int i = 0; i < numBuckets; i++)
		{
			LinkedList<E> bucket = theSet[i];
			for(int j = 0; j < bucket.size(); j++)
			{
				values.add((E) bucket.get(j));
			}
		}
		return values;
	}
	
	
	@Override
	public Iterator<E> iterator() 
	{
		ArrayList<E> it = new ArrayList<>();
		
		for(LinkedList<E> ll : theSet)
		{
			it.addAll(ll);
		}
		
		return it.iterator();
	}

	@Override
	public boolean add(E e) 
	{
		if(this.contains(e)) 
		{
			return false;
		}
		else
		{
			expandIfNeeded();
			
			LinkedList<E> bucket = theSet[hashFunction(e)];
			
			bucket.add(e);
			this.numElements++;
		}
		
		return true;
	}

	@Override
	public boolean remove(E e) 
	{
		if(this.contains(e)) 
		{
			return false;
		}
		else
		{
			
			LinkedList<E> bucket = theSet[hashFunction(e)];
			
			bucket.remove(e);
			this.numElements--;
		}
		return true;
	}

	@Override
	public boolean contains(E e) 
	{
		
		LinkedList<E> bucket = theSet[hashFunction(e)];
		return bucket.contains(e);
	}

	@Override
	public void addAll(Set<E> T) 
	{
		for (E e : T) {
			add(e);
		}
	}

	@Override
	public void retainAll(Set<E> T) 
	{
		HashSet<E> temp = new HashSet<>();
		
		for(E e : T) 
		{
			if(contains(e)) 
			{
				temp.add(e);
			}
		}
		this.theSet = temp.theSet;
	}

	@Override
	public void removeAll(Set<E> T) 
	{
		for(E e : T)
		{
			if(contains(e))
			{
				remove(e);
			}
		}	
	}
	
	public boolean isEmpty()
	{
		return !(this.numElements > 0); 
	}
	
	public String toString()
	{
		String r = " ";
		
		Iterable<E> values = this.toArrayList();
		
		for(E e : values)
		{
			r += e + " ";
		}
		
		return r;
	}	
}










