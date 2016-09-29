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
			this.deleteItSelf();
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
				System.out.println("Message from client: " + message);
			}
		} catch(IOException e) {e.printStackTrace(); this.deleteItSelf();}
	}
	
	void synchronized deleteItSelf() 
	{
		try
		{
			if (socket!=null && !socket.isClosed())	
				socket.close();
		} catch(IOException e) {e.printStackTrace();}
	}
	
}
