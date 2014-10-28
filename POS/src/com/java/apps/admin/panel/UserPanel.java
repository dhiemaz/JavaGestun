/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.java.apps.admin.panel;

import com.java.apps.database.ConnectSQLLite;
import com.java.apps.pojo.UserItems;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import javax.imageio.ImageIO;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Ultimate
 */
public class UserPanel extends javax.swing.JPanel {
      
    private UserItems items;
    private UserTableModel user_table_model = new UserTableModel();
    private String path_image_file = new String();
    
    private boolean isUpdate = false;
    private int user_id = 0;
    

    /**
     * Creates new form UserPanel
     */
    public UserPanel() {
        initComponents();
        
        // Set initial save button disable //
        btn_add_user.setEnabled(false);
        btn_cancel.setEnabled(false);
        
        Reload_Table();
    }
    
    private void Reload_Table(){
        
        // Insert user data model into table user //
        user_table_model = new UserTableModel();
        tbl_user.setModel(user_table_model);
        tbl_user.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_user.getColumnModel().getColumn(0).setPreferredWidth(150);  // action //        
        tbl_user.getColumnModel().getColumn(1).setPreferredWidth(100);  // username //
        tbl_user.getColumnModel().getColumn(2).setPreferredWidth(180);   // fullname //
        tbl_user.getColumnModel().getColumn(3).setPreferredWidth(270);  // address //
        tbl_user.getColumnModel().getColumn(4).setPreferredWidth(130);  // city //
        tbl_user.getColumnModel().getColumn(5).setPreferredWidth(80);  // zipcode //
        tbl_user.getColumnModel().getColumn(6).setPreferredWidth(100);  // phone //
        tbl_user.getColumnModel().getColumn(7).setPreferredWidth(120);  // mobile //
        tbl_user.getColumnModel().getColumn(8).setPreferredWidth(80);  // role //
        
                        
        EditDeleteRenderer edit_delete_renderer = new EditDeleteRenderer();
        tbl_user.getColumnModel().getColumn(0).setCellRenderer(edit_delete_renderer);
        tbl_user.getColumnModel().getColumn(0).setCellEditor(new EditdeleteEditor());
        tbl_user.setRowHeight(edit_delete_renderer.getTableCellRendererComponent(tbl_user, null, true, true, 0, 0).getPreferredSize().height);  
                
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<UserItems> user_items = sql_connection.getUserData();
                             
        int columns = user_items.size();                      
        for (int x = 0; x < columns; x++){                            
            user_table_model.add(user_items.get(x));
        }        
    }
    
    public class UserTableModel extends AbstractTableModel {
        private ArrayList<UserItems> items;

        public UserTableModel() {
            items = new ArrayList<>();
        }

        @Override
        public String getColumnName(int column) {
            String value = null;
            switch (column) {
                case 0:
                    value = "Action";
                    break;               
                case 1:
                    value = "Username";
                    break;
                case 2:
                    value = "Fullname";
                    break;                
                case 3:
                    value = "Address";
                    break;
                case 4:
                    value = "City";
                    break;
                case 5:
                    value = "ZipCode";
                    break;
                case 6:
                    value = "Phone";
                    break;
                case 7:
                    value = "Mobile";
                    break;
                case 8:
                    value = "Role";
                    break;                
            }
            return value;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class value = Object.class;
            switch (columnIndex) {               
                case 1:
                    // username //
                    value = String.class;
                    break;
                case 2:
                    // fullname //
                    value = String.class;
                    break;                
                case 3:
                    // address //
                    value = String.class;
                    break;
                case 4:
                    // city //
                    value = String.class;
                    break;
                case 5:
                    // zipcode //
                    value = String.class;
                    break;
                case 6:
                    // phone //
                    value = String.class;
                    break;
                case 7:
                    // mobile //
                    value = String.class;
                    break;
                case 8:
                    // role //
                    value = String.class;
                    break;                
            }
            return value;
        }

        @Override
        public int getRowCount() {
            return items.size();
        }

