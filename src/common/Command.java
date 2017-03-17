package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by ialbert on 17.03.2017.
 */
public abstract class Command {
    Message message;

    /**
     * Can overwrite in subclasses, so it would be used in validation.
     * Changing regex can result in exceptions from parseCommand.
     */
    protected String regex = "^/[a-z]+(\\s#([a-zA-Z0-9]+))*$";

    protected String command;
    protected List<String> arguments = new ArrayList<>();
    protected String VALIDATION_SUCCESS_CODE = "Command validation OK.";
    protected String VALIDATION_ERROR_CODE = "Validation error: command should look like " + regex;
    protected final static String UNKNOWN_COMMAND = "unknown_command";

    public Command(Message message) {
        this.message = message;
        if(message!=null) parseCommand();
    }

    /**
     * Fills fields:
     *      command
     *      arguments
     * Shouldn't produce any exceptions.
     * Used internally, environment should just call constructor with not null Message, or
     * set it explicitly via setMessage(...)
     */
    private void parseCommand() {
        String messageText = message.getMessageText();
        if(messageText==null || messageText.length()<=1 || !isValid()) {
            command = UNKNOWN_COMMAND;
        } else {
            String[] parts = messageText.split("\\s");
            command = parts[0].substring(1);
            if(parts.length>1) {
                parts = Arrays.copyOfRange(parts, 1, parts.length);
                for(String arg : parts) {
                    arguments.add(arg.substring(1));
                }
            }
        }
    }

    /**
     * Used by a fabric realization, when message null throws RuntimeException
     */
    public void setMessage(Message message) {
        if(message==null) throw new RuntimeException("Setting null message for command");
        else {
            this.message = message;
            parseCommand();
        }
    }

    public String execute() {
        if (VALIDATION_SUCCESS_CODE.equals(validate()))
            return innerExecute();
        else return validate();
    }

    protected boolean isValid() {
        if(message==null) return false;
        return message.getMessageText().matches(regex);
    }

    /**
     * Override in subclasses.
     * @return
     * VALIDATION_SUCCESS_CODE - if command passes validation
     * any other string - error message to send to client
     */
    protected String validate() {
        if(isValid())
            return VALIDATION_SUCCESS_CODE;
        else
            return VALIDATION_ERROR_CODE;
    }

    /**
     * Actual job, done by command.
     * @return ---> execute() ----> returns answer to command execution.
     */
    protected abstract String innerExecute();

    /**
     * For fabric method.
     * @return
     */
    @Override
    public Object clone() {
        try {
            Command clone = (Command) super.clone();
            clone.clearArgs();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Some shit with ServerSideClientCommand cloning...");
        }
    }
    /*
    * For deep cloning
    */
    private void clearArgs() {
        this.arguments = new ArrayList<>();
    }

    public String getCommandText() {
        return message.getCommandText();
    }

    /**
     * So that execute returns readable code
     */
    public void setValidation_Error_Code(String VALIDATION_ERROR_CODE) {
        this.VALIDATION_ERROR_CODE = VALIDATION_ERROR_CODE;
    }
}
