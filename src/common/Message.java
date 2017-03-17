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
    /**
     * todo toString must throw all the fields into string
     *
     * For now, it only pushes sender and text.
     * After change will be done, to add new properties for a message:
     * 1) Add to propAllowed
     * 2) Add into toString()
     *
     */
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
    }

    public Message(String sender, String messageText) {
        this.sender = sender;
        this.messageText = messageText;
    }

    public boolean isCommand() {
        return messageText.startsWith("/") && messageText.length()>1;
    }


    /**
     * When a message is passed to a stream, toString method must correspond
     * to #parseMethodFromStream(...)
     * Also, client side must take care of escaping '~' symbol in messages
     * @return
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for(String prop : properties.keySet()) {
            res.append(prop + "=" + properties.get(prop) + "~");
        }
        res.append("sender=" + sender + "~" + "messageText=" + messageText);
        return res.toString();
    }

    public String getSender() {
        return sender;
    }

    public String getCommandText() {
        if(!isCommand()) return null;
        else {
            if(messageText.contains(" ")) // if there is arguments
                return messageText.substring(1, messageText.indexOf(" "));
            else
                return messageText.substring(1, messageText.length());
        }
    }

    public String getMessageText() {
        return this.messageText;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * Map defines list of available properties: sender, color, importancy, etc.
     */
    private void addProperty(String prop, String val) {
        if(propAllowed.contains(prop))
            properties.put(prop, val);
        else System.out.println("WARN: unknown property in message.");
    }
}
