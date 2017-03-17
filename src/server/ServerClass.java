package server;

import java.io.*;
import java.net.*;
import java.util.*;

import common.Message;
import utilities.server.MessageQueue;
import utilities.server.*;

public class ServerClass
{
	static ArrayList<RunnableClient> connections = new ArrayList<RunnableClient>();
	static ServerSocket serverSocket = null;
	public static boolean exitFlag = false;
	private static MessageQueue messagesQueue = new MessageQueue(new Message("Server", "Welcome!"), 10);
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
		UtilClass utilities = null;
		ServerClass server = null;

		try
		{
			server = new ServerClass();
			
			utilities = new UtilClass(server);
			(new Thread(utilities)).start();

			Socket clientSocket = null;
			
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
		RunnableClient client = new RunnableClient(this, clientSocket);
		(new Thread(client)).start();

		connections.add(client);

		//send messages
		for(MessageQueue.MessageIterator iter = messagesQueue.iterator(); iter.hasPrevious(); )
		{
			MessageQueue.MessageQueueElement singleMessage = iter.previous();
			client.sendMessageToClient(singleMessage.getMessage());
		}	
	}


	
	public void sendMessage(RunnableClient sender, Message message)
	{
		for(RunnableClient connectionIterator : connections)
		{
			if(connectionIterator!=sender)
			{
				connectionIterator.sendMessageToClient(message);
			}
		}	
		synchronized(messagesQueue) {messagesQueue.push(message);}
	
	}

	static public void removeClient(RunnableClient client, String code)
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
			for(RunnableClient connectionIterator : connections)
			{
				connectionIterator.closeConnection("Server class is releasing RunnableConnectionHandler: '"+connectionIterator.getClientName()+"'");
			}
	}
}


