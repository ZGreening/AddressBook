import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class PersonTest
{
  Person test_Person = new Person("John","Doe","123 Fake Street","Fort Myers","FL","33901","0123456789");

  @Test
  void personPhoneNumberMustbe10Numbers(){
    assertNotNull(new Person("John","Doe","123 Fake Street","Fort Myers","FL","33901","0123456789"));
  }

  @Test
  void personPhoneNumberMustbe10NumbersNegativeCase(){
    assertThrows(IllegalArgumentException.class, () -> new Person("John","Doe","123 Fake Street","Fort Myers","FL","33901","00123456789" ));
   }

  @Test
  void personObjectZipCodeMustBe5NumbersToPass(){
  assertNotNull(new Person("John","Doe","123 Fake Street","Fort Myers","FL","33901","0123456789"));
  }

  @Test
  void personObjectZipCodeMustBe5NumbersToPassNegativeCase(){
    assertThrows(IllegalArgumentException.class, () -> new Person("John","Doe","123 Fake Street","Fort Myers","FL","33901K","0123456789" ));
    //assertNotNull(new Person("John","Doe","123 Fake Street","Fort Myers","FLR","33901","0123456789"));
  }

  @Test
  void statePassesRegexIf2AlphaCharacterAreEnteredNegativeTestCaseShouldThrowError(){
    assertThrows(IllegalArgumentException.class, () -> new Person("John","Doe","123 Fake Street","Fort Myers","Florida","33901","0123456789" ));
    //assertNotNull(new Person("John","Doe","123 Fake Street","Fort Myers","FLR","33901","0123456789"));
  }

  @Test
  void statePassesRegexIf2AlphaCharacterAreEntered(){
    //System.out.println("Run?");
    //new Person("John","Doe","123 Fake Street","Fort Myers","fl2","33901","0123456789");
  assertNotNull(new Person("John","Doe","123 Fake Street","Fort Myers","FL","33901","0123456789"));
  }

  @Test
  void statePassesRegexIf2AlphaCharacterAreEnteredThrowsError(){
    assertThrows(IllegalArgumentException.class, () ->  new Person("Zeke","Doe","123 Fake Street","Fort Myers","FLR","33901","0123456789")
    );
  }

  @ParameterizedTest(name = "#{index} - Person Test with args: {0}")
  @CsvSource({
      ", ,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      "J, P,  931 DisneyStreet, Orlando,FL, 33123, 987654321",
      "2, P12,  931 DisneyStreet, Orlando,FL, 33123, 987654321",

  })
  void FirstNamePassesRegexOfAtleast2CharactersSpecialCharacterAllowedThrowsError(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertThrows(IllegalArgumentException.class, () ->  new Person(firstName,lastName,address,city,state,zip,phone));

  }

  @ParameterizedTest(name = "#{index} - Person Test with arg: {0}")
  @CsvSource({
      "Joel, Masters,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      "Mary, Poe,  931 DisneyStreet, Orlando,FL, 33123, 0987654321",
      "MarcusReallyLongName2*, Poe,  931 DisneyStreet, Orlando,FL, 33123, 0987654321"

  })
  void FirstNamePassesRegexOfAtleast2CharactersSpecialCharacterAllowed(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertNotNull(new Person(firstName,lastName,address,city,state,zip,phone));
  }

  @ParameterizedTest(name = "#{index} - Person Test with args: {1}")
  @CsvSource({
      "John, ,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      "Mary, P,  931 DisneyStreet, Orlando,FL, 33123, 987654321",
      "Lambda, 1,  931 DisneyStreet, Orlando,FL, 33123, 987654321",

  })
  void LastNamePassesRegexOfAtleast2CharactersSpecialCharacterAllowedThrowsError(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertThrows(IllegalArgumentException.class, () ->  new Person(firstName,lastName,address,city,state,zip,phone));
  //assertNotNull(new Person(firstName,lastName,address,city,state,zip,phone));
  }

  @ParameterizedTest(name = "#{index} - Person Test with arg: {0}")
  @CsvSource({
      "Joel, Masters,  420 Yeehaw Avenue, LeeHigh Anchors,FL, 33901, 0123456789",
      "Mary, Po,  931 DisneyStreet, Orlando,FL, 33123, 0987654321",

  })
  void LastNamePassesRegexOfAtleast2CharactersSpecialCharacterAllowed(String firstName, String lastName, String address, String city, String state, String zip,
      String phone) {
    assertNotNull(new Person(firstName,lastName,address,city,state,zip,phone));
  }

  @Test
  public void getFirstName() {
    assertEquals("John",test_Person.getFirstName());
  }

  @Test
  public void getLastName() {
    assertEquals("Doe",test_Person.getLastName());
  }

  @Test
  public void getAddress() {
    assertEquals("123 Fake Street",test_Person.getAddress());
  }

  @Test
  public void getCity() {
    assertEquals("Fort Myers",test_Person.getCity());
  }

  @Test
  public void getState() {
    assertEquals("FL",test_Person.getState());
  }

  @Test
  public void getZip() {
    assertEquals("33901",test_Person.getZip());
  }

  @Test
  public void getPhone() {
    assertEquals("0123456789",test_Person.getPhone());
  }

  @Test
  public void badName(){
    assertEquals("Doe",test_Person.getField(0));
  }
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
  @Test
  public void toStringTest() {
     assertEquals("Doe, John",test_Person.toString());
  }

}