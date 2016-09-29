package server;

import java.io.*;
import java.net.*;
import java.util.*;
import utilities.UtilClass;


class RunnableConnectionHandler implements Runnable
{
	Socket socket = null;
	InputStream byteStream = null;
	InputStreamReader charStream = null;
	BufferedReader textStream = null;
	String clientName = null;
	/* 
	OutputStream outByteStream = null;
	OutputStream
	*/
	
	public RunnableConnectionHandler(Socket socket) throws IOException
	{
		this.socket = socket;
		try
		{
			byteStream = socket.getInputStream();
			charStream = new InputStreamReader(byteStream);
			textStream = new BufferedReader(charStream);
		} catch (IOException e)
		{
			this.closeConnection();
			throw e;
		}
		
	}
		
	public synchronized void run()
	{
		try
		{
			String message;
			while((message=textStream.readLine())!=null)
			{		
				System.out.println("Message from " + getClientName() +": "+ message);
			}
		} catch(IOException e) 
		{
			if(socket.isClosed())
				System.out.println("Connection with client "+ getClientName() + " is closed.");
			else
			{
				e.printStackTrace(); this.closeConnection();
			}
		}
	}
	
	void closeConnection() 
	{
		System.out.println("RunnableConectionHandler closeConnection() entry;");
		try
		{
			synchronized(socket)
			{
				if (socket!=null && !socket.isClosed())	
							socket.close();

			}
		//	if (socket!=null && !socket.isClosed())	
		//		socket.close();
		}
	       	catch(IOException e) 
		{
			System.out.println("RunnableConnectionHandler closeConnection() exception"); 
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
