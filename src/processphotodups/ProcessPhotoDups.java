/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processphotodups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Dennis
 */
public class ProcessPhotoDups {

    private static final Logger LOGGER
            = Logger.getLogger(DupPhotoRecordMgr.class.getName());
    ProcessDupsUI ui = null;

    ActionListener baseListener = null;
    ActionListener dupListener = null;
    DupPhotoRecordMgr dpm;

    long currentRowNumber = 0;
    long totalRowCount = 0;

    ProcessPhotoDups() {
        LOGGER.setLevel(Level.ALL);
        LOGGER.fine( "Constuctor");
        baseListener = (ActionEvent evt) -> {
            baseButtonClicked(evt);
        };
        dupListener = (ActionEvent evt) -> {
            dupButtonClicked(evt);
        };
        dpm = new DupPhotoRecordMgr();
        totalRowCount = dpm.getDupRecordCount();
        ui = new ProcessDupsUI(baseListener, dupListener);
        ui.setVisible(true);
        // instantiate Dup Photo Record Manager

        if (!dpm.next()) {
            JOptionPane.showMessageDialog(
                    null, "Duplicate Table Empty", "No Duplicates",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        processDupEntry();
    }

    private void processDupEntry() {
        currentRowNumber += 1;
        //int currNum = dpm .getRow();
        long id = dpm.getId();
        System.out.println("Id for first dup record: " + id);
        String dupFilePath = dpm.getDupFilePath();
        String baseFilePath = dpm.getBaseFilePath();

        ui.showDupPhoto(dupFilePath);
        ui.showBasePhoto(baseFilePath);

        String stat = currentRowNumber + " of " + totalRowCount;
        ui.setStatusLine(stat);
    }

    void baseButtonClicked(ActionEvent e) {
        JButton whichBtn = (JButton) e.getSource();
        if (!(whichBtn instanceof JButton)) {
            System.out.println("Event not triggered by Button");
            System.exit(10);
        }
        String buttonName = whichBtn.getText();
        ActionButton ab = ActionButton.fromString(buttonName);
        String message = "";
        switch (ab) {
            case REPLACE: {
                message = "Replacing";
                break;
            }
            case DELETE: {
                message = "Deleting";
                break;
            }
            case MOVE: {
                message = "Moving";
                break;
            }
            case RENAME: {
                message = "Renaming";
                break;
            }
        }
        JOptionPane.showMessageDialog(
                null, "BASE:" + message, "Title",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void dupButtonClicked(ActionEvent e) {
        JButton whichBtn = (JButton) e.getSource();
        if (!(whichBtn instanceof JButton)) {
            System.out.println("Event not triggered by Button");
            System.exit(10);
        }
        String buttonName = whichBtn.getText();
        ActionButton ab = ActionButton.fromString(buttonName);
        String message = "";
        switch (ab) {
            case SKIP: {
                dpm.markSkipped();
                moveToNext();
                break;
            }
            case DELETE: {
                dpm.markDelete();
                moveToNext();
                break;
            }
            case MOVE: {
                message = "Moving";
                moveToNext();
                break;
            }
            case RENAME: {
                message = "Renaming";
                moveToNext();
                break;
            }
        }
        if (message.isEmpty())return;
        
        JOptionPane.showMessageDialog(
                null, "DUP: " + message, "Title",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void moveToNext() {
        if (dpm.next()) {
            processDupEntry();
        } else {
            JOptionPane.showMessageDialog(
                    null, "Last Dup Entry", "Finished",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Logger.getLogger(DupDbHelper.class.getName()).log(Level.FINE,
                        null, "Main");
                new ProcessPhotoDups();

            }
        });
    }

}
