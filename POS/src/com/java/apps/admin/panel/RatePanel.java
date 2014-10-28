/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.java.apps.admin.panel;

import com.java.apps.database.ConnectSQLLite;
import com.java.apps.pojo.AdditionalRateItems;
import com.java.apps.pojo.RateItems;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Ultimate
 */
public class RatePanel extends javax.swing.JPanel {
    
    private RateItems items;
    private AdditionalRateItems additional_rate_items;
    private AdditionalChargeModel additional_charge_data_model = new AdditionalChargeModel();
    private RateTransactionTableModel rate_transaction_data_model = new RateTransactionTableModel();    
    
    private boolean isUpdate = false;
    private int rate_id = 0, rate_transaction_id = 0;

    /**
     * Creates new form RatePanel1
     */
    public RatePanel() {
        initComponents();
        
        // Set initial save button disable //
        btn_add_transaction_rate.setEnabled(false);
        btn_add_additional_charge.setEnabled(false);
        
        btn_cancel_add_additional_charge.setEnabled(false);
        btn_cancel_rate_transaction.setEnabled(false);
                      
        // Handling fee //
        AbstractDocument ad_txt_handling_fee = (AbstractDocument)txt_charge_amount.getDocument();
        ad_txt_handling_fee.setDocumentFilter(new FieldListener());
                                               
        // Reload table transaction //
        Reload_Table_Transaction();
        
        // Reload table additional rate //
        Reload_Table_Additional_Rate();
    }
    
    public void Reload_Table_Additional_Rate(){
        additional_charge_data_model = new AdditionalChargeModel();
        tbl_additional_charge.setModel(additional_charge_data_model);
        tbl_additional_charge.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_additional_charge.getColumnModel().getColumn(0).setPreferredWidth(150);  // Action //        
        tbl_additional_charge.getColumnModel().getColumn(1).setPreferredWidth(200);  // Rate Name //
        tbl_additional_charge.getColumnModel().getColumn(2).setPreferredWidth(150);  // Amount //        
                                
        EditDeleteRenderer edit_delete_renderer = new EditDeleteRenderer();
        tbl_additional_charge.getColumnModel().getColumn(0).setCellRenderer(edit_delete_renderer);
        tbl_additional_charge.getColumnModel().getColumn(0).setCellEditor(new EditdeleteEditor());
        tbl_additional_charge.setRowHeight(edit_delete_renderer.getTableCellRendererComponent(tbl_additional_charge, null, true, true, 0, 0).getPreferredSize().height);  
        
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<AdditionalRateItems> charge_items = sql_connection.getAdditionalChargeData();                        
                
        int columns = charge_items.size();                      
        for (int x = 0; x < columns; x++){                            
            additional_charge_data_model.add(charge_items.get(x));
        }       
    }
            
    public void Reload_Table_Transaction(){
        
        // Insert member data model into table rate //
        rate_transaction_data_model = new RateTransactionTableModel();
        tbl_rate_transaction.setModel(rate_transaction_data_model);
        tbl_rate_transaction.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_rate_transaction.getColumnModel().getColumn(0).setPreferredWidth(150);  // Action //        
        tbl_rate_transaction.getColumnModel().getColumn(1).setPreferredWidth(250);  // Rate Name
        tbl_rate_transaction.getColumnModel().getColumn(2).setPreferredWidth(70);   // Rate (%)
                        
        EditDeleteTransactionRenderer edit_delete_transaction_renderer = new EditDeleteTransactionRenderer();
        tbl_rate_transaction.getColumnModel().getColumn(0).setCellRenderer(edit_delete_transaction_renderer);
        tbl_rate_transaction.getColumnModel().getColumn(0).setCellEditor(new EditdeleteTransactionEditor());
        tbl_rate_transaction.setRowHeight(edit_delete_transaction_renderer.getTableCellRendererComponent(tbl_rate_transaction, null, true, true, 0, 0).getPreferredSize().height);  
        
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<RateItems> rate_items = sql_connection.getRateTransactionData();                        
                
        int columns = rate_items.size();                      
        for (int x = 0; x < columns; x++){                            
            rate_transaction_data_model.add(rate_items.get(x));
        }        
    }
    
    public class AdditionalChargeModel extends AbstractTableModel {

        private ArrayList<AdditionalRateItems> items;

