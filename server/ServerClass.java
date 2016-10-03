package server;

import java.io.*;
import java.net.*;
import java.util.*;
import utilities.UtilClass;

public class ServerClass
{
	static ArrayList<RunnableConnectionHandler> connections = new ArrayList<RunnableConnectionHandler>();
	static ServerSocket serverSocket = null;
	public static boolean exitFlag = false;

	public static void main(String [] args)
	{
		UtilClass utilities = new UtilClass();
		Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
            try {
                Thread.sleep(200);
		utilities.stopServer("JVM shouting down...");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
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
				connectionIterator.sendMessageToClient(String message);
			}
		}	
	
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
			try
			{
				//System.out.println("now Runnable closing");
				for(RunnableConnectionHandler connectionIterator : connections)
				{
					System.out.println("RunnableConnectionHandler closing with name '"+connectionIterator.getClientName()+"'.");

					connectionIterator.closeConnection();
				}	
			} 
			//catch(Exception e)
			//{
			//	e.printStackTrace();
			//}
			
	}
}


