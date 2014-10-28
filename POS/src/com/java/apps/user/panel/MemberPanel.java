/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.java.apps.user.panel;

import com.java.apps.database.ConnectSQLLite;
import com.java.apps.pojo.AttachmentItem;
import com.java.apps.pojo.BankItems;
import com.java.apps.pojo.CardMemberItems;
import com.java.apps.pojo.MemberItems;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Ultimate
 */
public class MemberPanel extends javax.swing.JPanel {
    
    private MemberTableModel member_table_model = new MemberTableModel(); 
    private MemberCardTableModel card_table_model = new MemberCardTableModel();
    private MemberAttachmentTableModel attacment_table_model = new MemberAttachmentTableModel();
    private DefaultTableModel search_member_model = new DefaultTableModel();
    private MemberItems items;
    
    private boolean isUpdate = false, isEdit = false;    
    
        
    /**
     * Creates new form MemberPanel
     */
    public MemberPanel() {
        initComponents();
                       
        // Set property component //
        btn_save.setEnabled(false);
        btn_cancel.setEnabled(false);
        btn_add_card.setEnabled(false);
        btn_add_attachment.setEnabled(false);
        
        cbo_search_by.setSelectedIndex(0);
        txt_member_name.setEnabled(false);
        txt_citizenship_id.setEnabled(false);
        btn_search_member.setEnabled(false);
                                                               
        // Citizenship, zipcode, mobile only allow number //                
        txt_citizenship.addKeyListener(new KeyAdapterNumbersOnly());        
        txt_mobile.addKeyListener(new KeyAdapterNumbersOnly());
        txt_card_number.addKeyListener(new KeyAdapterNumbersOnly());
        
        AbstractDocument ad_txt_min_limit = (AbstractDocument)txt_min_limit.getDocument();
        ad_txt_min_limit.setDocumentFilter(new FieldListener());
        
        AbstractDocument ad_txt_max_limit = (AbstractDocument)txt_max_limit.getDocument();
        ad_txt_min_limit.setDocumentFilter(new FieldListener());
                                                                               
        // Reload Table //
        ReloadTable();
        LoadTableCard();
        LoadTableAttachment();
                
        // Populate Combobox Card //
        PopulateCard();                       
    }
    
    private void PopulateCard(){
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<BankItems> bank_items = sql_connection.getCardData();
        
        cbo_card.addItem("-- select card --");
        for (BankItems item : bank_items){
            cbo_card.addItem(item.card_name);
        }   
    }
    
    private void LoadTableCard(){
        // Insert member data model into table member //
        card_table_model = new MemberCardTableModel();
        tbl_card_member.setModel(card_table_model);
        tbl_card_member.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_card_member.getColumnModel().getColumn(0).setPreferredWidth(35);  // action //
        tbl_card_member.getColumnModel().getColumn(1).setPreferredWidth(120);  // card id //
        tbl_card_member.getColumnModel().getColumn(2).setPreferredWidth(140);  // card name //
        tbl_card_member.getColumnModel().getColumn(3).setPreferredWidth(120);  // card type //              
        tbl_card_member.getColumnModel().getColumn(4).setPreferredWidth(120);  // expired //
        tbl_card_member.getColumnModel().getColumn(5).setPreferredWidth(120);  // min limit //
        tbl_card_member.getColumnModel().getColumn(6).setPreferredWidth(120);  // max limit //
                        
        DeleteRenderer delete_renderer = new DeleteRenderer();
        tbl_card_member.getColumnModel().getColumn(0).setCellRenderer(delete_renderer);
        tbl_card_member.getColumnModel().getColumn(0).setCellEditor(new DeleteEditor());
        tbl_card_member.setRowHeight(delete_renderer.getTableCellRendererComponent(tbl_card_member, null, true, true, 0, 0).getPreferredSize().height);                         
    }
    
    private void LoadTableAttachment(){        
        attacment_table_model = new MemberAttachmentTableModel();
        tbl_attachment.setModel(attacment_table_model);
        tbl_attachment.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_attachment.getColumnModel().getColumn(0).setPreferredWidth(35);   // action //
        tbl_attachment.getColumnModel().getColumn(1).setPreferredWidth(180);  // name //
        tbl_attachment.getColumnModel().getColumn(2).setPreferredWidth(80);  // type //              
                        
        DeleteRenderer delete_renderer = new DeleteRenderer();
        tbl_attachment.getColumnModel().getColumn(0).setCellRenderer(delete_renderer);
        tbl_attachment.getColumnModel().getColumn(0).setCellEditor(new DeleteEditor());
        tbl_attachment.setRowHeight(delete_renderer.getTableCellRendererComponent(tbl_attachment, null, true, true, 0, 0).getPreferredSize().height);                         
    }
            
    private void ReloadTable(){
        // Insert member data model into table member //
        member_table_model = new MemberTableModel();
        tbl_member_data.setModel(member_table_model);
        tbl_member_data.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_member_data.getColumnModel().getColumn(0).setPreferredWidth(160);  // action //
        tbl_member_data.getColumnModel().getColumn(1).setPreferredWidth(150);   // memberid //
        tbl_member_data.getColumnModel().getColumn(2).setPreferredWidth(180);  // fullname //        
        tbl_member_data.getColumnModel().getColumn(3).setPreferredWidth(270);  // address //
        tbl_member_data.getColumnModel().getColumn(4).setPreferredWidth(120);  // mobile //
        tbl_member_data.getColumnModel().getColumn(5).setPreferredWidth(200);  // email //        
        tbl_member_data.getColumnModel().getColumn(6).setPreferredWidth(180);  // registered //
        tbl_member_data.getColumnModel().getColumn(7).setPreferredWidth(180);  // updated //        
                        
        EditDeleteRenderer edit_delete_renderer = new EditDeleteRenderer();
        tbl_member_data.getColumnModel().getColumn(0).setCellRenderer(edit_delete_renderer);
        tbl_member_data.getColumnModel().getColumn(0).setCellEditor(new EditdeleteEditor());
        tbl_member_data.setRowHeight(edit_delete_renderer.getTableCellRendererComponent(tbl_member_data, null, true, true, 0, 0).getPreferredSize().height);  
                
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<MemberItems> member_items = sql_connection.getMemberData();
                
        int columns = member_items.size();                      
        for (int x = 0; x < columns; x++){                            
            member_table_model.add(member_items.get(x));
        } 
    }
    
