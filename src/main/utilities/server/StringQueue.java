package utilities.server;
public class StringQueue
{
	final int size;
	private StringQueueElement lastEl;
	private StringQueueElement bottomEl;
	private int filled;
	public StringQueue(String arg, int size)
	{
		if((size<=2))
			throw new IllegalArgumentException("There are at least 2 elements in Queue, also string must be not null.");
		else
			this.size = size;
		lastEl = new StringQueueElement(arg);
		bottomEl = lastEl;
		filled=1;
	}
	//public getLast
	//public push, if <10 all simple, if >10 go to the 10th end destroy link to 11
	public static void main(String [] args)
	{
		StringQueue queue = new StringQueue("string1", 10);
		for(int i=1; i<=10; i++)
		{
			queue.push("string"+i);
		}
		StringQueueElement single = queue.getLast();
		while(true)
		{	
			System.out.println("StringElement: "+single.getStr());
			if(!single.hasPrevious())
				break;
			else
				single=single.getPrevious();
		}
		queue.push("11 string yo");
		System.out.println("new");
		single=queue.getLast();
		while(true)
		{	
			System.out.println("StringElement: "+single.getStr());
			if(!single.hasPrevious())
				break;
			else
				single=single.getPrevious();
		}
	}


	public StringIterator iterator()
	{
		return new StringIterator(this);

	}

	public void push(String str)
	{
		StringQueueElement newOne = new StringQueueElement(str);
		newOne.setNext(lastEl);
		if(filled==size)
		{
			bottomEl = bottomEl.getPrevious();
			bottomEl.setNext(null);
		}
		else
		{
			filled+=1;
		}
		lastEl = newOne;
		
	}
	public StringQueueElement getFirst()
	{
		return lastEl;
	}
	public StringQueueElement getLast()
	{
		return bottomEl;
	}
}
