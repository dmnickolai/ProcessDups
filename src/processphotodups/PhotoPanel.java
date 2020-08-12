/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processphotodups;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 *
 * @author Dennis
 */
public class PhotoPanel extends JPanel{
       
    JLabel displayedPhotoPath;
    JPanel me = null;
    Dimension dim = new Dimension(300,300);
    JLabel photoHolder;
    String panelTitle;
    ImageIcon noPhoto = null;
    public PhotoPanel(String title, ActionButton[] buttons, ActionListener eventHandler) {
        super();
        me = this;
        
        URL url = getClass().getResource("/images/notfound.png");
        noPhoto = (new javax.swing.ImageIcon(url));
        int status = noPhoto.getImageLoadStatus();
        if (status != MediaTracker.COMPLETE) {
            Logger.getLogger(DupDbHelper.class.getName()).log(Level.SEVERE, null, "Resouce Missing");
            System.exit(9);
        }
        setBorder(new LineBorder(Color.BLUE,3));
        setBackground(Color.PINK);
        //setLayout (new BoxLayout(me , BoxLayout.Y_AXIS));
        setLayout(new BorderLayout(5,5));
        JLabel titleLabel = new JLabel ("Base Photo");
        // set font
        Font font = new Font("Courier", Font.BOLD,14);
        titleLabel.setFont(font);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
 
        //
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        displayedPhotoPath = new JLabel("File Path");
        displayedPhotoPath.setHorizontalAlignment(SwingConstants.CENTER);
        innerPanel.add(displayedPhotoPath, BorderLayout.NORTH);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new LineBorder(Color.BLACK,3));
        leftPanel.setBackground(Color.CYAN);
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT,4,4));
        
        JPanel gridPanel = new JPanel();
        gridPanel.setBorder(new LineBorder(Color.BLACK,3));
        gridPanel.setBackground(Color.ORANGE);
        int k = buttons.length;
        gridPanel.setLayout(new GridLayout(k, 1, 5, 5));
        gridPanel.setBorder(new LineBorder(Color.BLACK,3));
        gridPanel.setBackground(Color.RED);
        //  Populate grid with buttons
        for( ActionButton thisButton: buttons) {
            JButton b = new JButton(thisButton.getText());
            gridPanel.add(b);
        }
        
        leftPanel.add (gridPanel);
        innerPanel.add(leftPanel, BorderLayout.WEST);
        //  Build Photo holder
        photoHolder = new JLabel();     
        photoHolder.setBorder(new LineBorder(Color.BLACK,1));
        photoHolder.setHorizontalAlignment(SwingConstants.CENTER);        
        photoHolder.setPreferredSize(dim);
        //photoHolder.setMinimumSize(dim);
        //photoHolder.setMaximumSize(dim);
        innerPanel.add(photoHolder, BorderLayout.CENTER);
        
        add(innerPanel, BorderLayout.CENTER);
        
    }
    
    
     public void showPhoto(String filePath)  {
        //int ok = MediaTracker.COMPLETE;
        //int err = MediaTracker.ERRORED;
        //int aborted = MediaTracker.ABORTED;
        
        displayedPhotoPath.setText(filePath);
        photoHolder.setText("");
        ImageIcon dupFoto = getImageIcon(filePath);
        Image fotoImage = dupFoto.getImage();
        int fotoImageHeigth = dupFoto.getIconHeight();
        int fotoImageWidth = dupFoto.getIconWidth();
        float aspectRatio = (float)fotoImageWidth/(float)fotoImageHeigth; 
        
        Dimension dim = photoHolder.getPreferredSize();
        int y = dim.height;
        
        
        float newX =  (y * aspectRatio);
        int newWidth = (int) newX;
               
        Image newimg = fotoImage.getScaledInstance(newWidth, y,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        ImageIcon image3 = new ImageIcon(newimg);  
        //jLabel2.setBounds(0, 0, newWidth, y);
        Dimension newDim = new Dimension(newWidth, y);
        photoHolder.setMinimumSize(newDim);
        photoHolder.setMaximumSize(newDim);
        photoHolder.setPreferredSize(newDim);
        //photoFrame.setBackground(Color.ORANGE);
        photoHolder.setIcon(image3);               
    }
    
    private ImageIcon getImageIcon(String path) {
        File file = new File(path);
        if (! file.exists()) return noPhoto;
        file = null;
        int err = MediaTracker.ERRORED;  // 4
        int aborted = MediaTracker.ABORTED; // 2
        int complete = MediaTracker.COMPLETE;  //8
        int loading = MediaTracker.LOADING;  //1
        ImageIcon icon = new ImageIcon(path);
        int status = icon.getImageLoadStatus();
        if ( status == MediaTracker.COMPLETE) { return icon;};
        return noPhoto;
    }   
}
