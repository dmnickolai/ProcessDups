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

class DupDbHelper {
    
    private static final Logger LOGGER = 
            Logger.getLogger(DupDbHelper.class.getName());
    
    //  Constants necessary to open DB Connection
    private static final String dbDriver = "com.mysql.cj.jdbc.Driver";
    private static final String dbName = "photos_catalog";
    private static final String dbUrl = "jdbc:mysql://localhost:3306/" + dbName;
    private static final String dbUser ="dennis";
    static final String dbPassword = "55555";
    
    // table names
    private String baseTableName = "testBase";
    private String dupsTableName = baseTableName + "Dups";
    
    // Constants for SQL commands
    private String selectAllDupSQL = "SELECT * FROM " + dupsTableName +" LIMIT ?;";
  
    private String selectBaseSQL = "SELECT * FROM " + baseTableName + " WHERE timestamp = ?;";
    private String selectDupSQL = "SELECT * FROM " + dupsTableName + 
            " WHERE skipped = 0 AND markedfordelete = 0 " +
            " LIMIT ?;";
    
    // Prepared statement objects
    private PreparedStatement selectBase = null;
    private PreparedStatement selectDups = null;
    private PreparedStatement selectAllDups = null;
    // Class Variables
    
    private Connection dbConn = null;
    private ResultSet resultSet = null;
     
   // Constructor
    DupDbHelper() {
        LOGGER.setLevel(Level.FINE);
        LOGGER.log(Level.FINE,"In DbHelper Constuctor");
        dbConn = getDbConnection();
        prepareSQLStatements();
        
    }
    
    // the following methods perform viarions of SELECT statements
    int dbSelect(String[] columns) {
        throw new UnsupportedOperationException("db Select Not supported yet."); 
    }
    
    ResultSet dbSelectAll(int limit){
        try {
            selectAllDups.setInt(1, limit);
            resultSet = selectAllDups.executeQuery();
            return resultSet  ;
        } catch (SQLException ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("dbSelect returned SQL Exception.");
    }
    
    ResultSet dbSelect(String[] columns, String where) {
        throw new UnsupportedOperationException("dbSelect with where not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    ResultSet dbSelect(String[] columns, int limit) {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    ResultSet dbSelect(String[] columns, String where, int limit) {
        throw new UnsupportedOperationException("dbSelect with limit/where not supported yet."); 
    }
    
    Connection getDbConnection() {
        LOGGER.log(Level.FINE,"in getDbConnection");
       //if connection exists, return it
       if (dbConn != null) return dbConn;
       LOGGER.log(Level.FINE,"need to make DB Conn");
       // open database connection
       try { 
           Class.forName(dbDriver);
           Connection conn = DriverManager.getConnection(dbUrl, dbUser,dbPassword);
           System.out.println("Connection OK");
           dbConn = conn;
           LOGGER.log(Level.FINE,"DB Conn OK");
           return conn;          
       }
       catch (SQLException ex){
           Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
           LOGGER.severe("Aborting app.....");
           System.exit(1);
       }
       catch (Exception ex){
           System.out.println(ex.getMessage() + ex.getCause().toString());
           System.out.println("Aborting app.....");
           System.exit(1);
       }
       return null;
    }

    void closeDbConnection(){
        try{
            if (dbConn!= null)
                dbConn.close();
        }
        catch(SQLException ex) {
            // report error then ignore
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            dbConn = null;
        }
    }
    
    boolean next() {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }
    
    int getRow() {
        try {
            return resultSet.getRow();
        } catch (SQLException ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    private void prepareSQLStatements() {
        try {        
            selectBase = dbConn.prepareStatement(selectBaseSQL);  
            System.out.println(selectBase);
            System.out.println(selectDupSQL);
            selectDups = dbConn.prepareCall(selectDupSQL, ResultSet.TYPE_SCROLL_SENSITIVE, 
                        ResultSet.CONCUR_UPDATABLE);
            System.out.println(selectDups);
            selectAllDups = dbConn.prepareStatement(selectAllDupSQL, ResultSet.TYPE_SCROLL_SENSITIVE, 
                        ResultSet.CONCUR_UPDATABLE);
            
        }
        catch (Exception ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.severe("Aborting app.....");
            System.exit(2);
        }  
         
    }
    
   
}
