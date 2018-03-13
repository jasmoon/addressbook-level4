package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.Calendar;
import seedu.address.model.ReadOnlyCalendar;


/**
 * Represents a storage for {@link Calendar}.
 */
public interface CalendarStorage {

    /**
     * Returns the file path of the data file.
     */
    String getCalendarFilePath();

    /**
     * Returns Calendar data as a {@link ReadOnlyCalendar}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyCalendar> readCalendar() throws DataConversionException, IOException;

    /**
     * @see #getCalendarFilePath()
     */
    Optional<ReadOnlyCalendar> readCalendar(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyCalendar} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAddressBook(ReadOnlyCalendar addressBook) throws IOException;

    /**
     * @see #saveAddressBook(ReadOnlyCalendar)
     */
    void saveAddressBook(ReadOnlyCalendar addressBook, String filePath) throws IOException;

    /**
     * Saves the given {@link ReadOnlyCalendar} to the fixed temporary location.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void backupAddressBook(ReadOnlyCalendar addressBook) throws IOException;
}
