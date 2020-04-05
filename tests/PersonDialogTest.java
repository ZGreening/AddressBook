import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
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

class PersonDialogTest {

    @Rule
    private static FrameFixture window = null;
    public static TemporaryFolder folder = new TemporaryFolder();


    @BeforeAll
    public static void init() {
        // Prevent program exiting
        NoExitSecurityManagerInstaller.installNoExitSecurityManager();

        // Required for full AssertJ GUI testing
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    void setUp() throws IOException, ClassNotFoundException{
        // Initialize window
        AddressBookGUI frame = GuiActionRunner.execute(() -> new AddressBookGUI());
        window = new FrameFixture(frame);
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
   
    /**
     * This tests that a person can be added.
     */

    @Test
    void addPersonValid() {
        window.button("add").click();
        DialogFixture dialog = window.dialog();

        // Type 'Muffin','Man','1234 Dreary Lane','Cookie City','WA','66666', and '1234567890'
        // into the respective boxes
        dialog.textBox("firstName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_U, VK_F, VK_F, VK_I, VK_N);
        dialog.textBox("lastName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_A, VK_N);
        dialog.textBox("address").pressAndReleaseKeys(VK_1, VK_2, VK_3, VK_4, VK_SPACE).pressKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_D).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_R, VK_E, VK_A, VK_R, VK_Y, VK_SPACE).pressKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_L).releaseKey(VK_SHIFT).pressAndReleaseKeys(VK_A, VK_N, VK_E);
        dialog.textBox("city").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_C).releaseKey(VK_SHIFT)
                .pressAndReleaseKeys(VK_O, VK_O, VK_K, VK_I, VK_E, VK_SPACE).pressKey(VK_SHIFT).pressAndReleaseKeys(VK_C)
                .releaseKey(VK_SHIFT).pressAndReleaseKeys(VK_I, VK_T, VK_Y);
        dialog.textBox("state").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_W, VK_A).releaseKey(VK_SHIFT);
        dialog.textBox("zip").pressAndReleaseKeys(VK_6, VK_6, VK_6, VK_6, VK_6);
        dialog.textBox("phone").pressAndReleaseKeys(VK_1, VK_2, VK_3, VK_4, VK_5, VK_6, VK_7, VK_8, VK_9, VK_0);

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Test person is added
        window.table().requireRowCount(1);

    }

    /**
     * This tests that a person cannot be added with improper characters.
     */

    @Test
    void addPersonInvalid() {
        // Test when user exits with OK
        window.button("add").click();
        DialogFixture dialog = window.dialog();

        dialog.textBox("firstName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_1).releaseKey(VK_SHIFT)
            .pressAndReleaseKeys(VK_2, VK_2, VK_4, VK_5, VK_6);
        
        dialog.textBox("lastName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
            .pressAndReleaseKeys(VK_9, VK_7);
        
        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Test person is not added
        window.table().requireRowCount(0);

    }


    @Test
    void addPersonNullFirst() {
        // Test when user exits with OK
        window.button("add").click();
        DialogFixture dialog = window.dialog();

        dialog.textBox("firstName").pressKey(VK_SPACE);

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Test person is not added
        window.table().requireRowCount(0);

    }


    @Test
    void addPersonMixedInvlaid() {
        // Test first name valid and lastname invalid when user exits with OK
        window.button("add").click();
        DialogFixture dialog = window.dialog();

        dialog.textBox("firstName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
            .pressAndReleaseKeys(VK_U, VK_F, VK_F, VK_I, VK_N);
        dialog.textBox("lastName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
            .pressAndReleaseKeys(VK_9, VK_7);

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Test person is not added
        window.table().requireRowCount(0);

    }

    // @Test
    // void addPersonMixedInvlaid2() {
    //     // Test when user exits with OK
    //     window.button("add").click();
    //     DialogFixture dialog = window.dialog();

    //     dialog.textBox("firstName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_1).releaseKey(VK_SHIFT)
    //         .pressAndReleaseKeys(VK_2, VK_2, VK_4, VK_5, VK_6);
    //     // dialog.textBox("lastName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
    //     //     .pressAndReleaseKeys(VK_A, VK_N);

    //     // Click 'OK'
    //     dialog.button(JButtonMatcher.withText("OK")).click();

    // }

    @Test
    void addPersonNull() {
        // Test when user exits with OK
        window.button("add").click();
        DialogFixture dialog = window.dialog();

        // Click 'OK'
        dialog.button(JButtonMatcher.withText("OK")).click();

        // Test person is not added
        window.table().requireRowCount(0);

    }

    // @Test
    // void addPersonCancel() {
    //     // Test when user exits with Cancel
    //     window.button("add").click();
    //     DialogFixture dialog = window.dialog();

    //     dialog.textBox("firstName").pressKey(VK_SHIFT).pressAndReleaseKeys(VK_M).releaseKey(VK_SHIFT)
    //     .pressAndReleaseKeys(VK_U, VK_F, VK_F, VK_I, VK_N);

    //     // Click 'OK'
    //     dialog.button(JButtonMatcher.withText("Cancel")).click();

    // }

    // @Test
    // void nullPerson() {
    //     AddressBookGUI frame = GuiActionRunner.execute(() -> new AddressBookGUI());
    //     window = new FrameFixture(frame);
    //     window.show();
    //     PersonDialog nullDialog = new PersonDialog(frame, null);

    // }

}