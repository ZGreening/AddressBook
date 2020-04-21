import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * AddressBookControllerIntegrationTest.java This Test class contains
 * integration tests for AddressBook Controller Class This class uses Mockito to
 * mock classes for mock testing.
 */
@RunWith(MockitoJUnitRunner.class)
class AddressBookControllerIntegrationTest {
    Person person_mock; // Mock person class
    AddressBook addressBook_mock;// stub AddressBook class
    AddressBook addressBook_spy;
    AddressBookController controllerTest;

    @Rule
    TemporaryFolder folder = new TemporaryFolder();

    /**
     * This method is ran before each test is run. Is intended to set up variables
     * for test to use.
     */
    @BeforeEach
    void setUp() {
        // Mock Functionality of AddressBook
        addressBook_mock = mock(AddressBook.class);

        // Spy of AddressBook
        addressBook_spy = spy(AddressBook.class);

        // A Test Object of AddressBook
        controllerTest = new AddressBookController(addressBook_mock);

        // Mock of Person class
        person_mock = mock(Person.class);
    }

    /**
     * This method runs after a test is done. Clears data for the next test ot use
     */
    @AfterEach
    void tearDown() {
        controllerTest = null;
        person_mock = null;
        addressBook_mock = null;
        addressBook_spy = null;
    }

    /**
     * This test case mocks the AddressBook, to test if it's method is ran
     */
    @Test
    public void addPersonToAddressBookMock() {
        // Mockito method of handling void return functions
        doNothing().when(addressBook_mock).add(isA(Person.class));

        controllerTest.add(person_mock);

        // Verify method is called
        verify(addressBook_mock).add(isA(Person.class));
    }

    /**
     * This test case uses AddressBook Spy, to verify that a person is successfully
     * added
     */
    @Test
    public void addPersonToAddressBookSpy() {
        // Initialize with Spy instead of mock
        controllerTest = new AddressBookController(addressBook_spy);
        controllerTest.add(person_mock);

        // verify method was hit
        verify(addressBook_spy).add(isA(Person.class));

        // Verify Person added to ArrayList
        assertEquals(1, addressBook_spy.getPersons().length);
    }

    /**
     * This test verifies that Mocked AddressBook set method is ran
     */
    @Test
    public void setPersonToAddressBookMock() {
        doNothing().when(addressBook_mock).set(isA(int.class), isA(Person.class));
        controllerTest.set(0, person_mock);

        // verify method was hit
        verify(addressBook_mock).set(0, person_mock);
    }

    /**
     * This test case uses AddressBook Spy, to verify that a person is successfully
     * replaced.
     */
    @Test
    public void setPersonToAddressBookSpy() {
        controllerTest = new AddressBookController(addressBook_spy);

        // Add a test person first
        controllerTest.add(person_mock);

        // Create new person
        Person test_Person2 = new Person("Jane", "Dorian", "987 Westbrook Blvd", "Chincinnati", "OH", "43123",
                "0123456789");

        // Replace old with new person
        controllerTest.set(0, test_Person2);

        // verify methodd was called
        verify(addressBook_spy).set(isA(int.class), isA(Person.class));
        assertEquals("Dorian, Jane", addressBook_spy.get(0).toString());
    }

    /**
     * This method mocks the AddressBook to verify the remove function is performed.
     */
    @Test
    public void removePersonFromAddressBookMock() {
        doNothing().when(addressBook_mock).remove(isA(int.class));
        controllerTest.remove(0);

        // verify method was called
        verify(addressBook_mock).remove(isA(int.class));
    }

    /**
     * This test case uses AddressBook Spy, to verify that a person is successfully
     * removed.
     */
    @Test
    public void removePersonToAddressBookSpy() {
        controllerTest = new AddressBookController(addressBook_spy);

        // Add a test person first
        controllerTest.add(person_mock);
        controllerTest.remove(0);
        verify(addressBook_spy).remove(isA(int.class));

        // Assert exception because array list but must be empty
        assertThrows(IndexOutOfBoundsException.class, () -> addressBook_spy.get(0));
    }

    /**
     * This method stubs a address book, to return a set value "person mock" to
     * verify the method is called.
     */
    @Test
    public void getPersonFromAddressBookStub() {
        // Stub method, to return person_mock when called
        when(addressBook_mock.get(0)).thenReturn(person_mock);

        // Call test controller method
        controllerTest.get(0);

        // verify method was called
        verify(addressBook_mock).get(0);

        // Verify return value is the same
        assertEquals(person_mock, controllerTest.get(0));
    }

    /**
     * This method address book spy and Person mock to verify that the address book
     * gets the person's first and last name as toString
     */
    @Test
    public void getPersonFromAddressBookSpy() {
        // Initialize with spy
        controllerTest = new AddressBookController(addressBook_spy);

        // Add person mock
        controllerTest.add(person_mock);

        // Mock person's return value
        when(person_mock.toString()).thenReturn("Bread, Ginger");
        controllerTest.get(0);
        verify(addressBook_spy).get(0);// Verify method is ran

        // Assert that when controller test runs get(), then mocked return is called
        assertEquals("Bread, Ginger", controllerTest.get(0).toString());
    }

    /**
     * This method uses a addressBook mock to check if clear method is called.
     */
    @Test
    public void clearAddressMock() {
        doNothing().when(addressBook_mock).clear();
        controllerTest.clear();

        // verify method was called
        verify(addressBook_mock).clear();
    }

    /**
     * This method uses a addressBook spy to check if clear method is ran
     * successfully.
     */
    @Test
    public void clearAddressSpy() {
        controllerTest = new AddressBookController(addressBook_spy);
        addressBook_spy.add(person_mock);
        controllerTest.clear();
        verify(addressBook_spy).clear();
        assertEquals(0, addressBook_spy.getPersons().length);
    }

    /**
     * This method makes a spy of AddressBook and will throw an error when trying to
     * save with an invalid file
     */
    @Test
    public void throwSQLExceptionWhenSavingAddressBook() throws SQLException {
        AddressBook addressBookSpy = spy(AddressBook.class);
        AddressBookController controllerSpy = spy(new AddressBookController(addressBookSpy));

        doThrow(SQLException.class).when(controllerSpy).save(isA(File.class));
        assertThrows(SQLException.class, () -> controllerSpy.save(new File("test")));
    }

    /**
     * This test checks if saving fileSystem with a valid file does not throw an
     * error.
     */
    @Test
    public void testSavingFileSuccessfulSpy() throws IOException {
        controllerTest = new AddressBookController(addressBook_spy);
        folder.create(); // Create test folder
        final File file = folder.newFile("MyTestFile");// Create test file
        try {
            assertDoesNotThrow(() -> controllerTest.save(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This test checks if a valid file can be opened by filesystem. Intensive file
     * checking is done in FileSystem Test
     */
    @Test
    public void testOpeningFileMck() throws IOException {
        controllerTest = new AddressBookController(addressBook_spy);
        folder.create(); // Create test folder
        final File file = folder.newFile("MyTestFile");// Create test file
        try {
            Person test_Person2 = new Person("Jane", "Dorian", "987 Westbrook Blvd", "Cincinnati", "OH", "43123",
                    "0123456789");
            addressBook_spy.add(test_Person2);

            // Save file first
            assertDoesNotThrow(() -> controllerTest.save(file));

            // Then Open it
            assertDoesNotThrow(() -> controllerTest.open(file));

            // Check if Information is same
            assertEquals("Dorian, Jane", addressBook_spy.get(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
