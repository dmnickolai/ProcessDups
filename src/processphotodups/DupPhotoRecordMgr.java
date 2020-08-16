/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processphotodups;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dennis
 */
public class DupPhotoRecordMgr {

    private static final Logger LOGGER
            = Logger.getLogger(DupPhotoRecordMgr.class.getName());
    // Constants
    private DupDbHelper myDbHelper;
    private ResultSet currentRS;
    private long mostRecentID;
    private int limit = 10;  // number of records to retrieve

// Constructor receives instance of DupsDbHelper
    DupPhotoRecordMgr() {
        Handler handlerObj = new ConsoleHandler();
        handlerObj.setLevel(Level.ALL);
        LOGGER.addHandler(handlerObj);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
        LOGGER.log(Level.FINE, "In DbHelper Constuctor");
        
        myDbHelper = new DupDbHelper();
        currentRS = myDbHelper.dbSelectWhere(limit, false, false);
        LOGGER.info(getRecordSetSize() + " rows from Select");
    }

    boolean next() {
        boolean b = false;
        try {
            b = currentRS.next();
            if (b) {
                mostRecentID = currentRS.getLong("Id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DupPhotoRecordMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }

    void getSomeDupRecords(int limit) {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }

    long getId() {
        long id = -1;
        try {
            id = currentRS.getLong("ID");
        } catch (SQLException ex) {
            Logger.getLogger(DupPhotoRecordMgr.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnsupportedOperationException("SQL Exception.");
        }
        return id;
    }

    String getBaseFilePath() {
        String fp = "";
        try {

            fp = currentRS.getString("baseFilePath");
        } catch (SQLException ex) {
            Logger.getLogger(DupPhotoRecordMgr.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnsupportedOperationException("SQL Exception.");
        }
        return fp;
    }

    String getTimeStamp() {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }

    String getDupFilePath() {
        String fp = "";
        try {
            fp = currentRS.getString("filePath");
        } catch (SQLException ex) {
            Logger.getLogger(DupPhotoRecordMgr.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnsupportedOperationException("SQL Exception.");
        }
        return fp;
    }

    long getDupRecordCount() {
        return myDbHelper.getDupTableCount();
    }

    void markDelete() {
        myDbHelper.markToDelete(getId());
    }

    void markSkipped() {
        myDbHelper.markDupSkipped(getId());
    }

    private void getMoreRecords() {

        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }

    int getRecordSetSize() {
        try {
            currentRS.last();
            int size = currentRS.getRow();
            currentRS.beforeFirst();
            return size;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return -1;
    }

}
