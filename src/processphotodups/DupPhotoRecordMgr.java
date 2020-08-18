/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processphotodups;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Dennis
 */
public class DupPhotoRecordMgr {

    private static final Logger LOGGER
            = Logger.getLogger(DupPhotoRecordMgr.class.getName());
    // Constants
    private static final int limit = 10;  // number of records to retrieve
    private static final boolean whereDeleted = false;
    private static final boolean whereSkipped = false;
    private static final String logFileFolder = "K:\\Deleted from App";
    private static final String logFilePath = logFileFolder + "\\appLog.log";
    // Class varibles
    private DupDbHelper myDbHelper;
    private ResultSet currentRS;
    private long mostRecentID;
    private boolean includeMarked = false;

// Constructor receives instance of DupsDbHelper
    DupPhotoRecordMgr(boolean includeMarked) {
        this.includeMarked = includeMarked;
        Handler consoleHandlerObj = new ConsoleHandler();
        consoleHandlerObj.setLevel(Level.ALL);
        LOGGER.addHandler(consoleHandlerObj);
        assureFolderExists(logFileFolder);
        FileHandler fileHandlerObj = null;
        try {
            fileHandlerObj = new FileHandler(logFilePath);
            fileHandlerObj.setFormatter(new SimpleFormatter());
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            System.exit(31);
        } catch (SecurityException ex) {
            LOGGER.severe(ex.getMessage());
            System.exit(32);
        }
        fileHandlerObj.setLevel(Level.ALL);
        LOGGER.addHandler(fileHandlerObj);
        LOGGER.setLevel(Level.WARNING);
        LOGGER.setUseParentHandlers(false);
        
        LOGGER.log(Level.FINE, "In Record Manager Constuctor");
        
        myDbHelper = new DupDbHelper();
        currentRS = myDbHelper.dbSelect(limit, includeMarked);
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
    
    void deleteFromDupDb() {
        myDbHelper.deleteDupRow(getId());
    }

    void markDelete() {
        logAction ("Delete");
        myDbHelper.markToDelete(getId());
    }

    void markSkipped() {
        logAction ("Skipped");
        myDbHelper.markDupSkipped(getId());
    }
    
    void logAction (String action){
        LOGGER.info("ID=" + getId() + " marked as " + action);
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
    
    /**
     * Assures that the folder exists by creating it if not found
     *
     * @param path folder path to validate or create
     */
    // Validates if folder exists or creates it if not
    private void assureFolderExists(String path) {
        File testFile = new File(path);
        if (testFile.exists() && testFile.isDirectory()) {
            return;
        }
        makeFolder(path);
    }

    //  Make folder whose path is input parameter
    private void makeFolder(String path) {
        //Creating a File object
        File file = new File(path);
        //Creating the directory
        boolean result = file.mkdir();
        if (!result) {
            System.out.println("Couldn’t create specified directory: " + path);
            System.exit(5);
        }
    }

    private void moveBinFile(File sourceFile, String destFileString) {
        byte[] buffer = null;
        try {
            //create FileInputStream object for source file
            FileInputStream fin = new FileInputStream(sourceFile);
            //create FileOutputStream object for destination file
            FileOutputStream fout = new FileOutputStream(destFileString);
            if (buffer == null) {
                buffer = new byte[16000];
            }
            // copy file big hunk at at time
            int noOfBytes = 0;

            //read bytes from source file and write to destination file
            while ((noOfBytes = fin.read(buffer)) != -1) {
                fout.write(buffer, 0, noOfBytes);
            }
            //close the streams
            fin.close();
            fout.close();
        } //end of try
        catch (FileNotFoundException fnf) {
            System.out.println("Specified file not found :" + fnf);
        } catch (IOException ioe) {
            System.out.println("Error while copying file :" + ioe);
        }
    }

}
