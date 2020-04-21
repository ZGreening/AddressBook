import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A person dialog class for showing a persons data. Can be used to create a new
 * person or edit an existing person.
 */
public class PersonDialog extends JDialog {
    // Possible states of PersonDialog result
    public enum Result {
        OK, CANCEL
    }

    private static final long serialVersionUID = 1L; // For serialization
    private Result result = Result.CANCEL; // Default result to cancel

    // Fields of a person used for instantiation
    private JTextField firstName;
    private JTextField lastName;
    private JTextField address;
    private JTextField city;
    private JTextField state;
    private JTextField zip;
    private JTextField phone;

    /**
     * Constructor for the person dialog.
     * 
     * @param parent The parent frame for creating dialog ownership.
     */
    public PersonDialog(Frame parent) {
        // Call JDialog constructor
        super(parent);

        // Reusable variables
        JLabel l;
        AtomicReference<JPanel> p = new AtomicReference<>(new JPanel(new SpringLayout()));

        // Create first name text box
        l = new JLabel("First name:", JLabel.TRAILING);
        p.get().add(l);
        firstName = new JTextField(20);
        firstName.setName("firstName");
        l.setLabelFor(firstName);
        p.get().add(firstName);

        // Create last name text box
        l = new JLabel("Last name:", JLabel.TRAILING);
        p.get().add(l);
        lastName = new JTextField(20);
        lastName.setName("lastName");
        l.setLabelFor(lastName);
        p.get().add(lastName);

        // Create address text box
        l = new JLabel("Address:", JLabel.TRAILING);
        p.get().add(l);
        address = new JTextField(20);
        address.setName("address");
        l.setLabelFor(address);
        p.get().add(address);

        // Create city text box
        l = new JLabel("City:", JLabel.TRAILING);
        p.get().add(l);
        city = new JTextField(20);
        city.setName("city");
        l.setLabelFor(city);
        p.get().add(city);

        // Create state text box
        l = new JLabel("State:", JLabel.TRAILING);
        p.get().add(l);
        state = new JTextField(20);
        state.setName("state");
        l.setLabelFor(state);
        p.get().add(state);

        // Create zip text box
        l = new JLabel("ZIP code:", JLabel.TRAILING);
        p.get().add(l);
        zip = new JTextField(20);
        zip.setName("zip");
        l.setLabelFor(zip);
        p.get().add(zip);

        // Create phone text box
        l = new JLabel("Telephone:", JLabel.TRAILING);
        p.get().add(l);
        phone = new JTextField(20);
        phone.setName("phone");
        l.setLabelFor(phone);
        p.get().add(phone);

        // Organize grid
        SpringUtilities.makeCompactGrid(p.get(), 7, 2, 6, 6, 6, 6);

        // Create Ok button at bottom
        JPanel buttons = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.setMnemonic('O');
        okButton.addActionListener(e -> {
            result = Result.OK;
            setVisible(false);
        });
        buttons.add(okButton);

        // Create Cancel button at bottom
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic('C');
        cancelButton.addActionListener(e -> {
            result = Result.CANCEL;
            setVisible(false);
        });
        buttons.add(cancelButton);

        // Set window properties
        getContentPane().add(p.get(), BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.PAGE_END);
        pack();
        setTitle("Person Information");
        setModalityType(ModalityType.DOCUMENT_MODAL);
        setLocation((parent.getWidth() - getWidth()) / 2, (parent.getHeight() - getHeight()) / 2);
    }

    /**
     * An overloaded constructor. This constructor accepts a person and initializes
     * the vales of this dialog with the person object.
     * 
     * @param parent The parent frame for creating dialog ownership.
     * @param person The person to load into the dialog
     */
    public PersonDialog(Frame parent, Person person) {
        // Call basic constructor
        this(parent);

        // Load person
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        address.setText(person.getAddress());
        city.setText(person.getCity());
        state.setText(person.getState());
        zip.setText(person.getZip());
        phone.setText(person.getPhone());
    }

    /**
     * Get the result of the dialog. Will be ok if 'ok' button was clicked. Will be
     * cancel if 'cancel' button was clicked or the dialog was closed out of.
     * 
     * @return The result
     */
    public Result getResult() {
        return result;
    }

    /**
     * A function to get the person data out of the dialog
     * 
     * @return The person object consisting of data from the dialog. Null if the
     *         person could not be created.
     */
    public Person getPerson() {
        try {
            return new Person(firstName.getText(), lastName.getText(), address.getText(), city.getText(),
                    state.getText(), zip.getText(), phone.getText());
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}