    private void ReloadSearchMemberTable(){
        search_member_model = new DefaultTableModel(){
            Class[] types = new Class[]{                                
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class,
                java.lang.String.class,                
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
            };    
                
            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        
        search_member_model.addColumn("Citizenship");
        search_member_model.addColumn("Fullname");        
        search_member_model.addColumn("Address");
        search_member_model.addColumn("City");
        search_member_model.addColumn("Zipcode");
        search_member_model.addColumn("Mobile");
        search_member_model.addColumn("Email");
        search_member_model.addColumn("Registered");
        search_member_model.addColumn("Updated");
                
        tbl_search_result.setModel(search_member_model);
        tbl_search_result.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                
        tbl_search_result.getColumnModel().getColumn(0).setPreferredWidth(150);  // citizen number //
        tbl_search_result.getColumnModel().getColumn(1).setPreferredWidth(180);  // fullname //        
        tbl_search_result.getColumnModel().getColumn(2).setPreferredWidth(270);  // address //
        tbl_search_result.getColumnModel().getColumn(3).setPreferredWidth(130);  // city //
        tbl_search_result.getColumnModel().getColumn(4).setPreferredWidth(80);   // zipcode //
        tbl_search_result.getColumnModel().getColumn(5).setPreferredWidth(120);  // mobile //
        tbl_search_result.getColumnModel().getColumn(6).setPreferredWidth(200);  // email //        
        tbl_search_result.getColumnModel().getColumn(7).setPreferredWidth(180);  // registered //
        tbl_search_result.getColumnModel().getColumn(8).setPreferredWidth(180);  // updated //  
        
        
        if(cbo_search_by.getSelectedIndex() == 1){
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            ArrayList<MemberItems> member_items = sql_connection.SearchMemberByCitizenship(txt_citizenship_id.getText());
            
            for (MemberItems item : member_items){
            search_member_model.addRow(new Object[]{item.citizenship,
                                                    item.fullname, 
                                                    item.address,
                                                    item.city,
                                                    item.zipcode,
                                                    item.mobile,
                                                    item.email,
                                                    item.registered,
                                                    item.updated});
            } 
            
        }else if(cbo_search_by.getSelectedIndex() == 2){
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            ArrayList<MemberItems> member_items = sql_connection.SearchMemberByName(txt_member_name.getText());
            
            for (MemberItems item : member_items){
            search_member_model.addRow(new Object[]{item.citizenship,
                                                    item.fullname, 
                                                    item.address,
                                                    item.city,
                                                    item.zipcode,
                                                    item.mobile,
                                                    item.email,
                                                    item.registered,
                                                    item.updated});
            } 
            
        }else if(cbo_search_by.getSelectedIndex() == 3){
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            ArrayList<MemberItems> member_items = sql_connection.SearchMemberByNameAndCitizenship(txt_member_name.getText(), txt_citizenship_id.getText());
            
            for (MemberItems item : member_items){
            search_member_model.addRow(new Object[]{item.citizenship,
                                                    item.fullname, 
                                                    item.address,
                                                    item.city,
                                                    item.zipcode,
                                                    item.mobile,
                                                    item.email,
                                                    item.registered,
                                                    item.updated});
            }    
        }                                                    
    }
    
    public class MemberTableModel extends AbstractTableModel {
        private ArrayList<MemberItems> items;

        public MemberTableModel() {
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
                    value = "Citizenship Number";
                    break; 
                case 2:
                    value = "Fullname";
                    break;                               
                case 3:
                    value = "Address";
                    break;                            
                case 4:
                    value = "Mobile";
                    break;
                case 5:
                    value = "Email";
                    break;
                case 6:
                    value = "Registered";
                    break;
                case 7:
                    value = "Updated";
                    break;                                
            }
            return value;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class value = Object.class;
            switch (columnIndex) {
                case 1:
                    // citizen number //
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
                    // mobile //
                    value = String.class;
                    break;
                case 5:
                    // email //
                    value = String.class;
                    break;                
                case 6:
                    // registered //
                    value = String.class;
                    break; 
                case 7:
                    // updated //
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
            return 8;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            MemberItems obj = items.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:                      
                    break;
                case 1:
                    value = obj.citizenship;
                    break;   
                case 2:
                    value = obj.fullname;
                    break;                             
                case 3:
                    value = obj.address;
                    break;                
                case 4:
                    value = obj.mobile;
                    break;
                case 5:
                    value = obj.email;
                    break;
                case 6:
                    value = obj.registered;
                    break;
                case 7:  
                    value = obj.updated;
                    break;                                
            }
            return value;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                MemberItems value = items.get(rowIndex);
                if ("edit".equals(aValue)) {
                    edit(value);                                       
                } else {
                    remove(value);                            
                }
            }
        }

