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
    private String baseTableName = "testBase";
    private String dupsTableName = baseTableName + "Dups";
    private String logTableName = baseTableName + "Log";

    // Constants for SQL commands
    private String selectAllDupSQL = "SELECT * FROM " + dupsTableName + " LIMIT ?;";
    private String selectAllDupWhereSQL = "SELECT * FROM " + dupsTableName
            + " WHERE skipped = ? AND markedfordelete = ?  LIMIT ? ;";

    // Prepared statement objects
    private PreparedStatement selectAllDups = null;
    private PreparedStatement selecteAllDupsWhere = null;
    // Class Variables

    private Connection dbConn = null;
    private ResultSet resultSet = null;

    // Constructor
    DupDbHelper() {
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.ALL);
        LOGGER.addHandler(handlerObj);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
        LOGGER.log(Level.FINE, "In DbHelper Constuctor");
        dbConn = getDbConnection();
        prepareSQLStatements();
    }

    // the following methods perform viarions of SELECT statements
    int dbSelect(String[] columns) {
        throw new UnsupportedOperationException("db Select Not supported yet.");
    }

    ResultSet dbSelectAll(int limit) {
        try {
            selectAllDups.setInt(1, limit);
            resultSet = selectAllDups.executeQuery();

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            System.exit(21);
        }
        return resultSet;

    }

    ResultSet dbSelectWhere(int limit, boolean skipped, boolean deleted) {
        try {
            selecteAllDupsWhere.setInt(3, limit);
            int getSkipped = 0;
            if (skipped) {
                getSkipped = 1;
            }
            int getDeleted = 0;
            if (deleted) {
                getDeleted = 1;
            }
            selecteAllDupsWhere.setInt(1, getSkipped);
            selecteAllDupsWhere.setInt(2, getDeleted);
            resultSet = selecteAllDupsWhere.executeQuery();
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
        LOGGER.log(Level.FINE, "in getDbConnection");
        //if connection exists, return it
        if (dbConn != null) {
            return dbConn;
        }
        LOGGER.log(Level.FINE, "need to make DB Conn");
        // open database connection
        try {
            Class.forName(dbDriver);
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("Connection OK");
            dbConn = conn;
            LOGGER.log(Level.FINE, "DB Conn OK");
            return conn;
        } catch (SQLException ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.severe("Aborting app.....");
            System.exit(1);
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + ex.getCause().toString());
            System.out.println("Aborting app.....");
            System.exit(1);
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
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbConn = null;
        }
    }

    public void markDupSkipped(long id) {
        try {
            
            PreparedStatement update = dbConn.prepareStatement("UPDATE " + dupsTableName + 
                    " SET skipped = 1 WHERE id = ?");
            update.setLong(1, id);
            int j = update.getUpdateCount();
            int count = update.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void markToDelete(long id) {
        try {
            PreparedStatement update = dbConn.prepareStatement("UPDATE " + dupsTableName + 
                    " SET markedfordelete = 1 WHERE id = ?");
            update.setLong(1, id);
            int j = update.getUpdateCount();
            int count = update.executeUpdate();
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
            selectAllDups = dbConn.prepareStatement(selectAllDupSQL, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            selecteAllDupsWhere = dbConn.prepareStatement(selectAllDupWhereSQL, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
        } catch (Exception ex) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.severe("Aborting app.....");
            System.exit(2);
        }
    }
}
