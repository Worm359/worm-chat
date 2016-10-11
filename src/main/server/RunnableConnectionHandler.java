package server;

import java.io.*;
import java.net.*;
import java.util.*;
//import utilities.server.UtilClass;
import utilities.server.UtilClientCommands;
public class RunnableConnectionHandler implements Runnable
{
	public ServerClass server = null;
	Socket socket = null;
	InputStream byteStream = null;
	InputStreamReader charStream = null;
	BufferedReader textStream = null;
	//OutputStream outputByteStream = null;
	PrintWriter messenger = null; 
	String clientName = null;
	/* 
	OutputStream outByteStream = null;
	OutputStream
	*/
	
	public RunnableConnectionHandler(ServerClass server, Socket socket) throws IOException
	{
		this.server = server;
		this.socket = socket;
		try
		{
			byteStream = socket.getInputStream();
			charStream = new InputStreamReader(byteStream);
			textStream = new BufferedReader(charStream);

			messenger = new PrintWriter(socket.getOutputStream());
		} catch (IOException e)
		{
			this.closeConnection("failed to create instance of RunnableConnectionHandler");
			throw e;
		}

		
	}
		
	public void run()
	{
		//read lines from client
		try
		{
			String message;
			while((message=textStream.readLine())!=null)
			{		
//TODO parse message or command, if message serer.sendMessage, otherwise, command to static UtilClass.execCommand();
				if(UtilClientCommands.isCommand(message))
					UtilClientCommands.executeCommand(this, message);
				else
				{
					System.out.println(getClientName() +": "+ message);
					server.sendMessage(this, message);
				}
			}
		} catch(IOException e) 
		{
			if(socket.isClosed())
			{
				System.out.println("Connection with client "+ getClientName() + " is closed.");

			}
			else
			{
				e.printStackTrace(); 
				this.closeConnection("failed to read message from client");
			}
			
		}
		finally
		{
			server.removeClient(this, "RCH thread with name '"+getClientName()+"' ends execution");
		}
	}
	
	public void beginCommunication()
	{

	}

	public synchronized void sendMessageToClient(String message)
	{
			if(!socket.isClosed())
				messenger.println(message);
			if(messenger.checkError())
				this.closeConnection("unable to send message to client");
	}

	public synchronized void closeConnection(String code) 
	{
		try
		{
			System.out.println("RunnableConnectionHandler is closing with code: '"+code+"'");
				if (socket!=null && !socket.isClosed())	
							socket.close();

		//	if (socket!=null && !socket.isClosed())	
		//		socket.close();
		}
	       	catch(IOException e) 
		{
			System.out.println("RunnableConnectionHandler closeConnection() catched IOException"); 
			e.printStackTrace();
		}

	}
	public String getClientName()
	{
		if (clientName!=null)
			return clientName;
		else return "null";
	}
	public void setClientName(String name)
	{
		clientName = name;
	}
}
