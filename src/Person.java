import java.util.regex.Pattern;

/**
 * The Person class which stores all contact information about a single person.
 * This class is the main content of the program. Multiple person objects are
 * stored in an address book
 */
public class Person {

    // List of field names for address book
    public static final String[] fields = { "Last Name", "First Name", "Address", "City", "State", "ZIP", "Phone" };

    // Person fields
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phone;

    /**
     * A helper function to check regex.
     * 
     * @param string The string to check
     * @param regex  The regex to compare the string to
     * @return Returns true if the regex is satisfied, false otherwise.
     */
    private boolean checkRegex(String string, String regex) {
        return Pattern.compile(regex).matcher(string).matches();
    }

    /**
     * A constructor to instantiate a new person.
     * 
     * @param firstName The person's first name
     * @param lastName  The person's last name
     * @param address   The person's address
     * @param city      The person's home city
     * @param state     The person's home state
     * @param zip       The person's zip
     * @param phone     The person's phone number
     * @throws IllegalArgumentException Thrown if parameters for person creation
     *                                  were incorrect. Person name must be at least
     *                                  2 chars long. State must be two capital
     *                                  letter. Zip must be 5 numbers. Phone must be
     *                                  10 numbers.
     */
    public Person(String firstName, String lastName, String address, String city, String state, String zip,
            String phone) {
        // Check if the person's first name, last name, state, zip and phone number meet
        // requirements
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (!checkRegex(firstName, "\\S{2,}")) {
            throw new IllegalArgumentException("First Name requires at least 2 characters.");
        }
        if (!checkRegex(lastName, "\\S{2,}")) {
            throw new IllegalArgumentException("Last Name requires at least 2 characters.");
        }
        if (!checkRegex(state, "[A-Z]{2,2}")) {
            throw new IllegalArgumentException("State Must be 2 Characters");
        }
        if (!checkRegex(zip, "\\d{5}")) {
            throw new IllegalArgumentException("ZipCode Must be 5 numbers");
        }
        if (!checkRegex(phone, "\\d{10}")) {
            throw new IllegalArgumentException("Phone Number Must be 10 numbers");
        }

        // Assignment variables to class attributes
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
    }

    /**
     * Getter for person's first name attribute.
     * 
     * @return The person's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for person's last name attribute.
     * 
     * @return The person's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter for person's address attribute.
     * 
     * @return The person's address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter for person's city attribute.
     * 
     * @return The person's city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Getter for person's state attribute.
     * 
     * @return The person's state.
     */
    public String getState() {
        return state;
    }

    /**
     * Getter for person's zip attribute.
     * 
     * @return The person's zip.
     */
    public String getZip() {
        return zip;
    }

    /**
     * Getter for person's phone attribute.
     * 
     * @return The person's phone.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * An overridden toString() function to print the person's name in "last, first"
     * format.
     * 
     * @return The person's name in "last, first" format.
     */
    @Override
    public String toString() {
        return lastName + ", " + firstName;
    }

    /**
     * A function to find if a string exists in any of the person attributes.
     * 
     * @param findMe The string to find
     * @return True if the string exists in this person object, false otherwise
     */
    public boolean containsString(String findMe) {
        Pattern p = Pattern.compile(Pattern.quote(findMe), Pattern.CASE_INSENSITIVE);
        return p.matcher(firstName).find() || p.matcher(lastName).find() || p.matcher(address).find()
                || p.matcher(city).find() || p.matcher(state).find() || p.matcher(zip).find()
                || p.matcher(phone).find();
    }

    /**
     * A function to get an attribute via integer index. Used for tables.
     * 
     * @param field The integer attribute to find.
     * @return The contents of the corresponding attribute.
     * @throws IllegalArgumentException Thrown if field is out of bounds.
     */
    public String getField(int field) {
        switch (field) {
            case 0:
                return lastName;
            case 1:
                return firstName;
            case 2:
                return address;
            case 3:
                return city;
            case 4:
                return state;
            case 5:
                return zip;
            case 6:
                return phone;
            default:
                throw new IllegalArgumentException("Field number out of bounds");
        }
    }
}