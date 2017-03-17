package server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
//import utilities.server.UtilClass;
import common.Message;
import server.commands.ServerSideClientCommand;
import server.commands.ServerSideClientCommandFabric;
import utilities.server.UtilClass;
import server.commands.UtilClientCommands;
public class RunnableClient implements Runnable
{
	public ServerClass server = null;
	private Socket socket = null;
	private InputStream byteStream = null;
	private InputStreamReader charStream = null;
	private BufferedReader textStream = null;
	private PrintWriter messenger = null;
	private String clientName = null;
	private Map<String, String> messagesProperties = new HashMap<>();

	public RunnableClient(ServerClass server, Socket socket) throws IOException
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
			String inputFromClient;
			Message message;
			while((inputFromClient=textStream.readLine())!=null)
			{
				message = new Message(inputFromClient);
				message.setSender(clientName);
			    //message = new Message(clientName, inputFromClient);
				/*if(!message.getCommandText().equals(clientName)) {
					System.out.println("WARN: unsychronized client names on server-client sides");
					message.setSender(clientName);
				}*/
				if(message.isCommand()) {
                    sendMessageToClient(
                            new Message("Server", ServerSideClientCommandFabric.getCommand(this, message).execute()));
                } else
				{
					UtilClass.print(message);
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

	public synchronized void sendMessageToClient(Message message)
	{
			if(!socket.isClosed())
				messenger.println(message.toString());
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
