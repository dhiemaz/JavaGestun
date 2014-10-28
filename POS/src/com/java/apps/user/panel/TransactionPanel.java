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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Ultimate
 */
public class TransactionPanel extends javax.swing.JPanel {
    
    DefaultTableModel credit_card_data_model, transaction_data_model;
    SearchMemberPanel search_member_panel;    
    
    private MemberCardTableModel card_table_model = new MemberCardTableModel();    
    private boolean isMember = false, searchMember = false, selected_card = false;
    private int card_id = 0;
    
    /**
     * Creates new form TransactionPanel1
     */
    public TransactionPanel() {
        initComponents();
        
        // Tab Insert Transaction //
        btn_add_card.setEnabled(false);
        cbo_card.setEnabled(false);
        btn_save.setEnabled(false);
        btn_cancel.setEnabled(false);
        
        // Tab Search Transaction //
        cbo_search_criteria.setEnabled(true);
        txt_customer_name.setEnabled(false);
        btn_search_name.setEnabled(false);
        dt_date_to.setEnabled(false);
        dt_date_from.setEnabled(false);
        btn_search_transaction.setEnabled(false);
        
        // Nominal Transaction //
        AbstractDocument ad_txt_nominal_transaction = (AbstractDocument)txt_nominal_transaction.getDocument();
        ad_txt_nominal_transaction.setDocumentFilter(new FieldListener());
        
        // Nominal Accepted //
        AbstractDocument ad_txt_nominal_accepted = (AbstractDocument)txt_nominal_accepted.getDocument();
        ad_txt_nominal_accepted.setDocumentFilter(new FieldListener());
        
        // Settle Amount //
        AbstractDocument ad_txt_total_settle = (AbstractDocument)txt_settle_amount.getDocument();
        ad_txt_total_settle.setDocumentFilter(new FieldListener());
        
        // Cash Amount //
        AbstractDocument ad_txt_cash_amount = (AbstractDocument)txt_cash_amount.getDocument();
        ad_txt_cash_amount.setDocumentFilter(new FieldListener());
                
        // Bank Charge //
        AbstractDocument ad_txt_bank_charge = (AbstractDocument)txt_bank_charge.getDocument();
        ad_txt_bank_charge.setDocumentFilter(new FieldListener());
        
        // Load table transaction card member //
        LoadTableTransactionCard();
        
        // Populate credit card database //
        PopulateCard();
        PopulateProfile();
        
        // Load table transaction //
        populate_table_transaction();
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
    
    private void PopulateCard(){
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<BankItems> bank_items = sql_connection.getCardData();
        
        cbo_card.addItem("-- select card --");
        for (BankItems item : bank_items){
            cbo_card.addItem(item.card_name);
        }   
    }
    
    private void PopulateProfile(){
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<ProfileItems> items = sql_connection.getProfileData();
        
        cbo_machine_profile.addItem("-- select profile --");
        for (ProfileItems item : items){
            cbo_machine_profile.addItem(item.profile_name);
        }   
    }
    
    private void LoadTableTransactionCard(){
        credit_card_data_model = new DefaultTableModel(){
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }            
        };
             
        credit_card_data_model.addColumn("");
        credit_card_data_model.addColumn("Id");        
        credit_card_data_model.addColumn("Card Name");
        credit_card_data_model.addColumn("Type");
        
        tbl_credit_card_member.setModel(credit_card_data_model);
        tbl_credit_card_member.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        tbl_credit_card_member.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbl_credit_card_member.getColumnModel().getColumn(1).setPreferredWidth(25);
        tbl_credit_card_member.getColumnModel().getColumn(2).setPreferredWidth(140);
        tbl_credit_card_member.getColumnModel().getColumn(3).setPreferredWidth(120);        
        
        credit_card_data_model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int nRow = credit_card_data_model.getRowCount();
                int nChoose = 0, nMarked = 0;  
                
                for (int i = 0; i < nRow; i++) {
                    boolean choose = Boolean.parseBoolean(credit_card_data_model.getValueAt(i, 0).toString());
                    if (choose && nMarked == 0) {  
                        card_id = Integer.parseInt(credit_card_data_model.getValueAt(i, 1).toString());
                        nChoose++;
                        nMarked = 1;                                     
                    }else{
                        card_id = 0;
                        if(nChoose != 0 && nMarked == 0){
                           nChoose--;
                           nMarked = 1;
                        }                                                                                                         
                    }
                }
                
                if(nChoose > 0){
                    selected_card = true;
                    if(dt_transaction_date.getEditor().getText().length() < 1 ||
                       txt_member_name.getText().length() < 1 ||
                       txt_percentage.getText().length() < 1 ||
                       txt_nominal_transaction.getText().length() < 1 ||
                       txt_nominal_accepted.getText().length() < 1 ||
                       txt_settle_amount.getText().length() < 1 ||
                       txt_cash_amount.getText().length() < 1 ||
                       txt_bank_charge.getText().length() < 1 ||
                       !selected_card){
            
                        btn_save.setEnabled(false);
                        btn_cancel.setEnabled(false);            
                    }else{
                        btn_save.setEnabled(true);
                        btn_cancel.setEnabled(true);  
                    }                    
                }else{
                    btn_save.setEnabled(false);
                    btn_cancel.setEnabled(false);                                                            
                }
            }            
        });
    }
    
    private void LoadTableCard(){
        // Insert member data model into table member //
        card_table_model = new MemberCardTableModel();
        tbl_credit_card_member.setModel(card_table_model);
        tbl_credit_card_member.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_credit_card_member.getColumnModel().getColumn(0).setPreferredWidth(35);  // action //
        tbl_credit_card_member.getColumnModel().getColumn(1).setPreferredWidth(25);  // card id //
        tbl_credit_card_member.getColumnModel().getColumn(2).setPreferredWidth(140);  // card name //
        tbl_credit_card_member.getColumnModel().getColumn(3).setPreferredWidth(120);  // card type //              
                        
        DeleteRenderer delete_renderer = new DeleteRenderer();
        tbl_credit_card_member.getColumnModel().getColumn(0).setCellRenderer(delete_renderer);
        tbl_credit_card_member.getColumnModel().getColumn(0).setCellEditor(new DeleteEditor());
        tbl_credit_card_member.setRowHeight(delete_renderer.getTableCellRendererComponent(tbl_credit_card_member, null, true, true, 0, 0).getPreferredSize().height);                         
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
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dt_transaction_date = new org.jdesktop.swingx.JXDatePicker();
        jLabel2 = new javax.swing.JLabel();
        txt_member_name = new javax.swing.JTextField();
        btn_search = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_transaction_note = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        cbo_machine_profile = new javax.swing.JComboBox();
        btn_cancel = new javax.swing.JButton();
        btn_save = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_credit_card_member = new javax.swing.JTable();
        btn_add_card = new javax.swing.JButton();
        txt_percentage = new javax.swing.JFormattedTextField();
        txt_nominal_transaction = new javax.swing.JFormattedTextField();
        txt_nominal_accepted = new javax.swing.JFormattedTextField();
        cbo_card = new javax.swing.JComboBox();
        txt_settle_amount = new javax.swing.JFormattedTextField();
        txt_cash_amount = new javax.swing.JFormattedTextField();
        txt_bank_charge = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_transaction = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_transaction1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cbo_search_criteria = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        txt_customer_name = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        dt_date_from = new org.jdesktop.swingx.JXDatePicker();
        jLabel15 = new javax.swing.JLabel();
        dt_date_to = new org.jdesktop.swingx.JXDatePicker();
        btn_search_transaction = new javax.swing.JButton();
        btn_search_name = new javax.swing.JButton();

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1150, 648));

        jPanel1.setPreferredSize(new java.awt.Dimension(1159, 620));

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

        jLabel3.setText("Percentage : ");

        jLabel4.setText("%");

        jLabel5.setText("Nominal transaction : ");

        jLabel6.setText("Nominal accepted : ");

        jLabel7.setText("Total Settle : ");

        jLabel8.setText("Cash amount : ");

        jLabel10.setText("Note : ");

        txt_transaction_note.setColumns(20);
        txt_transaction_note.setRows(5);
        jScrollPane1.setViewportView(txt_transaction_note);

        jLabel11.setText("EDC Machine : ");

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

        jLabel12.setText("Bank charge : ");

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

        try {
            txt_percentage.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#.#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txt_percentage.setToolTipText("Percentage");
        txt_percentage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_percentageKeyReleased(evt);
            }
        });

        txt_nominal_transaction.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txt_nominal_transaction.setToolTipText("Nominal Transaction (Rp)");
        txt_nominal_transaction.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_nominal_transactionKeyReleased(evt);
            }
        });

        txt_nominal_accepted.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txt_nominal_accepted.setToolTipText("Nominal Accepted (Rp)");
        txt_nominal_accepted.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_nominal_acceptedKeyReleased(evt);
            }
        });

        txt_settle_amount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));

        txt_cash_amount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));

        txt_bank_charge.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cbo_card, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_add_card))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cancel))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbo_machine_profile, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel10)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(txt_member_name, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btn_search, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                    .addComponent(dt_transaction_date, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_percentage, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_bank_charge)
                                    .addComponent(txt_cash_amount)
                                    .addComponent(txt_settle_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_nominal_transaction)
                                    .addComponent(txt_nominal_accepted, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addContainerGap())
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
                    .addComponent(btn_search, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txt_member_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_percentage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_nominal_transaction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_nominal_accepted, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_settle_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_cash_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txt_bank_charge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cbo_machine_profile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbo_card)
                    .addComponent(btn_add_card))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_save)
                    .addComponent(btn_cancel))
                .addContainerGap())
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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
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
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 856, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Input data", jPanel1);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Transaction Data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        tbl_transaction1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(tbl_transaction1);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Search parameter", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jLabel9.setText("Search by : ");

        cbo_search_criteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Select category search --", "Customer name", "Transaction date", "Customer name and Transaction date" }));
        cbo_search_criteria.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbo_search_criteriaItemStateChanged(evt);
            }
        });

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/member_icon1_small.png"))); // NOI18N
        jLabel13.setText("Customer name : ");

        txt_customer_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_customer_nameActionPerformed(evt);
            }
        });

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_calender_small.png"))); // NOI18N
        jLabel14.setText("Date from : ");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_calender_small.png"))); // NOI18N
        jLabel15.setText("Date to : ");

        btn_search_transaction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_search_small.png"))); // NOI18N
        btn_search_transaction.setText("Search");
        btn_search_transaction.setIconTextGap(9);

        btn_search_name.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_search_small.png"))); // NOI18N
        btn_search_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_search_nameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbo_search_criteria, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_customer_name, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_search_name, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dt_date_from, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dt_date_to, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_search_transaction, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(177, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cbo_search_criteria, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_customer_name, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_search_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dt_date_from, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dt_date_to, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_search_transaction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.getAccessibleContext().setAccessibleName("Transaction data");

        jTabbedPane1.addTab("Search data", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1229, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 628, Short.MAX_VALUE)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Transaction data");
    }// </editor-fold>//GEN-END:initComponents

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

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        dt_transaction_date.getEditor().setText("");
        txt_member_name.setText("");
        txt_nominal_transaction.setText("");
        txt_nominal_accepted.setText("");
        txt_settle_amount.setText("");
        txt_cash_amount.setText("");
        txt_bank_charge.setText("");
        txt_transaction_note.setText("");
        
        credit_card_data_model.setRowCount(0);
        
        btn_add_card.setEnabled(false);
        cbo_card.setEnabled(false);
        
        btn_save.setEnabled(false);
        btn_cancel.setEnabled(false);
        
    }//GEN-LAST:event_btn_cancelActionPerformed

    private void txt_percentageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_percentageKeyReleased
        if(dt_transaction_date.getEditor().getText().length() < 1 ||
           txt_member_name.getText().length() < 1 ||
           txt_percentage.getText().length() < 1 ||
           txt_nominal_transaction.getText().length() < 1 ||
           txt_nominal_accepted.getText().length() < 1 ||
           txt_settle_amount.getText().length() < 1 ||
           txt_cash_amount.getText().length() < 1 ||
           txt_bank_charge.getText().length() < 1 ||
           !selected_card){
            
            btn_save.setEnabled(false);
            btn_cancel.setEnabled(false);            
        }else{
            btn_save.setEnabled(true);
            btn_cancel.setEnabled(true);  
        }
    }//GEN-LAST:event_txt_percentageKeyReleased

    private void txt_nominal_transactionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nominal_transactionKeyReleased
        if(dt_transaction_date.getEditor().getText().length() < 1 ||
           txt_member_name.getText().length() < 1 ||
           txt_percentage.getText().length() < 1 ||
           txt_nominal_transaction.getText().length() < 1 ||
           txt_nominal_accepted.getText().length() < 1 ||
           txt_settle_amount.getText().length() < 1 ||
           txt_cash_amount.getText().length() < 1 ||
           txt_bank_charge.getText().length() < 1 ||
           !selected_card){
            
            btn_save.setEnabled(false);
            btn_cancel.setEnabled(false);            
        }else{
            btn_save.setEnabled(true);
            btn_cancel.setEnabled(true);  
        }
    }//GEN-LAST:event_txt_nominal_transactionKeyReleased

    private void txt_nominal_acceptedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nominal_acceptedKeyReleased
        if(dt_transaction_date.getEditor().getText().length() < 1 ||
           txt_member_name.getText().length() < 1 ||
           txt_percentage.getText().length() < 1 ||
           txt_nominal_transaction.getText().length() < 1 ||
           txt_nominal_accepted.getText().length() < 1 ||
           txt_settle_amount.getText().length() < 1 ||
           txt_cash_amount.getText().length() < 1 ||
           txt_bank_charge.getText().length() < 1 ||
           !selected_card){
            
            btn_save.setEnabled(false);
            btn_cancel.setEnabled(false);            
        }else{
            btn_save.setEnabled(true);
            btn_cancel.setEnabled(true);  
        }
    }//GEN-LAST:event_txt_nominal_acceptedKeyReleased

    private void txt_member_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_member_nameKeyReleased
        if(dt_transaction_date.getEditor().getText().length() < 1 ||
           txt_member_name.getText().length() < 1 ||
           txt_percentage.getText().length() < 1 ||
           txt_nominal_transaction.getText().length() < 1 ||
           txt_nominal_accepted.getText().length() < 1 ||
           txt_settle_amount.getText().length() < 1 ||
           txt_cash_amount.getText().length() < 1 ||
           txt_bank_charge.getText().length() < 1 ||
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

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        try{
            ConnectSQLLite sql_connect = new ConnectSQLLite();            
            ProfileItems profile_item = sql_connect.getProfileIDbyProfileName(cbo_machine_profile.getSelectedItem().toString());
           
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
                                    
            TransactionItems items = new TransactionItems();
            items.setTransaction_date(formater.format(dt_transaction_date.getDate()));
            items.setFullname(txt_member_name.getText());            
            items.setPercentage(txt_percentage.getText() == null ? "0" : txt_percentage.getText());
            items.setNominal_transaction(txt_nominal_transaction.getText() == null ? "0" : txt_nominal_transaction.getText());
            items.setNominal_accepted(txt_nominal_accepted.getText() == null ? "0" : txt_nominal_accepted.getText());
            items.setTotal_settle(txt_settle_amount.getText() == null ? "0" : txt_settle_amount.getText());
            items.setCash_amount(txt_cash_amount.getText() == null ? "0" : txt_cash_amount.getText());
            items.setBank_charge(txt_bank_charge.getText() == null ? "0" : txt_bank_charge.getText());             
            items.setCard_id(card_id);
            items.setNote(txt_transaction_note.getText());
            items.setProfile(profile_item.profile_id);
            
            boolean isSuccess = sql_connect.InsertTransaction(items);
            if(isSuccess){
                JOptionPane.showMessageDialog(null, "Transaction save successfully", "Notification", 1);
                
                dt_transaction_date.getEditor().setText("");
                txt_member_name.setText("");
                txt_nominal_transaction.setText("");
                txt_nominal_accepted.setText("");
                txt_settle_amount.setText("");
                txt_cash_amount.setText("");
                txt_bank_charge.setText("");
                txt_transaction_note.setText("");
                txt_percentage.setText("");

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

    private void txt_customer_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_customer_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_customer_nameActionPerformed

    private void btn_search_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_search_nameActionPerformed
        search_member_panel = new SearchMemberPanel();
        JDialog search_member_dialog = new JDialog();
        search_member_dialog.add(search_member_panel);
        search_member_dialog.setSize(500, 250);
        search_member_dialog.setResizable(false);
        search_member_dialog.setModal(true);
        search_member_dialog.setLocationRelativeTo(null);
        search_member_dialog.setVisible(true);
        
        if(ValueHolder.getSearch_transaction_member_id()!= 0){
            isMember = true;            
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            MemberItems single_member_item = sql_connection.getSingleMemberData(ValueHolder.getSearch_transaction_member_id());
            txt_customer_name.setText(single_member_item.fullname);                                    
            ValueHolder.setSearch_transaction_member_id(0);            
        }
    }//GEN-LAST:event_btn_search_nameActionPerformed

    private void cbo_search_criteriaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbo_search_criteriaItemStateChanged
        switch(cbo_search_criteria.getSelectedIndex()){
            case 0 :
                txt_customer_name.setEnabled(false);
                dt_date_to.setEnabled(false);
                dt_date_to.setEnabled(false);
                btn_search_name.setEnabled(false);
                btn_search_transaction.setEnabled(false);
                break;
            case 1 : 
                txt_customer_name.setEnabled(true);
                btn_search_name.setEnabled(true);
                dt_date_to.setEnabled(false);
                dt_date_from.setEnabled(false);
                btn_search_transaction.setEnabled(true);
                break;
            case 2 : 
                txt_customer_name.setEnabled(false);
                btn_search_name.setEnabled(false);
                dt_date_to.setEnabled(true);
                dt_date_from.setEnabled(true);
                btn_search_transaction.setEnabled(true);
                break;
            case 3 :
                txt_customer_name.setEnabled(true);
                btn_search_name.setEnabled(true);
                dt_date_to.setEnabled(true);
                dt_date_from.setEnabled(true);
                btn_search_transaction.setEnabled(true);
                break;
        }
    }//GEN-LAST:event_cbo_search_criteriaItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_card;
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_search_name;
    private javax.swing.JButton btn_search_transaction;
    private javax.swing.JComboBox cbo_card;
    private javax.swing.JComboBox cbo_machine_profile;
    private javax.swing.JComboBox cbo_search_criteria;
    private org.jdesktop.swingx.JXDatePicker dt_date_from;
    private org.jdesktop.swingx.JXDatePicker dt_date_to;
    private org.jdesktop.swingx.JXDatePicker dt_transaction_date;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbl_credit_card_member;
    private javax.swing.JTable tbl_transaction;
    private javax.swing.JTable tbl_transaction1;
    private javax.swing.JFormattedTextField txt_bank_charge;
    private javax.swing.JFormattedTextField txt_cash_amount;
    private javax.swing.JTextField txt_customer_name;
    private javax.swing.JTextField txt_member_name;
    private javax.swing.JFormattedTextField txt_nominal_accepted;
    private javax.swing.JFormattedTextField txt_nominal_transaction;
    private javax.swing.JFormattedTextField txt_percentage;
    private javax.swing.JFormattedTextField txt_settle_amount;
    private javax.swing.JTextArea txt_transaction_note;
    // End of variables declaration//GEN-END:variables
}
