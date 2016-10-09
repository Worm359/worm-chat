package utilities.server;
public class StringQueueElement
{
	private String str;
	private StringQueueElement next;
	private StringQueueElement previous;

	public StringQueueElement(String arg)
	{
		if(arg==null)
			throw new IllegalArgumentException();
		this.str = arg;
	}
	
	public String getStr()
	{
		return str;
	}

	public void setNext(StringQueueElement next)
	{
		this.next = next;
		if(next!=null)
			next.setPrevious(this);
	}
	
	public StringQueueElement getNext()
	{
		return next;
	}
	public boolean hasNext()
	{
		if(this.next==null)
			return false;
		else
			return true;
	}

	public void setPrevious(StringQueueElement previous)
	{
		this.previous = previous;
	}
	public StringQueueElement getPrevious()
	{
		return previous;
	}
	public boolean hasPrevious()
	{
		if(previous==null)
			return false;
		else
			return true;
	}

}


