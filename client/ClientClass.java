package client;
import java.io.Console;
import java.io.*;
import java.net.*;
class ConsoleException extends Exception
{
	public ConsoleException(String s)
	{
		super(s);
	}
}
public class ClientClass
{

	Console console;
	static private Socket clientSocket = null;
	public boolean exitFlag = false;
	private PrintWriter printer = null;
		
	public ClientClass() throws ConsoleException, IOException
	{	
		try
		{ 
			console = System.console();
			if(console==null)
			{
				throw (new ConsoleException("Error while creating console; WormClient cannot be started in this environment."));
			}
			clientSocket = new Socket ("127.0.0.1", 1234);
			printer = new PrintWriter(clientSocket.getOutputStream());
		}
		catch(IOException e)
		{
			throw e;
		}
	}
	//comand!!! create a socket connection, with name.
	//start a thread for sending messages.
	//hear for messages in the current thread.
	public static void main(String [] args)
	{
		String message = null;
		ClientClass client = null;
		try
		{
		client = new ClientClass();
		}
		catch(ConsoleException e)
		{
			e.printStackTrace();
			return;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			client.stopClient("Error while creating a socket.");
			return;
		}
		while(client.exitFlag!=true && ((message = client.console.readLine())!=null))
		{
			if(client.exitFlag!=true)
				client.printer.print(message+"\n");

			if(client.printer.checkError())
			{
				client.stopClient("error occured while printing to output stream in main loop");
			}
		}
	}

	public synchronized void stopClient(String code)
	{
		if(exitFlag!=true)
		{
			exitFlag=true;
			System.out.println("WormClient is stopping; \n Shutting down command was called with code: '"+code+"'");
			releaseClientResourses();

		}
		else
		{
			System.out.println("Somebody want to kill client again with code: '"+code+"'");
		}
	
	}

 	private void releaseClientResourses()
	{
		try
		{
			if((clientSocket!=null)&&(!clientSocket.isClosed()))
				clientSocket.close();		
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}	
	}	
	/*
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
	*/
}
