package seedu.address.model.activity;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Activity in the remark book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Activity {

    private final Name name;
    private final DateTime dateTime;
    private final Remark remark;

    private final UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Activity(Name name, DateTime dateTime, Remark remark, Set<Tag> tags) {
        requireAllNonNull(name, dateTime, remark, tags);
        this.name = name;
        this.dateTime = dateTime;
        this.remark = remark;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
    }

    public Name getName() {
        return name;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public Remark getRemark() {
        return remark;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Activity)) {
            return false;
        }

        Activity otherActivity = (Activity) other;
        return otherActivity.getName().equals(this.getName())
                && otherActivity.getDateTime().equals(this.getDateTime())
                && otherActivity.getRemark().equals(this.getRemark());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, dateTime, remark, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Date And Time: ")
                .append(getDateTime())
                .append(" Remark: ")
                .append(getRemark())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
