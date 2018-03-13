package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowHelpRequestEvent;

/**
 * Format full help instructions for every command for display or command requested
 */
public class HelpCommand extends Command {

    private static String commandRequest = null;
    public static final String COMMAND_WORD = "help";
    public static final String COMMAND_ALIAS = "man";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "/" + COMMAND_ALIAS + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";


    //@@author jasmoon
    public HelpCommand()    {
    }

    public HelpCommand(String args) {
        commandRequest = args.trim();
    }

    @Override
    public CommandResult execute() {
        if (commandRequest == null)   {
            EventsCenter.getInstance().post(new ShowHelpRequestEvent());
            return new CommandResult(SHOWING_HELP_MESSAGE);
        } else   {
            switch(commandRequest) {

            case AddCommand.COMMAND_WORD:
                return new CommandResult(AddCommand.MESSAGE_USAGE);

            case EditCommand.COMMAND_WORD:
                return new CommandResult(EditCommand.MESSAGE_USAGE);

            case SelectCommand.COMMAND_WORD:
                return new CommandResult(SelectCommand.MESSAGE_USAGE);

            case DeleteCommand.COMMAND_WORD:
                return new CommandResult(DeleteCommand.MESSAGE_USAGE);

            case FindCommand.COMMAND_WORD:
                return new CommandResult(FindCommand.MESSAGE_USAGE);

            default:
                return new CommandResult(MESSAGE_USAGE);
            }
        }
    }

    public static String getCommandRequest() {
        return commandRequest;
    }
}
