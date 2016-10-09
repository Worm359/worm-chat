package client;
import java.io.Console;
import java.io.*;
import java.io.IOException;
import java.net.*;
import utilities.client.UtilClientClass;
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
	public PrintWriter printer = null;
	private String name;
	public ClientClass() throws ConsoleException, IOException
	{	
		try
		{ 
			console = System.console();
			if(console==null)
			{
				throw (new ConsoleException("Error while creating console; WormClient cannot be started in this environment."));
			}
			name=console.readLine("Enter your name: ");
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
		/*
		//Add JVM shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
			    try {
				Thread.sleep(200);
				client.stopServer("JVM shouting down...");
			    } catch (InterruptedException e) {
				e.printStackTrace();
			    }
			}
			});*/
		
		//create vars for messages, client, receiver
		String message = null;
		ClientClass client = null;
		RunnableClientReceiver receiver = null;
		try
		{
			client = new ClientClass();
			receiver = new RunnableClientReceiver(client.clientSocket, client);
		}
		catch(ConsoleException e)
		{
			e.printStackTrace();
			return;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			client.stopClient("Error while creating a socket or openning a stream.");
			return;
		}
		(new Thread(receiver)).start();

		//main loop on sending messegers to server
		while(client.exitFlag!=true && ((message = client.console.readLine(client.name+": "))!=null))
		{
			if(client.exitFlag!=true)
			{
				if(UtilClientClass.isCommand(message))
						UtilClientClass.execute(client, message);	
				client.printer.print(message+"\n");
				if(client.printer.checkError())
				{
					client.stopClient("error occured while printing to output stream in main loop");
				}
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
}
