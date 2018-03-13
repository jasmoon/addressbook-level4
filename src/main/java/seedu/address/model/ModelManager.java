package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.CalendarChangedEvent;
import seedu.address.model.activity.Activity;
import seedu.address.model.activity.exceptions.ActivityNotFoundException;
import seedu.address.model.activity.exceptions.DuplicateActivityException;


/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Calendar addressBook;
    private final FilteredList<Activity> filteredActivities;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyCalendar addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new Calendar(addressBook);
        filteredActivities = new FilteredList<>(this.addressBook.getPersonList());
    }

    public ModelManager() {
        this(new Calendar(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyCalendar newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyCalendar getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new CalendarChangedEvent(addressBook));
    }

    @Override
    public synchronized void deleteActivity(Activity target) throws ActivityNotFoundException {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addActivity(Activity activity) throws DuplicateActivityException {
        addressBook.addActivity(activity);
        updateFilteredActivityList(PREDICATE_SHOW_ALL_ACTIVITY);
        indicateAddressBookChanged();
    }

    @Override
    public void updateActivity(Activity target, Activity editedActivity)
            throws DuplicateActivityException, ActivityNotFoundException {
        requireAllNonNull(target, editedActivity);

        addressBook.updateActivity(target, editedActivity);
        indicateAddressBookChanged();
    }

    //=========== Filtered Activity List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Activity} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Activity> getFilteredActivityList() {
        return FXCollections.unmodifiableObservableList(filteredActivities);
    }

    @Override
    public void updateFilteredActivityList(Predicate<Activity> predicate) {
        requireNonNull(predicate);
        filteredActivities.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && filteredActivities.equals(other.filteredActivities);
    }

}
