import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.security.NoExitSecurityManagerInstaller;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import javax.swing.JFrame;

/**
 * PersonDialogTest.java
 * This test class tests PersonDialog Class
 * This class uses AssertJ to run automated testing.
 */
class PersonDialogTest {

    @Rule
    private static DialogFixture window = null;
    private static PersonDialog personDialog = null;
    public static TemporaryFolder folder = new TemporaryFolder();

    /**
     * This method is the absolute first to run.
     */
    @BeforeAll
    public static void init() {
        // Prevent program exiting
        NoExitSecurityManagerInstaller.installNoExitSecurityManager();

        // Required for full AssertJ GUI testing
        FailOnThreadViolationRepaintManager.install();
    }

    /**
     * This method runs after init() and before each test method.
     * @throws IOException when a file cannot be read or saved.
     * @throws ClassNotFoundException when Class cannot be found.
     */
    @BeforeEach
    void setUp() throws IOException, ClassNotFoundException {
        // Initialize window
        personDialog = GuiActionRunner.execute(() -> new PersonDialog(new JFrame()));
        window = new DialogFixture(personDialog);
        window.show();
    }

    /**
     * This test is run after each test
     */
    @AfterEach
    void tearDown() {
        // Close assertJ window gui
        window.cleanUp();
    }

    /**
     * This test case is run after all tests are done
     */
    @AfterAll
    public static void clean() {
        // Re-enable program to close after testing completes
        NoExitSecurityManagerInstaller.installNoExitSecurityManager().uninstall();
    }

    ///////////////////////////////////////////////////////////////////////////
    //                              UNIT TESTS                               //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Test that the program will add a valid person.
     */
    @Test
    void getValidPerson() {
        // Type 'John','Doe','1234 SomeStreet','SomeCity','FL','12345', and '1234567890'
        // into the respective boxes
        window.textBox("firstName").enterText("John");
        window.textBox("lastName").enterText("Doe");
        window.textBox("address").enterText("1234 SomeStreet");
        window.textBox("city").enterText("SomeCity");
        window.textBox("state").enterText("FL");
        window.textBox("zip").enterText("12345");
        window.textBox("phone").enterText("1234567890");

        // Test Get Person for a valid person
        Person person = personDialog.getPerson();

        // Check persons values
        assertEquals( "John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("1234 SomeStreet", person.getAddress());
        assertEquals("SomeCity", person.getCity());
        assertEquals("FL", person.getState());
        assertEquals("12345", person.getZip());
        assertEquals("1234567890", person.getPhone());

        // Click 'OK' (execute lambda)
        window.button(JButtonMatcher.withText("OK")).click();

        // Check dialog result is OK
        assertEquals(PersonDialog.Result.OK, personDialog.getResult());
    }

    /**
     * Test that a invalid person is not added to addressBook.
     */
    @Test
    void addPersonInvalid() {
        // Create person with numbers in place of person's name
        window.textBox("firstName").enterText("12345");
        window.textBox("lastName").enterText("67890");

        // Check persons is null
        assertEquals(null, personDialog.getPerson());

        // Click 'OK'
        window.button(JButtonMatcher.withText("OK")).click();

        // Check dialog result is was cancel still
        assertEquals(PersonDialog.Result.CANCEL, personDialog.getResult());

        //Check text fields are still filled
        window.textBox("firstName").requireText("12345");
        window.textBox("lastName").requireText("67890");
    }

    /**
     * Tests the PersonDialog will exit will cancel button is pressed.
     */
    @Test
    void addPersonCancel() {
        // Fill first name then cancel
        window.textBox("firstName").enterText("John");

        // Click 'Cancel'
        window.button(JButtonMatcher.withText("Cancel")).click();

        // Check dialog result is Cancel
        assertEquals(PersonDialog.Result.CANCEL, personDialog.getResult());
    }

    /**
     * Tests that person dialog text fields will be
     * filled with data of a person selected.
     */
    @Test
    void openWithPerson() {
        // Close empty window
        window.cleanUp();

        // Open window with person
        PersonDialog frame = GuiActionRunner.execute(() -> new PersonDialog(new JFrame(),
                new Person("John", "Doe", "123 Fake Street", "Fort Myers", "FL", "33901", "0123456789")));
        window = new DialogFixture(frame);
        window.show();

        // Check that required fields have been filled
        window.textBox("firstName").requireText("John");
        window.textBox("lastName").requireText("Doe");
        window.textBox("address").requireText("123 Fake Street");
        window.textBox("city").requireText("Fort Myers");
        window.textBox("state").requireText("FL");
        window.textBox("zip").requireText("33901");
        window.textBox("phone").requireText("0123456789");
    }
}