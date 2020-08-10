/*
 * This class is designed to isolate the rest of the application for any specifics
 * of the selecte DBMS.  It opens a DB connection in its construction.  
 * The app should close that connection when exiting.
 * Methods for each DML are defined as thse methods are specific to the chosen DBMS
 * This class absorbs all SQLExceptions.
 */
package processphotodups;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dennis Nickolai
 */
public class ProcessDupsUI {

/**
 *
 * @author Dennis
 */
public class DupDbHelper {
    
    //  Constants necessary to open DB Connection
    static final String dbDriver = "com.mysql.cj.jdbc.Driver";
    static final String dbName = "photos_catalog";
    static final String dbUrl = "jdbc:mysql://localhost:3306/" + dbName;
    static final String dbUser ="dennis";
    static final String dbPassword = "55555";
    
    Connection dbConn = null;
     
    String baseTableName = "testBase";
    String dupsTableName = baseTableName + "Dups";
    
    String selectBaseSQL = "SELECT * FROM " + baseTableName + " WHERE timestamp = ?;";
    PreparedStatement selectBase = null;
    
    String selectDupSQL = "SELECT * FROM " + dupsTableName + 
            " WHERE skipped = 0 AND markedfordelete = 0 " +
            " LIMIT ?;";
    PreparedStatement selectDups = null;
    
    ResultSet allDups = null;
    
    //ResultSet dupsTable = null;
    
    DupDbHelper() {
        dbConn = getDbConnection();
        try {        
            selectBase = dbConn.prepareStatement(selectBaseSQL);  
            System.out.println(selectBase);
            System.out.println(selectDupSQL);
            selectDups = dbConn.prepareCall(selectDupSQL, ResultSet.TYPE_SCROLL_SENSITIVE, 
                        ResultSet.CONCUR_UPDATABLE);
            System.out.println(selectDups);
        }
        catch (Exception ex) {
            System.out.println("Error in Initialize:" + ex.getMessage());
            System.exit(2);
        }  
    }
    
    ResultSet getDupDbEntries (int limit){
        allDups = null;
        int rowCount = 0;
        
        try {
            selectDups.setInt(1, limit);
            allDups = selectDups.executeQuery();
            if (allDups.last()) {//make cursor to point to the last row in the ResultSet object
                 rowCount = allDups.getRow();
                 allDups.beforeFirst(); //make cursor to point to the front of the ResultSet object, just before the first row.
               }
            System.out.println("Total number of rows in ResultSet object = "+rowCount);
        } catch (SQLException ex) {
             Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Exception in Dups:" + ex.getMessage());
            System.exit(7);
        }
        return allDups;
    }
    
    int getResultSetSize (ResultSet rs) {
        int rowCount = 0;
        try {
            if (rs.last()) {//make cursor to point to the last row in the ResultSet object
                rowCount = allDups.getRow();
                allDups.beforeFirst(); //make cursor to point to the front of the ResultSet object, just before the first row.
            }
        } catch (SQLException ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(12);
        }
        return rowCount;
    }
    
    Connection getDbConnection() {
       //if connection exists, return it
       if (dbConn != null) return dbConn;
       // open database connection
       try { 
           Class.forName(dbDriver);
           Connection conn = DriverManager.getConnection(dbUrl, dbUser,dbPassword);
           System.out.println("Connection OK");
           dbConn = conn;
           return conn;          
       }
       catch (SQLException ex){
           System.out.println("SQL Exception:" + ex.getMessage());
           System.out.println("Aborting app.....");
           System.exit(1);
       }
       catch (Exception ex){
           System.out.println(ex.getMessage() + ex.getCause().toString());
           System.out.println("Aborting app.....");
           System.exit(1);
       }
       return null;
    }

    public void closeDbConnection(){
        try{
            if (dbConn!= null)
                dbConn.close();
        }
        catch(SQLException ex) {
            // report error then ignore
            System.out.println("Exception in closs db connection, " + ex.getMessage());
        }
        finally {
            dbConn = null;
        }
    }
    
    public void markDupSkipped() {
        try {
            int id = allDups.getInt("id");
            PreparedStatement update = dbConn.prepareStatement("UPDATE " + dupsTableName + 
                    " SET skipped = 1 WHERE id = ?");
            update.setInt(1, id);
            int j = update.getUpdateCount();
            int count = update.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void markToDelete() {
        try {
            int id = allDups.getInt("id");
            PreparedStatement update = dbConn.prepareStatement("UPDATE " + dupsTableName + 
                    " SET markedfordelete = 1 WHERE id = ?");
            update.setInt(1, id);
            int j = update.getUpdateCount();
            int count = update.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
