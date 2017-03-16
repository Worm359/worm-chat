package server.commands;

import common.Message;

/**
 * Created by ialbert on 16.03.2017.
 */
public class ServerAnswerToClient extends Message {
    public ServerAnswerToClient(String string) {
        super(string);
    }
}
