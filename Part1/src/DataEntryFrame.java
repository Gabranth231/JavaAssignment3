import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class DataEntryFrame extends JInternalFrame {
    private HashMap Fields;

    private DataEntry dataEntry;
    private int menu;
    private JPanel leftLabels, rightTextField;

    private static int xOffset = 0, yOffset = 0;

    private static final String AUTHOR_FNAME = "Author First Name", AUTHOR_LNAME = "Author Last Name",
            ISBN = "Book isbn ", PUBLISHER_NAME ="Publisher name", PUBLISHER_ID ="Publisher ID", TITLE = "Book Title",
            EDITION_NUMBER = "Edition number", COPYRIGHT_NUMBER = "Copyright", IMAGE_FILE = "Book Image",
            PRICE = "Price ";
        //Gui
    public DataEntryFrame(int num){
        super("Books Entry", true,true);

        Fields = new HashMap();
        menu = num;
        leftLabels = new JPanel();
        leftLabels.setLayout( new GridLayout( 9, 1, 0, 5 ) );
        rightTextField = new JPanel();
        rightTextField.setLayout( new GridLayout( 9, 1, 0, 5 ) );
        //switch for new author, new book and new publisher
        switch(menu){
            case 1:
                super.title = "Author Entry";
                createRow(AUTHOR_FNAME);
                createRow(AUTHOR_LNAME);
                break;
            case 2:
                super.title = "Title Entry";
                createRow(AUTHOR_FNAME);
                createRow(AUTHOR_LNAME);
                createRow(ISBN);
                createRow(TITLE);
                createRow(EDITION_NUMBER);
                createRow(COPYRIGHT_NUMBER);
                createRow(PUBLISHER_ID);
                createRow(IMAGE_FILE);
                createRow(PRICE);
                break;
            case 3:
                super.title = "Publisher Entry";
                createRow(PUBLISHER_NAME);
                break;
        }

        Container container = getContentPane();
        container.add( leftLabels, BorderLayout.WEST );
        container.add( rightTextField, BorderLayout.CENTER );

        setBounds( xOffset, yOffset, 300, 300 );
        xOffset = ( xOffset + 30 ) % 300;
        yOffset = ( yOffset + 30 ) % 300;

    }

    public void setDataEntry( DataEntry entry )
    {
        dataEntry = entry;
        switch (menu) {
            case 1:
                setField(AUTHOR_FNAME, dataEntry.getFirstName());
                setField(AUTHOR_LNAME, dataEntry.getLastName());
                break;
            case 2:
                setField(AUTHOR_FNAME, dataEntry.getFirstName());
                setField(AUTHOR_LNAME, dataEntry.getLastName());
                setField(ISBN,dataEntry.getIsbn());
                setField(TITLE,dataEntry.getTitle());
                setField(EDITION_NUMBER,String.valueOf(dataEntry.getEditionNum()));
                setField(COPYRIGHT_NUMBER,String.valueOf(dataEntry.getCopyrightYear()));
                setField(PUBLISHER_ID, String.valueOf(dataEntry.getPublisherID()));
                setField(IMAGE_FILE, dataEntry.getImageFile());
                setField(PRICE, String.valueOf(dataEntry.getPrice()));
                break;
            case 3:
                setField(PUBLISHER_NAME,dataEntry.getPublisherName());
                break;
        }
    }

    // store AddressBookEntry data from GUI and return
    // AddressBookEntry
    public DataEntry getDataEntry()
    {
        switch(menu) {
            case 1:
                dataEntry.setFirstName(getField(AUTHOR_FNAME));
                dataEntry.setLastName(getField(AUTHOR_LNAME));
                break;
            case 2:
                dataEntry.setFirstName(getField(AUTHOR_FNAME));
                dataEntry.setLastName(getField(AUTHOR_LNAME));
                dataEntry.setIsbn(getField(ISBN));
                dataEntry.setTitle(getField(TITLE));
                dataEntry.setEditionNum(getField(EDITION_NUMBER));
                dataEntry.setCopyrightYear(getField(COPYRIGHT_NUMBER));
                dataEntry.setPublisherID(Integer.valueOf(getField(PUBLISHER_ID)));
                dataEntry.setImageFile(getField(IMAGE_FILE));
                dataEntry.setPrice(getField(PRICE));
                break;
            case 3:
                dataEntry.setPublisherName(getField(PUBLISHER_NAME));
                break;
        }
        return dataEntry;
    }

    // set text in JTextField by specifying field's
    // name and value
    private void setField( String fieldName, String value )
    {
        JTextField field =
                ( JTextField ) Fields.get( fieldName );

        field.setText( value );
    }

    // get text in JTextField by specifying field's name
    private String getField( String fieldName )
    {
        JTextField field =
                ( JTextField ) Fields.get( fieldName );

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

        Fields.put( name, field );
    }
}  // end class AddressBookEntryFrame

