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

class PersonDialogTest {

    @Rule
    private static DialogFixture window = null;
    private static PersonDialog personDialog = null;
    public static TemporaryFolder folder = new TemporaryFolder();

    @BeforeAll
    public static void init() {
        // Prevent program exiting
        NoExitSecurityManagerInstaller.installNoExitSecurityManager();

        // Required for full AssertJ GUI testing
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    void setUp() throws IOException, ClassNotFoundException {
        // Initialize window
        personDialog = GuiActionRunner.execute(() -> new PersonDialog(new JFrame()));
        window = new DialogFixture(personDialog);
        window.show();
    }

    @AfterEach
    void tearDown() {
        // Close assertJ window gui
        window.cleanUp();
    }

    @AfterAll
    public static void clean() {
        // Re-enable program to close after testing completes
        NoExitSecurityManagerInstaller.installNoExitSecurityManager().uninstall();
    }

    ///////////////////////////////////////////////////////////////////////////
    //                              UNIT TESTS                               //
    ///////////////////////////////////////////////////////////////////////////

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
        assertEquals(person.getFirstName(), "John");
        assertEquals(person.getLastName(), "Doe");
        assertEquals(person.getAddress(), "1234 SomeStreet");
        assertEquals(person.getCity(), "SomeCity");
        assertEquals(person.getState(), "FL");
        assertEquals(person.getZip(), "12345");
        assertEquals(person.getPhone(), "1234567890");

        // Click 'OK' (execute lambda)
        window.button(JButtonMatcher.withText("OK")).click();

        // Check dialog result is OK
        assertEquals(personDialog.getResult(), PersonDialog.Result.OK);
    }

    @Test
    void addPersonInvalid() {
        // Create person with numbers in place of person's name
        window.textBox("firstName").enterText("12345");
        window.textBox("lastName").enterText("67890");

        // Check persons is null
        assertEquals(personDialog.getPerson(), null);

        // Click 'OK'
        window.button(JButtonMatcher.withText("OK")).click();

        // Check dialog result is OK
        assertEquals(personDialog.getResult(), PersonDialog.Result.OK);
    }

    @Test
    void addPersonCancel() {
        // Fill first name then cancel
        window.textBox("firstName").enterText("John");

        // Click 'Cancel'
        window.button(JButtonMatcher.withText("Cancel")).click();

        // Check dialog result is Cancel
        assertEquals(personDialog.getResult(), PersonDialog.Result.CANCEL);
    }

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