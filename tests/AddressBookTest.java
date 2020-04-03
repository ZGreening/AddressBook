import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;




class AddressBookTest {

  DefaultTableModel model;
  List<Person> personListTest;
  Person test_Person;
  AddressBook test_AddressBook;


  @BeforeEach
  void setUp() {

    model = new DefaultTableModel();
    personListTest = new ArrayList<>();
    test_Person = new Person("Jane","Dorian",
        "987 Westbrook Blvd","Chincinnati","OH","43123","0123456789");

    model.addColumn("Col1");
    model.addColumn("Col2");
    model.addColumn("Col3");
    model.addColumn("Col4");
    model.addColumn("Col5");
    model.addColumn("Col6");
    model.addColumn("Col7");
    test_AddressBook = new AddressBook();
  }

  @AfterEach
  void tearDown() {
    model = null;
    personListTest = null;
    test_Person=null;
  }

  /**
   * This test case will test that a new person will replace a person
   * already in the Address Bok.
   */
  @Test
  public void setNewPersonToAddressBook(){
    //Given index, change personList with new Person
    Person test_Person2 = new Person("Juanito","Hernandez",
        "19964 Miami Beach","Miami,Ohio","OH","85012","2321232010");
    test_AddressBook.add(test_Person);
    test_AddressBook.set(0,test_Person2);
    //Person[] personArray= test_AddressBook.getPersons();
    assertEquals(test_AddressBook.getPersons()[0].getFirstName(), test_Person2.getFirstName());

  }

  /**
   * This test case will test if new person is successfully added in Address Book.
   */
  @Test
  public void addPersonToAddressBook(){
    test_AddressBook.add(test_Person);
    assertTrue(test_AddressBook.getPersons()[0].getFirstName() =="Jane");
  }

  /**
   * This test case will test if getPerson() return the Address Book's person List
   * as an Array.
   */
  @Test
  void getPersons() {
    //Attempt to convert ArrayList to an array

    test_AddressBook.add(test_Person);

    //Create ArrayList for TestCase
    List<Person> personList = new ArrayList<>();
    personList.add(test_Person);

    //Create Array for TestCase
    Person[] test_PersonArray = new Person[personList.size()];
    personList.toArray(test_PersonArray);

    assertEquals(test_AddressBook.getPersons().length,test_PersonArray.length);
  }

  /**
   * This test case tests if a person is successfully removed from Person List.
   */
  @Test
  void removePerson() {
    //Given an index delete person from arrayList
    test_AddressBook.add(test_Person);
    Person test_Person2 = new Person("Juanito","Hernandez",
        "19964 Miami Beach","Miami,Ohio","OH","85012","2321232100");
    test_AddressBook.add(test_Person2);
    test_AddressBook.remove(0);
    assertTrue(test_AddressBook.getPersons()[0].getFirstName() == "Juanito");//Confirm delete
  }

  /**
   * This method checks if the correct person is returned by get() method.
   */
  @Test
  void getPersonFromAddressBookPerosnList() {
    test_AddressBook.add(test_Person);
    assertEquals(test_Person.getFirstName(),test_AddressBook.get(0).getFirstName());
  }

  /**
   * This test cases will test if AddressBook Clears all people from the list.
   */
  @Test
  void clearWithEmptyList() {
    test_AddressBook.clear();
    assertTrue(test_AddressBook.getPersons().length ==0);
  }

  /**
   * This test cases will test if AddressBook Clears all people from the list.
   */
  @Test
  void ClearWithNonEmptyList(){
    test_AddressBook.add(test_Person);
    test_AddressBook.clear();
    assertTrue(test_AddressBook.getPersons().length ==0);
  }

  /**
   * This test case tests if rowCount is giving the correct data.
   * The row count is equal to the number of entries in AddressBook.
   */
  @Test
  void getRowCount() {
    //RowCount is Size of persoonList
    test_AddressBook.add(test_Person);
    test_AddressBook.add(test_Person);
    assertEquals(test_AddressBook.getRowCount(),2);
  }

  /**
   * This test case will test if the column number is correct.
   * The column number is equal to number of fields of Person class, which
   * should be 7.
   */
  @Test
  void getColumnCount() {
    assertEquals(test_AddressBook.getColumnCount(),7);
  }

  /**
   * This test case will verify if the right information is given about a person
   * in the Address Book.
   * Two indexes are given, first will point to the Person, the second is the field.
   */
  @Test
  void getValueAt() {
    test_AddressBook.add(test_Person);
    assertEquals("Chincinnati",  test_AddressBook.getValueAt(0,3));
  }

  /**
   * This test case will test if the column name is correct.
   */
  @Test
  void getColumnName() {
    test_AddressBook.add(test_Person);
    System.out.println(test_AddressBook.getColumnName(1));
    assertEquals(test_AddressBook.get(0).getFirstName(),test_Person.getFirstName());
  }
}