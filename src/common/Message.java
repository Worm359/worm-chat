package common;

/**
 * Created by ialbert on 16.03.2017.
 */
public class Message {

    public Message(String arg) {
    }

    public boolean isCommand() {
        return false;
    }

    public String getCommandText() {
        return "";
    }
}