        public void add(MemberItems value) {            
            int startIndex = getRowCount();
            items.add(value);
            fireTableRowsInserted(startIndex, getRowCount() - 1);            
        }
                
        public void remove(MemberItems value) {            
            // Remove Member //
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.RemoveMember(value);
            if(isSuccess){
                
                // Remove Cards //
                isSuccess = sql_connection.RemoveCardsMember(value.citizenship);
                if(isSuccess){
                    
                    // Remove Attachments //
                    isSuccess = sql_connection.RemoveAttachmentsMember(value.citizenship);
                    if(isSuccess){
                        int startIndex = items.indexOf(value);                
                        items.remove(value);
                        fireTableRowsInserted(startIndex, startIndex);

                        if(isUpdate){
                            
                            // Reset Table //
                            LoadTableCard();
                            LoadTableAttachment();
                                                        
                            txt_fullname.setText("");
                            txt_citizenship.setText("");
                            txt_address.setText("");                            
                            txt_mobile.setText("");

                            // Reset button //
                            btn_save.setText("Save");
                            btn_save.setEnabled(false);
                            btn_cancel.setEnabled(false);
                            btn_add_card.setEnabled(false);
                            btn_add_attachment.setEnabled(false);

                            isUpdate = false;
                        }else{
                            
                            // Reset Table //
                            LoadTableCard();
                            LoadTableAttachment();
                            
                            txt_fullname.setText("");
                            txt_citizenship.setText("");
                            txt_address.setText("");                            
                            txt_mobile.setText("");

                            // Reset button //            
                            btn_save.setEnabled(false);
                            btn_cancel.setEnabled(false);
                            btn_add_card.setEnabled(false);
                            btn_add_attachment.setEnabled(false);
                        } 
                    }                            
                }                                           
            }               
        }
        
