import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class DataEntryFrame extends JInternalFrame {
    private HashMap authorFields;

    private DataEntry dataEntry;

    private JPanel leftLabels, rightTextField;

    private static int xOffset = 0, yOffset = 0;

    private static final String AUTHOR_FNAME = "Author First Name", AUTHOR_LNAME = "Author Last Name",
            ISBN = "Book isbn ", PUBLISHER_NAME ="Publisher name", TITLE = "Book Title",
            EDITION_NUMBER = "Edition number", COPYRIGHT_NUMBER = "Copyright", IMAGE_FILE = "Book Image",
            PRICE = "Price ";

    public DataEntryFrame(){
        super("Books Entry", true,true);

        authorFields = new HashMap();

        leftLabels = new JPanel();
        leftLabels.setLayout( new GridLayout( 9, 1, 0, 5 ) );
        rightTextField = new JPanel();
        rightTextField.setLayout( new GridLayout( 9, 1, 0, 5 ) );

        createRow(AUTHOR_FNAME);
        createRow(AUTHOR_LNAME);
        createRow(ISBN);
        createRow(PUBLISHER_NAME);
        createRow(TITLE);
        createRow(EDITION_NUMBER);
        createRow(COPYRIGHT_NUMBER);
        createRow(IMAGE_FILE);
        createRow(PRICE);

    }

    public void setDataEntry( DataEntry entry )
    {
        dataEntry = entry;
        String temp = "";
        setField( AUTHOR_FNAME, dataEntry.getFirstName() );
        setField( AUTHOR_LNAME, dataEntry.getLastName() );
        setField( ISBN, dataEntry.getIsbn() );
        setField( PUBLISHER_NAME, dataEntry.getPublisherName() );
        setField( TITLE, dataEntry.getTitle() );
        setField( EDITION_NUMBER, temp = String.valueOf(dataEntry.getEditionNum()));
        setField( COPYRIGHT_NUMBER, temp = String.valueOf(dataEntry.getCopyrightYear()) );
        setField( IMAGE_FILE, dataEntry.getImageFile() );
        setField( PRICE, temp = String.valueOf(dataEntry.getPrice()) );
    }

    // store AddressBookEntry data from GUI and return
    // AddressBookEntry
    public DataEntry getDataEntry()
    {
        dataEntry.setFirstName( getField( AUTHOR_FNAME ) );
        dataEntry.setLastName( getField( AUTHOR_LNAME ) );
        dataEntry.setIsbn( getField( ISBN ) );
        dataEntry.setPublisherName( getField( PUBLISHER_NAME ) );
        dataEntry.setTitle( getField( TITLE ) );
        dataEntry.setEditionNum( getField( EDITION_NUMBER ) );
        dataEntry.setCopyrightYear( getField( COPYRIGHT_NUMBER ) );
        dataEntry.setImageFile( getField( IMAGE_FILE ) );
        dataEntry.setPrice( getField( PRICE ) );

        return dataEntry;
    }

    // set text in JTextField by specifying field's
    // name and value
    private void setField( String fieldName, String value )
    {
        JTextField field =
                ( JTextField ) authorFields.get( fieldName );

        field.setText( value );
    }

    // get text in JTextField by specifying field's name
    private String getField( String fieldName )
    {
        JTextField field =
                ( JTextField ) authorFields.get( fieldName );

        return field.getText();
    }
    private void createRow( String name )
    {
        JLabel label = new JLabel( name, SwingConstants.RIGHT );
        label.setBorder(
                BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        leftLabels.add( label );

        JTextField field = new JTextField( 30 );
        rightTextField.add( field );

        authorFields.put( name, field );
    }
}  // end class AddressBookEntryFrame

