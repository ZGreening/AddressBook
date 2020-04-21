
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JTable;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Robot;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.security.NoExitSecurityManagerInstaller;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

/**
 * AddressBookGUITest.java
 * This is the testcase for testing the GUI itself.
 * It uses AssertJ to perform automated tests
 *
 *
 */

//NOTE: Due to a bug(s) in AssertJ/JDK, this entire class's
//tests fail on MacOS systems. This is partly because of the way
//the system simulates clicks on the GUI. It requires special
//security permissions to access the mouse and keyboard.
//SEE: https://github.com/joel-costigliola/assertj-swing/issues/25
public class AddressBookGUITest {


    //TemporaryFolder clears itself from memory when program ends
    @Rule
    public static TemporaryFolder folder = new TemporaryFolder();
    private static File testFile = null;
    private static FrameFixture window = null;
    private static AddressBookGUI addressBookGUI = null;

    /**
     * This method runs before each test, to initialize variables.
     *
     * @throws IOException if failure in reading or writing file
     * @throws ClassNotFoundException if Class isnt present
     */
    @BeforeEach
    public void initEach() throws IOException, ClassNotFoundException {
        // Prevent program exiting
        NoExitSecurityManagerInstaller.installNoExitSecurityManager();

        // Required for full AssertJ GUI testing
        FailOnThreadViolationRepaintManager.install();

        // Initialize window
        addressBookGUI = GuiActionRunner.execute(() -> new AddressBookGUI());
        window = new FrameFixture(addressBookGUI);
        window.show();

        // Create SQL test file
        folder.create();
        testFile = folder.newFile("myTestFile");
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + testFile.getAbsoluteFile());
                Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE persons (firstName TEXT, lastName TEXT, address TEXT, city TEXT, state TEXT, zip TEXT, phone TEXT)");
            statement.execute(
                    "INSERT INTO persons (firstName, lastName, address, city, state, zip, phone) VALUES (\"John\", \"Doe\", \"1234 SomeStreet\", \"SomeCity\", \"FL\", \"12345\", \"1234567890\")");
            statement.execute(
                    "INSERT INTO persons (firstName, lastName, address, city, state, zip, phone) VALUES (\"Jane\", \"Doe\", \"1234 SomeStreet\", \"SomeCity\", \"FL\", \"12345\", \"1234567890\")");
        } catch (SQLException exception) {
            System.out.println("Unable to create test file:\n" + exception);
        }
    }

    /**
     * This method runs after each test, to clear out leftover variables
     */
    @AfterEach
    public void cleanEach() {
        // Close assertJ window gui
        window.cleanUp();
    }

    /**
     * This is the last method that runs.
     */
    @AfterAll
    public static void clean() {
        // Re-enable program to close after testing completes
        NoExitSecurityManagerInstaller.installNoExitSecurityManager().uninstall();
    }

    ///////////////////////////////////////////////////////////////////////////
    //                                TESTS                                  //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * This is automated test that will open the window
     * and create a valid new person.
     */
    @Test
    public void canCreateNewPerson() {
        // Click and get dialog window
        window.button("add").click();
        DialogFixture dialog = window.dialog();

        // Type 'John','Doe','1234 SomeStreet','SomeCity','FL','12345', and '1234567890'
        // into the respective boxes
        dialog.textBox("firstName").enterText("John");
        dialog.textBox("lastName").enterText("Doe");
        dialog.textBox("address").enterText("1234 SomeStreet");
        dialog.textBox("city").enterText("SomeCity");
        dialog.textBox("state").enterText("FL");
        dialog.textBox("zip").enterText("12345");
        dialog.textBox("phone").enterText("1234567890");

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Test person is added
        window.table().requireRowCount(1);

        // Test the values match
        window.table().requireContents(
                new String[][] { { "Doe", "John", "1234 SomeStreet", "SomeCity", "FL", "12345", "1234567890" } });
    }

    /**
     * This is a negative test to check behavior of when
     * the GUI tries to add an invalid person
     */
    @Test
    public void cantCreateBadNewPerson() {
        // Click and get dialog window
        window.button("add").click();
        DialogFixture dialog = window.dialog();

        // Type just 'John','Doe'
        dialog.textBox("firstName").enterText("John");
        dialog.textBox("lastName").enterText("Doe");

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Test that there are still no people
        window.table().requireRowCount(0);
    }

    /**
     * This will test that the AddressBook GUI will
     * successfully allow the user to edit a person.
     */
    @Test
    public void canEditPerson() {
        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click 'John Doe' test person entry and click 'Edit'
        window.table().cell("John").click();
        window.button("edit").click();

        // Get the person dialog
        DialogFixture dialog = window.dialog();

        // Test person gets fully loaded
        dialog.textBox("firstName").requireText("John");
        dialog.textBox("lastName").requireText("Doe");
        dialog.textBox("address").requireText("1234 SomeStreet");
        dialog.textBox("city").requireText("SomeCity");
        dialog.textBox("state").requireText("FL");
        dialog.textBox("zip").requireText("12345");
        dialog.textBox("phone").requireText("1234567890");

        // Change John's state to 'GA'
        dialog.textBox("state").click().deleteText().enterText("GA");

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Test that the table contains the updated data
        window.table().requireContents(
                new String[][] { { "Doe", "John", "1234 SomeStreet", "SomeCity", "GA", "12345", "1234567890" },
                        { "Doe", "Jane", "1234 SomeStreet", "SomeCity", "FL", "12345", "1234567890" } });
    }

    /**
     * This test case will test if the GUI allows person to be deleted.
     */
    @Test
    public void canDeletePerson() {
        // Click 'open' item
        window.menuItem("file").click();
        window.menuItem("open").click();

        // Get the file chooser and select the test file
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Check table now has the two persons in file
        window.table().requireRowCount(2);

        // Click on the 'John Doe' entry
        window.table().cell("John").click();

        // Click 'delete'
        window.button("delete").click();

        // Test that only one row remains
        window.table().requireRowCount(1);

        // Check only person existing is not John
        window.table().requireContents(
                new String[][] { { "Doe", "Jane", "1234 SomeStreet", "SomeCity", "FL", "12345", "1234567890" } });
    }

    /**
     * This test case will test if the AddressBook GUI
     * will allow user to cancel adding a new person.
     */
    @Test
    public void canCreateNewPersonCancelled() {
        // Click and get dialog window
        window.button("add").click();
        DialogFixture dialog = window.dialog();

        // Type 'John'
        dialog.textBox("firstName").enterText("John");

        // Click 'Cancel'
        dialog.button(JButtonMatcher.withText("Cancel")).click();

        // Test person is not added
        window.table().requireRowCount(0);
    }

    /**
     * This testcase will test GUI will allow the user
     * to cancel editing a person.
     */
    @Test
    public void canEditPersonCancelled() {
        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click 'John Doe' test person entry and click 'Edit'
        window.table().cell("John").click();
        window.button("edit").click();

        // Get the person dialog
        DialogFixture dialog = window.dialog();

        // Change John's state to 'GA'
        dialog.textBox("state").click().deleteText().enterText("GA");

        // Click 'Cancel'
        dialog.button(JButtonMatcher.withText("Cancel")).click();

        // Test that the table is the same as the test file (Unchanged)
        window.table().requireContents(
                new String[][] { { "Doe", "John", "1234 SomeStreet", "SomeCity", "FL", "12345", "1234567890" },
                        { "Doe", "Jane", "1234 SomeStreet", "SomeCity", "FL", "12345", "1234567890" } });
    }

    /**
     * This is a functional test to check if the
     * AddressBookGUI will not allow edit to work
     * if there is no row selected.
     */
    @Test
    public void canEditPersonNoRowSelected() {
        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click edit button for no window
        window.button("edit").click();

        // Test that the edit button on the main menu
        // is still focused indicating nothing was opened
        window.button("edit").requireFocused();
    }

    /**
     * This ia a functional test to test if
     * AddressBook GUI will not allow user to delete
     * a person if there is no row seleceted
     */
    @Test
    public void canDeletePersonNoRowSelected() {
        // Click 'open' item
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Check table now has the two persons in file
        window.table().requireRowCount(2);

        // Click 'delete'
        window.button("delete").click();

        // Test that both rows remain
        window.table().requireRowCount(2);
    }

    /**
     * This is a functional test to test that the
     * program will allow user to start a new addressbook.
     */
    @Test
    public void canStartNewBook() {
        // Check that new item is clickable
        window.menuItem("new").requireEnabled();

        // Click 'new' item
        window.menuItem("file").click();
        window.menuItem("new").click();

        // Check that save item is now disabled
        window.menuItem("save").requireDisabled();

        // Check saveAs still matches state of save
        saveAndSaveAsMatchEnabledState();
    }

    /**
     * This test will test if program will allow
     * opening a blank AddressBok file.
     * @throws IOException when file cannot be read
     */
    @Test
    public void canOpenExistingBookBlankFile() throws IOException {
        // Check that open item is clickable
        window.menuItem("open").requireEnabled();

        // Click 'open' item
        window.menuItem("file").click();
        window.menuItem("open").click();

        // Create a blank file
        File testFile = folder.newFile("myOtherTestFile");
        testFile.createNewFile();

        // Get the file chooser and select the file saved
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Check error message is displayed
        window.optionPane().requireErrorMessage();

        // Delete file
        testFile.delete();
    }

    /**
     * This tests that the program will be able to
     * open a valid address book.
     */
    @Test
    public void canOpenExistingBook() {
        // Check that open item is clickable
        window.menuItem("open").requireEnabled();

        // Click 'open' item
        window.menuItem("file").click();
        window.menuItem("open").click();

        // Get the file chooser and select the file saved
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Check table now has the two persons in file
        window.table().requireRowCount(2);

        // Check that save item is now disabled
        window.menuItem("save").requireDisabled();

        // Check saveAs still matches state of save
        saveAndSaveAsMatchEnabledState();
    }

    /**
     * Tests that canceling from the save menu
     * successfully works.
     */
    @Test
    public void canOpenExistingBookCancelled() {
        // Check that open item is clickable
        window.menuItem("open").requireEnabled();

        // Click 'open' item
        window.menuItem("file").click();
        window.menuItem("open").click();

        // Get the file chooser and select the file saved
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().cancel();

        // Check table still has no people
        window.table().requireRowCount(0);
    }

    /**
     * This tests checks if program can successfully
     * overwrite a file with a valid addressbook.
     */
    @Test
    public void canSaveNewBookOverAnother() {
        // Add a person to a new book
        window.button("add").click();
        DialogFixture dialog = window.dialog();

        // Type 'John','Doe','1234 SomeStreet','SomeCity','FL','12345', and '1234567890'
        // into the respective boxes
        dialog.textBox("firstName").enterText("John");
        dialog.textBox("lastName").enterText("Doe");
        dialog.textBox("address").enterText("1234 SomeStreet");
        dialog.textBox("city").enterText("SomeCity");
        dialog.textBox("state").enterText("FL");
        dialog.textBox("zip").enterText("12345");
        dialog.textBox("phone").enterText("1234567890");

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Make sure save and saveAs are enabled
        window.menuItem("save").requireEnabled();
        window.menuItem("saveAs").requireEnabled();

        // Click 'save'
        window.menuItem("file").click();
        window.menuItem("save").click();

        // Save over test file
        window.fileChooser().selectFile(testFile);
        window.fileChooser().approve();

        // Check that question message is shown for overwriting a book
        window.optionPane().requireQuestionMessage();

        // Select 'No'
        window.dialog().button(JButtonMatcher.withText("No")).click();

        // Click 'save' again
        window.menuItem("file").click();
        window.menuItem("save").click();

        // Save over test file
        window.fileChooser().selectFile(testFile);
        window.fileChooser().approve();

        // Check that question message is shown for overwriting a book
        window.optionPane().requireQuestionMessage();

        // Select 'Yes' this time
        window.dialog().button(JButtonMatcher.withText("Yes")).click();
    }

    /**
     * This functional test will test if program can
     * save a addressbook that has been edited.
     * @throws IOException when file cannot be written
     */
    @Test
    public void canSaveEditedBook() throws IOException {
        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click 'John Doe' test person entry and click 'Edit'
        window.table().cell("John").click();
        window.button("edit").click();

        // Get the person dialog
        DialogFixture dialog = window.dialog();

        // Edit the person
        dialog.textBox("state").click().deleteText().enterText("GA");
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Check save button is active
        window.menuItem("save").requireEnabled();
        window.menuItem("saveAs").requireEnabled();

        // Click 'save' and save to file
        window.menuItem("file").click();
        window.menuItem("saveAs").click();
        window.fileChooser().setCurrentDirectory(folder.getRoot()).fileNameTextBox().enterText("test file");
        window.fileChooser().approve();

        // Test file exists
        File file = new File(folder.getRoot() + "/test file");
        assertTrue(file.exists());
    }

    /**
     * This tests will check that cancel function works
     * when opting to cancel before saving.
     * @throws IOException
     */
    @Test
    public void canSaveEditedBookCancelled() throws IOException {
        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click 'John Doe' test person entry and click 'Edit'
        window.table().cell("John").click();
        window.button("edit").click();

        // Get the person dialog
        DialogFixture dialog = window.dialog();

        // Edit the person
        dialog.textBox("state").click().deleteText().enterText("GA");
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Click 'save' and cancel
        window.menuItem("file").click();
        window.menuItem("saveAs").click();
        window.fileChooser().setCurrentDirectory(folder.getRoot()).fileNameTextBox().enterText("test file");
        window.fileChooser().cancel();

        // Test file does not exists
        File file = new File(folder.getRoot() + "/test file");
        assertFalse(file.exists());
    }

    /**
     * This tests that the print functionality works.
     */
    @Test
    public void canPrintBook() {
        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click print
        window.menuItem("file").click();
        window.menuItem("print").click();

        // Make sure that the print dialog is visible
        window.dialog().requireVisible();
    }

    /**
     * Tests that exception will be thrown is printing fails.
     * @throws PrinterException when print fails
     */
    @Test
    public void errorShowsOnPrintFail() throws PrinterException {
        // Clear the started program
        window.cleanUp();

        // Remove edtViolationException trigger for creating mocks
        FailOnThreadViolationRepaintManager.uninstall();

        // Set table to throw exception
        JTable tableMock = mock(JTable.class);
        doThrow(new PrinterException("A print error has occurred")).when(tableMock).print();
        addressBookGUI = GuiActionRunner.execute(() -> new AddressBookGUI(tableMock));

        // Start the application with the injected mocks
        window = new FrameFixture(addressBookGUI);
        window.show();

        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click print
        window.menuItem("file").click();
        window.menuItem("print").click();

        // Make sure that the warning dialog is visible
        window.optionPane().requireWarningMessage();
    }

    /**
     * This test will test that the program will open up
     * an error message if a save is unsuccessful.
     * @throws SQLException if database cannot be connected.
     */
    // NOTE: this test will appear to display incorrectly while running.
    // This is due to the way the GUI has been replaced. The test still
    // functions correctly
    @Test
    public void errorShowsOnSaveFail() throws SQLException {
        // Clear the started program
        window.cleanUp();

        // Remove edtViolationException trigger for creating spies
        FailOnThreadViolationRepaintManager.uninstall();

        // Set table to throw exception
        AddressBook addressBookSpy = spy(new AddressBook());
        AddressBookController controllerSpy = spy(new AddressBookController(addressBookSpy));
        doThrow(new SQLException("An error occurred during save")).when(controllerSpy).save(isA(File.class));
        addressBookGUI = GuiActionRunner.execute(() -> new AddressBookGUI(addressBookSpy, controllerSpy));

        // Start the application with the injected spies
        window = new FrameFixture(addressBookGUI);
        window.show();

        // Load sample address Book (so current file is not null)
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click and get dialog window
        window.button("add").click();
        DialogFixture dialog = window.dialog();

        // Type 'John','Doe','1234 SomeStreet','SomeCity','FL','12345', and '1234567890'
        // into the respective boxes
        dialog.textBox("firstName").enterText("John");
        dialog.textBox("lastName").enterText("Doe");
        dialog.textBox("address").enterText("1234 SomeStreet");
        dialog.textBox("city").enterText("SomeCity");
        dialog.textBox("state").enterText("FL");
        dialog.textBox("zip").enterText("12345");
        dialog.textBox("phone").enterText("1234567890");

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Click print
        window.menuItem("file").click();
        window.menuItem("save").click();

        // Make sure that the error dialog is visible
        window.optionPane().requireErrorMessage();
    }

    /**
     * This tests that dialogbox will popup if the program
     * tries to clear the addressbook with "new" button
     */
    @Test
    public void confirmDialogShowsOnNew() {
        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click 'John Doe' test person entry and click 'Edit'
        window.table().cell("John").click();
        window.button("edit").click();

        // Get the person dialog
        DialogFixture dialog = window.dialog();

        // Change John's state to 'GA'
        dialog.textBox("state").click().deleteText().enterText("GA");

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Click 'New'
        window.menuItem("file").click();
        window.menuItem("new").click();

        // Test that a question message is shown
        window.optionPane().requireQuestionMessage();

        // Select 'No'
        window.dialog().button(JButtonMatcher.withText("No")).click();

        // Click 'New' again
        window.menuItem("file").click();
        window.menuItem("new").click();

        // Test that a question message is shown
        window.optionPane().requireQuestionMessage();

        // Select 'Yes' this time
        window.dialog().button(JButtonMatcher.withText("Yes")).click();
    }

    /**
     * Tests that a dialog box will popup if the "open" is pressed
     * after an edit is made to the address book.
     */
    @Test
    public void confirmDialogShowsOnOpen() {
        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click 'John Doe' test person entry and click 'Edit'
        window.table().cell("John").click();
        window.button("edit").click();

        // Get the person dialog
        DialogFixture dialog = window.dialog();

        // Change John's state to 'GA'
        dialog.textBox("state").click().deleteText().enterText("GA");

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Click 'Open'
        window.menuItem("file").click();
        window.menuItem("open").click();

        // Test that a question message is shown
        window.optionPane().requireQuestionMessage();

        // Select 'No'
        window.dialog().button(JButtonMatcher.withText("No")).click();

        // Click 'Open' again
        window.menuItem("file").click();
        window.menuItem("open").click();

        // Test that a question message is shown
        window.optionPane().requireQuestionMessage();

        // Select 'Yes' this time
        window.dialog().button(JButtonMatcher.withText("Yes")).click();
    }

    /**
     * Tests that no dialog box will appear when ""quit" is pressed
     * when no edits have been made to address book.
     */
    @Test
    public void noConfirmDialogIfNothingChanged() {
        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click 'quit'
        window.menuItem("file").click();
        window.menuItem("quit").click();

        // Test that no option pane was shown (a component lookup of option pane will
        // fail)
        assertThrows(Exception.class, () -> window.optionPane());
    }

    /**
     * Test that when DialogBox pops up, pressing cancel
     * will not close program
     */
    @Test
    public void confirmDialogShowsOnQuitCancel() {
        // Remove no exit so that window dispatch event line can run (Needed for 100%
        // statement coverage)
        NoExitSecurityManagerInstaller.installNoExitSecurityManager().uninstall();

        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click 'John Doe' test person entry and click 'Edit'
        window.table().cell("John").click();
        window.button("edit").click();

        // Get the person dialog
        DialogFixture dialog = window.dialog();

        // Change John's state to 'GA'
        dialog.textBox("state").click().deleteText().enterText("GA");

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Click 'quit'
        window.menuItem("file").click();
        window.menuItem("quit").click();

        // Test that a question message is shown
        window.optionPane().requireQuestionMessage();

        // Test closing works
        window.optionPane().buttonWithText("No").click();
    }

    /**
     * Tests that pressing "Yes" on the Dialog Box
     * will close the program
     */
    @Test
    public void confirmDialogShowsOnWindowCloseConfirm() {
        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Click 'John Doe' test person entry and click 'Edit'
        window.table().cell("John").click();
        window.button("edit").click();

        // Get the person dialog
        DialogFixture dialog = window.dialog();

        // Change John's state to 'GA'
        dialog.textBox("state").click().deleteText().enterText("GA");

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Close window
        window.close();

        // Test that a question message is shown
        window.optionPane().requireQuestionMessage();

        // Test cancelling works
        window.optionPane().buttonWithText("Yes").click();
    }

    /**
     * This functional test will test that the search box
     * will filter out address book base on query
     */
    @Test
    public void canSearchPeople() {
        // Load sample address Book
        window.menuItem("file").click();
        window.menuItem("open").click();
        window.fileChooser().selectFile(testFile.getAbsoluteFile());
        window.fileChooser().approve();

        // Type 'jan'
        window.textBox().enterText("jan");

        // Check only 'Jane' shows
        window.table().requireRowCount(1);
        window.table().requireContents(
                new String[][] { { "Doe", "Jane", "1234 SomeStreet", "SomeCity", "FL", "12345", "1234567890" } });

        // Type 'jo'
        window.textBox().deleteText().enterText("jo");

        // Check only 'John' entry shows
        window.table().requireRowCount(1);
        window.table().requireContents(
                new String[][] { { "Doe", "John", "1234 SomeStreet", "SomeCity", "FL", "12345", "1234567890" } });

        // Type '12'
        window.textBox().deleteText().enterText("12");

        // Check both entries show
        window.table().requireRowCount(2);
        window.table().requireContents(
                new String[][] { { "Doe", "John", "1234 SomeStreet", "SomeCity", "FL", "12345", "1234567890" },
                        { "Doe", "Jane", "1234 SomeStreet", "SomeCity", "FL", "12345", "1234567890" } });
    }

    /**
     * This tests that save function is disabled when program starts.
     */
    @Test
    public void saveIsDisabledByDefault() {
        // Check if saving is disabled
        window.menuItem("save").requireDisabled();

        // Check save and saveAs states match
        saveAndSaveAsMatchEnabledState();
    }

    /**
     * This functional test will test that program is opened properly
     * @throws ClassNotFoundException
     */
    @Test
    public void programLaunchesCorrectly() throws ClassNotFoundException {
        // Get robot
        Robot robot = window.robot();

        // Clear the started program
        window.cleanUp();

        // Start the application
        AddressBookGUI.main(null);

        // Find the generated window and ensure it is showing. If one is not found
        // this test fails.
        window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            protected boolean isMatching(JFrame frame) {
                return "Address Book".equals(frame.getTitle()) && frame.isShowing();
            }
        }).using(robot);
    }

    /**
     * Tests that save and saveas buttons are matching states.
     */
    @Test
    public void saveAndSaveAsMatchEnabledState() {
        // Check if save and saveAs match enabled state
        assertEquals(window.menuItem("save").isEnabled(), window.menuItem("saveAs").isEnabled());
    }
}