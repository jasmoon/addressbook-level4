package seedu.address.model.activity;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Represents a Activity's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateAndTime(String)}
 */
public class DateTime {

    public static final String DATE_FORMAT = "MM/dd/yyyy hh:mm:ss";
    public static final String MESSAGE_DATETIME_CONSTRAINTS =
            "Date and Time numbers should be a date and should be in the format of "
            + DATE_FORMAT;
    // TODO : FIND A REGET TO VALIDATE THE DATE AND TIME.
    public static final String DATETIME_VALIDATION_REGEX = ".*";
    private final String value;
    private Date date;

    /**
     * Constructs a {@code DateTime}.
     *
     * @param dateAndTime A valid phone number.
     */
    public DateTime(String dateAndTime) {
        requireNonNull(dateAndTime);
        checkArgument(isValidDateAndTime(dateAndTime), MESSAGE_DATETIME_CONSTRAINTS);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            date = sdf.parse(dateAndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.value = dateAndTime;
    }

    /**
     * Returns true if a given string is a valid activity phone number.
     */
    public static boolean isValidDateAndTime(String test) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            sdf.parse(test);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        //return test.matches(DATETIME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String result = value;
        try {
            result = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            result = value;
        }
        return result;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateTime // instanceof handles nulls
                && this.value.equals(((DateTime) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
