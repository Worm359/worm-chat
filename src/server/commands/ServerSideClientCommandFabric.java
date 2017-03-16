package server.commands;

import common.Message;
import server.RunnableClient;

import java.util.HashMap;

/**
 * Created by ialbert on 16.03.2017.
 */
public  class ServerSideClientCommandFabric {
    public static ServerSideClientCommand getCommand(RunnableClient client, Message message) {
        if(!message.isCommand()) throw new RuntimeException("Message passed to fabric is not a command.");
        ServerSideClientCommand command;
        command = commands.get(message.getCommandText());
        command = (ServerSideClientCommand) command.clone();
        command.setMessage(message);
        command.setClient(client);
        return command;
    }

    private static HashMap<String, ServerSideClientCommand> commands = new HashMap<>();
    static {
        commands.put("setname", new ServerSideClientCommand() {
            @Override
            public String innerExecute() {
                client.setClientName(argument);
                String answer = "Your name was successfully changed to '" + argument + "'.";
                return answer;
            }
            @Override
            public String validate() {
                return ServerSideClientCommand.VALIDATION_SUCCESS_CODE;
            }
        });
    }
}
