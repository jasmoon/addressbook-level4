package seedu.address.logic.parser;

import java.util.ArrayList;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;



//@@author jasmoon

/**
 * Parses input arguments and create a new HelpCommand object.
 */
public class HelpCommandParser implements Parser<HelpCommand> {

    public final ArrayList<String> availableCommands;

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

        String commandRequest = args.trim();
        if (commandRequest.length() == 0) {
            return new HelpCommand();
        } else {
            if (availableCommands.contains(commandRequest))
                return new HelpCommand(args);

            else
                throw new ParseException(formInvalidMessage(commandRequest));
        }
    }

    /**
     *
     * @param commandRequest
     * @return String message for invalid command request.
     */
    private String formInvalidMessage(String commandRequest)    {
        return "Help for '" + commandRequest + "' is unknown or not available";
    }
}


