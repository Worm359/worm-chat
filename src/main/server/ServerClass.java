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
	public final int port;

	public final static int DEFAULT_PORT=1234;

	public ServerClass() throws IOException
	{
		this(DEFAULT_PORT);
	}

	public ServerClass(int port_arg) throws IOException
	{
		serverSocket = new ServerSocket(port_arg);
		port = port_arg;
	}

	

	public static void main(String [] args)
	{
		
/*
		//JVM shutdown hook for resource releasing
		Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
            try {
                Thread.sleep(200);
				
			UtilClass.stopServer("JVM shutting down...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		});
*/
		

		UtilClass utilities = null;
		ServerClass server = null;

		try
		{
			//serverSocket = new ServerSocket(1234); MAY THROW EXCEPTION
			server = new ServerClass();
			
			//class for serverside commands
			utilities = new UtilClass(server);
			//can input commands for serverside
			(new Thread(utilities)).start();

			Socket clientSocket = null;
			
			//MAY THROWS EXCEPTION
			server.listenForClients(clientSocket);
	
		} 
		catch (IOException e)
		{
			if(server!=null)
				UtilClass.stopServer(server, "Server main method exception.");
			e.printStackTrace();
		}
	}

	private void listenForClients(Socket socketVar) throws IOException
	{
		while(!exitFlag)
			{
				socketVar = serverSocket.accept();
				if(exitFlag==true)
					{
						releaseResourses();
						System.out.println("ServerClass main loop breaked;");
						break;
					}

				addClient(socketVar);
				
			}
	}




	private void addClient(Socket clientSocket) throws IOException
	{
		RunnableConnectionHandler client = new RunnableConnectionHandler(this, clientSocket);
		(new Thread(client)).start();
		client.beginCommunication();

		connections.add(client);

		//send messages
		for(StringIterator iter = messagesQueue.iterator(); iter.hasPrevious(); )
		{
			StringQueueElement singleMessage = iter.previous();
			client.sendMessageToClient(singleMessage.getStr());
		}	
	}


	
	public void sendMessage(RunnableConnectionHandler sender, String message)
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

	static public void removeClient(RunnableConnectionHandler client, String code)
	{
		client.closeConnection(code);
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


