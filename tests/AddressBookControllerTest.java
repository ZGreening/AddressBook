import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

/**
 * AddressBookControllerTest.java This test classes contain AddressBook
 * Controller unit tests This class uses JUnit as a test suite to execute tests.
 */
class AddressBookControllerTest {

    @Rule
    public static TemporaryFolder folder = new TemporaryFolder();
    private static File file = null; // A java file for testing
    private static Person test_Person; // test person
    private static AddressBook test_AddressBook; // test AddressBook
    private static AddressBookController controllerTest; // Test Controls

    /**
     * This method is met to set up variables for test cases. This method is run
     * before each test
     */
    @BeforeEach
    void setUp() {

        // model = new DefaultTableModel();
        test_Person = new Person("Jane", "Dorian", "987 Westbrook Blvd", "Cincinnati", "OH", "43123", "0123456789");
        test_AddressBook = new AddressBook();
        controllerTest = new AddressBookController(test_AddressBook);
    }

    /**
     * This method is ran after each test and clear out variables.
     */
    @AfterEach
    void tearDown() {
        test_Person = null;
        test_AddressBook = null;
        controllerTest = null;
    }

    /**
     * Functional and Positive Testing
     * This test case test will test if a person is successful added to Address
     * Book.
     */
    @Test
    void addPersonToList() {
        controllerTest.add(test_Person);
        assertEquals("Jane", controllerTest.get(0).getFirstName());
    }

    /**
     * Functional and Positive Testing
     * This case tests set method. Given an index a person object will be replaced
     * with a new person object.
     */
    @Test
    void setWithOnePersonInList() {
        controllerTest.add(test_Person);
        controllerTest.set(0, test_Person);

        assertEquals(controllerTest.get(0), test_Person);
        assertEquals("Dorian, Jane", controllerTest.get(0).toString());
    }

    /**
     * Functional and Negative Testing
     * This is a negative test case that will throw an Exception if the method tries
     * to set a new person on a index that doesn't exist.
     */
    @Test
    void setWithEmptyListShouldThrowError() {
        assertThrows(IndexOutOfBoundsException.class, () -> controllerTest.set(0, test_Person));
    }

    /**
     * Functional and Positive Testing
     * This test case will test that a person will be removed from Address Book.
     */
    @Test
    void removePersonFromListAndListWillNotBeEmpty() {
        controllerTest.add(test_Person); // Add person in AddressBook

        controllerTest.remove(0);// Remove Person
        // Check if removed person is in the list
        boolean isInArray = false;
        String search = "Dorian, Jane";
        for (int i = 0; i < test_AddressBook.getPersons().length; i++) {
            if (test_AddressBook.getPersons()[i].toString().equals(search)) {
                // Found Person!
                isInArray = true;
                break;
            }
        }

        // Assert Person not found
        assertFalse(isInArray);
        assertEquals(0, test_AddressBook.getPersons().length);
    }

    /**
     * Functional and Negative Testing
     * This is a negative test case to test that test that an Error is thrown if the
     * method tries to remove a person from a empty Person list.
     */
    @Test
    void removeOnEmptyListShouldThrowError() {
        assertThrows(IndexOutOfBoundsException.class, () -> controllerTest.remove(0));
    }

    /**
     * Functional and Positive Testing
     * This test case will test if AddressBook Controller will successfully add a
     * person.
     */
    @Test
    void getPersonOnNonEmptyList() {
        controllerTest.add(test_Person);
        assertEquals(controllerTest.get(0), test_Person);
    }

    /**
     * Functional and Negative Testing
     * This is a exception test which should throw an error if get method is called
     * when list is empty
     */
    @Test
    void getOnEmptyListShouldThrowError() {
        assertThrows(IndexOutOfBoundsException.class, () -> controllerTest.get(0));
    }

    /**
     * Functional and Positive Testing
     * This test case test if Address Book list will clear, given there is a person
     * already in the Address Book
     */
    @Test
    void clearList() {
        test_AddressBook.add(test_Person);
        // Array should not be empty
        assertFalse(test_AddressBook.getPersons().length == 0);

        controllerTest.clear();// Clear AddressBook
        // Assert its empty
        assertTrue(test_AddressBook.getPersons().length == 0);
        assertThrows(IndexOutOfBoundsException.class, () -> controllerTest.get(0));
    }

    /**
     * Functional and Positive Testing
     * This test case will test if program can open a normal file. The testing of
     * file system is for the FileSystem test class thus, the test here will be a
     * simple case of seeing if a acceptable file will open.
     */
    @Test
    public void openValidFile() throws IOException {

        // Create a new file for testing each time
        folder.create();
        file = folder.newFile("MyTestFile");

        // Add two people to address book and save
        test_AddressBook.add(new Person("John", "Doe", "1234 NON EXIST ROAD", "SomeCity", "FL", "12345", "1234567890"));
        test_AddressBook.add(new Person("Jane", "Doe", "1234 NON EXIST ROAD", "SomeCity", "FL", "12345", "1234567890"));
        assertDoesNotThrow(() -> FileSystem.saveFile(test_AddressBook, file));
        assertDoesNotThrow(() -> controllerTest.open(file));
    }

    /**
     * Functional and Positive Testing
     * This test case will check if program will be able to successfully save a
     * file.
     */
    @Test
    public void saveValidFile() throws IOException {

        // Create a new file for testing each time
        folder.create();
        file = folder.newFile("MyTestFile");

        // Add two people to address book
        test_AddressBook.add(new Person("John", "Doe", "1234 NON EXIST ROAD", "SomeCity", "FL", "12345", "1234567890"));
        test_AddressBook.add(new Person("Jane", "Doe", "1234 NON EXIST ROAD", "SomeCity", "FL", "12345", "1234567890"));

        assertDoesNotThrow(() -> controllerTest.save(file));
        assertTrue(file.exists()); // Check file exists on disk
    }
}
