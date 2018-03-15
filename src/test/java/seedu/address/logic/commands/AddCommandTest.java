package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Calendar;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyCalendar;
import seedu.address.model.activity.Activity;
import seedu.address.model.activity.exceptions.ActivityNotFoundException;
import seedu.address.model.activity.exceptions.DuplicateActivityException;

import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Activity validActivity = new PersonBuilder().build();

        CommandResult commandResult = getAddCommandForPerson(validActivity, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validActivity), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validActivity), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePersonException();
        Activity validActivity = new PersonBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);

        getAddCommandForPerson(validActivity, modelStub).execute();
    }

    @Test
    public void equals() {
        Activity alice = new PersonBuilder().withName("Alice").build();
        Activity bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different activity -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given activity.
     */
    private AddCommand getAddCommandForPerson(Activity activity, Model model) {
        AddCommand command = new AddCommand(activity);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addActivity(Activity activity) throws DuplicateActivityException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyCalendar newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyCalendar getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deleteActivity(Activity target) throws ActivityNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updateActivity(Activity target, Activity editedActivity)
                throws DuplicateActivityException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Activity> getFilteredActivityList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredActivityList(Predicate<Activity> predicate) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicateActivityException when trying to add a activity.
     */
    private class ModelStubThrowingDuplicatePersonException extends ModelStub {
        @Override
        public void addActivity(Activity activity) throws DuplicateActivityException {
            throw new DuplicateActivityException();
        }

        @Override
        public ReadOnlyCalendar getAddressBook() {
            return new Calendar();
        }
    }

    /**
     * A Model stub that always accept the activity being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Activity> personsAdded = new ArrayList<>();

        @Override
        public void addActivity(Activity activity) throws DuplicateActivityException {
            requireNonNull(activity);
            personsAdded.add(activity);
        }

        @Override
        public ReadOnlyCalendar getAddressBook() {
            return new Calendar();
        }
    }

}
