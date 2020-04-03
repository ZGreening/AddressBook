import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Click on Person, press Ctrl+Shift+T
//Click on "create new test case"
public class Person {

    public static final String[] fields = { "Last Name", "First Name", "Address", "City", "State", "ZIP", "Phone" };

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phone;

    boolean checkRegex(String name,String regex){
        //String patternString;

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(name);
        return matcher.matches();

    }

    public Person(String firstName, String lastName, String address, String city, String state, String zip,
            String phone) {
        if (firstName == null || firstName.isEmpty())
            throw new IllegalArgumentException("First name cannot be empty");
        if (lastName == null || lastName.isEmpty())
            throw new IllegalArgumentException("Last name cannot be empty");
        //Check if first name passes regex
        if (checkRegex(firstName,"\\S{2,}")== false){
            //Name didn't pass regex, send error
            throw new IllegalArgumentException("First Name have 2 characters.");
        }
        if (checkRegex(lastName,"\\S{2,}")== false){
            //Name didn't pass regex, send error
            throw new IllegalArgumentException("Last Name have 2 characters.");
        }


        if(checkRegex(state,"[A-Z]{2,2}") == false){
            throw new IllegalArgumentException("State Must be 2 Characters");
        }
        if(checkRegex(zip,"\\d{5}") == false){
            throw new IllegalArgumentException("ZipCode Must be 5 numbers");
        }
        //9 or 10 numbers?
        if(checkRegex(phone,"\\d{10}") == false){
            throw new IllegalArgumentException("Phone Number Must be 10 numbers");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    /**
     * Returns this Person's ZIP code.
     *
     * @return ZIP code of this Person
     */
    public String getZip() {
        return zip;
    }

    /**
     * Returns this Person's telephone number.
     *
     * @return Telephone number of this Person.
     */
    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return lastName + ", " + firstName;
    }

    public boolean containsString(String findMe) {
        Pattern p = Pattern.compile(Pattern.quote(findMe), Pattern.CASE_INSENSITIVE);
        return p.matcher(firstName).find() || p.matcher(lastName).find() || p.matcher(address).find()
                || p.matcher(city).find() || p.matcher(state).find() || p.matcher(zip).find()
                || p.matcher(phone).find();
    }

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