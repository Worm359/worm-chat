package utilities.server;
import java.util.NoSuchElementException;
public class StringIterator 
{
	public StringQueueElement element;
	public StringQueue queue;

	public StringIterator(StringQueue queue)
	{
			this.queue=queue;
	}

	public StringQueueElement previous()
	{
		if(element==null)
		{
			element=queue.getLast();
			return element;
		}
		else if(!this.hasPrevious())
			throw new NoSuchElementException();
		else
		{
			element=element.getPrevious();
			return element;
		}
	}
	public boolean hasPrevious()
	{
		if((element==null) || element.getPrevious()!=null)
			return true;
		else
			return false;
	}

	public static void main(String [] arg)
	{
		StringQueue queue = new StringQueue("string1", 10);
		System.out.println("Striniterator entry");
		for(int i=2; i<=10; i++)
		{
			System.out.println(i);
			queue.push("string" + i);
		}
		for(StringIterator iter=queue.iterator(); iter.hasPrevious(); )
		{
			StringQueueElement el = iter.previous();
			System.out.println("String in queue: "+ el.getStr());
		}

	}


}
