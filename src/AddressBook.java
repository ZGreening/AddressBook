import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * The address book class contains a list of the persons that make up the
 * address book. This class extends AbstractTableModel which allows for easier
 * maintenance of table data.
 */
public class AddressBook extends AbstractTableModel {

    private static final long serialVersionUID = 1L; //For serialization
    private transient ArrayList<Person> persons = new ArrayList<>();

    /**
     * A simple getter that returns an array of the persons in the address book.
     * 
     * @return an array of persons in the address book.
     */
    public Person[] getPersons() {
        return persons.toArray(new Person[persons.size()]);
    }

    /**
     * A method to add a person to the persons array and update the table.
     */
    public void add(Person p) {
        int newIndex = persons.size();
        persons.add(p);
        fireTableRowsInserted(newIndex, newIndex);
    }

    /**
     * A method to remove a person from the persons array and update the table.
     * 
     * @param index The table index of the person to remove.
     */
    public void remove(int index) {
        persons.remove(index);
        fireTableRowsDeleted(index, index);
    }

    /**
     * Sets the person at the given index to the Person specified.
     *
     * @param index  The table index of the person to update.
     * @param person Person to replace index location with.
     */
    public void set(int index, Person person) {
        persons.set(index, person);
        fireTableRowsUpdated(index, index);
    }

    /**
     * Get the person at the given index.
     * 
     * @param index The table index of the person to get
     * @return A person object of the person in that location.
     */
    public Person get(int index) {
        return persons.get(index);
    }

    /**
     * Clears this address book persons list and updates the tables.
     */
    public void clear() {
        if (persons == null || persons.isEmpty()) {
            return;
        }

        // Clear persons first
        int lastRow = persons.size() - 1;
        persons.clear();

        // Delete table rows
        fireTableRowsDeleted(0, lastRow);
    }

    /**
     * An overridden function to get the number of rows in the table.
     * 
     * @return The number of rows in the table.
     */
    @Override
    public int getRowCount() {
        return persons.size();
    }

    /**
     * An overridden function to get the number of columns in the table.
     * 
     * @return The number of columns in the table.
     */
    @Override
    public int getColumnCount() {
        return Person.fields.length;
    }

    /**
     * An overridden function to get the value in a specific table cell.
     * 
     * @return The contents of the specified table cell.
     */
    @Override
    public Object getValueAt(int row, int column) {
        return persons.get(row).getField(column);
    }

    /**
     * The column name of the specified column.
     */
    @Override
    public String getColumnName(int column) {
        return Person.fields[column];
    }
}