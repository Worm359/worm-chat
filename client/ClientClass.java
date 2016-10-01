package client;
import java.io.Console;
import java.io.*;
import java.net.*;
private ConsoleException extends Exception
{
	public ConsoleException()
	{
		super();
	}
}
public class ClientClass
{

	Console console;
	private Socket clientSocket = null;
	public ClientClass()
	{	
		try
		{ 
			console = System.console();
			clientSocket = new Socket ("127.0.0.1", 1234);
			if(console==null)
			{
				throw (new ConsoleException());
			}
		}
		catch(UnknownHostException e)
		{
			throw e;
		}
		catch(IOException e)
		{
			throw e;	
		}
	}
	//create a socket connection, with name.
	//start a thread for sending messages.
	//hear for messages in the current thread.
	public static void main(String [] args)
	{
		try
		{
		ClientClass client = new ClientClass();
		}
		catch(ConsoleException e)
		{
			e.printStackTrace();
			client.stopClient("Error while creating console; WormClient cannot be started in this environment.");
			return;
		}
		catch(UnknownHostException|IOException e)
		{
			e.printStackTrace();
			client.stopClient("Error while creating a socket.");
			return;
		}

		
	}

	boolean isAlive()
	{

	}
	
	String changeColor()
	{

	}

	String changeNickName()
	{

	}

	void getMessage()
	{

	}
}