        @Override
        public int getColumnCount() {
            return 9;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            UserItems obj = items.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:                    
                    break;                
                case 1:
                    value = obj.username;
                    break;
                case 2:
                    value = obj.fullname;
                    break;                
                case 3:
                    value = obj.address;
                    break;
                case 4:
                    value = obj.city;
                    break;
                case 5:
                    value = obj.zipcode;
                    break;
                case 6:
                    value = obj.phone;
                    break;
                case 7:
                    value = obj.mobile;
                    break;
                case 8:
                    value = obj.user_type;
                    break;               
            }
            return value;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                UserItems value = items.get(rowIndex);
                if ("edit".equals(aValue)) {
                    edit(value);                    
                } else {
                    remove(value);                            
                }
            }
        }

        public void add(UserItems value) {
            int startIndex = getRowCount();
            items.add(value);
            fireTableRowsInserted(startIndex, getRowCount() - 1);
        }

        public void remove(UserItems value) {
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.RemoveUser(value);
            if(isSuccess){
                int startIndex = items.indexOf(value);                
                items.remove(value);
                fireTableRowsInserted(startIndex, startIndex);
                
                if(isUpdate){
                    // Reset text input //
                    txt_username.setText("");
                    txt_fullname.setText("");
                    txt_password.setText("");
                    txt_address.setText("");
                    txt_city.setText("");
                    txt_zipcode.setText("");
                    txt_phone.setText("");
                    txt_mobile_phone.setText("");
                    txt_email.setText("");

                    // Reset picture //
                    lbl_picture.setIcon(null);
                    lbl_picture.revalidate();
                    
                    // Reset button //
                    btn_add_user.setText("Save");
                    btn_add_user.setEnabled(false);
                    btn_cancel.setEnabled(false); 
                    
                    isUpdate = false;
                }else{
                    // Reset text input //
                    txt_username.setText("");
                    txt_fullname.setText("");
                    txt_password.setText("");
                    txt_address.setText("");
                    txt_city.setText("");
                    txt_zipcode.setText("");
                    txt_phone.setText("");
                    txt_mobile_phone.setText("");
                    txt_email.setText("");

                    // Reset picture //
                    lbl_picture.setIcon(null);
                    lbl_picture.revalidate();
                    
                    // Reset button //
                    btn_add_user.setText("Save");
                    btn_add_user.setEnabled(false);
                    btn_cancel.setEnabled(false);                   
                }                
            }    
        }
        
        public void edit(UserItems value){
            isUpdate = true;
            user_id = value.user_id;
            txt_username.setText(value.username);
            txt_fullname.setText(value.fullname);
            txt_password.setText(value.password);
            txt_address.setText(value.address);
            txt_city.setText(value.city);
            txt_zipcode.setText(value.zipcode);
            txt_phone.setText(value.phone);
            txt_mobile_phone.setText(value.mobile);
            txt_email.setText(value.email);
            
            if(value.user_picture != null){
                ImageIcon icon = rescaleImageFromDB(value.user_picture, 105, 160);                
                lbl_picture.setIcon(icon); 
            }                    
                                                 
            btn_add_user.setText("Update");
            btn_add_user.setEnabled(true);
            btn_cancel.setEnabled(true);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }
    }
    
    public class EditDeletePane extends JPanel {

        private JButton edit;
        private JButton delete;
        private String state;

        public EditDeletePane() {
            setLayout(new GridBagLayout());
            edit = new JButton("Edit");
            edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_edit_small.png")));
            edit.setActionCommand("edit");
            
            delete = new JButton("Delete");
            delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_delete_small.png")));
            delete.setActionCommand("delete");

            add(edit);
            add(delete);

            ActionListener listener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    state = e.getActionCommand();
                }
            };

            edit.addActionListener(listener);
            delete.addActionListener(listener);
        }

        public void addActionListener(ActionListener listener) {
            edit.addActionListener(listener);
            delete.addActionListener(listener);
        }

        public String getState() {
            return state;
        }
    }
    
    public class EditDeleteRenderer extends DefaultTableCellRenderer {

        private EditDeletePane edit_delete_pane;

        public EditDeleteRenderer() {
            edit_delete_pane = new EditDeletePane();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                edit_delete_pane.setBackground(table.getSelectionBackground());
            } else {
                edit_delete_pane.setBackground(table.getBackground());
            }
            return edit_delete_pane;
        }
    }
    
    public class EditdeleteEditor extends AbstractCellEditor implements TableCellEditor {

        private EditDeletePane edit_delete_pane;

        public EditdeleteEditor() {
            edit_delete_pane = new EditDeletePane();
            edit_delete_pane.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            stopCellEditing();
                        }
                    });
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return edit_delete_pane.getState();
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                edit_delete_pane.setBackground(table.getSelectionBackground());
            } else {
                edit_delete_pane.setBackground(table.getBackground());
            }
            return edit_delete_pane;
        }
    }
    
    /**
     * Open File Image 
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public String OpenImageFile() throws IOException, ClassNotFoundException, SQLException{         
        File file = null;
        try {
            
            JFileChooser c = new JFileChooser(); 
            c.setAcceptAllFileFilterUsed(false);
            
            FileNameExtensionFilter JPEGfilter = new FileNameExtensionFilter("JPEG File", "jpg", "jpg");
            FileNameExtensionFilter PNGfilter = new FileNameExtensionFilter("PNG File", "png", "png");
            c.setFileFilter(JPEGfilter);
            c.setFileFilter(PNGfilter);
			
            if (c.showOpenDialog(UserPanel.this) == JFileChooser.APPROVE_OPTION) {
                String ext = ((FileNameExtensionFilter)c.getFileFilter()).getExtensions()[0];
                file = c.getSelectedFile();                                
                
                // JOptionPane.showMessageDialog(null, "Data berhasil disimpan di : "+file);                
            }else if(c.showOpenDialog(UserPanel.this) == JFileChooser.CANCEL_OPTION) {                                
                // Close JFileChooser //
            }                                                                                                       
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "internal error : "+e.toString());                        
        }
        
        return file.getAbsolutePath();
    }
    
    /**
     * getByteArray value from file 
     * @param filePath
     * @return 
     */
    private byte[] getByteArrayFromFile(String filePath){
        byte[] result = null;
        FileInputStream file_input =null;
        try{
            File imgFile = new File(filePath);
            file_input = new FileInputStream(imgFile);
            long imageSize = imgFile.length();
            
            if(imageSize>Integer.MAX_VALUE){
                // image is too large //
                return null;    
            }
            
            if(imageSize>0){
                result=new byte[(int)imageSize];
                file_input.read(result);
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                file_input.close();
            } catch (Exception e) {
            }
        }
        
        return result;
    }
    
    public ImageIcon rescaleImageFromDB(byte[] imageBytes, int maxHeight, int maxWidth)
    {
        int newHeight = 0, newWidth = 0;        // Variables for the new height and width
        int priorHeight = 0, priorWidth = 0;
        BufferedImage image = null;
        ImageIcon sizeImage;

        try {
            InputStream in = new ByteArrayInputStream(imageBytes);
            image = ImageIO.read(in);        // get the image
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Picture upload attempted & failed");
        }

        sizeImage = new ImageIcon(image);

        if(sizeImage != null)
        {
            priorHeight = sizeImage.getIconHeight(); 
            priorWidth = sizeImage.getIconWidth();
        }

        // Calculate the correct new height and width
        if((float)priorHeight/(float)priorWidth > (float)maxHeight/(float)maxWidth)
        {
            newHeight = maxHeight;
            newWidth = (int)(((float)priorWidth/(float)priorHeight)*(float)newHeight);
        }
        else 
        {
            newWidth = maxWidth;
            newHeight = (int)(((float)priorHeight/(float)priorWidth)*(float)newWidth);
        }

        // Resize the image

        // 1. Create a new Buffered Image and Graphic2D object
        BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();

        // 2. Use the Graphic object to draw a new image to the image in the buffer
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2.dispose();

        // 3. Convert the buffered image into an ImageIcon for return
        return (new ImageIcon(resizedImg));
    }
    
    /**
     * Rescale Image User 
     * @param source
     * @param maxHeight
     * @param maxWidth
     * @return 
     */
    public ImageIcon rescaleImage(File source,int maxHeight, int maxWidth)
    {
        int newHeight = 0, newWidth = 0;        // Variables for the new height and width
        int priorHeight = 0, priorWidth = 0;
        BufferedImage image = null;
        ImageIcon sizeImage;

        try {
            image = ImageIO.read(source);        // get the image
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Picture upload attempted & failed");
        }

        sizeImage = new ImageIcon(image);

        if(sizeImage != null)
        {
            priorHeight = sizeImage.getIconHeight(); 
            priorWidth = sizeImage.getIconWidth();
        }

        // Calculate the correct new height and width
        if((float)priorHeight/(float)priorWidth > (float)maxHeight/(float)maxWidth)
        {
            newHeight = maxHeight;
            newWidth = (int)(((float)priorWidth/(float)priorHeight)*(float)newHeight);
        }
        else 
        {
            newWidth = maxWidth;
            newHeight = (int)(((float)priorHeight/(float)priorWidth)*(float)newWidth);
        }

        // Resize the image

        // 1. Create a new Buffered Image and Graphic2D object
        BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();

        // 2. Use the Graphic object to draw a new image to the image in the buffer
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2.dispose();

        // 3. Convert the buffered image into an ImageIcon for return
        return (new ImageIcon(resizedImg));
    }
    
    /**
     * getMD5 Hex String for password
     * @param inputString
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public static String getMD5Hex(final String inputString) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(inputString.getBytes());
        byte[] digest = md.digest();

        return convertByteToHex(digest);
    }
    
    private static String convertByteToHex(byte[] byteData) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_user = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lbl_username = new javax.swing.JLabel();
        lbl_fullname = new javax.swing.JLabel();
        lbl_address = new javax.swing.JLabel();
        lbl_city = new javax.swing.JLabel();
        lbl_zipcode = new javax.swing.JLabel();
        lbl_phone = new javax.swing.JLabel();
        lbl_mobile_phone = new javax.swing.JLabel();
        lbl_email = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lbl_picture = new javax.swing.JLabel();
        txt_username = new javax.swing.JTextField();
        txt_fullname = new javax.swing.JTextField();
        txt_address = new javax.swing.JTextField();
        txt_city = new javax.swing.JTextField();
        txt_zipcode = new javax.swing.JTextField();
        txt_phone = new javax.swing.JTextField();
        txt_mobile_phone = new javax.swing.JTextField();
        txt_email = new javax.swing.JTextField();
        btn_add_user = new javax.swing.JButton();
        btn_change_user_picture = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();
        lbl_password = new javax.swing.JLabel();
        txt_password = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "User data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        tbl_user.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tbl_user);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "User Profile", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        lbl_username.setText("Username : ");

        lbl_fullname.setText("Full name  :");

        lbl_address.setText("Address : ");

        lbl_city.setText("City : ");

        lbl_zipcode.setText("Zipcode : ");

        lbl_phone.setText("Phone : ");

        lbl_mobile_phone.setText("Mobile Phone : ");

        lbl_email.setText("Email : ");

        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_picture, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_picture, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );

        txt_username.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_usernameKeyReleased(evt);
            }
        });

        txt_fullname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_fullnameKeyReleased(evt);
            }
        });

        txt_mobile_phone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_mobile_phoneKeyReleased(evt);
            }
        });

        btn_add_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_add_member_small.png"))); // NOI18N
        btn_add_user.setText("Save");
        btn_add_user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_userActionPerformed(evt);
            }
        });

        btn_change_user_picture.setText("change picture");
        btn_change_user_picture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_change_user_pictureActionPerformed(evt);
            }
        });

        btn_cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel.setText("Cancel");
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });

        lbl_password.setText("Password : ");

        txt_password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_passwordKeyReleased(evt);
            }
        });

        jLabel1.setText("*");

        jLabel2.setText("*");

        jLabel3.setText("*");

        jLabel4.setText("*");

        jLabel5.setText("note : * mandatory field");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_fullname, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_address, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(167, 167, 167))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lbl_password, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbl_username, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbl_city, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_zipcode, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txt_fullname, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_address)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_zipcode, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_city, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(8, 8, 8))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbl_mobile_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(txt_mobile_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lbl_email, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_add_user, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_cancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(btn_change_user_picture, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_change_user_picture))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_username)
                            .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_password)
                            .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_fullname)
                            .addComponent(txt_fullname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_address, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_address))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_city)
                            .addComponent(txt_city, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_zipcode)
                            .addComponent(txt_zipcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_phone)
                            .addComponent(txt_phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_mobile_phone)
                            .addComponent(txt_mobile_phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_email)
                            .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_add_user)
                    .addComponent(btn_cancel)
                    .addComponent(jLabel5))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_add_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_userActionPerformed
        if(isUpdate){
            try{
                items = new UserItems();
                items.setUser_id(user_id);
                items.setUsername(txt_username.getText());
                items.setFullname(txt_fullname.getText());
                items.setPassword(getMD5Hex(txt_password.getText()));
                items.setAddress(txt_address.getText());
                items.setCity(txt_city.getText());
                items.setZipcode(txt_zipcode.getText());
                items.setPhone(txt_phone.getText());
                items.setMobile(txt_mobile_phone.getText());
                items.setEmail(txt_email.getText());
                items.setUser_picture(getByteArrayFromFile(path_image_file));
                items.setUser_type("operator");

                ConnectSQLLite sql_connection = new ConnectSQLLite();
                boolean isSuccess = sql_connection.UpdateUser(items);
                if(isSuccess){
                    Reload_Table();

                    // Reset text input //
                    txt_username.setText("");
                    txt_fullname.setText("");
                    txt_password.setText("");
                    txt_address.setText("");
                    txt_city.setText("");
                    txt_zipcode.setText("");
                    txt_phone.setText("");
                    txt_mobile_phone.setText("");
                    txt_email.setText("");

                    // Reset picture //
                    lbl_picture.setIcon(null);
                    lbl_picture.revalidate();

                    // Reset button //
                    btn_add_user.setText("Save");
                    btn_add_user.setEnabled(false);
                    btn_cancel.setEnabled(false);

                    isUpdate = false;
                }  
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Failed to update user data : " + e.toString());
                e.printStackTrace();
            }
        }else{
            try{                
                items = new UserItems();                
                // Check if user has picture //
                if(path_image_file.length() > 1){
                    items.setUser_picture(getByteArrayFromFile(path_image_file));
                }                
                items.setUsername(txt_username.getText());
                items.setFullname(txt_fullname.getText());
                items.setPassword(getMD5Hex(txt_password.getText()));
                items.setAddress(txt_address.getText());
                items.setCity(txt_city.getText());
                items.setZipcode(txt_zipcode.getText());
                items.setPhone(txt_phone.getText());
                items.setMobile(txt_mobile_phone.getText());
                items.setEmail(txt_email.getText());
                
                items.setUser_type("operator");

                ConnectSQLLite sql_connection = new ConnectSQLLite();
                boolean isSuccess = sql_connection.InsertUser(items);
                if(isSuccess){
                    Reload_Table();                    

                    // Reset text input //
                    txt_username.setText("");
                    txt_fullname.setText("");
                    txt_password.setText("");
                    txt_address.setText("");
                    txt_city.setText("");
                    txt_zipcode.setText("");
                    txt_phone.setText("");
                    txt_mobile_phone.setText("");
                    txt_email.setText("");
                    
                    // Reset picture //
                    lbl_picture.setIcon(null);
                    lbl_picture.revalidate();

                    // Reset button //                
                    btn_add_user.setEnabled(false);
                    btn_cancel.setEnabled(false);

                    isUpdate = false;
                }   
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Failed to save user data : " + e.toString());
                e.printStackTrace();
            }             
        }
    }//GEN-LAST:event_btn_add_userActionPerformed

    private void btn_change_user_pictureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_change_user_pictureActionPerformed
        try{
            path_image_file = OpenImageFile();
            if(!path_image_file.isEmpty()){                                                
                ImageIcon icon = rescaleImage(new File(path_image_file), 105, 160);                
                lbl_picture.setIcon(icon);                
            }            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "internal error : "+e.toString());
        }        
    }//GEN-LAST:event_btn_change_user_pictureActionPerformed

    private void txt_usernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_usernameKeyReleased
        if(txt_username.getText().length() < 1 ||
           txt_fullname.getText().length() < 1 ||
           txt_password.getText().length() < 1 ||           
           txt_mobile_phone.getText().length() < 1){
           
            btn_add_user.setEnabled(false);
            btn_cancel.setEnabled(false);
        }else{
            btn_add_user.setEnabled(true);
            btn_cancel.setEnabled(true);
        }
    }//GEN-LAST:event_txt_usernameKeyReleased

    private void txt_passwordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_passwordKeyReleased
        if(txt_username.getText().length() < 1 ||
           txt_fullname.getText().length() < 1 ||
           txt_password.getText().length() < 1 ||           
           txt_mobile_phone.getText().length() < 1){
           
            btn_add_user.setEnabled(false);
            btn_cancel.setEnabled(false);
        }else{
            btn_add_user.setEnabled(true);
            btn_cancel.setEnabled(true);
        }
    }//GEN-LAST:event_txt_passwordKeyReleased

    private void txt_fullnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_fullnameKeyReleased
        if(txt_username.getText().length() < 1 ||
           txt_fullname.getText().length() < 1 ||
           txt_password.getText().length() < 1 ||           
           txt_mobile_phone.getText().length() < 1){
           
            btn_add_user.setEnabled(false);
            btn_cancel.setEnabled(false);
        }else{
            btn_add_user.setEnabled(true);
            btn_cancel.setEnabled(true);
        }
    }//GEN-LAST:event_txt_fullnameKeyReleased

    private void txt_mobile_phoneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_mobile_phoneKeyReleased
        if(txt_username.getText().length() < 1 ||
           txt_fullname.getText().length() < 1 ||
           txt_password.getText().length() < 1 ||           
           txt_mobile_phone.getText().length() < 1){
           
            btn_add_user.setEnabled(false);
            btn_cancel.setEnabled(false);
        }else{
            btn_add_user.setEnabled(true);
            btn_cancel.setEnabled(true);
        }
    }//GEN-LAST:event_txt_mobile_phoneKeyReleased

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        if(isUpdate){
            isUpdate = false;
            
            // Reset text input //
            txt_username.setText("");
            txt_fullname.setText("");
            txt_password.setText("");
            txt_address.setText("");
            txt_city.setText("");
            txt_zipcode.setText("");
            txt_phone.setText("");
            txt_mobile_phone.setText("");
            txt_email.setText("");
            
            // Reset picture //
            lbl_picture.setIcon(null);
            lbl_picture.revalidate();

            // Reset button //     
            btn_add_user.setText("Save");
            btn_add_user.setEnabled(false);
            btn_cancel.setEnabled(false);
        }else{
            
            // Reset text input //
            txt_username.setText("");
            txt_fullname.setText("");
            txt_password.setText("");
            txt_address.setText("");
            txt_city.setText("");
            txt_zipcode.setText("");
            txt_phone.setText("");
            txt_mobile_phone.setText("");
            txt_email.setText("");
                        
            // Reset picture //
            lbl_picture.setIcon(null);
            lbl_picture.revalidate();

            // Reset button //                 
            btn_add_user.setEnabled(false);
            btn_cancel.setEnabled(false);
        }      
    }//GEN-LAST:event_btn_cancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_user;
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_change_user_picture;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_address;
    private javax.swing.JLabel lbl_city;
    private javax.swing.JLabel lbl_email;
    private javax.swing.JLabel lbl_fullname;
    private javax.swing.JLabel lbl_mobile_phone;
    private javax.swing.JLabel lbl_password;
    private javax.swing.JLabel lbl_phone;
    private javax.swing.JLabel lbl_picture;
    private javax.swing.JLabel lbl_username;
    private javax.swing.JLabel lbl_zipcode;
    private javax.swing.JTable tbl_user;
    private javax.swing.JTextField txt_address;
    private javax.swing.JTextField txt_city;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_fullname;
    private javax.swing.JTextField txt_mobile_phone;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_phone;
    private javax.swing.JTextField txt_username;
    private javax.swing.JTextField txt_zipcode;
    // End of variables declaration//GEN-END:variables
}
