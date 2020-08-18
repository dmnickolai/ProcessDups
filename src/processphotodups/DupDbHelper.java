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
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dennis Nickolai
 */
class DupDbHelper {

    private static final Logger LOGGER
            = Logger.getLogger(DupDbHelper.class.getName());

    //  Constants necessary to open DB Connection
    private static final String dbDriver = "com.mysql.cj.jdbc.Driver";
    private static final String dbName = "photos_catalog";
    private static final String dbUrl = "jdbc:mysql://localhost:3306/" + dbName;
    private static final String dbUser = "dennis";
    static final String dbPassword = "55555";

    // table names
    private static final String baseTableName = "testBase";
    private static final String dupsTableName = baseTableName + "Dups";
    private static final String logTableName = baseTableName + "Log";

    // Constants for SQL commands
    private static final String selectDupSQL = "SELECT * FROM " + dupsTableName;
    private static final String selectWhereSQL = " WHERE skipped = 0 AND markedfordelete = 0";
    private static final String selectLimitSQL =  " LIMIT ?;";

    // Prepared statement objects
    private PreparedStatement selectDups = null;
    private PreparedStatement selectDupsWhere = null;
    // Class Variables

    private Connection dbConn = null;
    private ResultSet resultSet = null;

    // Constructor
    DupDbHelper() {
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.WARNING);
        LOGGER.addHandler(handlerObj);
        LOGGER.setLevel(Level.WARNING);
        LOGGER.setUseParentHandlers(false);
        LOGGER.log(Level.FINE, "In DbHelper Constuctor");
        dbConn = getDbConnection();
        prepareSQLStatements();
    }

    ResultSet dbSelect(int limit, boolean includeMarked) {
        try {
            selectDups.setInt(1, limit);
            selectDupsWhere.setInt(1, limit);
            if (includeMarked) {
                resultSet = selectDups.executeQuery();
            } else {
                resultSet = selectDupsWhere.executeQuery();
            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            System.exit(21);
        }
        return resultSet;

    }

    long getDupTableCount() {
        ResultSet rs = null;
        long cnt = 0;
        try {
            String sqlText = "SELECT Count(ID ) FROM " + dupsTableName + ";";
            PreparedStatement getCount = dbConn.prepareCall(sqlText);
            rs = getCount.executeQuery();
            rs.next();
            cnt = rs.getLong(1);
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            System.exit(20);
        }
        return cnt;
    }

    Connection getDbConnection() {
        //if connection exists, return it
        if (dbConn != null) {
            return dbConn;
        }
        LOGGER.log(Level.FINE, "need to make DB Conn");
        // open database connection
        try {
            Class.forName(dbDriver);
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            LOGGER.fine("Connection OK");
            dbConn = conn;
            LOGGER.log(Level.FINE, "DB Conn OK");
            return conn;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            LOGGER.severe("Aborting app.....");
            System.exit(1);
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
            LOGGER.severe("Aborting app.....");
            System.exit(2);
        }
        return null;
    }

    void closeDbConnection() {
        try {
            if (dbConn != null) {
                dbConn.close();
            }
        } catch (SQLException ex) {
            // report error then ignore
            LOGGER.severe(ex.getMessage());
            //System.exit(3);
        } finally {
            dbConn = null;
        }
    }

    public void markDupSkipped(long id) {
        int count = -1;
        try {
            PreparedStatement update = dbConn.prepareStatement("UPDATE " + dupsTableName
                    + " SET skipped = 1 WHERE id = ?");
            update.setLong(1, id);
            //int j = update.getUpdateCount();
            count = update.executeUpdate();
            if (count != 1) {
                LOGGER.severe("id=" + id + "was not updated");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteDupRow(long id){
        int count = -1;
        try {
            PreparedStatement deleteStmt = dbConn.prepareStatement("DELETE FROM " + dupsTableName
                    + " WHERE id = ?;");
            deleteStmt.setLong(1, id);            
            count = deleteStmt.executeUpdate();
            if (count != 1) {
                LOGGER.severe("id=" + id + "was not deleted");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    public void markToDelete(long id) {
        int count = 0;
        try {
            PreparedStatement update = dbConn.prepareStatement("UPDATE " + dupsTableName
                    + " SET markedfordelete = 1 WHERE id = ?");
            update.setLong(1, id);
            //int j = update.getUpdateCount();
            count = update.executeUpdate();
            if (count != 1) {
                LOGGER.severe("id=" + id + "was not updated");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            selectDups = dbConn.prepareStatement(selectDupSQL + selectLimitSQL, 
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            LOGGER.log(Level.FINE,selectDups.toString());
            selectDupsWhere = dbConn.prepareStatement(
                    selectDupSQL + selectWhereSQL + selectLimitSQL, 
                    ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            LOGGER.log(Level.FINE,selectDupsWhere.toString());
        } catch (Exception ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.severe("Aborting app.....");
            System.exit(2);
        }
    }
}
