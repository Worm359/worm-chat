package utilities.server;

import common.Message;

import java.util.NoSuchElementException;

/**
 * Created by ialbert on 16.03.2017.
 */
public class MessageQueue {
    final int size;
    private MessageQueueElement lastEl;
    private MessageQueueElement bottomEl;
    private int filled;

    public MessageQueue(Message message, int size) {
        if((size<=2))
			throw new IllegalArgumentException("There are at least 2 elements in Queue, also string must be not null.");
		else
			this.size = size;
		lastEl = new MessageQueueElement(message);
		bottomEl = lastEl;
		filled=1;
    }

    public void push(Message message)
	{
		MessageQueueElement newOne = new MessageQueueElement(message);
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

	public MessageIterator iterator() {
        return new MessageIterator(this);
    }

    public MessageQueueElement getFirst()
	{
		return lastEl;
	}
	public MessageQueueElement getLast()
	{
		return bottomEl;
	}

    public static class MessageIterator {
        public MessageQueueElement element;
	    public MessageQueue queue;

	    public MessageIterator(MessageQueue queue)
	    {
	    		this.queue=queue;
	    }

	    public MessageQueueElement previous()
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
    }

    public static class MessageQueueElement {
        private Message message;
        MessageQueueElement previous;
        MessageQueueElement next;

        public MessageQueueElement(Message message) {
            if(message==null) throw new IllegalArgumentException();
            this.message = message;
        }

        public Message getMessage() {
            return message;
        }

        public void setNext(MessageQueueElement next) {
            this.next = next;
            if(next!=null)
	    		next.setPrevious(this);
        }

        public MessageQueueElement getNext() {
            return next;
        }


	    public boolean hasNext()
	    {
	    	if(this.next==null)
	    		return false;
	    	else
	    		return true;
	    }

	    public void setPrevious(MessageQueueElement previous)
	    {
	    	this.previous = previous;
	    }

	    public MessageQueueElement getPrevious()
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
}
