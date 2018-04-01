package seedu.address.logic.parser;

import java.util.ArrayList;

import seedu.address.commons.core.Messages;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.commands.ListCommand;

//@@author jasmoon

/**
 * Parses input arguments and create a new ListCommand object.
 */
public class ListCommandParser implements Parser<ListCommand> {

    public final ArrayList<String> availableCommands;

    public ListCommandParser()  {
        availableCommands = new ArrayList<String>();
        availableCommands.add("task");
        availableCommands.add("event");
    }

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns an ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        String commandRequest = args.trim();

        if (commandRequest.length() == 0)  {
            return new ListCommand();
        } else  {
            if (availableCommands.contains(commandRequest))   {
                return new ListCommand(commandRequest);
            } else  {
                throw new ParseException(String.format(Messages.MESSAGE_INVALID_LIST_REQUEST, commandRequest));
            }
        }
    }

}
