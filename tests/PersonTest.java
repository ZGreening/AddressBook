import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.rules.ExpectedException;

/**
 * PersonTest.java
 * This test class will test the Person Class's unit tests
 *
 */
public class PersonTest
{
  Person test_Person = new Person("John","Doe","123 Fake Street","Fort Myers","FL","33901","0123456789");

  /**
   * PairWise Testing
   *  This test case will that the 10 digit number for PhoneNumber
   *  allows a success person to be created.
   */
  @ParameterizedTest(name = "#{index} - Person Test with number: {6}")
  @CsvSource({
      "Joel, Masters,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      "Mary, Po,  931 DisneyStreet, Orlando,FL, 33123, 0987654321",
      "Hersey, Chocolate,  931 CandyLane, CandyIsland, FL, 33123, 0545689410",

  })
  void personPhoneNumberMustBe10Numbers(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertNotNull(new Person(firstName,lastName,address,city,state,zip,phone));
  }

  /**
   * PairWise Testing
   *  This test case will tests that exception is thrown if phone number
   *  is not 10 consecutive numbers.
   */
  @ParameterizedTest(name = "#{index} - Person Test with number: {6}")
  @CsvSource({
      "Joel, Masters,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 012345678954",
      "Mary, Po,  931 DisneyStreet, Orlando,FL, 33123, 098-765-4321",
      "Hersey, Chocolate,  931 CandyLane, CandyIsland, FL, 33123, 0",
      "DoughBoy, PillsBerry,  931 CandyLane, CandyIsland, FL, 33123, 1800DOUGH"
  })

  void personInvalidPhoneNumberThrowsException(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertThrows(IllegalArgumentException.class, () ->new Person(firstName, lastName, address, city, state, zip, phone));
  }

  /**
   * PairWise Testing
   *  This test case will that the 10 digit number for PhoneNumber
   *  allows a success person to be created.
   */
  @ParameterizedTest(name = "#{index} - Person Test with zip: {5}")
  @CsvSource({
      "Joel, Masters,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      "Mary, Po,  931 DisneyStreet, Orlando,FL, 33123, 0987654321",
      "Hersey, Chocolate,  931 CandyLane, CandyIsland, FL, 10325, 0545689410",

  })

  void personObjectZipCodeMustBe5NumbersToPass(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertNotNull(new Person(firstName,lastName,address,city,state,zip,phone));
  }

  /**
   * PairWise Testing
   *  This test case will tests that exception is thrown
   *  if zip is not 5 consecutive numbers.
   */
  @ParameterizedTest(name = "#{index} - Person Test with zip: {5}")
  @CsvSource({
      "Joel, Masters,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 3390, 0123456789",
      "Mary, Po,  931 DisneyStreet, Orlando,FL, 33123AZ@#,0987654321 ",
      "Hersey, Chocolate,  931 CandyLane, CandyIsland, FL, 33123-33901, 0545689410",
      "DoughBoy, PillsBerry,  931 CandyLane, CandyIsland, FL, 123, 1800123460"
  })

  void zipCodeMustBe5NumbersToPass(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertThrows(IllegalArgumentException.class, () ->new Person(firstName, lastName, address, city, state, zip, phone));
  }

  /**
   * PairWise Testing
   *  Will Test that 2 letters are valid input for state,
   *  and the person is created
   */
  @ParameterizedTest(name = "#{index} - Person Test with state: {4}")
  @CsvSource({
      "Joel, Masters,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      "Mary, Po,  931 DisneyStreet, Orlando,MA, 33123, 0987654321",
      "Hersey, Chocolate,  931 CandyLane, CandyIsland, OH, 33123, 0545689410",
      "DoughBoy, PillsBerry,  931 CandyLane, CandyIsland, NJ, 33123, 1800123460"
  })
  void statePassesRegexIf2AlphaCharacterAreEntered(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertNotNull(new Person(firstName,lastName,address,city,state,zip,phone));
  }

  /**
   * PairWise Testing
   *  Tests that if state isn't 2 letters, an exception is thrown
   */
  @ParameterizedTest(name = "#{index} - Person Test with state: {4}")
  @CsvSource({
      "Joel, Masters,  420 Yeehaw Avenue, LeeHigh Anchors,FL2, 33901, 0123456789",
      "Mary, Po,  931 DisneyStreet, Orlando,ma, 33123, 0987654321",
      "Hersey, Chocolate,  931 CandyLane, CandyIsland, H, 33123, 0545689410",
      "DoughBoy, PillsBerry,  931 CandyLane, CandyIsland, New Jersey, 33123, 1800123460"
  })
  void invalidStateInputThrowsException(String firstName, String lastName, String address, String city, String state, String zip,
      String phone){
    assertThrows(IllegalArgumentException.class, () ->new Person(firstName, lastName, address, city, state, zip, phone));
    }

