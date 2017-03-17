package server.commands;

import common.Command;
import common.Message;
import server.RunnableClient;
import server.ServerClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ialbert on 16.03.2017.
 */
public  class ServerSideClientCommandFabric {
    public static Command getCommand(RunnableClient client, Message message) {
        if(!message.isCommand()) throw new RuntimeException("Message passed to fabric is not a command.");
        ServerSideClientCommand command;
        command = commands.get(message.getCommandText());
        if(command == null) {
            command = new ServerSideClientCommand(client, message) {
                            @Override
                            protected String innerExecute() {
                                return "Unknown command; list of available server commands:\n" + mapToString(commands);
                            }
                        };
        } else {
            command = (ServerSideClientCommand) command.clone();
        }
        command.setMessage(message);
        command.setClient(client);
        return command;
    }

    /**
     * Prototype realization.
     * 1) Implement innerExecute, with possibility to use ServerSideClientCommand client reference.
     * 2) Override regex if needed.
     * 3) Set VALIDATION_ERROR_CODE if u have what to say.
     */
    private static HashMap<String, ServerSideClientCommand> commands = new HashMap<>();
    static {
        ServerSideClientCommand setName = new ServerSideClientCommand(null, null) {
            String regex = "^/[a-z]+(\\s#([a-zA-Z0-9]+)){1}$";
            @Override
            public String innerExecute() {
                String newName = arguments.get(0);
                client.setClientName(newName);
                String answer = "Your name was successfully changed to '" + newName + "'.";
                return answer;
            }
        };
        setName.setValidation_Error_Code("Wrong command format. Should be: /setname #newName");
        commands.put("setname", setName);

        commands.put("quit", new ServerSideClientCommand(null, null) {
            @Override
            protected String innerExecute() {
                client.closeConnection("client closed connection");
			    ServerClass.removeClient(client, "user '"+client.getClientName()+"' said good bye!");
			    return "";
            }
        });
    }

    private static String mapToString(Map<String, ?> map) {
        StringBuilder res = new StringBuilder();
        int i = 0;
        for(String comm : map.keySet()) {
            res.append(i + ") " + comm + "\n");
        }
        return res.toString();
    }
}
