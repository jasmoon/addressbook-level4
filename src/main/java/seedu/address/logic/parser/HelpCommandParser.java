package seedu.address.logic.parser;

import seedu.address.logic.commands.*;
import seedu.address.logic.parser.exceptions.ParseException;

import java.util.ArrayList;

//@@author jasmoon

/**
 * Parses input arguments and create a new HelpCommand object.
 */
public class HelpCommandParser implements Parser<HelpCommand> {


    public final ArrayList<String> availableCommands;
    public static String commandRequest;
    public static final String MESSAGE_INVALID_HELP_COMMAND = "Help for command is unknown or not available";

    /**
     * HelpCommandParser constructor - creates an ArrayList which contains all commands open to the help function.
     */
    public HelpCommandParser()  {
        availableCommands = new ArrayList<String>();
        availableCommands.add("add");
        availableCommands.add("delete");
        availableCommands.add("edit");
        availableCommands.add("find");
        availableCommands.add("select");
    }

    /**
     * Parses the given {@code String} of arguments in the context of the HelpCommand
     * and returns an HelpCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */

    public HelpCommand parse(String args) throws ParseException {

        commandRequest = args.trim();
        if (commandRequest.length() == 0) {
            return new HelpCommand();
        }else {
            if(availableCommands.contains(commandRequest))
                return new HelpCommand(args);
            else
                throw new ParseException(MESSAGE_INVALID_HELP_COMMAND);
        }
    }
}


