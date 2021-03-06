import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * AddressBookIntegrationTest.java This TestClass will run integration tests for
 * AddressBook class This classes uses Mockito to run mock and stub tests. The
 * Person class is mocked as AddressBook interacts with that class.
 */
@RunWith(MockitoJUnitRunner.class)
class AddressBookIntegrationTest {

    Person test_Person; // Test Person
    AddressBook test_AddressBook; // Test AddressBook
    Person person_mock; // Person Mock
    Person person_spy;// person spy

    /**
     * This method runs first before each test.
     */
    @BeforeEach
    void setUp() {
        test_Person = new Person("Jane", "Dorian", "987 Westbrook Blvd", "Cincinnati", "OH", "43123", "0123456789");
        test_AddressBook = new AddressBook();
        person_spy = spy(test_Person);
        person_mock = mock(Person.class);

    }

    /**
     * This method runs after each test, to clear out variables.
     */
    @AfterEach
    void tearDown() {
        test_Person = null;
        test_AddressBook = null;
        person_mock = null;
        person_spy = null;
    }

    /**
     * Component Testing. This test case is a stub will use a Person mock to force a
     * custom return value.
     */
    @Test
    public void getValueAtStub() {
        test_AddressBook.add(person_mock);
        when(person_mock.getField(0)).thenReturn("Sheldon");
        test_AddressBook.getValueAt(0, 0);
        verify(person_mock).getField(isA(int.class));
        assertEquals("Sheldon", test_AddressBook.getValueAt(0, 0));
    }

    /**
     * Positive Testing. This test will use a spy of Person class to test that
     * correct value is retrieved from person
     */
    @Test
    public void getValueAtSpy() {
        test_AddressBook.add(person_spy);
        test_AddressBook.getValueAt(0, 0);
        verify(person_spy).getField(isA(int.class));
        assertEquals("Dorian", test_AddressBook.getValueAt(0, 0));
        assertEquals("Jane", test_AddressBook.getValueAt(0, 1));
    }
}