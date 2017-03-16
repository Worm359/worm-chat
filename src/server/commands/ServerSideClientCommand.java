package server.commands;

import common.Message;
import server.RunnableClient;

/**
 * Created by ialbert on 16.03.2017.
 */
public abstract class ServerSideClientCommand implements Cloneable{
    public final static String VALIDATION_SUCCESS_CODE = "Command validation OK.";
    Message message;
    RunnableClient client;
    public ServerSideClientCommand() {}
    public ServerSideClientCommand(RunnableClient client, Message message) {
        if(message!=null && client!=null) {
            this.message = message;
            this.client = client;
        } else throw new IllegalArgumentException();
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setClient(RunnableClient client) {
        this.client = client;
    }

    public ServerAnswerToClient executeItself() {
        if(VALIDATION_SUCCESS_CODE.equals(validate()))
            return new ServerAnswerToClient(innerExecute());
        else return new ServerAnswerToClient(validate());
    }

    /**
     * Override in subclasses.
     * @return
     * VALIDATION_SUCCESS_CODE - if command passes validation
     * any other string - error message to send to client
     */
    protected String validate() {
        return VALIDATION_SUCCESS_CODE;
    }

    /**
     * Concreate job, done by command.
     * @return
     */
    protected abstract String innerExecute();

    /**
     * For fabric method.
     * @return
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Some shit with ServerSideClientCommand cloning...");
        }
    }
}
