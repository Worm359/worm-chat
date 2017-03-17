package common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ialbert on 16.03.2017.
 */

/**
 * To send a message:
 * 1) message("Name", "text");
 * 2) addProperty(...) - color, level, smth else
 * 3) send message.toString()
 *
 * To receive a message:
 * 1) message(fromStream) (fromStream must be not null)
 * 2) possible irrelevant condition: if message isn't formatted "key1=value1~key2=value2..."
 *    in that case:
 *          messageText = null
 *          sender = null
 *    plus
 */
public class Message {

    private String messageText;
    private String sender;
    /**
     * Object, which creates message, is responsible for adding properties.
     */
    private Map<String, String> properties = new HashMap<>();

    /**
     * @param fromStream - must be not null
     */
    public Message(String fromStream) {
        parseMessageFromStream(fromStream);
    }

    private void parseMessageFromStream(String fromStream) {
        String[] parts = fromStream.split("~");
        for(String part : parts) {
            int equalPos = part.indexOf('=');
            if(equalPos < part.length()-1 && equalPos>0) {
                String prop = part.substring(0, equalPos);
                String val = part.substring(equalPos+1, part.length());
                addProperty(prop, val);
            }
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

    /**
     * Map defines list of available properties: sender, color, importancy, etc.
     */
    public void addProperty(String prop, String val) {
        properties.put(prop, val);
    }
}
