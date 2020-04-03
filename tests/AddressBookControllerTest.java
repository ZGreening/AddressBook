import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

class AddressBookControllerTest {

  //DefaultTableModel model;
  List<Person> personListTest;
  Person test_Person;
  AddressBook test_AddressBook;

  AddressBookController controllerTest;

  @BeforeEach
  void setUp() {

    //model = new DefaultTableModel();
    personListTest = new ArrayList<>();
    test_Person = new Person("Jane","Dorian",
        "987 Westbrook Blvd","Chincinnati","OH","43123","0123456789");
    test_AddressBook = new AddressBook();
    controllerTest = new AddressBookController(test_AddressBook);
  }

  @AfterEach
  void tearDown() {
    //model = null;
    personListTest = null;
    test_Person=null;
    test_AddressBook = null;
    controllerTest = null;
  }

  /**
   * This test case test will test if a person is succesffuly added to Address Book.
   */
  @Test
  void addPersonToList() {
    controllerTest.add(test_Person);

    System.out.println(controllerTest.get(0).toString());
    assertEquals(controllerTest.get(0).getFirstName(),"Jane");
  }

  /**
   * This case tests set method. Given an index a person object will be replaced
   * with a new person object.
   */
  @Test
  void setWith1PersonInList() {
    //given index, and person chnage person in address book
    //doNothing().when(addressBookMock).set(0,test_Person);
    controllerTest.add(test_Person);
    controllerTest.set(0,test_Person);

    assertEquals(controllerTest.get(0),test_Person);
  }

  /**
   * This is a negative test case that will throw an Exception if the method
   * tries to set a new person on a index that doesnt exist.
   */
  @Test
  void setWithEmptyListShouldThrowError() {
    assertThrows(IndexOutOfBoundsException.class, () -> controllerTest.set(0,test_Person));

  }

  /**
   * This test case will test that a person will be removed from Address Book.
   */
  @Test
  void removePersonFromListAndListWillNotBeEmpty() {
    //doNothing().when(addressBookMock).remove(0);
    controllerTest.add(test_Person);
    int originalsize = controllerTest.addressBook.getPersons().length;

    controllerTest.remove(0);
    int newsize = controllerTest.addressBook.getPersons().length;

    assertEquals(newsize+1, originalsize);
  }

  /**
   * This is a negative test case to test that test that an Error is thrown
   * if the method tries to remove a person from a empty Person list.
   */
  @Test
  void removeOnEmptyListShouldThrowError() {
    assertThrows(IndexOutOfBoundsException.class, () -> controllerTest.remove(0));
  }

  /**
   * This test case will test if Address Book Person clears correctly.
   */
  @Test
  void getPersonOnNONEmpyList() {
    controllerTest.add(test_Person);

    //System.out.println(controllerTest.get(0).toString());
    assertEquals(controllerTest.get(0),test_Person);
  }

  @Test
  void getOnEmpyListShouldThrowError() {
    assertThrows(IndexOutOfBoundsException.class, () -> controllerTest.get(0));
  }

  /**
   * This test case test if Address Book list will clear, given there
   * is a person already in the Address Book
   *
   */
  @Test
  void clearList() {
    test_AddressBook.add(test_Person);
    assertFalse(test_AddressBook.getPersons().length==0);

    controllerTest.clear();
    assertTrue(test_AddressBook.getPersons().length==0);
  }

  @Rule
  public static TemporaryFolder folder = new TemporaryFolder();
  private static AddressBook addressBook = null; //An generic address book
  private static FileSystem fileSystem=null; //The file system for testing
  private static File file = null;           //A java file for testing

  /**
   * This test case will test if program can open a normal file.
   * The testing of file system is for the FileSystem test class thus, the test here will
   * be a simple case of seeing if a acceptable file will open.
   *
   * @throws IOException
   */
  @Test
  public void openValidFile() throws IOException {
    //Create a new filesystem each time
    fileSystem = new FileSystem();

    //Create a new file for testing each time
    folder.create();
    file = folder.newFile("MyTestFile");

    //Add two people to address book and save
    test_AddressBook.add(new Person("John","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));
    test_AddressBook.add(new Person("Jane","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));
    assertDoesNotThrow(() -> fileSystem.saveFile(test_AddressBook,file));
    assertDoesNotThrow( () -> controllerTest.open(file));
  }

  /**
   * This testcase will check if program will be able to successfully save a file.
   *
   * @throws IOException
   */
  @Test
  public void saveValidFile() throws IOException {

    //Create a new filesystem each time
    fileSystem = new FileSystem();

    //Create a new file for testing each time
    folder.create();
    file = folder.newFile("MyTestFile");

    //Add two people to address book
    test_AddressBook.add(new Person("John","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));
    test_AddressBook.add(new Person("Jane","Doe","1234 NON EXIST ROAD","SomeCity","FL","12345","1234567890"));

    assertDoesNotThrow(() -> controllerTest.save(file));
    assertTrue(file.exists()); //Check file exists on disk
  }

  /**
   * This testcase will test if the AddressBook Controller returns
   * the right address book object.
   */
  @Test
  void getAddressBook() {
    assertEquals(test_AddressBook, controllerTest.getModel());
  }
}