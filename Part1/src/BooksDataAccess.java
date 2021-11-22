import java.util.List;
import java.sql.*;
public interface BooksDataAccess {
    public List<DataEntry> findAuthor(String authorFirst,String authorLast);
    public DataEntry findBook(String Title);
    public boolean saveEntry( DataEntry dataEntry);
    public boolean newAuthor(DataEntry dataEntry) throws SQLException;
    public boolean newTitle(DataEntry dataEntry) throws SQLException;
    public boolean newPublisher(DataEntry dataEntry) throws SQLException;
    public boolean deleteAuthor(DataEntry dataEntry);
    public boolean deleteTitle(DataEntry dataEntry);
    public boolean deletePublisher(DataEntry dataEntry);
    public void close();
}
