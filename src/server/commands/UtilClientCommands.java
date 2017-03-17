package server.commands;
import common.Message;
import server.RunnableClient;

public class UtilClientCommands
{
	public static void executeCommand(ServerSideClientCommand command)
	{
		String answer = command.execute();
		if(!"quit".equals(command.getCommandText())) command.client.sendMessageToClient(new Message("Server", answer));
	}
}
