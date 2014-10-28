/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.java.apps.user.panel;

import com.java.apps.database.ConnectSQLLite;
import com.java.apps.pojo.BankItems;
import com.java.apps.pojo.MemberItems;
import com.java.apps.pojo.ProfileItems;
import com.java.apps.pojo.RateItems;
import com.java.apps.pojo.TransactionItems;
import com.java.apps.pojo.ValueHolder;
import com.java.apps.user.panel.popup.SearchMemberPanel;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import sun.java2d.pipe.RenderingEngine;

/**
 *
 * @author Ultimate
 */
public class newTrasactionPanel extends javax.swing.JPanel {
    
    private DefaultTableModel credit_card_data_model, transaction_data_model;
    private SearchMemberPanel search_member_panel;    
    
    private MemberCardTableModel card_table_model = new MemberCardTableModel();   
    private boolean isMember = false, searchMember = false, selected_card = false;
    private int card_id = 0;
    
    private long nominal_transaction = 0, selling_price = 0, handling_fee = 0, transfer_bank = 0, stamp_price = 0,
                 bank_fees = 0, clean_fees = 0, total = 0, nominal_gestun = 0, nominal_accepted = 0, total_paid = 0;
    
    private double transaction_rate = 0, profile_machine_rate = 0;
    
    /**
     * Creates new form newTrasactionPanel
     */
    public newTrasactionPanel() {
        initComponents();
        
        cb_handling_fee.setSelected(false);
        cb_transfer_bank.setSelected(false);
        cb_stamp_price.setSelected(false);
        
        txt_handling_fee.setText("0");
        txt_transfer_bank.setText("0");
        txt_stamp_price.setText("0");
        
        txt_handling_fee.setEditable(false);
        txt_transfer_bank.setEditable(false);
        txt_stamp_price.setEditable(false);
        
        
        btn_save.setEnabled(false);
        btn_cancel.setEnabled(false);
        btn_add_card.setEnabled(false);
        
        
        //AbstractDocument ad_txt_nominal_transaction = (AbstractDocument)txt_nominal_transaction.getDocument();
        //ad_txt_nominal_transaction.setDocumentFilter(new FieldListener());
        
        AbstractDocument ad_txt_handling_fee = (AbstractDocument)txt_handling_fee.getDocument();
        ad_txt_handling_fee.setDocumentFilter(new FieldListener());
        
        AbstractDocument ad_txt_transfer_bank = (AbstractDocument)txt_transfer_bank.getDocument();
        ad_txt_transfer_bank.setDocumentFilter(new FieldListener());
        
        AbstractDocument ad_txt_stamp_price = (AbstractDocument)txt_stamp_price.getDocument();
        ad_txt_stamp_price.setDocumentFilter(new FieldListener());
        
        AbstractDocument ad_txt_nominal_accepted = (AbstractDocument)txt_nominal_accepted.getDocument();
        ad_txt_nominal_accepted.setDocumentFilter(new FieldListener());
        
        
        
        // Populate credit card //        
        PopulateCard();
        
        // Populate rate transaction //
        PopulateRateTransaction();
        
        // Populate machine profile //
        PopulateProfile();
        
    }
    
    private void PopulateProfile(){
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<ProfileItems> items = sql_connection.getProfileData();
        
        cbo_machine_profile.addItem("-- select profile --");
        for (ProfileItems item : items){
            cbo_machine_profile.addItem(item.profile_name);
        }   
    }
    
    private void PopulateCard(){
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<BankItems> bank_items = sql_connection.getCardData();
        
        cbo_card.addItem("-- select card --");
        for (BankItems item : bank_items){
            cbo_card.addItem(item.card_name);
        }   
    }
    
    private void PopulateRateTransaction(){
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<RateItems> rate_items = sql_connection.getRateTransactionData();
        cbo_rate_transaction.addItem("-- select rate --");
        for (RateItems item : rate_items){
            cbo_rate_transaction.addItem(item.rate_name);
        }   
    }
    