  /**
   * PairWise Testing
   * Tests if first name is a valid input, then person can be created.
   * First Name must be at least 2 characters can have numbers or letters.
   */
  @ParameterizedTest(name = "#{index} - Person Test with first name: {0}")
  @CsvSource({
      "A1, Fruit,  420 Garden Street, Fruit Island, AL , 12345, 0223475689",
      "Joel, Masters,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      "Mary, Poe,  931 DisneyStreet, Orlando,FL, 33123, 0987654321",
      "MarcusReallyLongName2*, Poe,  931 DisneyStreet, Orlando,FL, 33123, 0987654321"

  })
  void validFirstNameAtLeast2CharactersAlphaNumeric(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertNotNull(new Person(firstName,lastName,address,city,state,zip,phone));
  }

  /**
   * PairWise Testing
   *Tests invalid first names, that will throw an exception.
   */
  @ParameterizedTest(name = "#{index} - Person Test with first name: {0}")
  @CsvSource({
      "A, Fruit,  420 Garden Street, Fruit Island, AL , 12345, 0223475689",
      "2, Poe,  931 DisneyStreet, Orlando,FL, 33123, 0987654321",

  })
  void invalidFirstNamesThrowsException(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    Exception exception=assertThrows(IllegalArgumentException.class, () ->  new Person(firstName,lastName,address,city,state,zip,phone));

    String expectedMessage = "First Name requires at least 2 characters.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  /**
   * PairWise Testing
   *Tests invalid first names, that will throw an exception.
   * This case for null inputs.
   */
  @ParameterizedTest(name = "#{index} - Person Test with first name: {0}")
  @CsvSource({
      " , Masters,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      " , Pepper,  931 DisneyStreet, Orlando,FL, 33123, 0987654321",

  })
  void invalidFirstNamesEmptyThrowsException(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    Exception exception=assertThrows(IllegalArgumentException.class, () ->  new Person(firstName,lastName,address,city,state,zip,phone));

    String expectedMessage = "First name cannot be empty";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  /**
   * PairWise Testing
   * Tests if last name is a valid input, then person can be created.
   * Last Name must be at least 2 characters can have numbers or letters.
   */
  @ParameterizedTest(name = "#{index} - Person Test with first name: {0}")
  @CsvSource({
      "A1, Fruit,  420 Garden Street, Fruit Island, AL , 12345, 0223475689",
      "Joel, Masters,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      "Mary, Poe,  931 DisneyStreet, Orlando,FL, 33123, 0987654321",
      "MarcusReallyLongName2*, Short,  931 DisneyStreet, Orlando,FL, 33123, 0987654321"

  })
  void validLastNameAtLeast2CharactersAlphaNumeric(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertNotNull(new Person(firstName,lastName,address,city,state,zip,phone));
  }

  /**
   *Tests invalid last names, that will throw an exception.
   */
  @ParameterizedTest(name = "#{index} - Person Test with first name: {1}")
  @CsvSource({
     "Mary, 1,  931 DisneyStreet, Orlando,FL, 33123, 0987654321",
      "LongNameMarcus2, H,  931 DisneyStreet, Orlando,FL, 33123, 0987654321"

  })
  void invalidLastNamesThrowsException(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->  new Person(firstName,lastName,address,city,state,zip,phone));

    String expectedMessage = "Last Name requires at least 2 characters.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }
  /**
   *Tests invalid last names, that will throw an exception.
   * This case tests null input.
   */
  @ParameterizedTest(name = "#{index} - Person Test with first name: {1}")
  @CsvSource({
      "A1,  ,  420 Garden Street, Fruit Island, AL , 12345, 0223475689",
      "Joel, ,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
  })
  void invalidLastNameLengthThrowException(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->  new Person(firstName,lastName,address,city,state,zip,phone));

    String expectedMessage = "Last name cannot be empty";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  /**
   * PairWise Testing
   * Tests if  City is a valid input, then person can be created.
   * City are Valid if input is 2-40 alphanumeric characters, spaces allowed.
   */
  @ParameterizedTest(name = "#{index} - Person Test with address: {2}")
  @CsvSource({
      "A1, Fruit,  2010 Garden Street, Fruit Island, AL , 12345, 0223475689",
      "Joel, Masters,  9000 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      "Mary, Poe,  931 DisneyStreet, Orlando,FL, 33123, 0987654321",
      "MarcusReallyLongName2*, Short,  931 DisneyStreet, Orlando,FL, 33123, 0987654321"

  })
  void validCityBetween2And40Characters(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertNotNull(new Person(firstName,lastName,address,city,state,zip,phone));
  }

  /**
   * PairWise Testing
   * Tests that exception is thrown if city isn't the right length.
   */
  @ParameterizedTest(name = "#{index} - Person Test with city: {3}")
  @CsvSource({
      "A1, Fruit, 99850 Candy Lane, Nulla facilisi. Ut eros enim ullamcorper id lectus ullamcorper bibendum aliquet nulla. Vivamus ut lorem ut mi sollicitudin vestibulum, AL , 12345, 0223475689",
      "Joel, Masters,  9000 Yeehaw Avenue, 3,FL, 33901, 0123456789",
      "Mary, Poe, Disney Land, O,FL, 33123, 0987654321",

  })
  void isInvalidCityTrowsException(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->  new Person(firstName,lastName,address,city,state,zip,phone));

    String expectedMessage = "City requires at least 2 characters.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

  }

  /**
   * PairWise Testing
   * Tests if  address is a valid input, then person can be created.
   * Address are Valid if field is 2-40 alphanumeric characters, spaces allowed.
   */
  @ParameterizedTest(name = "#{index} - Person Test with address: {2}")
  @CsvSource({
      "A1, Fruit,  2010 Garden Street, Fruit Island, AL , 12345, 0223475689",
      "Joel, Masters,  9000 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      "Mary, Poe,  931 DisneyStreet, Orlando,FL, 33123, 0987654321",
      "MarcusReallyLongName2*, Short,  931 DisneyStreet, Orlando,FL, 33123, 0987654321"

  })
  void validAddressBetween2And40Characters(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertNotNull(new Person(firstName,lastName,address,city,state,zip,phone));
  }

  /**
   * PairWise Testing
   * Tests that exception is thrown if Address isn't the right length
   */
  @ParameterizedTest(name = "#{index} - Person Test with address: {2}")
  @CsvSource({
      "A1, Fruit, Lorem ipsum dolor sit amet consectetur adipiscing elit. Maecenas ornare enim quis lobortis, Fruit Island, AL , 12345, 0223475689",
      "Joel, Masters, 0, LeeHigh Anchors,FL, 33901, 0123456789",
      "Mary, Poe, A, Orlando,FL, 33123, 0987654321",

  })
  void isInvalidAddressTrowsException(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->  new Person(firstName,lastName,address,city,state,zip,phone));

    String expectedMessage = "Address requires at least 2 characters.";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

  }


