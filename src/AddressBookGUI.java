import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.regex.Pattern;

/**
 * The main program gui. This class sets up the swing application layout and
 * adds the lambda functionality to them.
 */
public class AddressBookGUI extends JFrame {

    /**
     * The application main function.
     */
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        SwingUtilities.invokeLater(() -> {
            AddressBookGUI gui = new AddressBookGUI();
            gui.setVisible(true);
        });
    }

    private static final long serialVersionUID = 1L; // For serialization
    private AddressBook addressBook = new AddressBook();
    private transient AddressBookController controller = new AddressBookController(addressBook);
    private JTable nameList = new JTable(addressBook);
    private final transient TableRowSorter<AddressBook> tableRowSorter = new TableRowSorter<>(addressBook);
    private final JButton addButton = new JButton("Add...");
    private final JButton editButton = new JButton("Edit...");
    private final JButton deleteButton = new JButton("Delete");
    private final JMenuItem newItem = new JMenuItem("New", 'N');
    private final JMenuItem openItem = new JMenuItem("Open", 'O');
    private final JMenuItem saveItem = new JMenuItem("Save", 'S');
    private final JMenuItem saveAsItem = new JMenuItem("Save As...", 'A');
    private final JMenuItem printItem = new JMenuItem("Print", 'P');
    private final JMenuItem quitItem = new JMenuItem("Exit", 'X');
    private final JTextField searchTextField = new JTextField("");

    // The file to save
    private File currentFile = null;

    /**
     * Used for tests NOTE: using package protection not public
     * 
     * @param addressBook For passing in a mock/spy
     * @param controller  For passing in a mock/spy
     */
    AddressBookGUI(AddressBook addressBook, AddressBookController controller) {
        this();
        this.addressBook = addressBook;
        this.controller = controller;
    }

    /**
     * Used for tests NOTE: using package protection not public
     * 
     * @param table For passing in a mock/spy
     */
    AddressBookGUI(JTable table) {
        this();
        nameList = table;
    }

    /**
     * The main constructor for creating the GUI. This function initializes all the
     * gui components style and lambda functionality.
     */
    public AddressBookGUI() {
        // Give names for GUI components
        nameList.setName("table");
        addButton.setName("add");
        editButton.setName("edit");
        deleteButton.setName("delete");
        newItem.setName("new");
        openItem.setName("open");
        saveItem.setName("save");
        saveAsItem.setName("saveAs");
        printItem.setName("print");
        quitItem.setName("quit");
        searchTextField.setName("search");

        // Arrange the window controls
        nameList.setRowSorter(tableRowSorter);
        nameList.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(nameList);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create file menu button in the bar
        JMenu file = new JMenu("File");
        file.setMnemonic('F');
        file.setName("file");

        // Give new item functionality and add it to file menu bar
        newItem.addActionListener(e -> {
            if (saveItem.isEnabled() && JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to create a new address book? Any unsaved progress will be lost.",
                    "New Address Book", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                return;
            }
            controller.clear();
            saveItem.setEnabled(false);
        });
        file.add(newItem);

        // Give open item functionality and add it to file menu bar
        openItem.addActionListener(e -> {
            if (saveItem.isEnabled() && JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to open a different address book? Any unsaved progress will be lost.",
                    "Open Address Book", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                return;
            }
            final JFileChooser jfc = new JFileChooser();
            if (JFileChooser.APPROVE_OPTION != jfc.showOpenDialog(this)) {
                return;
            }
            try {
                controller.open(jfc.getSelectedFile());
                currentFile = jfc.getSelectedFile();
                saveItem.setEnabled(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage(), "Open",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        file.add(openItem);

        // Give save item functionality and add it to file menu bar
        saveItem.setEnabled(false);
        saveItem.addActionListener(e -> {
            if (currentFile == null) {
                saveAsItem.doClick();
                return;
            }
            try {
                controller.save(currentFile);
                saveItem.setEnabled(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving the file: " + ex.getMessage(), "Save",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Set saveAsItem with the same state as saveItem
        saveItem.addChangeListener(e -> saveAsItem.setEnabled(saveItem.isEnabled()));
        file.add(saveItem);

        // Give save as item functionality and add it to file menu bar
        saveAsItem.setEnabled(false);
        saveAsItem.addActionListener(e -> {
            final JFileChooser jfc = new JFileChooser();
            if (JFileChooser.APPROVE_OPTION != jfc.showSaveDialog(this)) {
                return;
            }
            File tempFile = currentFile;
            currentFile = jfc.getSelectedFile();
            if (currentFile.exists() && JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to overwrite this file?", "Are you sure?", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE)) {
                currentFile = tempFile; // Restore old state of current file to prevent accidental overwrites later on
                return;
            }
            saveItem.doClick();
        });
        file.add(saveAsItem);

        // Separate next item
        file.add(new JSeparator());

        // Give print item functionality and add it to file menu bar
        printItem.addActionListener(e -> {
            try {
                nameList.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing failed: " + ex.getMessage(), "Print",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        file.add(printItem);

        // Separate next item
        file.add(new JSeparator());

        // Give quit item functionality and add it to file menu bar
        quitItem.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        file.add(quitItem);

        // Add file menu item, and search text box label to menu bar
        menuBar.add(file);
        menuBar.add(new JSeparator());
        menuBar.add(new JLabel("Search: "));

        // Add search text box functionality and
        searchTextField.setMaximumSize(new Dimension(15000, 50));
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            // Listen to the Document so the list filters immediately
            // (EventListener on JTextField requires "Enter" before firing)
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                tableRowSorter.setRowFilter(RowFilter.regexFilter("(?iu)" + Pattern.quote(searchTextField.getText())));
            }
        });
        menuBar.add(searchTextField);

        // Create panel with add, edit, and delete button functionality
        JPanel addEditDelPanel = new JPanel();

        // Create add button
        addButton.setMnemonic('A');
        addButton.addActionListener(e -> {
            PersonDialog dialog = new PersonDialog(this);
            dialog.setVisible(true);
            if (dialog.getResult() != PersonDialog.Result.OK)
                return;
            controller.add(dialog.getPerson());
            saveItem.setEnabled(true);
        });
        addEditDelPanel.add(addButton);

        // Create edit button
        editButton.setMnemonic('E');
        editButton.addActionListener(e -> {
            int selectedRow = nameList.getSelectedRow();
            if (selectedRow == -1) {
                return;
            }
            int index = nameList.convertRowIndexToModel(selectedRow);
            Person oldPerson = controller.get(index);
            PersonDialog dialog = new PersonDialog(this, oldPerson);
            dialog.setVisible(true);
            if (dialog.getResult() != PersonDialog.Result.OK) {
                return;
            }
            controller.set(index, dialog.getPerson());
            saveItem.setEnabled(true);
        });
        addEditDelPanel.add(editButton);

        // Create delete button
        deleteButton.setMnemonic('D');
        deleteButton.addActionListener(e -> {
            int selectedRow = nameList.getSelectedRow();
            if (selectedRow == -1) {
                return;
            }
            controller.remove(nameList.convertRowIndexToModel(selectedRow));
            saveItem.setEnabled(true);
        });
        addEditDelPanel.add(deleteButton);

        // Combine add, edit, and delete panel with tip label and display at bottom
        JPanel panelPanel = new JPanel(new BorderLayout());
        panelPanel.add(addEditDelPanel, BorderLayout.LINE_START);
        panelPanel.add(new JLabel("TIP: You can sort by clicking the column headers"), BorderLayout.LINE_END);
        getContentPane().add(panelPanel, BorderLayout.PAGE_END);

        // Set main window parameters
        setSize(800, 600);
        setLocationByPlatform(true);
        setTitle("Address Book");
        setJMenuBar(menuBar);

        // Listen for window closing events to interrupt and check for unsaved data if
        // necessary
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                JFrame frame = (JFrame) e.getSource();
                // We use saveItem.isEnabled to indicate whether there are unsaved changes or
                // not
                if (!saveItem.isEnabled() || JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to exit? Your changes will be lost.", "Exit", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                } else {
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }
}