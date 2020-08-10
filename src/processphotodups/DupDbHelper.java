/*
 * This class is designed to isolate the rest of the application for any specifics
 * of the selecte DBMS.  It opens a DB connection in its construction.  
 * The app should close that connection when exiting.
 * Methods for each DML are defined as thse methods are specific to the chosen DBMS
 * This class absorbs all SQLExceptions.
 */
package processphotodups;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dennis Nickolai
 */

class DupDbHelper {
    
    //  Constants necessary to open DB Connection
    static final String dbDriver = "com.mysql.cj.jdbc.Driver";
    static final String dbName = "photos_catalog";
    static final String dbUrl = "jdbc:mysql://localhost:3306/" + dbName;
    static final String dbUser ="dennis";
    static final String dbPassword = "55555";
    
    // table names
    String baseTableName = "testBase";
    String dupsTableName = baseTableName + "Dups";
    
    // Constants for SQL commands
    String selectBaseSQL = "SELECT * FROM " + baseTableName + " WHERE timestamp = ?;";
    String selectDupSQL = "SELECT * FROM " + dupsTableName + 
            " WHERE skipped = 0 AND markedfordelete = 0 " +
            " LIMIT ?;";
    
    // Prepared statement objects
    PreparedStatement selectBase = null;
    PreparedStatement selectDups = null;
    // Class Variables
    
    Connection dbConn = null;
    ResultSet resultSet = null;
     
   // Constructor
    DupDbHelper() {
        dbConn = getDbConnection();
        prepareSQLStatements();
        
    }
    
    
   

    int dbSelect(String[] columns) {
        throw new UnsupportedOperationException("db Select Not supported yet."); 
    }
    
    int dbSelect(String[] columns, String where) {
        throw new UnsupportedOperationException("dbSelect with where not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    int dbSelect(String[] columns, int limit) {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    int dbSelect(String[] columns, String where, int limit) {
        throw new UnsupportedOperationException("dbSelect with limit/where not supported yet."); 
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

    void closeDbConnection(){
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
    
    boolean next() {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }
    
    int getRow() {
        return resultSet.getRow();
    }

    private void prepareSQLStatements() {
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


}