        public void edit(MemberItems value){                        
            isUpdate = true;            
            txt_fullname.setText(value.fullname);
            txt_citizenship.setText(Integer.toString(value.citizenship));            
            txt_address.setText(value.address);
            txt_mobile.setText(value.mobile);
            txt_email.setText(value.email);
            
            // Reset table card member //
            LoadTableCard();
                                            
            // Reset table attachment //
            LoadTableAttachment();
            
            // Get card member from database //
            ConnectSQLLite sql_connect = new ConnectSQLLite();
            ArrayList<BankItems> card_items = sql_connect.getCardMemberData(value.citizenship);
            
            // Add to table card member //
            for(BankItems item : card_items){
                ((MemberCardTableModel)tbl_card_member.getModel()).add(item);                 
            }
                        
            // Get attachment member from database //
            ArrayList<AttachmentItem> attachments = sql_connect.getAttachmentsMemberData(value.citizenship);
            
            // Add to table attachment member //
            for(AttachmentItem item : attachments){
                ((MemberAttachmentTableModel)tbl_attachment.getModel()).add(item);
            }
                                                                                                                                       
            btn_save.setText("Update");
            btn_save.setEnabled(true);
            btn_cancel.setEnabled(true); 
            btn_add_card.setEnabled(true);
            btn_add_attachment.setEnabled(true);
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
            // Button edit //
            edit = new JButton("edit");
            edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_edit_small.png")));
            edit.setActionCommand("edit");
            
            // Button remove //
            delete = new JButton("remove");
            delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_delete_small.png")));
            delete.setActionCommand("remove");

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
       
    public class MemberAttachmentTableModel extends AbstractTableModel {
        private ArrayList<AttachmentItem> items;

        public MemberAttachmentTableModel() {
            items = new ArrayList<>();
        }

        @Override
        public String getColumnName(int column) {
            String value = null;
            switch (column) {
                case 0:
                    value = "";
                    break;
                case 1:
                    value = "Name";
                    break;
                case 2:
                    value = "Type";
                    break;
                case 3:
                    value = "Path";
                    break;
            }
            return value;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class value = Object.class;
            switch (columnIndex) {                
                case 1:
                    // name //
                    value = String.class;
                    break;
                case 2:
                    // type //
                    value = String.class;
                    break;                                
                case 3:
                    // path //
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
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            AttachmentItem obj = items.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:                      
                    break;
                case 1:
                    value = obj.name;
                    break;
                case 2:
                    value = obj.type;
                    break;  
                case 3:
                    value = obj.path;
                    break;  
            }
            return value;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                AttachmentItem value = items.get(rowIndex);
                if ("remove".equals(aValue)) {
                    remove(value);                    
                } 
            }
        }

        public void add(AttachmentItem value) {
            // if is update, then remove or add directly affect database //
            // otherwise, wait until save button pressed
            if(isUpdate){
                int member_id = Integer.parseInt(txt_citizenship.getText());
                byte[] file = getByteArrayFromFile(value.path);
                ConnectSQLLite connect_sql = new ConnectSQLLite();                
                boolean isSuccess = connect_sql.InsertSingleAttachment(member_id, value, file);
                if(isSuccess){
                    // Update field update //
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Calendar calender = Calendar.getInstance();
                    connect_sql.InsertFieldUpdated(calender.getTime().toString(), Integer.parseInt(txt_citizenship.getText()));
                    
                    int startIndex = getRowCount();
                    items.add(value);
                    fireTableRowsInserted(startIndex, getRowCount() - 1); 
                    
                    ReloadTable();
                }
            }else{
                int startIndex = getRowCount();
                items.add(value);
                fireTableRowsInserted(startIndex, getRowCount() - 1); 
            }                       
        }
                
        public void remove(AttachmentItem value) {         
            // if is update, then remove or add directly affect database //
            // otherwise, wait until save button pressed
            if(isUpdate){
                ConnectSQLLite connect_sql = new ConnectSQLLite();                
                boolean isSuccess = connect_sql.RemoveSingleAttachmentMember(value);
                if(isSuccess){
                    // Update field update //
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Calendar calender = Calendar.getInstance();
                    connect_sql.InsertFieldUpdated(calender.getTime().toString(), Integer.parseInt(txt_citizenship.getText()));
                    
                    int startIndex = items.indexOf(value);                
                    items.remove(value);
                    fireTableRowsInserted(startIndex, startIndex); 
                    
                    ReloadTable();
                }
            }else{
                int startIndex = items.indexOf(value);                
                items.remove(value);
                fireTableRowsInserted(startIndex, startIndex);   
            }                                                
        }
                 
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }
    }
    
    public class MemberCardTableModel extends AbstractTableModel {
        private ArrayList<BankItems> items;

        public MemberCardTableModel() {
            items = new ArrayList<>();
        }
        
        @Override
        public String getColumnName(int column) {
            String value = null;
            switch (column) {
                case 0:
                    value = "";
                    break;
                case 1:
                    value = "Number";
                    break;
                case 2:
                    value = "Card Name";
                    break;
                case 3:
                    value = "Type";
                    break;  
                case 4:
                    value = "Expired";
                    break;  
                case 5:
                    value = "Min Limit";
                    break;  
                case 6:
                    value = "Max Limit";
                    break;                   
            }
            return value;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class value = Object.class;
            switch (columnIndex) {
                case 1:
                    // card id //
                    value = Integer.class;
                    break;
                case 2:
                    // card name //
                    value = String.class;
                    break;
                case 3:
                    // card type //
                    value = String.class;
                    break;  
                case 4:
                    // expired //
                    value = String.class;
                    break; 
                case 5:
                    // min limit //
                    value = String.class;
                    break; 
                case 6:
                    // max limit //
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
            return 7;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            BankItems obj = items.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:                      
                    break;
                case 1:
                    value = obj.card_id;
                    break;
                case 2:
                    value = obj.card_name;
                    break;
                case 3:
                    value = obj.card_type_name;
                    break;   
                case 4:
                    value = obj.expired;
                    break;   
                case 5:
                    value = obj.min_limit;
                    break;   
                case 6:
                    value = obj.max_limit;
                    break;   
            }
            return value;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                BankItems value = items.get(rowIndex);
                if ("remove".equals(aValue)) {
                    remove(value);                    
                } 
            }
        }

        
        public void insert(BankItems value){
            if(isUpdate){                
                CardMemberItems item = new CardMemberItems();
                item.setCard_id(value.card_id);
                item.setMember_id(Integer.parseInt(txt_citizenship.getText())); 
                item.setCard_number(Integer.parseInt(txt_card_number.getText()));
                
                SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                item.setExpired(formater.format(dt_card_expired.getDate()));
                item.setMin_limit(txt_min_limit.getText());
                item.setMax_limit(txt_max_limit.getText());
                ConnectSQLLite connect_sql = new ConnectSQLLite();                
                boolean isSuccess = connect_sql.InsertSingleCardMember(item);
                if(isSuccess){
                    
                    // Update field update //
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Calendar calender = Calendar.getInstance();
                    connect_sql.InsertFieldUpdated(calender.getTime().toString(), Integer.parseInt(txt_citizenship.getText()));
                    
                    int startIndex = getRowCount();
                    items.add(value);
                    fireTableRowsInserted(startIndex, getRowCount() - 1);
                    
                    dt_card_expired.getEditor().setText("");
                    txt_min_limit.setText("");
                    txt_max_limit.setText("");
                    txt_card_number.setText("");
                    cbo_card.setSelectedIndex(0);
                    
                    ReloadTable();
                }                
            }else{
                int startIndex = getRowCount();
                items.add(value);
                fireTableRowsInserted(startIndex, getRowCount() - 1); 
                
                dt_card_expired.getEditor().setText("");
                txt_min_limit.setText("");
                txt_max_limit.setText("");
                txt_card_number.setText("");
                cbo_card.setSelectedIndex(0);
            }    
        }
        
        public void add(BankItems value) { 
            // if is update, then add or remove directly to database //
            // otherwise, wait until save button pressed
            int startIndex = getRowCount();
            items.add(value);
            fireTableRowsInserted(startIndex, getRowCount() - 1);

            dt_card_expired.getEditor().setText("");
            txt_min_limit.setText("");
            txt_max_limit.setText("");
            cbo_card.setSelectedIndex(0);                  
        }
                
        public void remove(BankItems value) {
            // if is update, then add or remove directly to database //
            // otherwise, wait until save button pressed
            if(isUpdate){                
                CardMemberItems item = new CardMemberItems();
                item.setCard_id(value.card_id);
                item.setMember_id(Integer.parseInt(txt_citizenship.getText()));
                ConnectSQLLite connect_sql = new ConnectSQLLite();   
                boolean isSuccess = connect_sql.RemoveSingleCardMember(item);
                if(isSuccess){
                    // Update field update //
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Calendar calender = Calendar.getInstance();
                    connect_sql.InsertFieldUpdated(calender.getTime().toString(), Integer.parseInt(txt_citizenship.getText()));
                                        
                    int startIndex = items.indexOf(value);                
                    items.remove(value);
                    fireTableRowsInserted(startIndex, startIndex); 
                    
                    ReloadTable();
                }
            }else{
                int startIndex = items.indexOf(value);                
                items.remove(value);
                fireTableRowsInserted(startIndex, startIndex);   
            }                                                
        }
                 
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }
    }
    
    public class DeletePane extends JPanel {
        
        private JButton delete;
        private String state;

        public DeletePane() {
            setLayout(new GridBagLayout());
                        
            // Button remove //
            delete = new JButton();
            delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_delete_small.png")));
            delete.setActionCommand("remove");
            
            add(delete);

            ActionListener listener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    state = e.getActionCommand();
                }
            };
            
            delete.addActionListener(listener);
        }

        public void addActionListener(ActionListener listener) {            
            delete.addActionListener(listener);
        }

        public String getState() {
            return state;
        }
    }
    
    public class DeleteRenderer extends DefaultTableCellRenderer {

        private DeletePane delete_pane;

        public DeleteRenderer() {
            delete_pane = new DeletePane();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                delete_pane.setBackground(table.getSelectionBackground());
            } else {
                delete_pane.setBackground(table.getBackground());
            }
            return delete_pane;
        }
    }
    
    public class DeleteEditor extends AbstractCellEditor implements TableCellEditor {

        private DeletePane delete_pane;

        public DeleteEditor() {
            delete_pane = new DeletePane();
            delete_pane.addActionListener(new ActionListener() {
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
            return delete_pane.getState();
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                delete_pane.setBackground(table.getSelectionBackground());
            } else {
                delete_pane.setBackground(table.getBackground());
            }
            return delete_pane;
        }
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
    
    class FieldListener extends DocumentFilter{
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException  
        {
            try {
                if (string.equals(".") && !fb.getDocument().getText(0, fb.getDocument().getLength()).contains(".")) {
                    super.insertString(fb, offset, string, attr);
                    return;
                }
                Double.parseDouble(string);
                super.insertString(fb, offset, string, attr);
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }
        } 
                
        @Override  
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)throws BadLocationException 
        {  
             try {
                if (text.equals(".") && !fb.getDocument().getText(0, fb.getDocument().getLength()).contains(".")) {
                    super.insertString(fb, offset, text, attrs);
                    return;
                }
                Double.parseDouble(text);
                super.replace(fb, offset, length, text, attrs);
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }       
    
    public ArrayList<String> OpenFile() throws IOException, ClassNotFoundException, SQLException{         
        ArrayList<String> item = new ArrayList<>();
        File file = null;
        try {
            
            JFileChooser c = new JFileChooser(); 
            c.setAcceptAllFileFilterUsed(false);
            
            FileNameExtensionFilter JPEGfilter = new FileNameExtensionFilter("JPEG File", "jpg", "jpg");
            FileNameExtensionFilter PNGfilter = new FileNameExtensionFilter("PNG File", "png", "png");
            c.setFileFilter(JPEGfilter);
            c.setFileFilter(PNGfilter);
			
            if (c.showOpenDialog(MemberPanel.this) == JFileChooser.APPROVE_OPTION) {
                String ext = ((FileNameExtensionFilter)c.getFileFilter()).getExtensions()[0];
                file = c.getSelectedFile();
                
                // 1. file name
                // 2. file extension
                // 3. absolute path
                item.add(file.getName());
                item.add(ext);
                item.add(file.getAbsolutePath());
                
                // JOptionPane.showMessageDialog(null, "Data berhasil disimpan di : "+file);                
            }else if(c.showOpenDialog(MemberPanel.this) == JFileChooser.CANCEL_OPTION) {                                
                // Close JFileChooser //
            }                                                                                                       
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "internal error : "+e.toString());                        
        }
        
        return item;
    }
          
    /**
     * Get content in table card 
     * @param table
     * @return 
     */
    public ArrayList<CardMemberItems> getTableCardData (JTable table) {
        ArrayList<CardMemberItems> card_member = new ArrayList<>();           
        ConnectSQLLite connect_sql = new ConnectSQLLite();
        
        int nRow = ((MemberCardTableModel)table.getModel()).getRowCount(), 
            nCol = ((MemberCardTableModel)table.getModel()).getColumnCount();        
        
        for (int i = 0 ; i < nRow ; i++){
            CardMemberItems item = new CardMemberItems();
            item.member_id = Integer.parseInt(txt_citizenship.getText());            
            item.setCard_number(Integer.parseInt(((MemberCardTableModel)table.getModel()).getValueAt(i, 1).toString()));
            item.setCard_id(connect_sql.GetCardIDfromCardName(((MemberCardTableModel)table.getModel()).getValueAt(i, 2).toString()));
            item.setExpired(((MemberCardTableModel)table.getModel()).getValueAt(i, 4).toString());
            item.setMin_limit(((MemberCardTableModel)table.getModel()).getValueAt(i, 5).toString());
            item.setMax_limit(((MemberCardTableModel)table.getModel()).getValueAt(i, 6).toString());
            card_member.add(item);            
        }                            
        return card_member;
    }
    
    /**
     * Get content in table attachment
     * @param table
     * @return 
     */
    public ArrayList<AttachmentItem> getTableAttachmentData (JTable table) {
        ArrayList<AttachmentItem> attacments = new ArrayList<>();                                       
        int nRow = ((MemberAttachmentTableModel)table.getModel()).getRowCount(), 
            nCol = ((MemberAttachmentTableModel)table.getModel()).getColumnCount();        
        for (int i = 0 ; i < nRow ; i++){
            AttachmentItem item = new AttachmentItem();
            item.member_id = Integer.parseInt(txt_citizenship.getText());
            item.name = ((MemberAttachmentTableModel)table.getModel()).getValueAt(i, 1).toString();
            item.type = ((MemberAttachmentTableModel)table.getModel()).getValueAt(i, 2).toString();
            item.path = ((MemberAttachmentTableModel)table.getModel()).getValueAt(i, 3).toString();
            item.attachment = getByteArrayFromFile(((MemberAttachmentTableModel)table.getModel()).getValueAt(i, 3).toString());                        
            attacments.add(item);
        }                            
        return attacments;
    }
    
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
    
    public class KeyAdapterNumbersOnly extends KeyAdapter {

        /**
         * Regular expression which defines the allowed characters.
         */
        private String allowedRegex = "[^0-9]";

        /**
         * Key released on field.
         */
        public void keyReleased(KeyEvent e) {
            String curText = ((JTextComponent) e.getSource()).getText();
            curText = curText.replaceAll(allowedRegex, "");

            ((JTextComponent) e.getSource()).setText(curText);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_fullname = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_mobile = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_card_member = new javax.swing.JTable();
        btn_add_card = new javax.swing.JButton();
        cbo_card = new javax.swing.JComboBox();
        dt_card_expired = new org.jdesktop.swingx.JXDatePicker();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_min_limit = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        txt_max_limit = new javax.swing.JFormattedTextField();
        txt_card_number = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_attachment = new javax.swing.JTable();
        btn_add_attachment = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        txt_citizenship = new javax.swing.JFormattedTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_address = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_member_data = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        lbl_member_name = new javax.swing.JLabel();
        txt_member_name = new javax.swing.JTextField();
        btn_search_member = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cbo_search_by = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        txt_citizenship_id = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbl_search_result = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1150, 630));

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1150, 660));

        jPanel1.setPreferredSize(new java.awt.Dimension(1159, 601));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Member input", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jLabel2.setText("Fullname : ");

        txt_fullname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_fullnameKeyReleased(evt);
            }
        });

        jLabel3.setText("Citizenship Number : ");

        jLabel4.setText("Address : ");

        jLabel7.setText("Mobile Phone : ");

        txt_mobile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_mobileKeyReleased(evt);
            }
        });

        jLabel17.setText("Email : ");

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Card Data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        tbl_card_member.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tbl_card_member);

        btn_add_card.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_add_small.png"))); // NOI18N
        btn_add_card.setIconTextGap(9);
        btn_add_card.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_cardActionPerformed(evt);
            }
        });

        jLabel5.setText("Expired : ");

        jLabel6.setText("Card Type :");

        jLabel9.setText("Min Limit : ");

        txt_min_limit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));

        jLabel10.setText("Max Limit : ");

        txt_max_limit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));

        jLabel11.setText("Card Number :");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dt_card_expired, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_card_number)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txt_max_limit, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                                    .addComponent(txt_min_limit))
                                .addGap(4, 4, 4)
                                .addComponent(btn_add_card)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(cbo_card, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_card, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_card_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dt_card_expired, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_min_limit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txt_max_limit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btn_add_card, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Attachment", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        tbl_attachment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(tbl_attachment);

        btn_add_attachment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_add_small.png"))); // NOI18N
        btn_add_attachment.setIconTextGap(9);
        btn_add_attachment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_attachmentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_add_attachment)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_add_attachment)
                .addContainerGap())
        );

        btn_cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel.setText("Cancel");
        btn_cancel.setIconTextGap(9);
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });

        btn_save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_add_member_small.png"))); // NOI18N
        btn_save.setText("Save");
        btn_save.setIconTextGap(9);
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        txt_citizenship.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("0"))));

        txt_address.setColumns(20);
        txt_address.setLineWrap(true);
        txt_address.setRows(2);
        jScrollPane2.setViewportView(txt_address);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_citizenship))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_email))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_mobile))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_fullname))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)
                                .addComponent(btn_cancel)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_fullname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_citizenship, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_mobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancel)
                    .addComponent(btn_save))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Member data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        tbl_member_data.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbl_member_data);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 805, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 599, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Input data", jPanel1);

        jPanel2.setPreferredSize(new java.awt.Dimension(1159, 632));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Member transaction history", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search parameter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        lbl_member_name.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/member_icon1_small.png"))); // NOI18N
        lbl_member_name.setText("Member Name : ");

        btn_search_member.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_search_small.png"))); // NOI18N
        btn_search_member.setText("Search");
        btn_search_member.setIconTextGap(9);
        btn_search_member.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_search_memberActionPerformed(evt);
            }
        });

        jLabel1.setText("Search by : ");

        cbo_search_by.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Choose criteria --", "Citizenship", "Name", "Citizenship and Name" }));
        cbo_search_by.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_search_byItemStateChanged(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_id_small.png"))); // NOI18N
        jLabel8.setText("Citizenship id : ");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbo_search_by, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_citizenship_id)
                .addGap(18, 18, 18)
                .addComponent(lbl_member_name, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_member_name, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_search_member, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(169, 169, 169))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_member_name)
                    .addComponent(txt_member_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_search_member)
                    .addComponent(jLabel1)
                    .addComponent(cbo_search_by, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txt_citizenship_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tbl_search_result.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(tbl_search_result);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Search data", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calender = Calendar.getInstance();
        
        items = new MemberItems();
        
        if(isUpdate){            
            items = new MemberItems();           
            items.setFullname(txt_fullname.getText());
            items.setCitizenship(Integer.parseInt(txt_citizenship.getText()));
            items.setAddress(txt_address.getText());
            items.setMobile(txt_mobile.getText());            
            items.setUpdated(calender.getTime().toString());            
            
            // update table_member with new value if any //
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.UpdateMember(items);
            if(isSuccess){ 
                ReloadTable();
                LoadTableCard();
                LoadTableAttachment();

                txt_fullname.setText("");
                txt_citizenship.setText("");
                txt_address.setText(""); 
                txt_mobile.setText("");
                txt_email.setText("");

                // Reset button //
                btn_save.setText("Save");
                btn_save.setEnabled(false);
                btn_cancel.setEnabled(false);
                btn_add_attachment.setEnabled(false);
                
                txt_card_number.setText("");
                dt_card_expired.getEditor().setText("");
                txt_min_limit.setText("");
                txt_max_limit.setText("");

                isUpdate = false;                                                                                                                                                                                                                            
            }    
        }else{
            try{                                
                items.setFullname(txt_fullname.getText());
                items.setCitizenship(Integer.parseInt(txt_citizenship.getText()));
                items.setAddress(txt_address.getText()); 
                items.setMobile(txt_mobile.getText());                
                items.setEmail(txt_email.getText());
                items.setRegistered(calender.getTime().toString());
                items.setUpdated("");
                
                // Insert member //
                ConnectSQLLite sql_connection = new ConnectSQLLite();
                boolean isSuccess = sql_connection.InsertMember(items);
                if(isSuccess){                                        
                    // Insert card member //
                    ArrayList<CardMemberItems> cards = getTableCardData(tbl_card_member);                                    
                    if(!cards.isEmpty()){                        
                        isSuccess = sql_connection.InsertCardMember(cards);
                        if(isSuccess){                                                    
                            // Insert attachment member //
                            ArrayList<AttachmentItem> attachments = getTableAttachmentData(tbl_attachment);
                            if(!attachments.isEmpty()){
                                isSuccess = sql_connection.InsertAttachment(attachments);                        
                                if(isSuccess){
                                    ReloadTable();
                                    LoadTableCard();
                                    LoadTableAttachment();

                                    txt_fullname.setText("");
                                    txt_citizenship.setText("");
                                    txt_address.setText("");
                                    txt_mobile.setText("");
                                    txt_email.setText("");
                                    
                                    txt_card_number.setText("");
                                    dt_card_expired.getEditor().setText("");
                                    txt_min_limit.setText("");
                                    txt_max_limit.setText("");

                                    // Reset button //                    
                                    btn_save.setEnabled(false);
                                    btn_cancel.setEnabled(false);
                                    btn_add_card.setEnabled(false);
                                    btn_add_attachment.setEnabled(false);
                                } 
                            }else{
                                // Member doesn't have credit card and attachment //
                                // Member still saved and all component reset //
                                ReloadTable();
                                LoadTableCard();
                                LoadTableAttachment();

                                txt_fullname.setText("");
                                txt_citizenship.setText("");
                                txt_address.setText("");
                                txt_mobile.setText("");
                                txt_email.setText("");

                                txt_card_number.setText("");
                                dt_card_expired.getEditor().setText("");
                                txt_min_limit.setText("");
                                txt_max_limit.setText("");

                                // Reset button //                    
                                btn_save.setEnabled(false);
                                btn_cancel.setEnabled(false);
                                btn_add_card.setEnabled(false);
                                btn_add_attachment.setEnabled(false);
                            }                                                                            
                        } 
                    }else{
                       // Member doesn't have credit card //
                       // Insert attachment member //
                       ArrayList<AttachmentItem> attachments = getTableAttachmentData(tbl_attachment);
                       if(!attachments.isEmpty()){
                           isSuccess = sql_connection.InsertAttachment(attachments);                        
                           if(isSuccess){
                                ReloadTable();
                                LoadTableCard();
                                LoadTableAttachment();

                                txt_fullname.setText("");
                                txt_citizenship.setText("");
                                txt_address.setText("");
                                txt_mobile.setText("");
                                txt_email.setText("");

                                txt_card_number.setText("");
                                dt_card_expired.getEditor().setText("");
                                txt_min_limit.setText("");
                                txt_max_limit.setText("");

                                // Reset button //                    
                                btn_save.setEnabled(false);
                                btn_cancel.setEnabled(false);
                                btn_add_card.setEnabled(false);
                                btn_add_attachment.setEnabled(false);
                           }
                       }else{
                            // Member doesn't have credit card and attachment //
                            // Member still saved and all component reset //
                            ReloadTable();
                            LoadTableCard();
                            LoadTableAttachment();

                            txt_fullname.setText("");
                            txt_citizenship.setText("");
                            txt_address.setText("");
                            txt_mobile.setText("");
                            txt_email.setText("");

                            txt_card_number.setText("");
                            dt_card_expired.getEditor().setText("");
                            txt_min_limit.setText("");
                            txt_max_limit.setText("");

                            // Reset button //                    
                            btn_save.setEnabled(false);
                            btn_cancel.setEnabled(false);
                            btn_add_card.setEnabled(false);
                            btn_add_attachment.setEnabled(false);
                       }
                    }                                                                                                                       
                }               
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Failed to save member data : " + e.toString());
                e.printStackTrace();
            }                         
        } 
    }//GEN-LAST:event_btn_saveActionPerformed

    private void txt_fullnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_fullnameKeyReleased
        if(txt_fullname.getText().length() < 1 || 
           txt_citizenship.getText().length() < 1 ||
           txt_mobile.getText().length() < 1){
           btn_save.setEnabled(false);
           btn_cancel.setEnabled(false);
           btn_add_card.setEnabled(false);
           btn_add_attachment.setEnabled(false);
        }else{
           btn_save.setEnabled(true);
           btn_cancel.setEnabled(true);
           btn_add_card.setEnabled(true);
           btn_add_attachment.setEnabled(true);
        }
    }//GEN-LAST:event_txt_fullnameKeyReleased

    private void txt_mobileKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_mobileKeyReleased
        if(txt_fullname.getText().length() < 1 || 
           txt_citizenship.getText().length() < 1 ||
           txt_mobile.getText().length() < 1){
           btn_save.setEnabled(false);
           btn_cancel.setEnabled(false);
           btn_add_card.setEnabled(false);
           btn_add_attachment.setEnabled(false);
        }else{
           btn_save.setEnabled(true);
           btn_cancel.setEnabled(true);
           btn_add_card.setEnabled(true);
           btn_add_attachment.setEnabled(true);
        }
    }//GEN-LAST:event_txt_mobileKeyReleased

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        if(isUpdate){            
            LoadTableCard();
            LoadTableAttachment();
            
            txt_fullname.setText("");
            txt_citizenship.setText("");
            txt_address.setText("");   
            txt_mobile.setText("");
            txt_email.setText("");

            // Reset button //
            btn_save.setText("Save");
            btn_save.setEnabled(false);
            btn_cancel.setEnabled(false);
            btn_add_card.setEnabled(false);
            btn_add_attachment.setEnabled(false);
            
            dt_card_expired.getEditor().setText("");
            txt_min_limit.setText("");
            txt_max_limit.setText("");
            
            // Reset CBO card //
            cbo_card.setSelectedIndex(0);

            isUpdate = false;
            isEdit = false;
        }else{            
            LoadTableCard();
            LoadTableAttachment();
            
            txt_fullname.setText("");
            txt_citizenship.setText("");
            txt_address.setText("");
            txt_mobile.setText("");
            txt_email.setText("");

            // Reset button //            
            btn_save.setEnabled(false);
            btn_cancel.setEnabled(false);   
            btn_add_card.setEnabled(false);
            btn_add_attachment.setEnabled(false);
            
            // Reset CBO card //
            cbo_card.setSelectedIndex(0);
        }
    }//GEN-LAST:event_btn_cancelActionPerformed

    private void btn_add_cardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_cardActionPerformed
        
        if(cbo_card.getSelectedIndex() != 0 ){
            
            if(dt_card_expired.getEditor().getText().length() < 1 ||
                txt_min_limit.getText().length() < 1 ||
                txt_max_limit.getText().length() < 1 ||
                txt_card_number.getText().length() < 1){
                
                JOptionPane.showMessageDialog(null, "Error, card parameter empty");
            }else{
                BankItems item = new BankItems();
                ConnectSQLLite sql_connect = new ConnectSQLLite();
                item = sql_connect.getSingleCardData(cbo_card.getSelectedIndex());

                SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");            
                item.setExpired(formater.format(dt_card_expired.getDate()));
                item.setMin_limit(txt_min_limit.getText());
                item.setMax_limit(txt_max_limit.getText());
                //item.setCard_id(Integer.parseInt(txt_card_number.getText()));
                ((MemberCardTableModel)tbl_card_member.getModel()).insert(item);
            }            
        }else{
            JOptionPane.showMessageDialog(null, "Choose card type");
        }           
    }//GEN-LAST:event_btn_add_cardActionPerformed

    private void btn_add_attachmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_attachmentActionPerformed
        try{
            AttachmentItem attachment = new AttachmentItem();
            ArrayList<String> items = OpenFile();
            if(!items.isEmpty()){
                attachment.setName(items.get(0));
                attachment.setType(items.get(1));
                attachment.setPath(items.get(2));
                
                ((MemberAttachmentTableModel)tbl_attachment.getModel()).add(attachment);
            }            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "internal error : "+e.toString());
        }  
    }//GEN-LAST:event_btn_add_attachmentActionPerformed

    private void btn_search_memberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_search_memberActionPerformed
        ReloadSearchMemberTable();
    }//GEN-LAST:event_btn_search_memberActionPerformed

    private void cbo_search_byItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_search_byItemStateChanged
        switch(cbo_search_by.getSelectedIndex()){
            case 0 :
                txt_citizenship_id.setEnabled(false);
                txt_member_name.setEnabled(false);
                btn_search_member.setEnabled(false);
                break;
            case 1 : 
                txt_citizenship_id.setEnabled(true);
                txt_member_name.setEnabled(false);
                btn_search_member.setEnabled(true);
                break;
            case 2 :
                txt_citizenship_id.setEnabled(false);
                txt_member_name.setEnabled(true);
                btn_search_member.setEnabled(true);
                break;
            case 3 : 
                txt_citizenship_id.setEnabled(true);
                txt_member_name.setEnabled(true);
                btn_search_member.setEnabled(true);
                break;
        }
    }//GEN-LAST:event_cbo_search_byItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_attachment;
    private javax.swing.JButton btn_add_card;
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_search_member;
    private javax.swing.JComboBox cbo_card;
    private javax.swing.JComboBox cbo_search_by;
    private org.jdesktop.swingx.JXDatePicker dt_card_expired;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbl_member_name;
    private javax.swing.JTable tbl_attachment;
    private javax.swing.JTable tbl_card_member;
    private javax.swing.JTable tbl_member_data;
    private javax.swing.JTable tbl_search_result;
    private javax.swing.JTextArea txt_address;
    private javax.swing.JTextField txt_card_number;
    private javax.swing.JFormattedTextField txt_citizenship;
    private javax.swing.JTextField txt_citizenship_id;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_fullname;
    private javax.swing.JFormattedTextField txt_max_limit;
    private javax.swing.JTextField txt_member_name;
    private javax.swing.JFormattedTextField txt_min_limit;
    private javax.swing.JTextField txt_mobile;
    // End of variables declaration//GEN-END:variables
}
