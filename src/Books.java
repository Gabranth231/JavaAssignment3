import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Books extends JFrame {
    private JDesktopPane desktopPane;

    Action newAction,saveAction,
            deleteAction,searchAuthor,searchBook,exitAction;

    public Books(){
        super("Books Database");

        JToolBar toolBar = new JToolBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        //setup Actions
        newAction = new NewAction();
        saveAction = new SaveAction();
        saveAction.setEnabled(false);
        deleteAction = new DeleteAction();
        deleteAction.setEnabled(false);
        searchAuthor = new SearchAuthor();
        searchBook = new SearchBook();
        exitAction = new ExitAction();
        //....
        toolBar.add(newAction);
        toolBar.add(saveAction);
        toolBar.add(deleteAction);
        toolBar.add( new JToolBar.Separator() );
        toolBar.add(searchAuthor);
        toolBar.add(searchBook);

        fileMenu.add( newAction );
        fileMenu.add( saveAction );
        fileMenu.add( deleteAction );
        fileMenu.addSeparator();
        fileMenu.add( searchAuthor );
        fileMenu.add( searchBook );
        fileMenu.addSeparator();
        fileMenu.add( exitAction );

        JMenuBar menuBar = new JMenuBar();
        menuBar.add( fileMenu );
        setJMenuBar( menuBar );

        desktopPane = new JDesktopPane();
        Container c = getContentPane();
        c.add( toolBar, BorderLayout.NORTH );
        c.add( desktopPane, BorderLayout.CENTER );

        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing( WindowEvent event )
                    {
                        shutDown();
                    }
                }
        );

        // set window size and display window
        Toolkit toolkit = getToolkit();
        Dimension dimension = toolkit.getScreenSize();

        // center window on screen
        setBounds( 100, 100, dimension.width - 200,
                dimension.height - 200 );

        setVisible( true );
    }

    private void shutDown()
    {
        //database.close();   // close database connection
        System.exit( 0 );   // terminate program
    }
    private DataEntryFrame createDataEntryFrame()
    {
        DataEntryFrame frame = new DataEntryFrame();
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        frame.addInternalFrameListener(
                new InternalFrameAdapter() {
                    // internal frame becomes active frame on desktop
                    public void internalFrameActivated(
                            InternalFrameEvent event )
                    {
                        saveAction.setEnabled( true );
                        deleteAction.setEnabled( true );
                    }

                    // internal frame becomes inactive frame on desktop
                    public void internalFrameDeactivated(
                            InternalFrameEvent event )
                    {
                        saveAction.setEnabled( false );
                        deleteAction.setEnabled( false );
                    }
                }  // end InternalFrameAdapter anonymous inner class
        ); // end call to addInternalFrameListener

        return frame;
    }  // end method createAddressBookEntryFrame

    public static void main( String args[] )
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Books();
            }
        });
    }


    private class NewAction extends AbstractAction{

        public NewAction() {
            putValue( NAME, "NewAction" );
            putValue( SMALL_ICON, new ImageIcon(
                    getClass().getResource( "images/New24.png" ) ) );
            putValue( SHORT_DESCRIPTION, "NewA" );
            putValue( LONG_DESCRIPTION,
                    "Add a new address book entry" );
            putValue( MNEMONIC_KEY, new Integer( 'N' ) );
        }

        // display window in which user can input entry
        public void actionPerformed( ActionEvent e )
        {
            DataEntryFrame entryFrame =
                    createDataEntryFrame();

            // set new AddressBookEntry in window
            entryFrame.setDataEntry(
                    new DataEntry() );

            // display window
            desktopPane.add( entryFrame );
            entryFrame.setVisible( true );
        }
    }

    private class SaveAction extends AbstractAction{
        public SaveAction() {
            putValue( NAME, "Save" );
            putValue( SMALL_ICON, new ImageIcon(
                    getClass().getResource( "images/Save24.png" ) ) );
            putValue( SHORT_DESCRIPTION, "Save" );
            putValue( LONG_DESCRIPTION,
                    "Save an address book entry" );
            putValue( MNEMONIC_KEY, new Integer( 'S' ) );
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    private class DeleteAction extends AbstractAction{
        public DeleteAction() {
            putValue( NAME, "Delete" );
            putValue( SMALL_ICON, new ImageIcon(
                    getClass().getResource( "images/Delete24.png" ) ) );
            putValue( SHORT_DESCRIPTION, "Delete" );
            putValue( LONG_DESCRIPTION,
                    "Delete an address book entry" );
            putValue( MNEMONIC_KEY, new Integer( 'D' ) );
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    private class SearchAuthor extends AbstractAction{      //books or authors
        public SearchAuthor() {
            putValue( NAME, "Search Author" );
            putValue( SMALL_ICON, new ImageIcon(
                    getClass().getResource( "images/Find24.png" ) ) );
            putValue( SHORT_DESCRIPTION, "Search Author" );
            putValue( LONG_DESCRIPTION,
                    "Search for any books from the Author" );
            putValue( MNEMONIC_KEY, new Integer( 'a' ) );
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    private class SearchBook extends AbstractAction{      //books or authors
        public SearchBook() {
            putValue( NAME, "Search Books" );
            putValue( SMALL_ICON, new ImageIcon(
                    getClass().getResource( "images/Find24.png" ) ) );
            putValue( SHORT_DESCRIPTION, "Search Books" );
            putValue( LONG_DESCRIPTION,
                    "Search for a Book" );
            putValue( MNEMONIC_KEY, new Integer( 'b' ) );
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    private class ExitAction extends AbstractAction{
        public ExitAction() {
            putValue( NAME, "Exit" );
            putValue( SHORT_DESCRIPTION, "Exit" );
            putValue( LONG_DESCRIPTION, "Terminate the program" );
            putValue( MNEMONIC_KEY, new Integer( 'x' ) );
        }
        @Override
        public void actionPerformed(ActionEvent e) {shutDown();}
    }

}
