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

import static java.awt.event.KeyEvent.*;

import java.io.IOException;

import javax.swing.JFrame;

class PersonDialogTest {

    @Rule
    private static DialogFixture window = null;
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
        PersonDialog frame = GuiActionRunner.execute(() -> new PersonDialog(new JFrame()));
        window = new DialogFixture(frame);
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
    ///////////////////////////////// Unit Tests //////////////////////////////////

    /**
     * This tests that a person can be added.
     */

    @Test
    void addPersonValid() {
        // Type 'Muffin','Man','1234 Dreary Lane','Cookie City','WA','66666', and '1234567890'
        // into the respective boxes
        window.textBox("firstName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_U, VK_F, VK_F, VK_I, VK_N);
        window.textBox("lastName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_A, VK_N);
        window.textBox("address").pressAndReleaseKeys(VK_1, VK_2, VK_3, VK_4, VK_SPACE).pressKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_D).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_R, VK_E, VK_A, VK_R, VK_Y, VK_SPACE).pressKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_L).releaseKey(VK_SHIFT).pressAndReleaseKeys(VK_A, VK_N, VK_E);
        window.textBox("city").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_C).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_O, VK_O, VK_K, VK_I, VK_E, VK_SPACE).pressKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_C).releaseKey(VK_SHIFT).pressAndReleaseKeys(VK_I, VK_T, VK_Y);
        window.textBox("state").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_W, VK_A).releaseKey(VK_SHIFT);
        window.textBox("zip").pressAndReleaseKeys(VK_6, VK_6, VK_6, VK_6, VK_6);
        window.textBox("phone").pressAndReleaseKeys(VK_1, VK_2, VK_3, VK_4, VK_5, VK_6, VK_7, VK_8, VK_9, VK_0);

        // Click 'OK'
        window.button(JButtonMatcher.withText("OK")).click();
    }

    /**
     * This tests that a person cannot be added with improper characters.
     */

    /*@Test
    void addPersonInvalid() {
        // Test when user exits with OK
        DialogFixture dialog = window.dialog();
        dialog.textBox("firstName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_1).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_2, VK_2, VK_4, VK_5, VK_6);

        dialog.textBox("lastName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_9, VK_7);

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Test person is not added
        window.table().requireRowCount(0);

    }*/

    /*@Test
    void addPersonInvalidFirst() {
        // Test when user exits with OK
        dialog.textBox("firstName").pressKey(VK_SPACE);

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();
    }*/

    /*@Test
    void addPersonMixedInvlaid() {
        // Test first name valid and lastname invalid when user exits with OK
        window.textBox("firstName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_U, VK_F, VK_F, VK_I, VK_N);
        window.textBox("lastName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_9, VK_7);

        // Click 'OK'
        window.button(JButtonMatcher.withText("OK")).click();
    }*/

    // @Test
    // void addPersonMixedInvlaid2() {
    // // Test when user exits with OK
    // window.button("add").click();
    // DialogFixture dialog = window.dialog();

    // dialog.textBox("firstName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_1).releaseKey(VK_SHIFT)
    // .pressAndReleaseKeys(VK_2, VK_2, VK_4, VK_5, VK_6);
    // //
    // dialog.textBox("lastName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
    // // .pressAndReleaseKeys(VK_A, VK_N);

    // // Click 'OK'
    // dialog.button(JButtonMatcher.withText("OK")).click();

    // }

    /*@Test
    void addPersonEmpty() {
        // Test when user exits with OK
        DialogFixture dialog = window.dialog();

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Test person is not added
        window.table().requireRowCount(0);

    }*/

    // @Test
    // void addPersonCancel() {
    // // Test when user exits with Cancel
    // window.button("add").click();
    // DialogFixture dialog = window.dialog();

    // dialog.textBox("firstName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
    // .pressAndReleaseKeys(VK_U, VK_F, VK_F, VK_I, VK_N);

    // // Click 'OK'
    // dialog.button(JButtonMatcher.withText("Cancel")).click();

    // }

    @Test
    void openWithPerson() {
        //Close empty window
        window.cleanUp();

        //Open window with person
        PersonDialog frame = GuiActionRunner.execute(() -> new PersonDialog(new JFrame(),new Person("John","Doe","123 Fake Street","Fort Myers","FL","33901","0123456789")));
        window = new DialogFixture(frame);
        window.show();

        //Check that required fields have been filled
        window.textBox("firstName").requireText("John");
        window.textBox("lastName").requireText("Doe");
        window.textBox("address").requireText("123 Fake Street");
        window.textBox("city").requireText("Fort Myers");
        window.textBox("state").requireText("FL");
        window.textBox("zip").requireText("33901");
        window.textBox("phone").requireText("0123456789");
    }
}