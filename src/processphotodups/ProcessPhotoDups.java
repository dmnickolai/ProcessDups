/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processphotodups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Dennis
 */
public class ProcessPhotoDups {
    
    ProcessDupsUI ui = null;
    
    ActionListener baseListener = null;
    ActionListener dupListener = null;
    DupPhotoRecordMgr dpm;
    ProcessPhotoDups () {
       Logger.getLogger(DupDbHelper.class.getName()).log(Level.FINE,
                        null, "Consturctor");
        baseListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            baseButtonClicked(evt);
            };
        };
        dupListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            dupButtonClicked(evt);
            };
        };
        
        ui = new ProcessDupsUI(baseListener, dupListener);
        ui.setVisible(true);
        // instantiate Dup Photo Record Manager
        dpm = new DupPhotoRecordMgr();
        if (!dpm.next()) {
            JOptionPane.showMessageDialog(
                     null, "Duplicate Table Empty", "No Duplicates", 
                     JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        processDupEntry();
    }
    
    private void processDupEntry() {
       
            //int currNum = dpm .getRow();
            long id = dpm.getId();
        System.out.println("Id for first dup record: " + id);
            String dupFilePath = dpm.getDupFilePath();
            String baseFilePath = dpm.getBaseFilePath();
            
            ui.showDupPhoto(dupFilePath);
            ui.showBasePhoto(baseFilePath);              
    }
      
    void baseButtonClicked(ActionEvent e) {
        JButton whichBtn = (JButton)e.getSource();
        if (!(whichBtn instanceof JButton )) {
            System.out.println("Event not triggered by Button");
            System.exit(10);
        }
        String buttonName = whichBtn.getText();
        ActionButton ab = ActionButton.fromString(buttonName);
        String message="";
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
        JButton whichBtn = (JButton)e.getSource();
        if (!(whichBtn instanceof JButton )) {
            System.out.println("Event not triggered by Button");
            System.exit(10);
        }
        String buttonName = whichBtn.getText();
        ActionButton ab = ActionButton.fromString(buttonName);
        String message="";
        switch (ab) {
            case SKIP: {
                message = "Skipping";
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
        /*
        switch (buttonName){
            case "Skip": {
                
            
                try { 
                    dbHelper.markDupSkipped();
                    if (dupsTable.next())
                        processDup(dupsTable); 
                    else {
                            me.dispose(); return;
                }
                    //}
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    System.exit(8);
                }
                message = "Skippping dup photo";
                break;
           
            }
            case "Move": {
                message = "Moving photo";
                break;
            }
            case "Rename": {
                message = "Renameing Photo";
                break;
            }
            case "Delete": {
                
                int a=JOptionPane.showConfirmDialog(this,"You are about to delete file\n" +  
                        dupPhotoPanel.displayedPhotoPath.getText() + "\nAre you sure?");  
                if(a==JOptionPane.YES_OPTION) {

                    message = "Bye bye to photo";
                    try { 
                        dbHelper.markToDelete();
                        if (!dupsTable.next())  {                        
                                me.dispose(); 
                                return;}
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        System.exit(8);
                    }
                processDup(dupsTable); 
                break;
            }
        } 
        */
        JOptionPane.showMessageDialog(
                     null, "DUP: " + message, "Title", 
                     JOptionPane.INFORMATION_MESSAGE);
    } 
        
       

    /*
        dupsTable =dbHelper.getDupDbEntries(10);
        if (dbHelper.getResultSetSize(dupsTable) == 0) {
            JOptionPane.showMessageDialog(
                     null, "Duplicate DB Table Empty", "Title", 
                     JOptionPane.INFORMATION_MESSAGE);
        }
       try {
           dupsTable.next();
       } catch (SQLException ex) {
           Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
       }
        */
    

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
