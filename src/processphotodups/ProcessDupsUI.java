/*
 * This class is designed to isolate the rest of the application for any specifics
 * of the selecte DBMS.  It opens a DB connection in its construction.  
 * The app should close that connection when exiting.
 * Methods for each DML are defined as thse methods are specific to the chosen DBMS
 * This class absorbs all SQLExceptions.
 */
package processphotodups;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;



/**
 *
 * @author Dennis Nickolai
 */
public class ProcessDupsUI extends JFrame{
    
   
    
   private Container mainContainer = null;
   private ProcessDupsUI me = null; 
   private ResultSet dupsTable = null;
   private Icon noPhoto = null;
   private JLabel lblStatus=null;
   
   //DupDbHelper dbHelper = new DupDbHelper();
   private PhotoPanel basePhotoPanel = null;
   private PhotoPanel dupPhotoPanel = null;
   
   private ActionButton[] baseButtons =  {ActionButton.REPLACE, ActionButton.RENAME, ActionButton.MOVE};
   private ActionButton[] dupButtons =  {ActionButton.SKIP, ActionButton.DELETE, ActionButton.RENAME, ActionButton.MOVE};
   
   public ProcessDupsUI( ActionListener baseListener, ActionListener dupListener) {
        super("Process Photos with Same Timestamp");
        initClass();        
        buildUI(baseListener, dupListener);   
    }
   
   void showBasePhoto(String filePath) {
       basePhotoPanel.showPhoto(filePath);
   }
   
   void showDupPhoto(String filePath) {
       dupPhotoPanel.showPhoto(filePath);
   }
   
   private void buildUI(ActionListener baseListener, ActionListener dupListener) {
        // Setup full window
        mainContainer.setLayout(new BorderLayout(8,6));
        mainContainer.setBackground(Color.YELLOW);
        getRootPane().setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.GREEN ));
        // Build and add title panel
        JPanel topPanel = new JPanel();
        topPanel.setBorder(new LineBorder(Color.BLACK,3));
        topPanel.setBackground(Color.ORANGE);
        JLabel topLine = new JLabel("My New App");
        topLine.setOpaque(true);
        topLine.setAlignmentX(Component.CENTER_ALIGNMENT);
        topLine.setHorizontalTextPosition(JLabel.CENTER);
        topPanel.add(topLine, BorderLayout.NORTH);
        add(topPanel, BorderLayout.NORTH);
        // Build working panel showing Base and Dup photos
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        
        // Event handler for Base Panel buttons
        basePhotoPanel = new PhotoPanel("Base Photo", baseButtons, baseListener);
        
          
        add(basePhotoPanel,BorderLayout.CENTER);
        middlePanel.add(basePhotoPanel);
       
        dupPhotoPanel = new PhotoPanel("Possible Duplicate", dupButtons, dupListener);
        middlePanel.add(dupPhotoPanel);
        add(middlePanel,BorderLayout.CENTER);

        JPanel p = new JPanel();
        p.setBorder(new LineBorder(Color.BLACK,3));
        p.setBackground(Color.WHITE);
        lblStatus = new JLabel("Status");
        p.add(lblStatus);
        add(p,BorderLayout.SOUTH);
        
   }
   
   void setStatusLine(String stat){
       lblStatus.setText(stat);
   }
   
    private void initClass() {
        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1000,900);
        //setLocationRelativeTo(null); 

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        me = this;
        // Capture app closing to close DB connection
        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                /*
               if (dbHelper !=null) {
                   dbHelper.closeDbConnection();
                   

                }
*/
            System.exit(0);
            }
        };
         me = this;       
        this.addWindowListener(exitListener);
        mainContainer = getContentPane();
       
    }
    
    
}
