import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;

public class FileSystem {

    /**
     * A function to read the data out of an existing AddressBook saved on disk.
     * Note: this function deletes the data in the current AddressBook.
     * 
     * @param addressBook The AddressBook to read the data into.
     * @param file        The file to read the data from.
     * @throws SQLException          Thrown if the data in the file is not in the
     *                               expected SQL format
     * @throws FileNotFoundException Thrown if the file does not exist or is
     *                               unreadable
     */
    public void readFile(AddressBook addressBook, File file) throws SQLException, FileNotFoundException {
        // Throw exception if the file is not usable or doesn't exist
        if (!file.exists() || !file.canRead()) {
            throw new FileNotFoundException();
        }

        // Connect SQL database and execute query to retrieve data
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        Statement statement = connection.createStatement();
        ResultSet rs = statement
                .executeQuery("SELECT lastName, firstName, address, city, state, zip, phone FROM persons");

        // Clear the current AddressBook contents
        addressBook.clear();

        // Iterate through all the records, adding them to the AddressBook
        while (rs.next()) {
            Person p = new Person(rs.getString("firstName"), rs.getString("lastName"), rs.getString("address"),
                    rs.getString("city"), rs.getString("state"), rs.getString("zip"), rs.getString("phone"));
            addressBook.add(p);
        }

        // Close DB connection
        statement.close();
        rs.close();
        connection.close();
    }

    /**
     * A function to save an address book to disk. The function erases any existing
     * address book in the file.
     * 
     * @param addressBook The address book to save to disk.
     * @param file        The file the address book will be saved to.
     * @throws SQLException Thrown if database issue occurs (Should not be thrown in
     *                      this method)
     */
    public void saveFile(AddressBook addressBook, File file) throws SQLException {
        // Initialize database connection
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        Statement statement = connection.createStatement();

        // Drop any data in the file
        statement.execute("DROP TABLE IF EXISTS persons");

        // Prepare table for storing data
        statement.execute(
                "CREATE TABLE persons (firstName TEXT, lastName TEXT, address TEXT, city TEXT, state TEXT, zip TEXT, phone TEXT)");

        // Prepare a statement for database insertion
        PreparedStatement insert = connection.prepareStatement(
                "INSERT INTO persons (lastName, firstName, address, city, state, zip, phone) VALUES (?, ?, ?, ?, ?, ?, ?)");

        // Iterate through all people and all their data fields to insert them into the
        // file database on disk
        for (Person p : addressBook.getPersons()) {
            for (int i = 0; i < Person.fields.length; i++) {
                insert.setString(i + 1, p.getField(i));
            }
            insert.executeUpdate();
        }

        // Close database resources
        statement.close();
        connection.close();
    }
}