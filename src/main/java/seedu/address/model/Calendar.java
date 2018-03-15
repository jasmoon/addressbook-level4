package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.model.activity.Activity;
import seedu.address.model.activity.UniqueActivityList;
import seedu.address.model.activity.exceptions.ActivityNotFoundException;
import seedu.address.model.activity.exceptions.DuplicateActivityException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class Calendar implements ReadOnlyCalendar {

    private final UniqueActivityList activities;
    private final UniqueTagList tags;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        activities = new UniqueActivityList();
        tags = new UniqueTagList();
    }

    public Calendar() {}

    /**
     * Creates an Calendar using the Persons and Tags in the {@code toBeCopied}
     */
    public Calendar(ReadOnlyCalendar toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setActivities(List<Activity> activities) throws DuplicateActivityException {
        this.activities.setPersons(activities);
    }

    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    /**
     * Resets the existing data of this {@code Calendar} with {@code newData}.
     */
    public void resetData(ReadOnlyCalendar newData) {
        requireNonNull(newData);
        setTags(new HashSet<>(newData.getTagList()));
        List<Activity> syncedActivityList = newData.getPersonList().stream()
                .map(this::syncWithMasterTagList)
                .collect(Collectors.toList());

        try {
            setActivities(syncedActivityList);
        } catch (DuplicateActivityException e) {
            throw new AssertionError("AddressBooks should not have duplicate persons");
        }
    }

    //// activity-level operations

    /**
     * Adds a activity to the address book.
     * Also checks the new activity's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the activity to point to those in {@link #tags}.
     *
     * @throws DuplicateActivityException if an equivalent activity already exists.
     */
    public void addActivity(Activity p) throws DuplicateActivityException {
        Activity activity = syncWithMasterTagList(p);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any activity
        // in the activity list.
        activities.add(activity);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code Calendar}'s tag list will be updated with the tags of {@code editedPerson}.
     *
     * @throws DuplicateActivityException if updating the activity's details causes the activity to be equivalent to
     *      another existing person in the list.
     * @throws ActivityNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Activity)
     */
    public void updateActivity(Activity target, Activity editedActivity)
            throws DuplicateActivityException, ActivityNotFoundException {
        requireNonNull(editedActivity);

        Activity syncedEditedActivity = syncWithMasterTagList(editedActivity);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any activity
        // in the activity list.
        activities.setPerson(target, syncedEditedActivity);
    }

    /**
     *  Updates the master tag list to include tags in {@code activity} that are not in the list.
     *  @return a copy of this {@code activity} such that every tag in this activity points to
     *  a Tag object in the master list.
     */
    private Activity syncWithMasterTagList(Activity activity) {
        final UniqueTagList personTags = new UniqueTagList(activity.getTags());
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        // used for checking activity tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of activity tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new Activity(
                activity.getName(), activity.getDateTime(), activity.getRemark(), correctTagReferences);
    }

    /**
     * Removes {@code key} from this {@code Calendar}.
     * @throws ActivityNotFoundException if the {@code key} is not in this {@code Calendar}.
     */
    public boolean removePerson(Activity key) throws ActivityNotFoundException {
        if (activities.remove(key)) {
            return true;
        } else {
            throw new ActivityNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    //// util methods

    @Override
    public String toString() {
        return activities.asObservableList().size() + " persons, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<Activity> getPersonList() {
        return activities.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Calendar // instanceof handles nulls
                && this.activities.equals(((Calendar) other).activities)
                && this.tags.equalsOrderInsensitive(((Calendar) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(activities, tags);
    }
}
