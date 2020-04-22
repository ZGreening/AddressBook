import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class AddressBookController {

    AddressBook addressBook;

    /**
     * AddressBookController constructor which initializes the controller with the
     * passed address book.
     * 
     * @param addressBook The address book for the controller to handle.
     */
    public AddressBookController(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * An intermediate function to add a person to the current address book.
     * 
     * @param person The person object to add.
     */
    public void add(Person person) {
        addressBook.add(person);
    }

    /**
     * An intermediate function to set the person in a specific index in the current
     * address book.
     * 
     * @param index  The index of the person to replace.
     * @param person The person replace the contents of that index with.
     */
    public void set(int index, Person person) {
        addressBook.set(index, person);
    }

    /**
     * An intermediate function to remove a person in the current address book.
     * 
     * @param index The index of the person to remove.
     */
    public void remove(int index) {
        addressBook.remove(index);
    }

    /**
     * An intermediate function to get a person at a specific index in the current
     * address book.
     * 
     * @param index The index of the person to get.
     */
    public Person get(int index) {
        return addressBook.get(index);
    }

    /**
     * An intermediate function to clear the address book.
     */
    public void clear() {
        addressBook.clear();
    }

    /**
     * An intermediate function to open a saved address book.
     * 
     * @param file The saved file to load.
     * @throws FileNotFoundException Thrown if the file is unreadable or does not
     *                               exist.
     * @throws SQLException          Thrown if the file was not in correct format.
     */
    public void open(File file) throws FileNotFoundException, SQLException {
        FileSystem.readFile(addressBook, file);
        addressBook.fireTableDataChanged();
    }

    /**
     * An intermediate function to save the current address book.
     * 
     * @param file The file to save the contents of the address book to.
     * @throws SQLException Thrown if the file could not be saved.
     */
    public void save(File file) throws SQLException {
        FileSystem.saveFile(addressBook, file);
    }
}