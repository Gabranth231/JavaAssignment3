import java.util.List;
import java.sql.*;
public interface BooksDataAccess {
    public List<DataEntry> findAuthor(String authorLast);
    public DataEntry findBook(String Title);
    public boolean saveEntry( DataEntry dataEntry);
    public boolean newEntry(DataEntry dataEntry);
    public boolean deleteEntry(DataEntry)
}
