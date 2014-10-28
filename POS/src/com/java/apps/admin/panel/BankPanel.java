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
public class BankPanel extends javax.swing.JPanel {
    
    private BankItems items;    
    private boolean isUpdate = false;
    private BankTableModel bank_table_model = new BankTableModel();
    
    private int bank_id = 0;

    /**
     * Creates new form BankPanel
     */
    public BankPanel() {
        initComponents();
        
        // Set initial save button disable //
        btn_add_bank.setEnabled(false);
        btn_cancel.setEnabled(false);
        
        // Reload table //
        Reload_Table();
    }
    
    public void Reload_Table(){
        
        // Insert member data model into table rate //
        bank_table_model = new BankTableModel();
        tbl_bank.setModel(bank_table_model);
        tbl_bank.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_bank.getColumnModel().getColumn(0).setPreferredWidth(150);  // Action //        
        tbl_bank.getColumnModel().getColumn(1).setPreferredWidth(298);  // Bank Name //        
                        
        EditDeleteRenderer edit_delete_renderer = new EditDeleteRenderer();
        tbl_bank.getColumnModel().getColumn(0).setCellRenderer(edit_delete_renderer);
        tbl_bank.getColumnModel().getColumn(0).setCellEditor(new EditdeleteEditor());
        tbl_bank.setRowHeight(edit_delete_renderer.getTableCellRendererComponent(tbl_bank, null, true, true, 0, 0).getPreferredSize().height);  
                
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<BankItems> bank_items = sql_connection.getBankData();
                       
        int columns = bank_items.size();                      
        for (int x = 0; x < columns; x++){                            
            bank_table_model.add(bank_items.get(x));
        }         
    }
    
    public class BankTableModel extends AbstractTableModel {

        private ArrayList<BankItems> items;

        public BankTableModel() {
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
                    value = "Bank Name";
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
            }
            return value;
        }

        @Override
        public int getRowCount() {
            return items.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            BankItems obj = items.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:
                    break;                
                case 1:
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
            boolean isSuccess = sql_connection.RemoveBank(value);
            if(isSuccess){
                int startIndex = items.indexOf(value);                
                items.remove(value);
                fireTableRowsInserted(startIndex, startIndex);
                
                if(isUpdate){
                    txt_bank_name.setText("");

                    btn_add_bank.setText("Save");
                    btn_add_bank.setEnabled(false);
                    btn_cancel.setEnabled(false);

                    isUpdate = false; 
                }else{
                    txt_bank_name.setText("");
                    
                    btn_add_bank.setEnabled(false);
                    btn_cancel.setEnabled(false);                  
                }                
            }             
        }
        
        public void edit(BankItems value){            
            isUpdate = true;
            bank_id = value.bank_id;
            txt_bank_name.setText(value.bank_name);
            
            btn_add_bank.setText("Update");
            btn_add_bank.setEnabled(true);
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
        tbl_bank = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lbl_bank_name = new javax.swing.JLabel();
        txt_bank_name = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btn_add_bank = new javax.swing.JButton();
        btn_cancel = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bank data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        tbl_bank.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbl_bank);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bank profile", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        lbl_bank_name.setText("Bank Name : ");

        txt_bank_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_bank_nameKeyReleased(evt);
            }
        });

        jLabel1.setText("*");

        btn_add_bank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_save_small.png"))); // NOI18N
        btn_add_bank.setText("Save");
        btn_add_bank.setIconTextGap(9);
        btn_add_bank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_bankActionPerformed(evt);
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

        jLabel2.setText("note : * mandatory field");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lbl_bank_name)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_bank_name, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_add_bank, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_bank_name)
                    .addComponent(txt_bank_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btn_add_bank)
                    .addComponent(btn_cancel))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        if(isUpdate){
            txt_bank_name.setText("");

            btn_add_bank.setText("Save");
            btn_add_bank.setEnabled(false);
            btn_cancel.setEnabled(false);

            isUpdate = false;
        }else{
            txt_bank_name.setText("");
            
            btn_add_bank.setEnabled(false);
            btn_cancel.setEnabled(false);
        }        
    }//GEN-LAST:event_btn_cancelActionPerformed

    private void btn_add_bankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_bankActionPerformed
        if(isUpdate){
            items = new BankItems();
            items.setBank_id(bank_id);
            items.setBank_name(txt_bank_name.getText());            
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.UpdateBank(items);
            if(isSuccess){
                Reload_Table();

                // Reset text input //
                txt_bank_name.setText("");                

                // Reset button //
                btn_add_bank.setText("Save");
                btn_add_bank.setEnabled(false);
                btn_cancel.setEnabled(false);

                isUpdate = false;
            }    
        }else{
            items = new BankItems();
            items.setBank_name(txt_bank_name.getText());                          
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.InsertBank(items);
            if(isSuccess){
                Reload_Table();
                
                // Reset text input //
                txt_bank_name.setText("");                

                // Reset button //                
                btn_add_bank.setEnabled(false);
                btn_cancel.setEnabled(false);
            }    
        } 
    }//GEN-LAST:event_btn_add_bankActionPerformed

    private void txt_bank_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_bank_nameKeyReleased
        if(txt_bank_name.getText().length() < 1){
            btn_add_bank.setEnabled(false);
            btn_cancel.setEnabled(false);
        }else{
            btn_add_bank.setEnabled(true);
            btn_cancel.setEnabled(true);
        }
    }//GEN-LAST:event_txt_bank_nameKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_bank;
    private javax.swing.JButton btn_cancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_bank_name;
    private javax.swing.JTable tbl_bank;
    private javax.swing.JTextField txt_bank_name;
    // End of variables declaration//GEN-END:variables
}
