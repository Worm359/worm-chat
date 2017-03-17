package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ialbert on 16.03.2017.
 */
public class Message {

    private String messageText;
    private String sender;
    private Map<String, String> properties = new HashMap<>();
    private static List<String> propAllowed = new ArrayList<>();
    static {
        propAllowed.add("sender");
        propAllowed.add("color");
        propAllowed.add("messageText");
    }

    public Message(String fromStream) {
        parseMessageFromStream(fromStream);
    }

    private void parseMessageFromStream(String fromStream) {
        String[] parts = fromStream.split("~");
        for(String part : parts) {
            String prop = part.substring(0, part.indexOf('='));
            String val = part.substring(part.indexOf('=')+1, part.length());
            addProperty(prop, val);
        }
        messageText = properties.get("messageText");
        sender = properties.get("sender");
        //message text
        //sender
    }

    private void addProperty(String prop, String val) {
        if(propAllowed.contains(prop))
            properties.put(prop, val);
        else System.out.println("WARN: unknown property in message.");
    }

    public Message(String sender, String messageText) {
        this.sender = sender;
        this.messageText = messageText;
    }


    public boolean isCommand() {
        return messageText.startsWith("/") && messageText.length()>1;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getCommandText() {
        if(!isCommand()) return null;
        else {
            if(messageText.contains(" "))
                return messageText.substring(1, messageText.indexOf(" "));
            else
                return messageText.substring(1, messageText.length());
        }
    }

    public String getMessageText() {
        return this.messageText;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for(String prop : properties.keySet()) {
            res.append(prop + "=" + properties.get(prop) + "~");
        }
        res.append("sender=" + sender + "~" + "messageText=" + messageText);
        return res.toString();
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
}
