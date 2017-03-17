package server.commands;

import common.Command;
import common.Message;
import server.RunnableClient;

/**
 * Created by ialbert on 16.03.2017.
 */
public abstract class ServerSideClientCommand extends Command implements Cloneable{
    //client field added, so that server commands would have access to the client representing object
    RunnableClient client;
    // Removed check if client not null, for fabric method
    public ServerSideClientCommand(RunnableClient client, Message message) {
        super(message);
        this.client = client;
    }

    public void setClient(RunnableClient client) {
        this.client = client;
    }
}
