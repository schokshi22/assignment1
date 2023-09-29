package ass1;
import java.util.Random;

public class SQueue<T> implements QueueInterface<T>, Shufflable 
{
	private T [] theArray;  // internal representation of a queue
	
	//this is an index in the array where to read the data 
	//at the front of the queue
	private int read; 
	
	//this is an index in the array where to write the next element 
	//at the end of the queue
	private int write;
	
	//variable to store the number of slots in theArray
	//just for consistency, technically not required, 
	//because you can get the capacity of theArray from theArray.length
	private int capacity;
	
	//this stores the size of the queue: 
	// the total number of actual elements in the queue
	private int size;
	
	//initialization of the internal array of predefined capacity
	
	public SQueue (int capacity)	
	{			
		this.capacity = capacity+1;
		this.read = 0;
		this.write = 0;
		this.size = 0;
		@SuppressWarnings("unchecked")
		T [] temp = (T []) new Object[this.capacity];
		theArray = temp;
	}	
	
	public int getSize() 
	{
		return this.size;
	}
	
	//add element to the end of the queue
	//wrap around the array as long as you did not reach the front 
	// of the queue
	// if no element can be added - throw exception
	public void enqueue(T newEntry) 
	{
		if (isFull())
			throw new FullQueueException();
		else
		{
			theArray[write] = newEntry;
			write = (write + 1) % capacity;
			size++;
		}
		
	}
	  
	//remove and return an element from the frony of the queue
	//because the array is circular - this object is at position read
	//if queue is empty - throw exception
	//if you remove element at position i of the array, 
	// please set theArray[read] = null; 
	// though technically not needed,
	//this will help you to see empty slots in theArray
	public T dequeue() 
	{
		if (isEmpty())
			throw new EmptyQueueException();
		else
		{
			T front = theArray[read];
			theArray[read] = null;
			read = (read+1) % capacity;
			size--;
			return front;
		}
	}
	  
	//Returns the entry at the front of this queue.
	//throw  EmptyQueueException if the queue is empty. 
	public T getFront() 
	{
		if (isEmpty())
			throw new EmptyQueueException();
		else
			return theArray[read];
	}
	  
	//Detect whether this queue is empty.
	public boolean isEmpty() 
	{	
		return read == write;
	}
	
	//Detect whether this queue is full.
	//this should prevent read and write indexes to become equal
	
	public boolean isFull() 
	{
		return (write+1) % capacity == read;
		
	}
	  
	//Removes all entries from this queue. 
	//think: can it potentially be done in one operation?
	public void clear() 
	{
		read = 0;
		write = 0;
		size = 0;
		for (int i = 0; i < theArray.length; i++)
		{
			theArray[i] = null;
		}
	}
	
	//implement the random reordering of the elements in theARray
	//try to come up with an efficient algorithm by yourself.
	//if not - check out Fisher-Yates shuffle algorithm, 
	//also known as the Knuth shuffle algorithm
	//be careful - you only need to shuffle elements 
	//between read and write index - not including the unoccupied slots of the array.
	public void shuffle() 
	{
		 int elementsToShuffle = size;
		 for (int i = read; i < (read + elementsToShuffle - 1); i++)
		 {
			int randomIndex = i + (int)(Math.random() * (elementsToShuffle - (i - read)));
			T temp = theArray[i % capacity];
			theArray[i % capacity] = theArray[randomIndex % capacity];
			theArray[randomIndex % capacity] = temp;
		 }
	}

	public void shuffle(long seed)
	{
		Random rand = new Random(seed);
		for (int i = 0, pos = read; i < size; pos++, i++)
		{
			int currentIndex = (pos) % capacity;
			int r = rand.nextInt(size - i);
			int randomIndex = (read + r) % capacity;

			T temp = theArray[currentIndex];
			theArray[currentIndex] = theArray[randomIndex];
			theArray[randomIndex] = temp;
		}
	}
	
	//this is used for debugging and testing
	//please do not change!
	public String toString() {
		StringBuilder result = new StringBuilder("SQueue: the array [" + theArray[0] );
        for (int i = 1; i < capacity; i++) {
        	result.append(", ");
        	result.append(theArray[i]);
        }
        result.append("] ");        
       
        result.append("Capacity:" + this.capacity);
        result.append(" Size:" + this.size);
        result.append(" Read:" + this.read);
        result.append(" Write:" + this.write);
        return result.toString();
	}

}
