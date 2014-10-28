/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.java.apps.admin.panel;

import com.java.apps.database.ConnectSQLLite;
import com.java.apps.pojo.BankItems;
import java.awt.Component;
import java.awt.GridBagLayout;
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

/**
 *
 * @author Ultimate
 */
public class CardPanel extends javax.swing.JPanel {
    
    private BankItems items;    
    private boolean isUpdate = false;
    private CardTableModel card_table_model = new CardTableModel();
    private int card_id = 0;

    /**
     * Creates new form CardPanel
     */
    public CardPanel() {
        initComponents();
        
        btn_add_card.setEnabled(false);
        btn_cancel.setEnabled(false);
        
        try{
            // Populate combobox //
            populate_cbo_bank();
        
            // Reload table //
            Reload_Table();
        }catch(Exception e){
            e.printStackTrace();
        }        
    }
    
    public void Reload_Table(){
        
        // Insert member data model into table rate //
        card_table_model = new CardTableModel();
        tbl_card.setModel(card_table_model);
        tbl_card.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_card.getColumnModel().getColumn(0).setPreferredWidth(150);  // Action //        
        tbl_card.getColumnModel().getColumn(1).setPreferredWidth(200);  // Card Name        
        tbl_card.getColumnModel().getColumn(2).setPreferredWidth(150);  // Card Type //
        tbl_card.getColumnModel().getColumn(3).setPreferredWidth(200);  // Bank Issuer //
        
                
        EditDeleteRenderer edit_delete_renderer = new EditDeleteRenderer();
        tbl_card.getColumnModel().getColumn(0).setCellRenderer(edit_delete_renderer);
        tbl_card.getColumnModel().getColumn(0).setCellEditor(new EditdeleteEditor());
        tbl_card.setRowHeight(edit_delete_renderer.getTableCellRendererComponent(tbl_card, null, true, true, 0, 0).getPreferredSize().height);  
                
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<BankItems> card_items = sql_connection.getCardData();
                       
        int columns = card_items.size();                      
        for (int x = 0; x < columns; x++){                            
            card_table_model.add(card_items.get(x));
        }        
    }
    
    public class CardTableModel extends AbstractTableModel {

        private ArrayList<BankItems> items;

        public CardTableModel() {
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
                    value = "Card Name";
                    break;                
                case 2:
                    value = "Card Type";
                    break;
                case 3:
                    value = "Bank Issuer";
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
                case 3:
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
                    value = obj.card_name;
                    break;                
                case 2:
                    value = obj.card_type_name;
                    break;
                case 3:
                    value = obj.bank_name;
                    break;                
            }
            return value;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                BankItems value = items.get(rowIndex);
                if ("edit".equals(aValue)) {                    
                    edit(value);                    
                } else {                     
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
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.RemoveCard(value);
            if(isSuccess){
                int startIndex = items.indexOf(value);                
                items.remove(value);
                fireTableRowsInserted(startIndex, startIndex);
                
                if(isUpdate){
                    txt_card_name.setText("");
                    txt_card_type.setText("");

                    btn_add_card.setText("Save");
                    btn_add_card.setEnabled(false);
                    btn_cancel.setEnabled(false);

                    isUpdate = false; 
                }else{
                    txt_card_name.setText("");
                    txt_card_type.setText("");
                    
                    btn_add_card.setEnabled(false);
                    btn_cancel.setEnabled(false);                 
                }                
            }             
        }
        
        public void edit(BankItems value){            
            isUpdate = true;
            card_id = value.card_id;
            txt_card_name.setText(value.card_name);
            txt_card_type.setText(value.card_type_name);
            cbo_bank.setSelectedIndex(value.bank_id);
            
            btn_add_card.setText("Update");
            btn_add_card.setEnabled(true);
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
    
    private void populate_cbo_bank() throws Exception {     
        ConnectSQLLite sql_connect = new ConnectSQLLite();        
        try {               
            ArrayList<BankItems> bank_items = sql_connect.getBankData();
            for (BankItems bank_item : bank_items) {
                cbo_bank.addItem(bank_item.bank_name);
            }            
        } catch (Exception e) {  
            JOptionPane.showMessageDialog(null, "Failed to get rate data : " + e.toString());  
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_card = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lbl_card_name = new javax.swing.JLabel();
        txt_card_name = new javax.swing.JTextField();
        lbl_card_type = new javax.swing.JLabel();
        txt_card_type = new javax.swing.JTextField();
        lbl_bank_issuer = new javax.swing.JLabel();
        cbo_bank = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btn_cancel = new javax.swing.JButton();
        btn_add_card = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Card Data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        tbl_card.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbl_card);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Card Profile", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        lbl_card_name.setText("Card name : ");

        txt_card_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_card_nameKeyReleased(evt);
            }
        });