        public AdditionalChargeModel() {
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
                    value = "Nama biaya";
                    break; 
                case 2:
                    value = "Jumlah";
                    break; 
            }
            return value;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class value = Object.class;
            switch (columnIndex) {                
                case 1:
                    value = String.class;
                    break; 
                case 2:
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
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            AdditionalRateItems obj = items.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:
                    break;                               
                case 1:
                    value = obj.rate_name;
                    break;                          
                case 2:
                    value = obj.amount;
                    break;  
            }
            return value;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                AdditionalRateItems value = items.get(rowIndex);
                if ("edit".equals(aValue)) {                    
                    edit(value);                    
                } else {                     
                    remove(value);
                }                                
            }
        }

        public void add(AdditionalRateItems value) {
            int startIndex = getRowCount();
            items.add(value);
            fireTableRowsInserted(startIndex, getRowCount() - 1);
        }

        public void remove(AdditionalRateItems value) {
            
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.RemoveAdditionalRate(value);
            if(isSuccess){
                int startIndex = items.indexOf(value);                
                items.remove(value);
                fireTableRowsInserted(startIndex, startIndex);
                
                if(isUpdate){
                    // Reset text input // 
                    txt_charge_name.setText("");
                    txt_charge_amount.setText("");                    

                    // Reset button //
                    btn_add_additional_charge.setText("Simpan");
                    btn_add_additional_charge.setEnabled(false);
                    btn_cancel_add_additional_charge.setEnabled(false);

                    isUpdate = false; 
                }else{
                    // Reset text input //
                    txt_charge_name.setText("");
                    txt_charge_amount.setText("");                    

                    // Reset button //                   
                    btn_add_additional_charge.setEnabled(false);
                    btn_cancel_add_additional_charge.setEnabled(false);                
                }                
            }            
        }
        
        public void edit(AdditionalRateItems value){
            isUpdate = true; 
            txt_charge_name.setText(value.rate_name);
            txt_charge_amount.setText(value.amount);           
            
            btn_add_additional_charge.setText("Ubah");
            btn_add_additional_charge.setEnabled(true);
            btn_cancel_add_additional_charge.setEnabled(true);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }
    }
    
    public class RateTransactionTableModel extends AbstractTableModel {

        private ArrayList<RateItems> items;

        public RateTransactionTableModel() {
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
                    value = "Name";
                    break;
                case 2:
                    value = "Rate(%)";
                    break;                
            }
            return value;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class value = Object.class;
            switch (columnIndex) {                
                case 1:
                    value = String.class;
                    break;
                case 2:
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
            return 3;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            RateItems obj = items.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:
                    break;               
                case 1:
                    value = obj.rate_name;
                    break;
                case 2:
                    value = obj.rate_value;
                    break;
                
            }
            return value;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                RateItems value = items.get(rowIndex);
                if ("edit".equals(aValue)) {                    
                    edit(value);                    
                } else {                     
                    remove(value);
                }  
            }
        }

        public void add(RateItems value) {
            int startIndex = getRowCount();
            items.add(value);
            fireTableRowsInserted(startIndex, getRowCount() - 1);
        }

        public void remove(RateItems value) {
            
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.RemoveRate(value);
            if(isSuccess){
                int startIndex = items.indexOf(value);                
                items.remove(value);
                fireTableRowsInserted(startIndex, startIndex);
                
                if(isUpdate){
                    // Reset text input //
                    txt_transaction_rate_name.setText("");
                    txt_transaction_rate_percentage.setText("");

                    // Reset button //
                    btn_add_transaction_rate.setText("Simpan");
                    btn_add_transaction_rate.setEnabled(false);
                    btn_cancel_rate_transaction.setEnabled(false);

                    isUpdate = false; 
                }else{
                    // Reset text input //
                    txt_transaction_rate_name.setText("");
                    txt_transaction_rate_percentage.setText("");

                    // Reset button //                   
                    btn_add_transaction_rate.setEnabled(false);
                    btn_cancel_rate_transaction.setEnabled(false);                  
                }                
            }            
        }
        
        public void edit(RateItems value){
            isUpdate = true;
            rate_id = value.rate_id;
            txt_transaction_rate_name.setText(value.rate_name);
            txt_transaction_rate_percentage.setText(value.rate_value);
            
            btn_add_additional_charge.setText("Ubah");
            btn_add_additional_charge.setEnabled(true);
            btn_cancel_rate_transaction.setEnabled(true);
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
            edit = new JButton("Edit");edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_edit_small.png")));
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
           
    public class EditDeleteRateTransactionPane extends JPanel {

        private JButton edit;
        private JButton delete;
        private String state;

        public EditDeleteRateTransactionPane() {
            setLayout(new GridBagLayout());
            edit = new JButton("Edit");
            edit = new JButton("Edit");edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_edit_small.png")));
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
    
    public class EditDeleteTransactionRenderer extends DefaultTableCellRenderer {

        private EditDeletePane edit_delete_pane;

        public EditDeleteTransactionRenderer() {
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
    
    public class EditdeleteTransactionEditor extends AbstractCellEditor implements TableCellEditor {

        private EditDeletePane edit_delete_pane;

        public EditdeleteTransactionEditor() {
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_rate_transaction = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txt_transaction_rate_name = new javax.swing.JTextField();
        btn_add_transaction_rate = new javax.swing.JButton();
        btn_cancel_rate_transaction = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txt_transaction_rate_percentage = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_min_amount = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_max_amount = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_additional_charge = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_charge_amount = new javax.swing.JFormattedTextField();
        btn_add_additional_charge = new javax.swing.JButton();
        btn_cancel_add_additional_charge = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_charge_name = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        txt_rate_name_cicilan_6 = new javax.swing.JTextField();
        btn_add_cicilan_6_bulan = new javax.swing.JButton();
        btn_cancel_cicilan_6_bulan = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        txt_persentase_cicilan_6_bulan = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_min_cicilan_6_bulan = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        txt_max_cicilan_6_bulan = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        txt_rate_name_cicilan_12_bulan = new javax.swing.JTextField();
        btn_add_cicilan_12_bulan = new javax.swing.JButton();
        btn_cancel_cicilan_12_bulan = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        txt_persentase_cicilan_12_bulan = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txt_min_cicilan_12_bulan = new javax.swing.JFormattedTextField();
        jLabel42 = new javax.swing.JLabel();
        txt_max_cicilan_12_bulan = new javax.swing.JFormattedTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jPanel21 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        txt_rate_name_cicilan_4 = new javax.swing.JTextField();
        btn_add_cicilan_3_bulan1 = new javax.swing.JButton();
        btn_cancel_cicilan_3_bulan1 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        txt_persentase_cicilan_3_bulan1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txt_min_cicilan_3_bulan1 = new javax.swing.JFormattedTextField();
        jLabel28 = new javax.swing.JLabel();
        txt_max_cicilan_3_bulan1 = new javax.swing.JFormattedTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txt_rate_name_cicilan_3 = new javax.swing.JTextField();
        btn_add_cicilan_3_bulan = new javax.swing.JButton();
        btn_cancel_cicilan_3_bulan = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        txt_persentase_cicilan_3_bulan = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_min_cicilan_3_bulan = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_max_cicilan_3_bulan = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel19 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        txt_rate_name_dana_talangan = new javax.swing.JTextField();
        btn_add_dana_talangan = new javax.swing.JButton();
        btn_cancel_dana_talangan = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        txt_persentase_dana_talangan = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txt_min_dana_talangan = new javax.swing.JFormattedTextField();
        jLabel35 = new javax.swing.JLabel();
        txt_max_dana_talangan = new javax.swing.JFormattedTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Rate data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        tbl_rate_transaction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tbl_rate_transaction);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jLabel13.setText("Nama Rate : ");

        txt_transaction_rate_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_transaction_rate_nameKeyReleased(evt);
            }
        });

        btn_add_transaction_rate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_save_small.png"))); // NOI18N
        btn_add_transaction_rate.setText("Simpan");
        btn_add_transaction_rate.setIconTextGap(9);
        btn_add_transaction_rate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_transaction_rateActionPerformed(evt);
            }
        });

        btn_cancel_rate_transaction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel_rate_transaction.setText("Batal");
        btn_cancel_rate_transaction.setIconTextGap(9);
        btn_cancel_rate_transaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancel_rate_transactionActionPerformed(evt);
            }
        });

        jLabel14.setText("Persentase :");

        txt_transaction_rate_percentage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_transaction_rate_percentageKeyReleased(evt);
            }
        });

        jLabel15.setText("%");

        jLabel3.setText("Nominal Minimal : ");

        jLabel4.setText("Nominal Maksimal : ");

        jLabel5.setText("Rp. ");

        jLabel6.setText("Rp.");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_add_transaction_rate, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cancel_rate_transaction, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(txt_transaction_rate_percentage, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15))
                            .addComponent(txt_transaction_rate_name)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_min_amount)
                                    .addComponent(txt_max_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(31, 31, 31))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txt_transaction_rate_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_min_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(11, 11, 11)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_max_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txt_transaction_rate_percentage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(12, 12, 12)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancel_rate_transaction)
                    .addComponent(btn_add_transaction_rate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Rate transaksi non-member", jPanel2);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data biaya tambahan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        tbl_additional_charge.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tbl_additional_charge);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jLabel2.setText("Nominal : Rp. ");

        txt_charge_amount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(""))));
        txt_charge_amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_charge_amountKeyReleased(evt);
            }
        });

        btn_add_additional_charge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_save_small.png"))); // NOI18N
        btn_add_additional_charge.setText("Simpan");
        btn_add_additional_charge.setIconTextGap(9);
        btn_add_additional_charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_additional_chargeActionPerformed(evt);
            }
        });

        btn_cancel_add_additional_charge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel_add_additional_charge.setText("Batal");
        btn_cancel_add_additional_charge.setIconTextGap(9);
        btn_cancel_add_additional_charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancel_add_additional_chargeActionPerformed(evt);
            }
        });

        jLabel1.setText("Nama biaya : ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btn_add_additional_charge, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cancel_add_additional_charge))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_charge_amount, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                            .addComponent(txt_charge_name))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_charge_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_charge_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_add_additional_charge)
                    .addComponent(btn_cancel_add_additional_charge))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Biaya tambahan", jPanel15);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data rate cicilan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jLabel19.setText("Nama Rate : ");

        txt_rate_name_cicilan_6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_rate_name_cicilan_6KeyReleased(evt);
            }
        });

        btn_add_cicilan_6_bulan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_save_small.png"))); // NOI18N
        btn_add_cicilan_6_bulan.setText("Simpan");
        btn_add_cicilan_6_bulan.setIconTextGap(9);
        btn_add_cicilan_6_bulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_cicilan_6_bulanActionPerformed(evt);
            }
        });

        btn_cancel_cicilan_6_bulan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel_cicilan_6_bulan.setText("Batal");
        btn_cancel_cicilan_6_bulan.setIconTextGap(9);
        btn_cancel_cicilan_6_bulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancel_cicilan_6_bulanActionPerformed(evt);
            }
        });

        jLabel20.setText("Persentase :");

        txt_persentase_cicilan_6_bulan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_persentase_cicilan_6_bulanKeyReleased(evt);
            }
        });

        jLabel21.setText("%");

        jLabel11.setText("Nominal Minimal : ");

        jLabel12.setText("Nominal Maksimal : ");

        jLabel22.setText("Rp. ");

        jLabel23.setText("Rp.");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_add_cicilan_6_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cancel_cicilan_6_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(txt_persentase_cicilan_6_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21))
                            .addComponent(txt_rate_name_cicilan_6)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel22))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_min_cicilan_6_bulan)
                                    .addComponent(txt_max_cicilan_6_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(31, 31, 31))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txt_rate_name_cicilan_6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txt_min_cicilan_6_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(11, 11, 11)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txt_max_cicilan_6_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txt_persentase_cicilan_6_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancel_cicilan_6_bulan)
                    .addComponent(btn_add_cicilan_6_bulan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(70, 70, 70))
        );

        jTabbedPane1.addTab("Cicilan 6 bulan", jPanel4);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data rate cicilan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jLabel38.setText("Nama Rate : ");

        txt_rate_name_cicilan_12_bulan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_rate_name_cicilan_12_bulanKeyReleased(evt);
            }
        });

        btn_add_cicilan_12_bulan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_save_small.png"))); // NOI18N
        btn_add_cicilan_12_bulan.setText("Simpan");
        btn_add_cicilan_12_bulan.setIconTextGap(9);
        btn_add_cicilan_12_bulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_cicilan_12_bulanActionPerformed(evt);
            }
        });

        btn_cancel_cicilan_12_bulan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel_cicilan_12_bulan.setText("Batal");
        btn_cancel_cicilan_12_bulan.setIconTextGap(9);
        btn_cancel_cicilan_12_bulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancel_cicilan_12_bulanActionPerformed(evt);
            }
        });

        jLabel39.setText("Persentase :");

        txt_persentase_cicilan_12_bulan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_persentase_cicilan_12_bulanKeyReleased(evt);
            }
        });

        jLabel40.setText("%");

        jLabel41.setText("Nominal Minimal : ");

        jLabel42.setText("Nominal Maksimal : ");

        jLabel43.setText("Rp. ");

        jLabel44.setText("Rp.");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_add_cicilan_12_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cancel_cicilan_12_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addComponent(txt_persentase_cicilan_12_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel40))
                            .addComponent(txt_rate_name_cicilan_12_bulan)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel44)
                                    .addComponent(jLabel43))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_min_cicilan_12_bulan)
                                    .addComponent(txt_max_cicilan_12_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(31, 31, 31))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(txt_rate_name_cicilan_12_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txt_min_cicilan_12_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addGap(11, 11, 11)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(txt_max_cicilan_12_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(txt_persentase_cicilan_12_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancel_cicilan_12_bulan)
                    .addComponent(btn_add_cicilan_12_bulan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cicilan 12 bulan", jPanel5);

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data rate cicilan 3 bulan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(jTable5);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
        );

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jLabel24.setText("Nama Rate : ");

        txt_rate_name_cicilan_4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_rate_name_cicilan_4KeyReleased(evt);
            }
        });

        btn_add_cicilan_3_bulan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_save_small.png"))); // NOI18N
        btn_add_cicilan_3_bulan1.setText("Simpan");
        btn_add_cicilan_3_bulan1.setIconTextGap(9);
        btn_add_cicilan_3_bulan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_cicilan_3_bulan1ActionPerformed(evt);
            }
        });

        btn_cancel_cicilan_3_bulan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel_cicilan_3_bulan1.setText("Batal");
        btn_cancel_cicilan_3_bulan1.setIconTextGap(9);
        btn_cancel_cicilan_3_bulan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancel_cicilan_3_bulan1ActionPerformed(evt);
            }
        });

        jLabel25.setText("Persentase :");

        txt_persentase_cicilan_3_bulan1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_persentase_cicilan_3_bulan1KeyReleased(evt);
            }
        });

        jLabel26.setText("%");

        jLabel27.setText("Nominal Minimal : ");

        jLabel28.setText("Nominal Maksimal : ");

        jLabel29.setText("Rp. ");

        jLabel30.setText("Rp.");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_add_cicilan_3_bulan1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cancel_cicilan_3_bulan1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(txt_persentase_cicilan_3_bulan1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel26))
                            .addComponent(txt_rate_name_cicilan_4)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel29))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_min_cicilan_3_bulan1)
                                    .addComponent(txt_max_cicilan_3_bulan1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(31, 31, 31))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txt_rate_name_cicilan_4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txt_min_cicilan_3_bulan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(11, 11, 11)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txt_max_cicilan_3_bulan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txt_persentase_cicilan_3_bulan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(12, 12, 12)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancel_cicilan_3_bulan1)
                    .addComponent(btn_add_cicilan_3_bulan1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Rate transaksi member", jPanel6);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data rate cicilan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jLabel16.setText("Nama Rate : ");

        txt_rate_name_cicilan_3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_rate_name_cicilan_3KeyReleased(evt);
            }
        });

        btn_add_cicilan_3_bulan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_save_small.png"))); // NOI18N
        btn_add_cicilan_3_bulan.setText("Simpan");
        btn_add_cicilan_3_bulan.setIconTextGap(9);
        btn_add_cicilan_3_bulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_cicilan_3_bulanActionPerformed(evt);
            }
        });

        btn_cancel_cicilan_3_bulan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel_cicilan_3_bulan.setText("Batal");
        btn_cancel_cicilan_3_bulan.setIconTextGap(9);
        btn_cancel_cicilan_3_bulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancel_cicilan_3_bulanActionPerformed(evt);
            }
        });

        jLabel17.setText("Persentase :");

        txt_persentase_cicilan_3_bulan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_persentase_cicilan_3_bulanKeyReleased(evt);
            }
        });

        jLabel18.setText("%");

        jLabel7.setText("Nominal Minimal : ");

        jLabel8.setText("Nominal Maksimal : ");

        jLabel9.setText("Rp. ");

        jLabel10.setText("Rp.");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_add_cicilan_3_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cancel_cicilan_3_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(txt_persentase_cicilan_3_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18))
                            .addComponent(txt_rate_name_cicilan_3)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_min_cicilan_3_bulan)
                                    .addComponent(txt_max_cicilan_3_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(31, 31, 31))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txt_rate_name_cicilan_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_min_cicilan_3_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(11, 11, 11)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_max_cicilan_3_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txt_persentase_cicilan_3_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(12, 12, 12)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancel_cicilan_3_bulan)
                    .addComponent(btn_add_cicilan_3_bulan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Cicilan 3 bulan", jPanel1);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data rate cicilan 3 bulan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
        );

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jLabel31.setText("Nama Rate : ");

        txt_rate_name_dana_talangan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_rate_name_dana_talanganKeyReleased(evt);
            }
        });

        btn_add_dana_talangan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_save_small.png"))); // NOI18N
        btn_add_dana_talangan.setText("Simpan");
        btn_add_dana_talangan.setIconTextGap(9);
        btn_add_dana_talangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_dana_talanganActionPerformed(evt);
            }
        });

        btn_cancel_dana_talangan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel_dana_talangan.setText("Batal");
        btn_cancel_dana_talangan.setIconTextGap(9);
        btn_cancel_dana_talangan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancel_dana_talanganActionPerformed(evt);
            }
        });

        jLabel32.setText("Persentase :");

        txt_persentase_dana_talangan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_persentase_dana_talanganKeyReleased(evt);
            }
        });

        jLabel33.setText("%");

        jLabel34.setText("Nominal Minimal : ");

        jLabel35.setText("Nominal Maksimal : ");

        jLabel36.setText("Rp. ");

        jLabel37.setText("Rp.");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_add_dana_talangan, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cancel_dana_talangan, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addComponent(txt_persentase_dana_talangan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel33))
                            .addComponent(txt_rate_name_dana_talangan)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel36))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_min_dana_talangan)
                                    .addComponent(txt_max_dana_talangan, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(31, 31, 31))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txt_rate_name_dana_talangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txt_min_dana_talangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addGap(11, 11, 11)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(txt_max_dana_talangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txt_persentase_dana_talangan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addGap(12, 12, 12)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancel_dana_talangan)
                    .addComponent(btn_add_dana_talangan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Rate transaksi dana talangan", jPanel10);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cancel_add_additional_chargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancel_add_additional_chargeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cancel_add_additional_chargeActionPerformed

    private void btn_add_additional_chargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_additional_chargeActionPerformed
        if(isUpdate){
            additional_rate_items = new AdditionalRateItems();
            additional_rate_items.setRate_id(rate_id);
            additional_rate_items.setRate_name(txt_charge_name.getText());
            additional_rate_items.setAmount(txt_charge_amount.getText());
           
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.UpdateAdditionalCharge(additional_rate_items);
            if(isSuccess){
                Reload_Table_Additional_Rate();

                txt_charge_name.setText("");
                txt_charge_amount.setText("");
                
                // Reset button //
                btn_add_additional_charge.setText("Simpan");
                btn_add_additional_charge.setEnabled(false);
                btn_cancel_add_additional_charge.setEnabled(false);

                isUpdate = false;
            }
        }else{
            additional_rate_items = new AdditionalRateItems();
            additional_rate_items.setRate_name(txt_charge_name.getText());
            additional_rate_items.setAmount(txt_charge_amount.getText());
            
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.InsertAdditionalCharge(additional_rate_items);
            if(isSuccess){
                Reload_Table_Additional_Rate();

                // Reset text input //                
                txt_charge_amount.setText("");
                txt_charge_name.setText("");

                // Reset button //
                btn_cancel_add_additional_charge.setEnabled(false);
                btn_add_additional_charge.setEnabled(false);
            }
        }
    }//GEN-LAST:event_btn_add_additional_chargeActionPerformed

    private void txt_charge_amountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_charge_amountKeyReleased
        if(txt_charge_amount.getText().length() < 1){

            btn_add_additional_charge.setEnabled(false);
            btn_cancel_add_additional_charge.setEnabled(false);
        }else{
            btn_add_additional_charge.setEnabled(true);
            btn_cancel_add_additional_charge.setEnabled(true);
        }
    }//GEN-LAST:event_txt_charge_amountKeyReleased

    private void btn_cancel_rate_transactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancel_rate_transactionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cancel_rate_transactionActionPerformed

    private void btn_add_transaction_rateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_transaction_rateActionPerformed
        if(txt_transaction_rate_name.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Parameter rate name is empty");
        }else if(txt_transaction_rate_percentage.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Parameter rate percentage is empty");
        }else{
            if(isUpdate){
                items = new RateItems();
                items.setRate_id(rate_transaction_id);
                items.setRate_name(txt_transaction_rate_name.getText());
                items.setRate_value(txt_transaction_rate_percentage.getText());
                ConnectSQLLite sql_connection = new ConnectSQLLite();
                boolean isSuccess = sql_connection.UpdateTransactionRate(items);
                if(isSuccess){
                    Reload_Table_Transaction();

                    // Reset text input //
                    txt_transaction_rate_name.setText("");
                    txt_transaction_rate_name.setText("");

                    // Reset button //
                    btn_add_transaction_rate.setText("Save");
                    btn_add_transaction_rate.setEnabled(false);
                    btn_cancel_rate_transaction.setEnabled(false);

                    isUpdate = false;
                }
            }else{
                items = new RateItems();
                items.setRate_name(txt_transaction_rate_name.getText());
                items.setRate_value(txt_transaction_rate_percentage.getText());
                ConnectSQLLite sql_connection = new ConnectSQLLite();
                boolean isSuccess = sql_connection.InsertRateTransaction(items);
                if(isSuccess){
                    Reload_Table_Transaction();

                    // Reset text input //
                    txt_transaction_rate_name.setText("");
                    txt_transaction_rate_percentage.setText("");
                    btn_add_transaction_rate.setEnabled(false);
                    btn_cancel_rate_transaction.setEnabled(false);
                }
            }
        }
    }//GEN-LAST:event_btn_add_transaction_rateActionPerformed

    private void txt_transaction_rate_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_transaction_rate_nameKeyReleased
        if(txt_transaction_rate_name.getText().length() < 1 || txt_transaction_rate_percentage.getText().length() < 1){
            btn_add_transaction_rate.setEnabled(false);
            btn_cancel_rate_transaction.setEnabled(false);
        }else{
            btn_add_transaction_rate.setEnabled(true);
            btn_cancel_rate_transaction.setEnabled(true);
        }
    }//GEN-LAST:event_txt_transaction_rate_nameKeyReleased

    private void txt_transaction_rate_percentageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_transaction_rate_percentageKeyReleased
        if(txt_transaction_rate_name.getText().length() < 1 || txt_transaction_rate_percentage.getText().length() < 1){
            btn_add_transaction_rate.setEnabled(false);
            btn_cancel_rate_transaction.setEnabled(false);
        }else{
            btn_add_transaction_rate.setEnabled(true);
            btn_cancel_rate_transaction.setEnabled(true);
        }
    }//GEN-LAST:event_txt_transaction_rate_percentageKeyReleased

    private void txt_rate_name_cicilan_3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rate_name_cicilan_3KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_rate_name_cicilan_3KeyReleased

    private void btn_add_cicilan_3_bulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_cicilan_3_bulanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_add_cicilan_3_bulanActionPerformed

    private void btn_cancel_cicilan_3_bulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancel_cicilan_3_bulanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cancel_cicilan_3_bulanActionPerformed

    private void txt_persentase_cicilan_3_bulanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_persentase_cicilan_3_bulanKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_persentase_cicilan_3_bulanKeyReleased

    private void txt_rate_name_cicilan_6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rate_name_cicilan_6KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_rate_name_cicilan_6KeyReleased

    private void btn_add_cicilan_6_bulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_cicilan_6_bulanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_add_cicilan_6_bulanActionPerformed

    private void btn_cancel_cicilan_6_bulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancel_cicilan_6_bulanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cancel_cicilan_6_bulanActionPerformed

    private void txt_persentase_cicilan_6_bulanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_persentase_cicilan_6_bulanKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_persentase_cicilan_6_bulanKeyReleased

    private void txt_rate_name_dana_talanganKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rate_name_dana_talanganKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_rate_name_dana_talanganKeyReleased

    private void btn_add_dana_talanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_dana_talanganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_add_dana_talanganActionPerformed

    private void btn_cancel_dana_talanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancel_dana_talanganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cancel_dana_talanganActionPerformed

    private void txt_persentase_dana_talanganKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_persentase_dana_talanganKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_persentase_dana_talanganKeyReleased

    private void txt_rate_name_cicilan_12_bulanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rate_name_cicilan_12_bulanKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_rate_name_cicilan_12_bulanKeyReleased

    private void btn_add_cicilan_12_bulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_cicilan_12_bulanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_add_cicilan_12_bulanActionPerformed

    private void btn_cancel_cicilan_12_bulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancel_cicilan_12_bulanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cancel_cicilan_12_bulanActionPerformed

    private void txt_persentase_cicilan_12_bulanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_persentase_cicilan_12_bulanKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_persentase_cicilan_12_bulanKeyReleased

    private void txt_rate_name_cicilan_4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rate_name_cicilan_4KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_rate_name_cicilan_4KeyReleased

    private void btn_add_cicilan_3_bulan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_cicilan_3_bulan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_add_cicilan_3_bulan1ActionPerformed

    private void btn_cancel_cicilan_3_bulan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancel_cicilan_3_bulan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cancel_cicilan_3_bulan1ActionPerformed

    private void txt_persentase_cicilan_3_bulan1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_persentase_cicilan_3_bulan1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_persentase_cicilan_3_bulan1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_additional_charge;
    private javax.swing.JButton btn_add_cicilan_12_bulan;
    private javax.swing.JButton btn_add_cicilan_3_bulan;
    private javax.swing.JButton btn_add_cicilan_3_bulan1;
    private javax.swing.JButton btn_add_cicilan_6_bulan;
    private javax.swing.JButton btn_add_dana_talangan;
    private javax.swing.JButton btn_add_transaction_rate;
    private javax.swing.JButton btn_cancel_add_additional_charge;
    private javax.swing.JButton btn_cancel_cicilan_12_bulan;
    private javax.swing.JButton btn_cancel_cicilan_3_bulan;
    private javax.swing.JButton btn_cancel_cicilan_3_bulan1;
    private javax.swing.JButton btn_cancel_cicilan_6_bulan;
    private javax.swing.JButton btn_cancel_dana_talangan;
    private javax.swing.JButton btn_cancel_rate_transaction;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable tbl_additional_charge;
    private javax.swing.JTable tbl_rate_transaction;
    private javax.swing.JFormattedTextField txt_charge_amount;
    private javax.swing.JTextField txt_charge_name;
    private javax.swing.JFormattedTextField txt_max_amount;
    private javax.swing.JFormattedTextField txt_max_cicilan_12_bulan;
    private javax.swing.JFormattedTextField txt_max_cicilan_3_bulan;
    private javax.swing.JFormattedTextField txt_max_cicilan_3_bulan1;
    private javax.swing.JFormattedTextField txt_max_cicilan_6_bulan;
    private javax.swing.JFormattedTextField txt_max_dana_talangan;
    private javax.swing.JFormattedTextField txt_min_amount;
    private javax.swing.JFormattedTextField txt_min_cicilan_12_bulan;
    private javax.swing.JFormattedTextField txt_min_cicilan_3_bulan;
    private javax.swing.JFormattedTextField txt_min_cicilan_3_bulan1;
    private javax.swing.JFormattedTextField txt_min_cicilan_6_bulan;
    private javax.swing.JFormattedTextField txt_min_dana_talangan;
    private javax.swing.JTextField txt_persentase_cicilan_12_bulan;
    private javax.swing.JTextField txt_persentase_cicilan_3_bulan;
    private javax.swing.JTextField txt_persentase_cicilan_3_bulan1;
    private javax.swing.JTextField txt_persentase_cicilan_6_bulan;
    private javax.swing.JTextField txt_persentase_dana_talangan;
    private javax.swing.JTextField txt_rate_name_cicilan_12_bulan;
    private javax.swing.JTextField txt_rate_name_cicilan_3;
    private javax.swing.JTextField txt_rate_name_cicilan_4;
    private javax.swing.JTextField txt_rate_name_cicilan_6;
    private javax.swing.JTextField txt_rate_name_dana_talangan;
    private javax.swing.JTextField txt_transaction_rate_name;
    private javax.swing.JTextField txt_transaction_rate_percentage;
    // End of variables declaration//GEN-END:variables
}
