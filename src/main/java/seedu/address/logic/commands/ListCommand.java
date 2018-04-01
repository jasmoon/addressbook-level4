package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowActivityRequestEvent;
import seedu.address.commons.events.ui.ShowEventOnlyRequestEvent;
import seedu.address.commons.events.ui.ShowTaskOnlyRequestEvent;

/**
  * Lists task or events, or both.
  */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_ALIAS = "ls";

    public static final String MESSAGE_SUCCESS = "Listed all tasks and events!";
    public static final String MESSAGE_SUCCESS_TASK = "Listed all tasks!";
    public static final String MESSAGE_SUCCESS_EVENT = "Listed all events!";

    private String commandRequest = null;

    public ListCommand()    {
    }

    public ListCommand(String commandRequest) {
        this.commandRequest = commandRequest;
    }

    @Override
    public CommandResult execute() {

        if (commandRequest == null)  {
            EventsCenter.getInstance().post(new ShowActivityRequestEvent());
            return new CommandResult(MESSAGE_SUCCESS);
        }
        switch(commandRequest)  {

        case "task":
            EventsCenter.getInstance().post(new ShowTaskOnlyRequestEvent());
            return new CommandResult(MESSAGE_SUCCESS_TASK);

        case "event":
            EventsCenter.getInstance().post(new ShowEventOnlyRequestEvent());
            return new CommandResult(MESSAGE_SUCCESS_EVENT);

        default:
            return new CommandResult(HelpCommand.MESSAGE_USAGE);
        }
    }
}
