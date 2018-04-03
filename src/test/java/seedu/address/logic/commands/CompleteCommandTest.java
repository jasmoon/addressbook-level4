package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalActivities.getTypicalDeskBoard;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ACTIVITY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_ACTIVITY;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.activity.Activity;



public class CompleteCommandTest {
    private Model model = new ModelManager(getTypicalDeskBoard(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Activity activityToComplete = model.getFilteredTaskList().get(INDEX_FIRST_ACTIVITY.getZeroBased());
        CompleteCommand completeCommand = prepareCommand(INDEX_FIRST_ACTIVITY);

        String expectedMessage = String.format(CompleteCommand.MESSAGE_COMPLETE_TASK_SUCCESS, activityToComplete);

        ModelManager expectedModel = new ModelManager(model.getDeskBoard(), new UserPrefs());
        Activity completedActivity = activityToComplete.getCompletedCopy();
        expectedModel.updateActivity(activityToComplete, completedActivity);

        assertCommandSuccess(completeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);
        CompleteCommand completeCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(completeCommand, model, Messages.MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX);
    }

    @Test
    //TODO
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_ACTIVITY);

        Index outOfBoundIndex = INDEX_SECOND_ACTIVITY;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getDeskBoard().getActivityList().size());

        CompleteCommand completeCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(completeCommand, model, Messages.MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX);
    }

    @Test
    //TODO
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Activity activityToComplete = model.getFilteredActivityList().get(INDEX_FIRST_ACTIVITY.getZeroBased());
        CompleteCommand deleteCommand = prepareCommand(INDEX_FIRST_ACTIVITY);
        Model expectedModel = new ModelManager(model.getDeskBoard(), new UserPrefs());

        // delete -> first activity deleted
        deleteCommand.execute();
        undoRedoStack.push(deleteCommand);

        // undo -> reverts addressbook back to previous state and filtered activity list to show all persons
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first activity deleted again

        Activity completedActivity = activityToComplete.getCompletedCopy();
        expectedModel.updateActivity(activityToComplete, completedActivity);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredActivityList().size() + 1);
        CompleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteCommand not pushed into undoRedoStack
        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Activity} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted activity in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the activity object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CompleteCommand completeCommand = prepareCommand(INDEX_FIRST_ACTIVITY);
        Model expectedModel = new ModelManager(model.getDeskBoard(), new UserPrefs());

        showPersonAtIndex(model, INDEX_SECOND_ACTIVITY);
        Activity activityToComplete = model.getFilteredTaskList().get(INDEX_FIRST_ACTIVITY.getZeroBased());
        // delete -> deletes second activity in unfiltered activity list / first activity in filtered activity list
        completeCommand.execute();
        undoRedoStack.push(completeCommand);

        // undo -> reverts addressbook back to previous state and filtered activity list to show all persons
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        Activity completedActivity = activityToComplete.getCompletedCopy();
        expectedModel.updateActivity(activityToComplete, completedActivity);
        assertNotEquals(activityToComplete, model.getFilteredActivityList().get(INDEX_FIRST_ACTIVITY.getZeroBased()));
        // redo -> deletes same second activity in unfiltered activity list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        CompleteCommand completeFirstCommand = prepareCommand(INDEX_FIRST_ACTIVITY);
        CompleteCommand completeSecondCommand = prepareCommand(INDEX_SECOND_ACTIVITY);

        // same object -> returns true
        assertTrue(completeFirstCommand.equals(completeFirstCommand));

        // same values -> returns true
        CompleteCommand completeFirstCommandCopy = prepareCommand(INDEX_FIRST_ACTIVITY);
        assertTrue(completeFirstCommand.equals(completeFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        completeFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(completeFirstCommand.equals(completeFirstCommandCopy));

        // different types -> returns false
        assertFalse(completeFirstCommand.equals(1));

        // null -> returns false
        assertFalse(completeFirstCommand.equals(null));

        // different activity -> returns false
        assertFalse(completeFirstCommand.equals(completeSecondCommand));
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private CompleteCommand prepareCommand(Index index) {
        CompleteCommand completeCommand = new CompleteCommand(index);
        completeCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return completeCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredActivityList(p -> false);

        assertTrue(model.getFilteredActivityList().isEmpty());
    }
}
