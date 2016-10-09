package utilities.server;
import server.RunnableConnectionHandler;
import server.ServerClass;
public class UtilClientCommands
{
	public static boolean isCommand(String mess)
	{
		return mess.startsWith(":c ");
	}

	public static void executeCommand(RunnableConnectionHandler client, String command)
	{
		String answer;
		String comm = command.substring(3);
		System.out.println("command received: "+comm);
		if(comm.equalsIgnoreCase("quit"))
		{
			System.out.println("Client '"+client.getClientName()+"' is disconnecting with quit command.");
			client.closeConnection("client closed connection");
			ServerClass.removeClient(client);
		}
		else if(comm.matches("^setname\\s[a-zA-Z]+$"))
		{
			System.out.println("setname command entry!");
			client.setClientName(comm.substring(8));	
			answer = "setname command received; your name is '"+comm.substring(8)+"' now.";
			client.sendMessageToClient(answer);
		}

		/*
		switch(command.substring(3))
		{
			case "quit":
			{
				System.out.println("Client '"+client.getName()+"' is disconnecting with quit command.");
				client.stopClient("client closed connection");
			}
			case "setname":
			{
			}
		}
		*/
	}
}
