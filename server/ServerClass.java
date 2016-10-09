package server;

import java.io.*;
import java.net.*;
import java.util.*;
import utilities.server.*;

public class ServerClass
{
	static ArrayList<RunnableConnectionHandler> connections = new ArrayList<RunnableConnectionHandler>();
	static ServerSocket serverSocket = null;
	public static boolean exitFlag = false;
	private static StringQueue messagesQueue = new StringQueue("Welcome to WormChat!!!", 10);
	public static void main(String [] args)
	{
		UtilClass utilities = new UtilClass();
		Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
            try {
                Thread.sleep(200);
		utilities.stopServer("JVM shutting down...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		});
		
		(new Thread(utilities)).start();

		/*
		InputStream byteStream = null; */
		try
		{
			serverSocket = new ServerSocket(1234);
			Socket clientSocket = null;
			while(!exitFlag)
			{
				clientSocket = serverSocket.accept();
				if(exitFlag==true)
					{
						releaseResourses();
						System.out.println("ServerClass main loop breaked;");
						break;
					}

				RunnableConnectionHandler client = new RunnableConnectionHandler(clientSocket);
				connections.add(client);
				(new Thread(client)).start();
				//send messages
				for(StringIterator iter = messagesQueue.iterator(); iter.hasPrevious(); )
				{
					StringQueueElement singleMessage = iter.previous();
					client.sendMessageToClient(singleMessage.getStr());
				}	
			}	
		} 
		
		catch (IOException e)
		{
			utilities.stopServer("Exception in ServerClass main loop!");
			e.printStackTrace();
		}
	}
	
	static public void sendMessage(RunnableConnectionHandler sender, String message)
	{
		for(RunnableConnectionHandler connectionIterator : connections)
		{
			if(connectionIterator!=sender)
			{
				connectionIterator.sendMessageToClient(message);
			}
		}	
		synchronized(messagesQueue) {messagesQueue.push(message);}
	
	}
	static public void removeClient(RunnableConnectionHandler client)
	{
		connections.remove(client);
	}

	static public synchronized void releaseResourses()
	{
			try 
			{if(serverSocket!=null && !serverSocket.isClosed()) 
				{
					System.out.println("ServerSocket closing");
					serverSocket.close();
				}
			}
			catch(IOException e) {e.printStackTrace();}
			//System.out.println("now Runnable closing");
			for(RunnableConnectionHandler connectionIterator : connections)
			{
				connectionIterator.closeConnection("Server class is releasing RunnableConnectionHandler: '"+connectionIterator.getClientName()+"'");
			}	
			//catch(Exception e)
			//{
			//	e.printStackTrace();
			//}
			
	}
}