        lbl_card_type.setText("Card type : ");

        txt_card_type.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_card_typeKeyReleased(evt);
            }
        });

        lbl_bank_issuer.setText("Bank Issuer : ");

        jLabel1.setText("*");

        jLabel2.setText("*");

        jLabel3.setText("*");

        jLabel4.setText("note : * mandatory field");

        btn_cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel.setText("Cancel");
        btn_cancel.setIconTextGap(9);
        btn_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelActionPerformed(evt);
            }
        });

        btn_add_card.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_save_small.png"))); // NOI18N
        btn_add_card.setText("Save");
        btn_add_card.setIconTextGap(9);
        btn_add_card.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_cardActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                        .addComponent(btn_add_card, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_bank_issuer, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                            .addComponent(lbl_card_type, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_card_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cbo_bank, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_card_name, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txt_card_type, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_card_name)
                    .addComponent(txt_card_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_card_type)
                    .addComponent(txt_card_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_bank_issuer)
                    .addComponent(cbo_bank, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(btn_cancel)
                    .addComponent(btn_add_card))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_card_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_card_nameKeyReleased
        if(txt_card_name.getText().length() < 1 ||
           txt_card_type.getText().length() < 1){
           
            btn_add_card.setEnabled(false);
            btn_cancel.setEnabled(false);
        }else{
            btn_add_card.setEnabled(true);
            btn_cancel.setEnabled(true);
        }
    }//GEN-LAST:event_txt_card_nameKeyReleased

    private void txt_card_typeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_card_typeKeyReleased
        if(txt_card_name.getText().length() < 1 ||
           txt_card_type.getText().length() < 1){
           
            btn_add_card.setEnabled(false);
            btn_cancel.setEnabled(false);
        }else{
            btn_add_card.setEnabled(true);
            btn_cancel.setEnabled(true);
        }
    }//GEN-LAST:event_txt_card_typeKeyReleased

    private void btn_add_cardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_cardActionPerformed
        if(isUpdate){
            items = new BankItems();
            items.setCard_id(card_id);
            items.setCard_name(txt_card_name.getText()); 
            items.setCard_type_name(txt_card_type.getText());
            items.setBank_id(cbo_bank.getSelectedIndex() + 1);
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.UpdateCard(items);
            if(isSuccess){
                Reload_Table();

                // Reset text input //
                txt_card_name.setText("");                
                txt_card_type.setText("");

                // Reset button //
                btn_add_card.setText("Save");
                btn_add_card.setEnabled(false);
                btn_cancel.setEnabled(false);

                isUpdate = false;
            }    
        }else{
            items = new BankItems();
            items.setCard_name(txt_card_name.getText());   
            items.setCard_type_name(txt_card_type.getText());
            items.setBank_id(cbo_bank.getSelectedIndex() + 1);
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.InsertCard(items);
            if(isSuccess){
                Reload_Table();                

                // Reset text input //
                txt_card_name.setText("");                
                txt_card_type.setText("");

                // Reset button //                
                btn_add_card.setEnabled(false);
                btn_cancel.setEnabled(false);
            }    
        } 
    }//GEN-LAST:event_btn_add_cardActionPerformed

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        if(isUpdate){            
            // Reset text input //
            txt_card_name.setText("");                
            txt_card_type.setText("");

            // Reset button //
            btn_add_card.setText("Save");
            btn_add_card.setEnabled(false);
            btn_cancel.setEnabled(false);

            isUpdate = false;              
        }else{
            // Reset text input //
            txt_card_name.setText("");                
            txt_card_type.setText("");

            // Reset button //            
            btn_add_card.setEnabled(false);
            btn_cancel.setEnabled(false);

            isUpdate = false;    
        } 
    }//GEN-LAST:event_btn_cancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_card;
    private javax.swing.JButton btn_cancel;
    private javax.swing.JComboBox cbo_bank;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_bank_issuer;
    private javax.swing.JLabel lbl_card_name;
    private javax.swing.JLabel lbl_card_type;
    private javax.swing.JTable tbl_card;
    private javax.swing.JTextField txt_card_name;
    private javax.swing.JTextField txt_card_type;
    // End of variables declaration//GEN-END:variables
}
