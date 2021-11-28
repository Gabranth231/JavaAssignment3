import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CloudDataAccess implements BooksDataAccess{
    private Connection connection;

    private PreparedStatement sqlFindAuthorTitles;
    private PreparedStatement sqlFindBook;
    private PreparedStatement sqlFindAuthorWithISBN;

    // reference to prepared statement for determining sqlAuthorID
    private PreparedStatement sqlAuthorID;
    private PreparedStatement sqlPublisherID;

    // references to prepared statements for inserting entry
    private PreparedStatement sqlInsertAuthor;
    private PreparedStatement sqlInsertTitles;
    private PreparedStatement sqlInsertPublisher;
    private PreparedStatement sqlInsertISBN;

    // references to prepared statements for updating entry
    private PreparedStatement sqlUpdateAuthor;
    private PreparedStatement sqlUpdateTitles;
    private PreparedStatement sqlUpdatePublisher;
    private PreparedStatement sqlUpdateISBN;

    // references to prepared statements for updating entry
    private PreparedStatement sqlDeleteAuthor;
    private PreparedStatement sqlDeleteTitles;
    private PreparedStatement sqlDeletePublisher;
    private PreparedStatement sqlDeleteISBN;


    public CloudDataAccess() throws Exception{
        connect();
        sqlAuthorID = connection.prepareStatement("SELECT authorID FROM authors WHERE firstName = ? AND lastName = ?");

        sqlFindAuthorTitles = connection.prepareStatement("SELECT * FROM titles WHERE isbn = ANY (SELECT isbn FROM authorisbn WHERE authorID = " +
                "ANY (SELECT authorID FROM authors WHERE firstName = ? AND lastName = ?))",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
        sqlFindBook = connection.prepareStatement("SELECT*FROM titles WHERE title = ?");
        sqlFindAuthorWithISBN = connection.prepareStatement("SELECT * FROM authors WHERE authorID =" +
                "(SELECT authorID FROM authorisbn WHERE isbn = ?)");

        sqlInsertAuthor = connection.prepareStatement("INSERT INTO authors (firstName,lastName) SELECT ?,? WHERE NOT EXISTS" +
                "(SELECT firstName,lastName FROM authors WHERE firstName = ? AND lastName = ?)");
        sqlInsertTitles = connection.prepareStatement("INSERT INTO titles (isbn,title,editionNumber,copyright,publisherID," +
                "imageFile,price)"+"VALUES (?,?,?,?,?,?,?)");
        sqlInsertISBN = connection.prepareStatement("INSERT INTO authorisbn(authorID,isbn)"+"VALUES(?,?)");
        sqlInsertPublisher = connection.prepareStatement("INSERT INTO publishers(publisherName)" + "VALUES (?)");

        sqlUpdateAuthor = connection.prepareStatement("UPDATE authors SET firstName = ?, lastName = ? " + "WHERE authorID = ?");
        sqlUpdatePublisher = connection.prepareStatement("UPDATE publishers SET publisherName = ? " + "WHERE publisherID = ?");
        sqlUpdateTitles = connection.prepareStatement("UPDATE titles SET title = ?,editionNumber = ?, copyright = ?," +
                "publisherID = ?, imageFile = ?,price = ? "+"WHERE isbn = ?");
        sqlUpdateISBN = connection.prepareStatement("UPDATE authorisbn SET isbn = ? "+"WHERE authorID = ?");

        sqlDeleteAuthor = connection.prepareStatement("DELETE FROM authors WHERE authorID = ?");
        sqlDeleteISBN = connection.prepareStatement("DELETE FROM authorisbn WHERE isbn = ?");
        sqlDeleteTitles = connection.prepareStatement("DELETE FROM titles WHERE isbn = ?");
        sqlDeletePublisher = connection.prepareStatement("DELETE FROM publishers WHERE publisherID = ?, ");


    }

    @Override
    public ResultSet findAuthor(String authorFirst,String authorLast) {
        try {
            // set query parameter and execute query
            sqlFindAuthorTitles.setString( 1, authorFirst);
            sqlFindAuthorTitles.setString( 2, authorLast);
            ResultSet resultSet = sqlFindAuthorTitles.executeQuery();

            sqlAuthorID.setString(1,authorFirst);
            sqlAuthorID.setString(2,authorLast);
            ResultSet AuthorResult = sqlAuthorID.executeQuery();

            if(AuthorResult.next()) {
                return resultSet;
            }
        }

        // catch SQLException
        catch ( SQLException sqlException ) {
            return null;
        }
        return null;
    }

    @Override
    public DataEntry findBook(String Title) {
        try{
            sqlFindBook.setString(1,Title);
            ResultSet resultSet = sqlFindBook.executeQuery();
            if(resultSet.next()){
                sqlFindAuthorWithISBN.setString(1,resultSet.getString(1));
                ResultSet authorData = sqlFindAuthorWithISBN.executeQuery();

                DataEntry book = new DataEntry(authorData.getInt("authorID"));
                book.setFirstName(authorData.getString("firstName"));
                book.setLastName(authorData.getString("lastName"));
                book.setIsbn(resultSet.getString(1));
                book.setTitle(resultSet.getString(2));
                book.setEditionNum(resultSet.getString(3));
                book.setCopyrightYear(resultSet.getString(4));
                book.setPublisherID(Integer.valueOf(resultSet.getString(5)));
                book.setImageFile(resultSet.getString(6));
                book.setPrice(resultSet.getString(7));

                return book;
            }
            else{
                return null;
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveEntry(DataEntry dataEntry) {
        try{
            String currentFrame = dataEntry.getTitle();
            int result;
            switch (currentFrame) {
                case "Author Entry":
                    sqlUpdateAuthor.setString(1,dataEntry.getFirstName());
                    sqlUpdateAuthor.setString(2,dataEntry.getLastName());
                    sqlUpdateAuthor.setString(3,String.valueOf(dataEntry.getAuthorID()));
                    result = sqlUpdateAuthor.executeUpdate();
                    if(result == 0){
                        connection.rollback();
                        return false;
                    }
                    else{
                        connection.commit();
                        return true;
                    }
                case "Publisher Entry":
                    sqlUpdatePublisher.setString(1,dataEntry.getPublisherName());
                    sqlUpdatePublisher.setString(2,String.valueOf(dataEntry.getPublisherID()));
                    result = sqlUpdateAuthor.executeUpdate();
                    if(result == 0){
                        connection.rollback();
                        return false;
                    }
                    else{
                        connection.commit();
                        return true;
                    }
                case "Title Entry":
                    sqlUpdateTitles.setString(1,dataEntry.getTitle());
                    sqlUpdateTitles.setString(1,String.valueOf(dataEntry.getEditionNum()));
                    sqlUpdateTitles.setString(1,dataEntry.getCopyrightYear());
                    sqlUpdateTitles.setString(1,String.valueOf(dataEntry.getPublisherID()));
                    sqlUpdateTitles.setString(1,dataEntry.getImageFile());
                    sqlUpdateTitles.setString(1,String.valueOf(dataEntry.getPrice()));
                    sqlUpdateTitles.setString(1,dataEntry.getIsbn());
                    result = sqlUpdateTitles.executeUpdate();
                    if(result == 0){
                        connection.rollback();
                        return false;
                    }
                    else{
                        connection.commit();
                        return true;
                    }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean newAuthor(DataEntry dataEntry) throws SQLException {
        // insert Author in database
        try {
            int result;

            // insert first and last name in names table
            sqlInsertAuthor.setString( 1, dataEntry.getFirstName() );
            sqlInsertAuthor.setString( 2, dataEntry.getLastName() );
            sqlInsertAuthor.setString( 3, dataEntry.getFirstName() );
            sqlInsertAuthor.setString( 4, dataEntry.getLastName() );
            result = sqlInsertAuthor.executeUpdate();

            // if insert fails, rollback and discontinue
            if ( result == 0 ) {
                connection.rollback(); // rollback insert
            }

            // determine new personID
            ResultSet resultPersonID = sqlAuthorID.executeQuery();

            if ( resultPersonID.next() ) {
                connection.commit();   // commit insert
                return true;           // insert successful
            }

            else
                return false;
        }  // end try

        // detect problems updating database
        catch ( SQLException sqlException ) {
            // rollback transaction
            try {
                connection.rollback(); // rollback update
                return false;          // update unsuccessful
            }

            // handle exception rolling back transaction
            catch ( SQLException exception ) {
                exception.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean newTitle(DataEntry dataEntry) throws SQLException {
        try{
            int result;
            sqlInsertAuthor.setString( 1, dataEntry.getFirstName() );
            sqlInsertAuthor.setString( 2, dataEntry.getLastName() );
            sqlInsertAuthor.setString( 3, dataEntry.getFirstName() );
            sqlInsertAuthor.setString( 4, dataEntry.getLastName() );
            sqlInsertAuthor.executeUpdate();


            sqlAuthorID.setString(1,dataEntry.getFirstName());
            sqlAuthorID.setString(2,dataEntry.getLastName());
            ResultSet resultSet = sqlAuthorID.executeQuery();

            sqlInsertTitles.setString(1,dataEntry.getIsbn());
            sqlInsertTitles.setString(2,dataEntry.getTitle());
            sqlInsertTitles.setString(3,String.valueOf(dataEntry.getEditionNum()));
            sqlInsertTitles.setString(4,String.valueOf(dataEntry.getCopyrightYear()));
            sqlInsertTitles.setString(5,String.valueOf(dataEntry.getPublisherID()));
            sqlInsertTitles.setString(6,dataEntry.getImageFile());
            sqlInsertTitles.setString(7,String.valueOf(dataEntry.getPrice()));
            result = sqlInsertTitles.executeUpdate();

            if(result == 0){
                connection.rollback();
                return false;
            }
            else{
                if(resultSet.next()) {
                    sqlInsertISBN.setString(1, resultSet.getString(1));
                    sqlInsertISBN.setString(2, dataEntry.getIsbn());
                    result = sqlInsertISBN.executeUpdate();

                    if (result == 0) {
                        connection.rollback();
                        return false;
                    }
                    connection.commit();   // commit insert
                    return true;           // insert successful
                }
                else {
                    connection.rollback();
                    return false;
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean newPublisher(DataEntry dataEntry) throws SQLException {
        try{
            int result;
            sqlInsertPublisher.setString(1,dataEntry.getPublisherName());
            result = sqlInsertPublisher.executeUpdate();
            if(result == 0){
                connection.rollback();
                return false;
            }
            else{
                connection.commit();
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAuthor(DataEntry dataEntry) {
        try{
            sqlDeleteAuthor.setString(1,String.valueOf(dataEntry.getAuthorID()));
            int result = sqlDeleteAuthor.executeUpdate();
            if(result == 0){
                connection.rollback();
                return false;
            }
            else{
                sqlDeleteAuthor.setString(1,String.valueOf(dataEntry.getAuthorID()));
                result = sqlDeleteAuthor.executeUpdate();

                if (result == 0) {
                    connection.rollback();
                    return false;
                }
                else{
                    connection.commit();
                    return true;
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteTitle(DataEntry dataEntry) {
        try{
            sqlDeleteISBN.setString(1,dataEntry.getIsbn());
            int result = sqlDeleteISBN.executeUpdate();
            if(result == 0){
                connection.rollback();
                return false;
            }
            else {
                sqlDeleteTitles.setString(1,dataEntry.getIsbn());
                connection.commit();
                return true;
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deletePublisher(DataEntry dataEntry) {
        try{
            sqlDeletePublisher.setString(1,String.valueOf(dataEntry.getPublisherID()));
            int result = sqlDeletePublisher.executeUpdate();
            if(result == 0){
                connection.rollback();
                return false;
            }
            else {
                connection.commit();
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() {

    }

    private void connect() throws Exception
    {
        // Cloudscape database driver class name
        String driver = "com.mysql.cj.jdbc.Driver";

        // URL to connect to addressbook database
        String url = "jdbc:mysql://localhost:3306/books";

        // load database driver class
        Class.forName( driver );

        // connect to database
        connection = DriverManager.getConnection( url,"root","root" );

        // Require manual commit for transactions. This enables
        // the program to rollback transactions that do not
        // complete and commit transactions that complete properly.
        connection.setAutoCommit( false );
    }
}