  /**
   * Tests first name is returned.
   */
  @Test
  public void getFirstName() {
    assertEquals("John",test_Person.getFirstName());
  }

  /**
   * Tests last name is returned.
   */
  @Test
  public void getLastName() {
    assertEquals("Doe",test_Person.getLastName());
  }

  /**
   * Tests Address is returned.
   */
  @Test
  public void getAddress() {
    assertEquals("123 Fake Street",test_Person.getAddress());
  }

  /**
   * Tests City is returned.
   */
  @Test
  public void getCity() {
    assertEquals("Fort Myers",test_Person.getCity());
  }

  /**
   * Tests State is returned.
   */
  @Test
  public void getState() {
    assertEquals("FL",test_Person.getState());
  }

  /**
   * Tests zip is returned.
   */
  @Test
  public void getZip() {
    assertEquals("33901",test_Person.getZip());
  }

  /**
   * Tests phone is returned
   */
  @Test
  public void getPhone() {
    assertEquals("0123456789",test_Person.getPhone());
  }

  /**
   * This test case tests the constainString method.
   * Each field of person is tested to ensure that
   * search bar will be able to find the person object.
   */
  @Test
  public void containsString() {
    assertTrue(test_Person.containsString("John"));
    assertFalse(test_Person.containsString("Michael"));
    assertTrue(test_Person.containsString("Doe"));
    assertTrue(test_Person.containsString("123 Fake Street"));
    assertTrue(test_Person.containsString("Fort Myers"));
    assertTrue(test_Person.containsString("FL"));
    assertTrue(test_Person.containsString("33901"));
    assertTrue(test_Person.containsString("0123456789"));
  }

  /**
   * This test cases tests the getField method
   * to ensure the correct data is returned.
   */
  @Test
  public void getField() {
    assertEquals(test_Person.getField(0),"Doe");
    assertEquals(test_Person.getField(1),"John");
    assertEquals(test_Person.getField(2),"123 Fake Street");
    assertEquals(test_Person.getField(3),"Fort Myers");
    assertEquals(test_Person.getField(4),"FL");
    assertEquals(test_Person.getField(5),"33901");
    assertEquals(test_Person.getField(6),"0123456789");
    Exception exception = assertThrows(Exception.class, () -> test_Person.getField(7));
    assertEquals("Field number out of bounds",exception.getMessage());
  }

  /**
   * This method tests the output of toString method.
   */
  @Test
  public void toStringTest() {
     assertEquals("Doe, John",test_Person.toString());
  }

}