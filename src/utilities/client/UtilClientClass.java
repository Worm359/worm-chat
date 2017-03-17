package utilities.client;
import client.ClientClass;
import common.Message;

public class UtilClientClass
{
	public static String execute(ClientClass client, String command)
	{
		String returnCode="default";
		if(!command.startsWith(":c "))
			throw new IllegalArgumentException("UtilClientClass: not a command");
		//command = command.substring(3);
		switch(command.substring(3))
		{
			case "quit":
			{
				//client.printer.println(command);
				try
				{
					Thread.sleep(3000);
				}
				catch(InterruptedException e)
				{}
				returnCode="Quit command was received on client side.";
				client.stopClient(returnCode);
				break;
			}

		}
		return returnCode;
	}

	public static void print(Message message) {
		System.out.println(message.getSender() + ": " + message.getMessageText());
	}
}