    private void populate_table_transaction(){
        transaction_data_model = new DefaultTableModel(){
            Class[] types = new Class[]{
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class,                 
                java.lang.String.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }            
        };
        
        transaction_data_model.addColumn("Transaction date");
        transaction_data_model.addColumn("Customer");        
        transaction_data_model.addColumn("Profile");
        transaction_data_model.addColumn("Rate (%)");
        transaction_data_model.addColumn("Note");
        
        tbl_transaction.setModel(transaction_data_model);
        tbl_transaction.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        tbl_transaction.getColumnModel().getColumn(0).setPreferredWidth(140);
        tbl_transaction.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbl_transaction.getColumnModel().getColumn(2).setPreferredWidth(130);
        tbl_transaction.getColumnModel().getColumn(3).setPreferredWidth(120);                    
        tbl_transaction.getColumnModel().getColumn(4).setPreferredWidth(350);
        
        ConnectSQLLite connect_sql = new ConnectSQLLite();
        ArrayList<TransactionItems> items = connect_sql.GetTransactionData();
        
        for(TransactionItems item : items){
            transaction_data_model.addRow(new Object[]{item.transaction_date,
                                                       item.fullname,
                                                       item.profile_name,
                                                       item.percentage,
                                                       item.note
                                                      });
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
                    value = "Id";
                    break;
                case 2:
                    value = "Card Name";
                    break;
                case 3:
                    value = "Type";
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

        public void add(BankItems value) {             
            int startIndex = getRowCount();
            items.add(value);
            fireTableRowsInserted(startIndex, getRowCount() - 1);                    
        }
                
        public void remove(BankItems value) {
            int startIndex = items.indexOf(value);                
            items.remove(value);
            fireTableRowsInserted(startIndex, startIndex);                                            
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

    class FieldListener extends DocumentFilter{
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException  
        {
            try {
                if (string.equals(",") && !fb.getDocument().getText(0, fb.getDocument().getLength()).contains(",")) {
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
                if (text.equals(",") && !fb.getDocument().getText(0, fb.getDocument().getLength()).contains(",")) {
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

        jLabel4 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dt_transaction_date = new org.jdesktop.swingx.JXDatePicker();
        jLabel2 = new javax.swing.JLabel();
        txt_member_name = new javax.swing.JTextField();
        btn_search = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cbo_machine_profile = new javax.swing.JComboBox();
        btn_cancel = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_credit_card_member = new javax.swing.JTable();
        btn_add_card = new javax.swing.JButton();
        txt_nominal_transaction = new javax.swing.JFormattedTextField();
        cbo_card = new javax.swing.JComboBox();
        cb_handling_fee = new javax.swing.JCheckBox();
        txt_handling_fee = new javax.swing.JFormattedTextField();
        cb_transfer_bank = new javax.swing.JCheckBox();
        txt_transfer_bank = new javax.swing.JFormattedTextField();
        cb_stamp_price = new javax.swing.JCheckBox();
        txt_stamp_price = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbo_rate_transaction = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txt_fee = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt_total = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_nominal_accepted = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        txt_must_paid = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txt_rate_transaction = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txt_selling_price = new javax.swing.JFormattedTextField();
        txt_rate_machine = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_transaction = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        jLabel4.setText("Rp. ");

        jLabel15.setText("Rp. ");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Transaction input", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));
        jPanel3.setToolTipText("Transaction Note (If any)");

        jLabel1.setText("Transaction date : ");

        dt_transaction_date.setToolTipText("Transaction Date");

        jLabel2.setText("Customer name : ");

        txt_member_name.setToolTipText("Customer or Member name");
        txt_member_name.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_member_nameFocusLost(evt);
            }
        });
        txt_member_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_member_nameKeyReleased(evt);
            }
        });

        btn_search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_search_small.png"))); // NOI18N
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });

        jLabel5.setText("Nominal transaction : ");

        jLabel11.setText("EDC Machine : ");

