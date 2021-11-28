import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class Books extends JFrame {
    private JDesktopPane desktopPane;
    private ResultSetTableModel tableModel;
    private JTable resultTable;
    Container border;
    Container content;

    Action newAuthor, newTitle, newPublisher, saveAction,
            deleteAction, searchAuthor, searchBook, exitAction;

    private BooksDataAccess dataAccess;

    public Books() throws SQLException, ClassNotFoundException {
        super("Books Database");
        String driver = "com.mysql.cj.jdbc.Driver";

        // URL to connect to books database
        String url = "jdbc:mysql://localhost:3306/books";

        // query to select entire authors table
        String query = "SELECT * FROM authors";

        try {
            dataAccess = new CloudDataAccess();
        }

        // detect problems with database connection
        catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        JToolBar toolBar = new JToolBar();
        tableModel = new ResultSetTableModel(driver, url, query);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        //setup Actions
        newAuthor = new NewAuthor();
        newPublisher = new NewPublisher();
        newTitle = new NewTitle();
        saveAction = new SaveAction();
        saveAction.setEnabled(false);
        deleteAction = new DeleteAction();
        deleteAction.setEnabled(false);
        searchAuthor = new SearchAuthor();
        searchBook = new SearchBook();
        exitAction = new ExitAction();
        //....
        toolBar.add(newAuthor);
        toolBar.add(newPublisher);
        toolBar.add(newTitle);
        toolBar.add(saveAction);
        toolBar.add(deleteAction);
        toolBar.add(new JToolBar.Separator());
        toolBar.add(searchAuthor);
        toolBar.add(searchBook);

        fileMenu.add(newAuthor);
        fileMenu.add(newPublisher);
        fileMenu.add(newTitle);
        fileMenu.add(saveAction);
        fileMenu.add(deleteAction);
        fileMenu.addSeparator();
        fileMenu.add(searchAuthor);
        fileMenu.add(searchBook);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        desktopPane = new JDesktopPane();
        content = new Container();
        content.setLayout(new GridLayout(2,1));
        border = getContentPane();
        resultTable = new JTable(tableModel);

        content.add(desktopPane);
        content.add(new JScrollPane(resultTable));


        border.add(toolBar, BorderLayout.NORTH);
        border.add(content);

        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent event) {
                        shutDown();
                    }
                }
        );

        // set window size and display window
        Toolkit toolkit = getToolkit();
        Dimension dimension = toolkit.getScreenSize();

        // center window on screen
        setBounds(100, 100, dimension.width - 200,
                dimension.height - 200);

        setVisible(true);
    }

    private void shutDown() {
        //database.close();   // close database connection
        System.exit(0);   // terminate program
    }

    private DataEntryFrame createDataEntryFrame(int num) {
        DataEntryFrame frame = new DataEntryFrame(num);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.addInternalFrameListener(
                new InternalFrameAdapter() {
                    // internal frame becomes active frame on desktop
                    public void internalFrameActivated(
                            InternalFrameEvent event) {
                        saveAction.setEnabled(true);
                        deleteAction.setEnabled(true);

                    }

                    // internal frame becomes inactive frame on desktop
                    public void internalFrameDeactivated(
                            InternalFrameEvent event) {
                        saveAction.setEnabled(false);
                        deleteAction.setEnabled(false);

                    }
                }  // end InternalFrameAdapter anonymous inner class
        ); // end call to addInternalFrameListener

        return frame;
    }  // end method createAddressBookEntryFrame

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Books();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class NewAuthor extends AbstractAction {

        public NewAuthor() {
            putValue(NAME, "NewAuthor");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/New24.png")));
            putValue(SHORT_DESCRIPTION, "NewA");
            putValue(LONG_DESCRIPTION,
                    "Add a new Author entry");
            putValue(MNEMONIC_KEY, new Integer('N'));
        }

        // display window in which user can input entry
        public void actionPerformed(ActionEvent e) {
            DataEntryFrame entryFrame =
                    createDataEntryFrame(1);

            // set new AddressBookEntry in window
            entryFrame.setDataEntry(
                    new DataEntry());

            // display window
            desktopPane.add(entryFrame);
            entryFrame.setVisible(true);
        }
    }

    private class NewTitle extends AbstractAction {

        public NewTitle() {
            putValue(NAME, "NewTitle");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/New24.png")));
            putValue(SHORT_DESCRIPTION, "NewT");
            putValue(LONG_DESCRIPTION,
                    "Add a new Title entry");
            putValue(MNEMONIC_KEY, new Integer('T'));
        }

        // display window in which user can input entry
        public void actionPerformed(ActionEvent e) {
            DataEntryFrame entryFrame =
                    createDataEntryFrame(2);

            // set new AddressBookEntry in window
            entryFrame.setDataEntry(
                    new DataEntry());

            // display window
            desktopPane.add(entryFrame);
            entryFrame.setVisible(true);
        }
    }

    private class NewPublisher extends AbstractAction {

        public NewPublisher() {
            putValue(NAME, "NewPublisher");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/New24.png")));
            putValue(SHORT_DESCRIPTION, "NewP");
            putValue(LONG_DESCRIPTION,
                    "Add a new Publisher entry");
            putValue(MNEMONIC_KEY, new Integer('P'));
        }

        // display window in which user can input entry
        public void actionPerformed(ActionEvent e) {
            DataEntryFrame entryFrame =
                    createDataEntryFrame(3);

            // set new AddressBookEntry in window
            entryFrame.setDataEntry(
                    new DataEntry());

            // display window
            desktopPane.add(entryFrame);
            entryFrame.setVisible(true);
        }
    }

    private class SaveAction extends AbstractAction {
        public SaveAction() {
            putValue(NAME, "Save");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/Save24.png")));
            putValue(SHORT_DESCRIPTION, "Save");
            putValue(LONG_DESCRIPTION,
                    "Save an address book entry");
            putValue(MNEMONIC_KEY, new Integer('S'));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String operation = "";
            // get currently active window
            DataEntryFrame currentFrame =
                    (DataEntryFrame) desktopPane.getSelectedFrame();

            // obtain AddressBookEntry from window
            DataEntry entry =
                    currentFrame.getDataEntry();
            String currentEntry = currentFrame.getTitle();
            // insert person in address book
            try {
                switch (currentEntry) {
                    case "Author Entry":
                        int authorID = entry.getAuthorID();

                        // determine string for message dialogs
                        operation = (authorID == 0) ? "Insertion " : "Update ";

                        // insert or update entry ///// check return type: true = ok, false = failure
                        if (authorID == 0)
                            dataAccess.newAuthor(entry);
                        else
                            dataAccess.saveEntry(entry);

                        // display success message
                        JOptionPane.showMessageDialog(desktopPane,
                                operation + " successful");
                        break;
                    case "Title Entry":
                        int AuthorID = entry.getAuthorID();

                        operation = (AuthorID == 0) ? "Insertion " : "Update ";
                        boolean check;
                        if (AuthorID == 0)
                            check = dataAccess.newTitle(entry);
                        else
                            check = dataAccess.saveEntry(entry);
                        if (check) {
                            JOptionPane.showMessageDialog(desktopPane, operation + "Successful");
                        } else {
                            JOptionPane.showMessageDialog(desktopPane, operation + "Failed");
                        }
                        break;
                    case "Publisher Entry":
                        int publisherID = entry.getPublisherID();

                        operation = (publisherID == 0) ? "Insertion" : "Update";

                        if (publisherID == 0)
                            dataAccess.newPublisher(entry);
                        else
                            dataAccess.saveEntry(entry);

                        JOptionPane.showMessageDialog(desktopPane, operation + "succesful");
                        break;

                }
            }  // end try

            // detect database errors
            catch (SQLException exception) {
                JOptionPane.showMessageDialog(desktopPane, exception,
                        "DataAccessException",
                        JOptionPane.ERROR_MESSAGE);
                exception.printStackTrace();
            }

            // close current window and dispose of resources
            currentFrame.dispose();
        }
    }

    private class DeleteAction extends AbstractAction {
        public DeleteAction() {
            putValue(NAME, "Delete");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/Delete24.png")));
            putValue(SHORT_DESCRIPTION, "Delete");
            putValue(LONG_DESCRIPTION,
                    "Delete an address book entry");
            putValue(MNEMONIC_KEY, new Integer('D'));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DataEntryFrame currentFrame =
                    (DataEntryFrame) desktopPane.getSelectedFrame();

            // get AddressBookEntry from window
            DataEntry dataEntry =
                    currentFrame.getDataEntry();
            String frameType = currentFrame.getTitle();

            switch (frameType) {
                case "Author Entry":
                    if (dataEntry.getAuthorID() == 0) {
                        JOptionPane.showMessageDialog(desktopPane,
                                "New entries must be saved before they can be " +
                                        "deleted. \nTo cancel a new entry, simply " +
                                        "close the window containing the entry");
                        return;
                    }
                    try {
                        dataAccess.deleteAuthor(dataEntry);
                        // display message indicating success
                        JOptionPane.showMessageDialog(desktopPane,
                                "Deletion successful");
                    } catch (HeadlessException headlessException) {
                        JOptionPane.showMessageDialog(desktopPane,
                                "Deletion Failed");
                    }
                    break;
                case "Title Entry":
                    if (dataEntry.getIsbn().isEmpty()) {
                        JOptionPane.showMessageDialog(desktopPane,
                                "New entries must be saved before they can be " +
                                        "deleted. \nTo cancel a new entry, simply " +
                                        "close the window containing the entry");
                        return;
                    }
                    try {
                        dataAccess.deleteTitle(dataEntry);
                        JOptionPane.showMessageDialog(desktopPane,
                                "Deletion successful");
                    } catch (HeadlessException headlessException) {
                        JOptionPane.showMessageDialog(desktopPane,
                                "Deletion Failed");
                    }
                    break;
                case "Publisher Entry":
                    if (dataEntry.getPublisherID() == 0) {
                        JOptionPane.showMessageDialog(desktopPane,
                                "New entries must be saved before they can be " +
                                        "deleted. \nTo cancel a new entry, simply " +
                                        "close the window containing the entry");
                        return;
                    }
                    try {
                        dataAccess.deletePublisher(dataEntry);
                        JOptionPane.showMessageDialog(desktopPane,
                                "Deletion successful");
                    } catch (HeadlessException headlessException) {
                        JOptionPane.showMessageDialog(desktopPane,
                                "Deletion Failed");
                    }
                    break;
            }
            // close current window and dispose of resources
            currentFrame.dispose();
        }
    }

    private class SearchAuthor extends AbstractAction {      //books or authors
        public SearchAuthor() {
            putValue(NAME, "Search Author");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/Find24.png")));
            putValue(SHORT_DESCRIPTION, "Search Author");
            putValue(LONG_DESCRIPTION,
                    "Search for any books from the Author");
            putValue(MNEMONIC_KEY, new Integer('a'));
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String firstName =
                    JOptionPane.showInputDialog(desktopPane,
                            "Enter author First name");
            String lastName =
                    JOptionPane.showInputDialog(desktopPane,
                            "Enter author last name");

            ResultSet resultSet = dataAccess.findAuthor(firstName, lastName);
            try {
                if (content.getComponentCount() > 1) {
                    content.remove(content.getComponent(1));
                }

                resultTable = new JTable(new ResultSetTableModel(resultSet));
                content.add(new JScrollPane(resultTable));
                revalidate();
                repaint();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class SearchBook extends AbstractAction {      //books or authors
        public SearchBook() {
            putValue(NAME, "Search Books");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/Find24.png")));
            putValue(SHORT_DESCRIPTION, "Search Books");
            putValue(LONG_DESCRIPTION,
                    "Search for a Book");
            putValue(MNEMONIC_KEY, new Integer('b'));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String title =
                    JOptionPane.showInputDialog(desktopPane,
                            "Enter Book title");

            DataEntry book = dataAccess.findBook(title);

            if (book != null) {
                DataEntryFrame dataEntryFrame = createDataEntryFrame(2);
                dataEntryFrame.setDataEntry(book);
                desktopPane.add(dataEntryFrame);
                dataEntryFrame.setVisible(true);
            }
        }
    }

    private class ExitAction extends AbstractAction {
        public ExitAction() {
            putValue(NAME, "Exit");
            putValue(SHORT_DESCRIPTION, "Exit");
            putValue(LONG_DESCRIPTION, "Terminate the program");
            putValue(MNEMONIC_KEY, new Integer('x'));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            shutDown();
        }
    }

}
