package seedu.address.model.util;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.Calendar;
import seedu.address.model.ReadOnlyCalendar;
import seedu.address.model.activity.Activity;
import seedu.address.model.activity.DateTime;
import seedu.address.model.activity.Name;
import seedu.address.model.activity.Remark;
import seedu.address.model.activity.exceptions.DuplicateActivityException;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code Calendar} with sample data.
 */
public class SampleDataUtil {
    public static Activity[] getSamplePersons() {
        return new Activity[] {
            new Activity(new Name("Alex Yeoh"), new DateTime("01/02/1991 23:59:59"),
                new Remark("Blk 30 Geylang Street 29, #06-40"),
                getTagSet("friends")),
            new Activity(new Name("Bernice Yu"), new DateTime("02/02/1992 23:59:59"),
                new Remark("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getTagSet("colleagues", "friends")),
            new Activity(new Name("Charlotte Oliveiro"), new DateTime("03/02/1991 23:59:59"),
                new Remark("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getTagSet("neighbours")),
            new Activity(new Name("David Li"), new DateTime("04/02/1991 23:59:59"),
                new Remark("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getTagSet("family")),
            new Activity(new Name("Irfan Ibrahim"), new DateTime("12/02/1991 23:59:59"),
                new Remark("Blk 47 Tampines Street 20, #17-35"),
                getTagSet("classmates")),
            new Activity(new Name("Roy Balakrishnan"), new DateTime("12/05/1991 23:59:59"),
                new Remark("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlyCalendar getSampleAddressBook() {
        try {
            Calendar sampleAb = new Calendar();
            for (Activity sampleActivity : getSamplePersons()) {
                sampleAb.addActivity(sampleActivity);
            }
            return sampleAb;
        } catch (DuplicateActivityException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String ... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
