/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processphotodups;

/**
 *
 * @author Dennis
 */
public class DupPhotoRecord {
    // Constants
    DupDbHelper myDbHelper;

// Constructor receives instance of DupsDbHelper
    DupPhotoRecord( DupDbHelper theDbHelper) {
        myDbHelper = theDbHelper;
    
}
    
    void getSomeDupRecords(int limit) {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }
    
    long getId() {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }
    
    String getTimeStamp() {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }
    
    String getDupFilePath() {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }
    
    String getBaseFilePath() {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }
    
    void markDelete() {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }
    
    void markSkipped() {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }
    
    boolean moveToNext() {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }
    
    private void getMoreRecords()
    {
        throw new UnsupportedOperationException("dbSelect with limit not supported yet.");
    }    
}
