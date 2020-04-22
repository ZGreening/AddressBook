import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * AddressBookTest.java
 * This is the testclass for AddressBook's unit tests
 */
class AddressBookTest {

  Person test_Person;
  AddressBook test_AddressBook;

  /**
   * This method runs before each test to initialize variables.
   */
  @BeforeEach
  void setUp() {
    test_Person = new Person("Jane","Dorian",
        "987 Westbrook Blvd","Chincinnati","OH","43123","0123456789");
    test_AddressBook = new AddressBook();
  }

  /**
   * This method runs before each test to clear variables.
   */
  @AfterEach
  void tearDown() {
    test_Person=null;
    test_AddressBook=null;
  }

  /**
   * Functional Testing and Positive Testing
   * This test case will test that a new person will replace a person
   * already in the Address Book.
   */
  @Test
  public void setNewPersonToAddressBook(){
    //Given index, change personList with new Person
    Person test_Person2 = new Person("Sweet","Tea",
        "19964 Miami Beach","Miami","OH","85012","2321232010");
    test_AddressBook.add(test_Person);
    test_AddressBook.set(0,test_Person2);
    //Person[] personArray= test_AddressBook.getPersons();
    assertEquals("Tea, Sweet",test_AddressBook.get(0).toString());

  }
  /**
   * Functional Testing and Negative
   * This test case will throw an error if set method is
   * called on a empty AddressBook
   */
  @Test
  public void setNewPersonToEmptyListThrowsError(){
    //Given index, change personList with new Person
    Person test_Person2 = new Person("Sweet","Tea",
        "19964 Miami Beach","Miami","OH","85012","2321232010");
    assertEquals(0,test_AddressBook.getPersons().length);
    assertThrows(IndexOutOfBoundsException.class,()->test_AddressBook.set(0,test_Person2));
  }

  /**
   * Functional Testing and Negative Testing
   * This test case will test that calling set with a null person
   * will not enter the AddressBook or change the table
   */
  @Test
  public void setNullPersonNothingHappens(){
    test_AddressBook.add(test_Person);
    test_AddressBook.set(0,null);
    assertNotNull(test_AddressBook.get(0));
    assertEquals("Dorian, Jane",test_AddressBook.get(0).toString());

  }

  /**
   * Functional Testing and Positive Testing
   * This test case will test if new person is successfully added in Address Book.
   */
  @Test
  public void addPersonToAddressBook(){
    test_AddressBook.add(test_Person);
    assertEquals("Dorian, Jane",test_AddressBook.get(0).toString());
  }

  /**
   * Functional Testing and Positive Testing
   * This test will check getPersons will return the Array of the
   * current persons List
   */
  @Test
  void getPersonsTest() {
    //Attempt to convert ArrayList to an array

    test_AddressBook.add(test_Person);

    assertEquals(1,test_AddressBook.getPersons().length);
    assertEquals("Dorian, Jane",test_AddressBook.getPersons()[0].toString());
  }

  /**
   * Functional Testing and Positive Testing
   * This test case tests if a person is successfully removed from Person List.
   */
  @Test
  void removePersonFromAddressBook() {
    //Given an index delete person from arrayList
    test_AddressBook.add(test_Person);
    Person test_Person2 = new Person("Steely","Phil",
        "19964 Miami Beach","Miami","OH","85012","2321232100");
    test_AddressBook.add(test_Person2);
    test_AddressBook.remove(0);
    boolean isInArray = false;
    String search = "Dorian, Jane";
    for(int i = 0; i < test_AddressBook.getPersons().length; i++)
    {
      if(test_AddressBook.getPersons()[i].toString().equals(search))
      {
        //Found Person!
        isInArray = true;
        break;
      }
    }
    //Assert Person not found
    assertFalse(isInArray);
    assertEquals("Phil, Steely", test_AddressBook.get(0).toString());
  }

  /**
   * Functional Testing and Negative Testing
   * This is a negative test case to test if Out of Bounds exception
   * is thrown if removing on empty list
   */
  @Test
  void removePersonFromEmptyAddressBookThrowsError(){
    assertThrows(IndexOutOfBoundsException.class, ()-> test_AddressBook.remove(0));
  }

  /**
   * Functional Testing and Positive Testing
   * This method checks if the correct person is returned by get method.
   */
  @Test
  void getPersonFromAddressBookPersonList() {
    test_AddressBook.add(test_Person);
    assertEquals(test_Person.getFirstName(),test_AddressBook.get(0).getFirstName());
  }

  /**
   * Functional Testing and Positive Testing
   * This test cases will test if AddressBook Clears all people from the list.
   */
  @Test
  void clearWithEmptyList() {
    test_AddressBook.clear();
    assertTrue(test_AddressBook.getPersons().length ==0);
  }

  /**
   * Functional Testing and Positive Testing
   * This test cases will test if AddressBook Clears all people from the list.
   */
  @Test
  void ClearWithNonEmptyList(){
    test_AddressBook.add(test_Person);
    test_AddressBook.clear();
    assertTrue(test_AddressBook.getPersons().length ==0);
  }

  /**
   * Functional Testing and Positive Testing
   * This test case tests if rowCount is giving the correct data.
   * The row count is equal to the number of entries in AddressBook.
   */
  @Test
  void getRowCount() {
    //RowCount is Size of personList
    test_AddressBook.add(test_Person);
    test_AddressBook.add(test_Person);
    assertEquals(2,test_AddressBook.getRowCount());
  }

  /**
   * Functional Testing and Positive Testing
   * This test case will test if the column number is correct.
   * The column number is equal to number of fields of Person class, which
   * should be 7.
   */
  @Test
  void getColumnCount() {
    assertEquals(7,test_AddressBook.getColumnCount());
  }

  /**
   * Functional Testing and Positive Testing
   * This test case will verify if the right information is given about a person
   * in the Address Book.
   * Two indexes are given, first will point to the Person, the second is the field.
   */
  @Test
  void getValueAt() {
    test_AddressBook.add(test_Person);
    Person test_Person2 = new Person("Steely","Phil",
        "19964 Miami Beach","Miami","OH","85012","2321232100");

    test_AddressBook.add(test_Person2);
    assertEquals("Chincinnati",  test_AddressBook.getValueAt(0,3));
    //Get value from Second person
    assertEquals("2321232100",  test_AddressBook.getValueAt(1,6));
  }

  /**
   * Functional Testing and Positive Testing
   * This test case will test if the column name is correct.
   * column name is the Person class's field member variable
   */
  @Test
  void getColumnName() {
    test_AddressBook.add(test_Person);
    assertEquals("Last Name",test_AddressBook.getColumnName(0));
    assertEquals("Phone",test_AddressBook.getColumnName(6));
  }
}