/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processphotodups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Dennis
 */
public class ProcessPhotoDups {
    
    static void baseButtonClicked(ActionEvent e) {
         JButton whichBtn = (JButton)e.getSource();
        if (!(whichBtn instanceof JButton )) {
            System.out.println("Event not triggered by Button");
            System.exit(10);
        }
        
        String buttonName = whichBtn.getText();
        String message="";
        switch (buttonName){
            case "Skip": {
                message = "Skippping  base photo";
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
                message = "Bye bye to photo";
                break;
            }
        }    
        JOptionPane.showMessageDialog(
                     null, message, "Title", 
                     JOptionPane.INFORMATION_MESSAGE);
        
    }
    
    static void dupButtonClicked(ActionEvent e) {
        JButton whichBtn = (JButton)e.getSource();
        if (!(whichBtn instanceof JButton )) {
            System.out.println("Event not triggered by Button");
            System.exit(10);
        }
        
        String buttonName = whichBtn.getText();
        String message="";
        switch (buttonName){
            case "Skip": {
                
            /*
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
            */
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
                /*
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
        //JOptionPane.showMessageDialog(
        //             null, message, "Title", 
        //             JOptionPane.INFORMATION_MESSAGE);
        */
    }
        }}

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
        
        
        ActionListener baseEventHandler = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                baseButtonClicked(e);
            }
        };
        // Event handler for Dup Panel buttons
        ActionListener dupEventHandler = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dupButtonClicked(e);
            }
        };
       
         
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProcessDupsUI(baseEventHandler, dupEventHandler).setVisible(true);
            }
        });
    }
    
}
