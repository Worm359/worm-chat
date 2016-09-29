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
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
            try {
                Thread.sleep(200);
                System.out.println("Shouting down ...");
                releaseResourses();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
		});
		
		UtilClass utilities = new UtilClass();
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
						System.out.println("ServerClass ending it's work.");
						break;
					}

				RunnableConnectionHandler client = new RunnableConnectionHandler(clientSocket);
				connections.add(client);
				(new Thread(client)).start();
			}	
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			releaseResourses();
		}
	}
	
	
	static public synchronized void releaseResourses()
	{
		try {if(serverSocket!=null && !serverSocket.isClosed()) serverSocket.close();}
			catch(IOException e) {e.printStackTrace();}
			try
			{
				for(RunnableConnectionHandler connectionIterator : connections)
				{
					connectionIterator.deleteItSelf();
				}	
			} 
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
	}
}