        cbo_machine_profile.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_machine_profileItemStateChanged(evt);
            }
        });

        btn_cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel.setText("Cancel");
        btn_cancel.setIconTextGap(9);
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });

        btn_save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_save_small.png"))); // NOI18N
        btn_save.setText("Save");
        btn_save.setIconTextGap(9);
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        tbl_credit_card_member.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tbl_credit_card_member);

        btn_add_card.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_add_small.png"))); // NOI18N

        txt_nominal_transaction.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00;(¤#,##0.00)"))));
        txt_nominal_transaction.setToolTipText("Nominal Transaction (Rp)");
        txt_nominal_transaction.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_nominal_transactionFocusLost(evt);
            }
        });
        txt_nominal_transaction.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_nominal_transactionKeyReleased(evt);
            }
        });

        cb_handling_fee.setText("Handling fee : ");
        cb_handling_fee.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_handling_feeItemStateChanged(evt);
            }
        });

        txt_handling_fee.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txt_handling_fee.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_handling_feeFocusLost(evt);
            }
        });
        txt_handling_fee.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_handling_feeKeyReleased(evt);
            }
        });

        cb_transfer_bank.setText("Transfer bank : ");
        cb_transfer_bank.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_transfer_bankItemStateChanged(evt);
            }
        });

        txt_transfer_bank.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txt_transfer_bank.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_transfer_bankFocusLost(evt);
            }
        });
        txt_transfer_bank.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_transfer_bankKeyReleased(evt);
            }
        });

        cb_stamp_price.setText("Stamp price : ");
        cb_stamp_price.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_stamp_priceItemStateChanged(evt);
            }
        });

        txt_stamp_price.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txt_stamp_price.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_stamp_priceFocusLost(evt);
            }
        });
        txt_stamp_price.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_stamp_priceKeyReleased(evt);
            }
        });

        jLabel3.setText("Rp. ");

        jLabel6.setText("Rp. ");

        jLabel7.setText("Rp. ");

        cbo_rate_transaction.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_rate_transactionItemStateChanged(evt);
            }
        });

        jLabel8.setText("Transaction rate : ");

        jLabel9.setText("Selling Price : ");

        jLabel12.setText("Rp. ");

        jLabel14.setText("Fee : ");

        jLabel16.setText("Rp. ");

        txt_fee.setEditable(false);
        txt_fee.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txt_fee.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_feeCaretUpdate(evt);
            }
        });

        jLabel13.setText("Total : ");

        jLabel18.setText("Rp. ");

        txt_total.setEditable(false);
        txt_total.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txt_total.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_totalCaretUpdate(evt);
            }
        });

        jLabel10.setText("Nominal accepted : ");

        jLabel17.setText("Rp. ");

        txt_nominal_accepted.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txt_nominal_accepted.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_nominal_acceptedKeyReleased(evt);
            }
        });

        jLabel19.setText("To be paid : ");

        txt_must_paid.setEditable(false);
        txt_must_paid.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel20.setText("Rp. ");

        jLabel21.setText("Rp. ");

        txt_rate_transaction.setEditable(false);

        jLabel22.setText("%");

        txt_selling_price.setEditable(false);
        txt_selling_price.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00;(¤#,##0.00)"))));
        txt_selling_price.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txt_selling_priceCaretUpdate(evt);
            }
        });

        txt_rate_machine.setEditable(false);

        jLabel23.setText("%");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cancel))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_must_paid)
                            .addComponent(txt_nominal_accepted)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(txt_rate_transaction, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel22))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(txt_rate_machine, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel23)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbo_rate_transaction, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbo_machine_profile, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_selling_price))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel18))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel16)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_total)
                                    .addComponent(txt_fee)))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(cb_handling_fee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(3, 3, 3)
                                        .addComponent(jLabel3)
                                        .addGap(6, 6, 6))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(7, 7, 7)
                                        .addComponent(jLabel21)
                                        .addGap(3, 3, 3)))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_nominal_transaction, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                                    .addComponent(txt_handling_fee)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cb_transfer_bank, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cb_stamp_price, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel6))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel7)))
                                .addGap(7, 7, 7)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txt_stamp_price, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_transfer_bank, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(txt_member_name, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btn_search, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(dt_transaction_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(cbo_card, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btn_add_card, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(17, 17, 17))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(dt_transaction_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txt_member_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_search, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(txt_nominal_transaction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel21))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cb_handling_fee)
                                    .addComponent(txt_handling_fee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cb_transfer_bank)
                                    .addComponent(jLabel6)))
                            .addComponent(txt_transfer_bank, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cb_stamp_price))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_stamp_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_rate_transaction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txt_rate_transaction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cbo_machine_profile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_rate_machine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12)
                    .addComponent(txt_selling_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16)
                    .addComponent(txt_fee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel18)
                    .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbo_card)
                    .addComponent(btn_add_card))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txt_nominal_accepted, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txt_must_paid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_save)
                    .addComponent(btn_cancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Transaction data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));
        jPanel4.setPreferredSize(new java.awt.Dimension(839, 425));

        tbl_transaction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tbl_transaction);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 596, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Input data", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 887, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 596, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Search data", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_member_nameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_member_nameFocusLost
        if(txt_member_name.getText().length() < 1){

            credit_card_data_model.setRowCount(0);
            cbo_card.setSelectedIndex(0);
            cbo_card.setEnabled(false);
            btn_add_card.setEnabled(false);

            btn_save.setEnabled(false);
            btn_cancel.setEnabled(false);

        }else{
            try{
                //LoadTableCard();
                credit_card_data_model.setRowCount(0);

                // Get card member from database //
                ConnectSQLLite sql_connect = new ConnectSQLLite();
                ArrayList<BankItems> card_items = sql_connect.getCardMemberDataByName(txt_member_name.getText());

                if(!card_items.isEmpty()){
                    isMember = true;
                    btn_add_card.setEnabled(false);
                    cbo_card.setEnabled(false);
                    // Add to table card member //
                    for(BankItems item : card_items){
                        //((MemberCardTableModel)tbl_credit_card_member.getModel()).add(item);
                        credit_card_data_model.addRow(new Object[]{false, item.card_id, item.card_name, item.card_type_name});
                    }
                }else{
                    isMember = false;
                    btn_add_card.setEnabled(true);
                    cbo_card.setEnabled(true);
                    credit_card_data_model.setRowCount(0);
                }

            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Failed to load data : " + e.toString());
            }
        }
    }//GEN-LAST:event_txt_member_nameFocusLost

    private void txt_member_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_member_nameKeyReleased
        if(dt_transaction_date.getEditor().getText().length() < 1 ||
            txt_member_name.getText().length() < 1 ||            
            txt_nominal_transaction.getText().length() < 1 ||
            txt_nominal_accepted.getText().length() < 1 ||            
            !selected_card){

            btn_save.setEnabled(false);
            btn_cancel.setEnabled(false);

            if(txt_member_name.getText().length() < 1){
                credit_card_data_model.setRowCount(0);
                cbo_card.setSelectedIndex(0);
                cbo_card.setEnabled(false);
                btn_add_card.setEnabled(false);
            }
        }else{
            btn_save.setEnabled(true);
            btn_cancel.setEnabled(true);
        }
    }//GEN-LAST:event_txt_member_nameKeyReleased

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed

        search_member_panel = new SearchMemberPanel();
        JDialog search_member_dialog = new JDialog();
        search_member_dialog.add(search_member_panel);
        search_member_dialog.setSize(500, 250);
        search_member_dialog.setResizable(false);
        search_member_dialog.setModal(true);
        search_member_dialog.setLocationRelativeTo(null);
        search_member_dialog.setVisible(true);

        //JOptionPane.showMessageDialog(null, "Invalid username or password");
        if(ValueHolder.getMember_id() != 0){
            isMember = true;
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            MemberItems single_member_item = sql_connection.getSingleMemberData(ValueHolder.getMember_id());
            txt_member_name.setText(single_member_item.fullname);

            // Reset table card //
            credit_card_data_model.setRowCount(0);

            // Get card member from database //
            ConnectSQLLite sql_connect = new ConnectSQLLite();
            ArrayList<BankItems> card_items = sql_connect.getCardMemberData(ValueHolder.getMember_id());

            if(!card_items.isEmpty()){
                isMember = true;
                btn_add_card.setEnabled(false);
                cbo_card.setEnabled(false);
                // Add to table card member //
                for(BankItems item : card_items){
                    //((MemberCardTableModel)tbl_credit_card_member.getModel()).add(item);
                    credit_card_data_model.addRow(new Object[]{false, item.card_id, item.card_name, item.card_type_name});
                }
            }else{
                isMember = false;
                btn_add_card.setEnabled(true);
                cbo_card.setEnabled(true);
                PopulateCard();
            }

            ValueHolder.setMember_id(0);
        }
    }//GEN-LAST:event_btn_searchActionPerformed

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        dt_transaction_date.getEditor().setText("");
        txt_member_name.setText("");
        txt_nominal_transaction.setText("");
        txt_nominal_accepted.setText("");
        
        credit_card_data_model.setRowCount(0);

        btn_add_card.setEnabled(false);
        cbo_card.setEnabled(false);

        btn_save.setEnabled(false);
        btn_cancel.setEnabled(false);

    }//GEN-LAST:event_btn_cancelActionPerformed

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        try{
            ConnectSQLLite sql_connect = new ConnectSQLLite();
            ProfileItems profile_item = sql_connect.getProfileIDbyProfileName(cbo_machine_profile.getSelectedItem().toString());

            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

            TransactionItems items = new TransactionItems();
            items.setTransaction_date(formater.format(dt_transaction_date.getDate()));
            items.setFullname(txt_member_name.getText());
            items.setNominal_transaction(txt_nominal_transaction.getText() == null ? "0" : txt_nominal_transaction.getText());
            items.setNominal_accepted(txt_nominal_accepted.getText() == null ? "0" : txt_nominal_accepted.getText());                        
            items.setCard_id(card_id);            
            items.setProfile(profile_item.profile_id);

            boolean isSuccess = sql_connect.InsertTransaction(items);
            if(isSuccess){
                JOptionPane.showMessageDialog(null, "Transaction save successfully", "Notification", 1);

                dt_transaction_date.getEditor().setText("");
                txt_member_name.setText("");
                txt_nominal_transaction.setText("");
                txt_nominal_accepted.setText("");                

                credit_card_data_model.setRowCount(0);

                btn_add_card.setEnabled(false);
                cbo_card.setSelectedIndex(0);
                cbo_card.setEnabled(false);

                cbo_machine_profile.setSelectedIndex(0);

                btn_save.setEnabled(false);
                btn_cancel.setEnabled(false);

                // Load table transaction //
                populate_table_transaction();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_saveActionPerformed

    private void txt_nominal_transactionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nominal_transactionKeyReleased
        if(dt_transaction_date.getEditor().getText().length() < 1 ||
            txt_member_name.getText().length() < 1 ||            
            txt_nominal_transaction.getText().length() < 1 ||
            txt_nominal_accepted.getText().length() < 1 ||            
            !selected_card){
                        
            nominal_transaction = Long.parseLong(txt_nominal_transaction.getText().isEmpty() ? "0" : txt_nominal_transaction.getText());
                        
            // (E7+K7+L7+M7)*2,4%
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);            
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Double.toString(selling_price));
            
            profile_machine_rate = Double.parseDouble(txt_rate_machine.getText().isEmpty() ? "0" : txt_rate_machine.getText());
            bank_fees =  (long)((nominal_transaction * profile_machine_rate) / 100);                        
            System.out.println("bank fees = " + selling_price);            
            
            clean_fees = selling_price - bank_fees;
            System.out.println("bank fees = " + selling_price);            
            txt_fee.setText(Long.toString(clean_fees));
                        
            btn_save.setEnabled(false);
            btn_cancel.setEnabled(false);
        }else{
            try{
                nominal_transaction = Long.parseLong(txt_nominal_transaction.getText().isEmpty() ? "0" : txt_nominal_transaction.getText());
                        
                // (E7+K7+L7+M7)*2,4%
                selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);            
                System.out.println("selling price = " + selling_price);
                txt_selling_price.setText(Double.toString(selling_price));

                profile_machine_rate = Double.parseDouble(txt_rate_machine.getText().isEmpty() ? "0" : txt_rate_machine.getText());
                bank_fees =  (long)((nominal_transaction * profile_machine_rate) / 100);                        
                System.out.println("bank fees = " + selling_price);            

                clean_fees = selling_price - bank_fees;
                System.out.println("bank fees = " + selling_price);            
                txt_fee.setText(Long.toString(clean_fees));

                btn_save.setEnabled(true);
                btn_cancel.setEnabled(true);
            }catch(Exception e){
                txt_nominal_transaction.setText("0");
                nominal_transaction = Long.parseLong(txt_nominal_transaction.getText().isEmpty() ? "0" : txt_nominal_transaction.getText());
                        
                // (E7+K7+L7+M7)*2,4%
                selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);            
                System.out.println("selling price = " + selling_price);
                txt_selling_price.setText(Double.toString(selling_price));

                profile_machine_rate = Double.parseDouble(txt_rate_machine.getText().isEmpty() ? "0" : txt_rate_machine.getText());
                bank_fees =  (long)((nominal_transaction * profile_machine_rate) / 100);                        
                System.out.println("bank fees = " + selling_price);            

                clean_fees = selling_price - bank_fees;
                System.out.println("bank fees = " + selling_price);            
                txt_fee.setText(Long.toString(clean_fees));
            }            
        }
    }//GEN-LAST:event_txt_nominal_transactionKeyReleased

    private void cb_handling_feeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_handling_feeItemStateChanged
        boolean isSelected = cb_handling_fee.isSelected();
        if(isSelected){
            txt_handling_fee.setText("");
            txt_handling_fee.setEditable(true);            
        }else{
            txt_handling_fee.setText("0");
            txt_handling_fee.setEditable(false);
            
            handling_fee = Long.parseLong(txt_handling_fee.getText().isEmpty() ? "0" : txt_handling_fee.getText());
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());
        
            // (E7+K7+L7+M7)*2,4%
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Double.toString(selling_price));
            
            total = handling_fee + transfer_bank + stamp_price + clean_fees;
            System.out.println("total = " + total);
            txt_total.setText(Long.toString(total));
        }
    }//GEN-LAST:event_cb_handling_feeItemStateChanged

    private void cb_transfer_bankItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_transfer_bankItemStateChanged
        boolean isSelected = cb_transfer_bank.isSelected();
        if(isSelected){
            txt_transfer_bank.setText("0");
            txt_transfer_bank.setEditable(true);            
        }else{
            txt_transfer_bank.setText("0");
            txt_transfer_bank.setEditable(false);
            
            transfer_bank = Long.parseLong(txt_transfer_bank.getText().isEmpty() ? "0" : txt_transfer_bank.getText());
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());
        
            // (E7+K7+L7+M7)*2,4%
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Double.toString(selling_price));
            
            total = handling_fee + transfer_bank + stamp_price + clean_fees;
            System.out.println("total = " + total);
            txt_total.setText(Long.toString(total));
        }
    }//GEN-LAST:event_cb_transfer_bankItemStateChanged

    private void cb_stamp_priceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_stamp_priceItemStateChanged
        boolean isSelected = cb_stamp_price.isSelected();
        if(isSelected){
            txt_stamp_price.setText("");
            txt_stamp_price.setEditable(true);            
        }else{
            txt_stamp_price.setText("0");
            txt_stamp_price.setEditable(false);
            
            stamp_price = Long.parseLong(txt_stamp_price.getText().isEmpty() ? "0" : txt_stamp_price.getText());            
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0.0" : txt_rate_transaction.getText());
        
            // (E7+K7+L7+M7)*2,4%
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Double.toString(selling_price));
            
            total = handling_fee + transfer_bank + stamp_price + clean_fees;
            System.out.println("total = " + total);
            txt_total.setText(Long.toString(total));
        }
    }//GEN-LAST:event_cb_stamp_priceItemStateChanged

    private void cbo_rate_transactionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_rate_transactionItemStateChanged
        if(cbo_rate_transaction.getSelectedIndex() != 0){
            ConnectSQLLite connect_sql = new ConnectSQLLite();
            RateItems item = connect_sql.getRateTransactionPercentage(cbo_rate_transaction.getSelectedIndex());
            txt_rate_transaction.setText(item.rate_value);
            
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Long.toString(selling_price));
            
            //total = handling_fee + transfer_bank + stamp_price + clean_fees;
            //System.out.println("total = " + total);
            //txt_total.setText(Long.toString(total));
        }else{
            txt_rate_transaction.setText("0");
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Long.toString(selling_price));
            
            //total = handling_fee + transfer_bank + stamp_price + clean_fees;
            //System.out.println("total = " + total);
            //txt_total.setText(Long.toString(total));
        }
    }//GEN-LAST:event_cbo_rate_transactionItemStateChanged

    private void txt_nominal_transactionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_nominal_transactionFocusLost
        try{
           nominal_transaction = Long.parseLong(txt_nominal_transaction.getText().isEmpty() ? "0" : txt_nominal_transaction.getText());
           transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());

           // (E7+K7+L7+M7)*2,4%
           selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
           System.out.println("selling price = " + selling_price);
           txt_selling_price.setText(Double.toString(selling_price)); 
           
           //nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
           //total_paid = nominal_gestun - nominal_accepted;
           //txt_must_paid.setText(Long.toString(total_paid));
        }catch(Exception e){
            txt_nominal_transaction.setText("0");            
            nominal_transaction = Long.parseLong(txt_nominal_transaction.getText().isEmpty() ? "0" : txt_nominal_transaction.getText());
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());

            // (E7+K7+L7+M7)*2,4%
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Double.toString(selling_price));
            
            //nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            //total_paid = nominal_gestun - nominal_accepted;
            //txt_must_paid.setText(Long.toString(total_paid));
        }        
    }//GEN-LAST:event_txt_nominal_transactionFocusLost

    private void txt_handling_feeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_handling_feeFocusLost
        try{
            handling_fee = Long.parseLong(txt_handling_fee.getText().isEmpty() ? "0" : txt_handling_fee.getText());
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());

            // (E7+K7+L7+M7)*2,4%
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Double.toString(selling_price));
            
            total = handling_fee + transfer_bank + stamp_price + clean_fees;
            System.out.println("total = " + total);
            txt_total.setText(Long.toString(total));
            
            //nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            //total_paid = nominal_gestun - nominal_accepted;
            //txt_must_paid.setText(Long.toString(total_paid));
        }catch(Exception e){
            txt_handling_fee.setText("0");
            handling_fee = Long.parseLong(txt_handling_fee.getText().isEmpty() ? "0" : txt_handling_fee.getText());
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());

            // (E7+K7+L7+M7)*2,4% //
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Double.toString(selling_price));
            
            total = handling_fee + transfer_bank + stamp_price + clean_fees;
            System.out.println("total = " + total);
            txt_total.setText(Long.toString(total));
            
            //nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            //total_paid = nominal_gestun - nominal_accepted;
            //txt_must_paid.setText(Long.toString(total_paid));
        }        
    }//GEN-LAST:event_txt_handling_feeFocusLost

    private void txt_transfer_bankFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_transfer_bankFocusLost
        try{
            transfer_bank = Long.parseLong(txt_transfer_bank.getText().isEmpty() ? "0" : txt_transfer_bank.getText());
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());

            // (E7+K7+L7+M7)*2,4%
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Double.toString(selling_price));  
            
            total = handling_fee + transfer_bank + stamp_price + clean_fees;
            System.out.println("total = " + total);
            txt_total.setText(Long.toString(total));
            
            //nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            //total_paid = nominal_gestun - nominal_accepted;
            //txt_must_paid.setText(Long.toString(total_paid));
        }catch(Exception e){
            txt_transfer_bank.setText("0");
            transfer_bank = Long.parseLong(txt_transfer_bank.getText().isEmpty() ? "0" : txt_transfer_bank.getText());
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());

            // (E7+K7+L7+M7)*2,4%
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Double.toString(selling_price));
            
            total = handling_fee + transfer_bank + stamp_price + clean_fees;
            System.out.println("total = " + total);
            txt_total.setText(Long.toString(total));
            
            //nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            //total_paid = nominal_gestun - nominal_accepted;
            //txt_must_paid.setText(Long.toString(total_paid));
        }        
    }//GEN-LAST:event_txt_transfer_bankFocusLost

    private void txt_stamp_priceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_stamp_priceFocusLost
        try{
            stamp_price = Long.parseLong(txt_stamp_price.getText().isEmpty() ? "0" : txt_stamp_price.getText());       
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());

            // (E7+K7+L7+M7)*2,4%
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Double.toString(selling_price));
            
            total = handling_fee + transfer_bank + stamp_price + clean_fees;
            System.out.println("total = " + total);
            txt_total.setText(Long.toString(total));
            
            //nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            //total_paid = nominal_gestun - nominal_accepted;
            //txt_must_paid.setText(Long.toString(total_paid));
        }catch(Exception e){
            txt_stamp_price.setText("0");
            stamp_price = Long.parseLong(txt_stamp_price.getText().isEmpty() ? "0" : txt_stamp_price.getText());       
            transaction_rate = Double.parseDouble(txt_rate_transaction.getText().isEmpty() ? "0" : txt_rate_transaction.getText());

            // (E7+K7+L7+M7)*2,4%
            selling_price =  (long)(((nominal_transaction + handling_fee + transfer_bank + stamp_price) * transaction_rate) / 100);
            System.out.println("selling price = " + selling_price);
            txt_selling_price.setText(Double.toString(selling_price));
            
            total = handling_fee + transfer_bank + stamp_price + clean_fees;
            System.out.println("total = " + total);
            txt_total.setText(Long.toString(total));
            
            //nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            //total_paid = nominal_gestun - nominal_accepted;
            //txt_must_paid.setText(Long.toString(total_paid));
        }        
    }//GEN-LAST:event_txt_stamp_priceFocusLost

    private void txt_handling_feeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_handling_feeKeyReleased
        handling_fee = Long.parseLong(txt_handling_fee.getText().isEmpty() ? "0" : txt_handling_fee.getText());
    }//GEN-LAST:event_txt_handling_feeKeyReleased

    private void txt_transfer_bankKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_transfer_bankKeyReleased
        transfer_bank = Long.parseLong(txt_transfer_bank.getText().isEmpty() ? "0" : txt_transfer_bank.getText());
    }//GEN-LAST:event_txt_transfer_bankKeyReleased

    private void txt_stamp_priceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_stamp_priceKeyReleased
        stamp_price = Long.parseLong(txt_stamp_price.getText().isEmpty() ? "0" : txt_stamp_price.getText());
    }//GEN-LAST:event_txt_stamp_priceKeyReleased

    private void cbo_machine_profileItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_machine_profileItemStateChanged
        if(cbo_machine_profile.getSelectedIndex() != 0){
            ConnectSQLLite connect_sql = new ConnectSQLLite();
            RateItems item = connect_sql.getRateProfilePercentage(cbo_machine_profile.getSelectedIndex());
            txt_rate_machine.setText(item.rate_value);
            System.out.println("percentage profile = " + item.rate_value);
            
            profile_machine_rate = Double.parseDouble(txt_rate_machine.getText().isEmpty() ? "0" : txt_rate_machine.getText());
            bank_fees =  (long)((nominal_transaction * profile_machine_rate) / 100);                        
            System.out.println("bank fees = " + selling_price);            
            
            clean_fees = selling_price - bank_fees;
            System.out.println("clean fees = " + selling_price);            
            txt_fee.setText(Long.toString(clean_fees));
            
            total = handling_fee + transfer_bank + stamp_price + clean_fees;
            System.out.println("total = " + total);
            txt_total.setText(Long.toString(total));
            
            //nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            //total_paid = nominal_gestun - nominal_accepted;
            //txt_must_paid.setText(Long.toString(total_paid));
        }else{
            txt_rate_machine.setText("0");
            profile_machine_rate = Double.parseDouble(txt_rate_machine.getText().isEmpty() ? "0" : txt_rate_machine.getText());
            bank_fees =  (long)((nominal_transaction * profile_machine_rate) / 100);                        
            System.out.println("bank fees = " + selling_price);            
            
            clean_fees = selling_price - bank_fees;
            System.out.println("bank fees = " + selling_price);            
            txt_fee.setText(Long.toString(clean_fees));
            
            total = handling_fee + transfer_bank + stamp_price + clean_fees;
            System.out.println("total = " + total);
            txt_total.setText(Long.toString(total));
            
            //nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            //total_paid = nominal_gestun - nominal_accepted;
            //txt_must_paid.setText(Long.toString(total_paid));
        }
    }//GEN-LAST:event_cbo_machine_profileItemStateChanged

    private void txt_totalCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_totalCaretUpdate
        try{
            nominal_gestun = total + selling_price + nominal_transaction;  
            nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            
            total_paid = nominal_gestun - nominal_accepted;
            txt_must_paid.setText(Long.toString(total_paid));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Internal error : " + e.toString()); 
        }
    }//GEN-LAST:event_txt_totalCaretUpdate

    private void txt_nominal_acceptedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nominal_acceptedKeyReleased
        if(txt_nominal_accepted.getText().length() < 1){
            nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            total_paid = nominal_gestun - nominal_accepted;
            txt_must_paid.setText(Long.toString(total_paid));
        }else{
            nominal_accepted = Long.parseLong(txt_nominal_accepted.getText().isEmpty() ? "0" : txt_nominal_accepted.getText());
            total_paid = nominal_gestun - nominal_accepted;
            txt_must_paid.setText(Long.toString(total_paid));
        }
    }//GEN-LAST:event_txt_nominal_acceptedKeyReleased

    private void txt_selling_priceCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_selling_priceCaretUpdate
        clean_fees = selling_price - bank_fees;
        txt_fee.setText(Long.toString(clean_fees));
    }//GEN-LAST:event_txt_selling_priceCaretUpdate

    private void txt_feeCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txt_feeCaretUpdate
        total = handling_fee + transfer_bank + stamp_price + clean_fees;
        txt_total.setText(Long.toString(total));
    }//GEN-LAST:event_txt_feeCaretUpdate


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_card;
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_search;
    private javax.swing.JCheckBox cb_handling_fee;
    private javax.swing.JCheckBox cb_stamp_price;
    private javax.swing.JCheckBox cb_transfer_bank;
    private javax.swing.JComboBox cbo_card;
    private javax.swing.JComboBox cbo_machine_profile;
    private javax.swing.JComboBox cbo_rate_transaction;
    private org.jdesktop.swingx.JXDatePicker dt_transaction_date;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbl_credit_card_member;
    private javax.swing.JTable tbl_transaction;
    private javax.swing.JTextField txt_fee;
    private javax.swing.JFormattedTextField txt_handling_fee;
    private javax.swing.JTextField txt_member_name;
    private javax.swing.JTextField txt_must_paid;
    private javax.swing.JFormattedTextField txt_nominal_accepted;
    private javax.swing.JFormattedTextField txt_nominal_transaction;
    private javax.swing.JTextField txt_rate_machine;
    private javax.swing.JTextField txt_rate_transaction;
    private javax.swing.JFormattedTextField txt_selling_price;
    private javax.swing.JFormattedTextField txt_stamp_price;
    private javax.swing.JTextField txt_total;
    private javax.swing.JFormattedTextField txt_transfer_bank;
    // End of variables declaration//GEN-END:variables
}
