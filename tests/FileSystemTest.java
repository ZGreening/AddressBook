
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.rules.TemporaryFolder;

public class FileSystemTest {

    //Temporary Folder provides a mock fileDirectory that deletes when Program ends
    @Rule
    public static TemporaryFolder folder = new TemporaryFolder();
    private static AddressBook addressBook = null; //An generic address book
    private static File file = null;           //A java file for testing

    /**
     * This method runs before before each test begins
     * @throws IOException if file cannot be read or written
     */
    @BeforeEach
    public void init() throws IOException {
        //Create a new address book each time
        addressBook = new AddressBook();

        //Create a new file for testing each time
        folder.create();
        file = folder.newFile("MyTestFile");
    }

    ///////////////////////////////////////////////////////////////////////////
    //                                 TESTS                                 //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Functional Testing
     * This testcase will test if an error is thrown
     * if the program tries to read a deleted file
     */
    @Test
    public void readNonExistingFile() {
        //Delete file
        file.delete();

        //Try reading deleted file
        assertThrows(FileNotFoundException.class, () -> FileSystem.readFile(addressBook,file));
    }

    /**
     * Functional Testing and Negative Testing
     * This test checks if exception is thrown if
     * an unreadable file is read
     */
    @Test
    public void readUnreadableFile() {
        //Make an unreadable mock file
        file = mock(File.class);
        doReturn(false).when(file).canRead();

        //Test that an exception is thrown
        assertThrows(FileNotFoundException.class, () -> FileSystem.readFile(addressBook,file));
    }

    /**
     * Functional Testing and Negative Testing
     * This testcase will test if SQLException is thrown
     * a file read is not a valid file.
     */
    @Test
    public void readNonSQLFile() {
        //Write some bytes that are not SQLite formatted
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write("This is some basic non-SQLite text".getBytes());
        } catch(IOException exception) {
            System.out.println("This should not be printed.");
        }

        //Test that SQL exception is thrown when file is not proper format
        assertThrows(SQLException.class, () -> FileSystem.readFile(addressBook,file));
    }

    /**
     * Functional Testing and Positive Testing
     * This test will test that the program will be able to
     * save a blank address book.
     */
    @Test
    public void saveBlankAddressBook() {
        //Test that file was saved correctly
        assertDoesNotThrow(() -> FileSystem.saveFile(addressBook,file));
        assertTrue(file.exists()); //Check file exists on disk
    }

    /**
     * Functional Testing and Positive Testing
     * This test case will test if the program will read a blank file.
     */
    @Test
    public void readBlankAddressBook() {
        //This should not throw an error, if this errors, so will TEST saveBlankAddressBook()
        //JUNIT says to not write tests with dependencies and that tests should be able to execute
        //in any order. Thus, this test will only succeed if the blank file can be saved AND read
        assertDoesNotThrow(() -> FileSystem.saveFile(addressBook,file));

        //Check the persistent file can be read with no exception
        assertDoesNotThrow(() -> FileSystem.readFile(addressBook,file));

        //Check no one is in blank address book
        assertTrue(addressBook.getPersons().length==0); 
    }

    /**
     * Functional Testing and Positive Testing
     * This test case will test the program saving a valid address book.
     */
    @Test @Timeout(value=120000) //120 seconds, two minutes max time to access database
    public void saveNormalAddressBook() {
        //Add two people to address book
        addressBook.add(new Person("John","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));
        addressBook.add(new Person("Jane","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));

        //Test that file was saved correctly
        assertDoesNotThrow(() -> FileSystem.saveFile(addressBook,file));
        assertTrue(file.exists()); //Check file exists on disk
    }

    /**
     * Function Testing and Positive Testing
     * This test case will test the program will open a valid file
     */
    @Test @Timeout(value=120000) //120 seconds, two minutes max time to access database
    public void readNormalAddressBook() {
        //Add two people to address book and save
        addressBook.add(new Person("John","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));
        addressBook.add(new Person("Jane","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));
        assertDoesNotThrow(() -> FileSystem.saveFile(addressBook,file));

        //Get new address book
        addressBook = new AddressBook();

        //Check the persistent file can be read with no exception
        assertDoesNotThrow(() -> FileSystem.readFile(addressBook,file));
        assertTrue(addressBook.getPersons().length==2); //Check both people exist

        //Check they are the same
        assertEquals("Doe, John",addressBook.getPersons()[0].toString());
        assertEquals("Doe, Jane",addressBook.getPersons()[1].toString());
    }

    /**
     * Functional Testing and Positive testing
     * This test case will test that program will be able to
     * successfully overwrite a saved file.
     */
    @Test
    public void saveOverFile() {
        //Add two people to address book and save
        addressBook.add(new Person("John","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));
        addressBook.add(new Person("Jane","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));
        assertDoesNotThrow(() -> FileSystem.saveFile(addressBook,file));

        //Get new address book
        addressBook=new AddressBook();

        //Create two additional people
        addressBook.add(new Person("Bob","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));
        addressBook.add(new Person("Joe","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));

        //Save over same file
        assertDoesNotThrow(() -> FileSystem.saveFile(addressBook,file));

        //Get new address book
        addressBook=new AddressBook();

        //Read file
        assertDoesNotThrow(() -> FileSystem.readFile(addressBook,file));

        //Check only two people exist
        assertTrue(addressBook.getPersons().length==2); 

        //Check they are second two people and not previous ones
        assertEquals("Doe, Bob",addressBook.getPersons()[0].toString());
        assertEquals("Doe, Joe",addressBook.getPersons()[1].toString());
    }
}