// Fig. 8.28: ResultSetTableModel.java
// A TableModel that supplies ResultSet data to a JTable.

// Java core packages

import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class ResultSetTableModel extends AbstractTableModel {
   private Connection connection;
   private Statement statement;
   private static ResultSet resultSet;
   private static ResultSetMetaData metaData;
   private int numberOfRows;

   public ResultSetTableModel(ResultSet resultSet) throws SQLException
   {
      this.resultSet = resultSet;
      setTable();
   }
   public ResultSetTableModel( String driver, String url,
                               String query ) throws SQLException, ClassNotFoundException
   {
      // load database driver class
      Class.forName( driver );

      // connect to database
      connection = DriverManager.getConnection( url,"root","root" );

      // create Statement to query database
      statement = connection.createStatement(
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY );

      // set query and execute it
      setQuery( query );
   }
   public ResultSetTableModel()
   {
      numberOfRows = 0;
   }

   // get class that represents column type
   public Class getColumnClass( int column )
   {
      // determine Java class of column
      try {
         String className = 
            metaData.getColumnClassName( column + 1 );
         
         // return Class object that represents className
         return Class.forName( className );
      }
      
      // catch SQLExceptions and ClassNotFoundExceptions
      catch ( Exception exception ) {
         exception.printStackTrace();
      }
      
      // if problems occur above, assume type Object 
      return Object.class;
   }

   // get number of columns in ResultSet
   public int getColumnCount() 
   {      
      // determine number of columns
      try {
         return metaData.getColumnCount(); 
      }
      
      // catch SQLExceptions and print error message
      catch ( SQLException sqlException ) {
         sqlException.printStackTrace();
      }
      
      // if problems occur above, return 0 for number of columns
      return 0;
   }

   // get name of a particular column in ResultSet
   public String getColumnName( int column )
   {       
      // determine column name
      try {
         return metaData.getColumnName( column + 1 );  
      }
      
      // catch SQLExceptions and print error message
      catch ( SQLException sqlException ) {
         sqlException.printStackTrace();
      }
      
      // if problems, return empty string for column name
      return "";
   }

   // return number of rows in ResultSet
   public int getRowCount() 
   { 
      return numberOfRows;
   }

   // obtain value in particular row and column
   public Object getValueAt( int row, int column )
   { 
      // obtain a value at specified ResultSet row and column
      try {
         resultSet.absolute( row + 1 );
         
         return resultSet.getObject( column + 1 );
      }
      
      // catch SQLExceptions and print error message
      catch ( SQLException sqlException ) {
         sqlException.printStackTrace();
      }
      
      // if problems, return empty string object
      return "";
   }
   
   // set new database query string
   public void setTable() throws SQLException
   {

      // obtain meta data for ResultSet
      metaData = resultSet.getMetaData();

      // determine number of rows in ResultSet
      resultSet.last();
      numberOfRows = resultSet.getRow();
        // get row number
      
      // notify JTable that model has changed
      fireTableStructureChanged();
   }
   public void setQuery( String query ) throws SQLException
   {
      // specify query and execute it
      resultSet = statement.executeQuery( query );

      // obtain meta data for ResultSet
      metaData = resultSet.getMetaData();

      // determine number of rows in ResultSet
      resultSet.last();                   // move to last row
      numberOfRows = resultSet.getRow();  // get row number

      // notify JTable that model has changed
      fireTableStructureChanged();
   }
}  // end class ResultSetTableModel



/**************************************************************************
 * (C) Copyright 2001 by Deitel & Associates, Inc. and Prentice Hall.     *
 * All Rights Reserved.                                                   *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
