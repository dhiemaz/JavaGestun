/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.java.apps.admin.panel;

import com.java.apps.database.ConnectSQLLite;
import com.java.apps.pojo.ProfileItems;
import com.java.apps.pojo.RateItems;
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
public class ProfilePanel extends javax.swing.JPanel {
    
    private ProfileTableModel profile_data_model = new ProfileTableModel();
    private RateItems items = new RateItems();
    private boolean isUpdate = false;
    private int profile_id = 0, rate_id = 0;    

    /**
     * Creates new form newProfilePanel
     */
    public ProfilePanel() {
        initComponents();
    }
    
    private void Reload_Table(){
        profile_data_model = new ProfileTableModel();
        tbl_profile.setModel(profile_data_model);
        tbl_profile.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_profile.getColumnModel().getColumn(0).setPreferredWidth(150);   // Action //        
        tbl_profile.getColumnModel().getColumn(1).setPreferredWidth(160);   // Profile Name        
        tbl_profile.getColumnModel().getColumn(2).setPreferredWidth(150);   // Persentase //
        
        EditDeleteRenderer edit_delete_renderer = new EditDeleteRenderer();
        tbl_profile.getColumnModel().getColumn(0).setCellRenderer(edit_delete_renderer);
        tbl_profile.getColumnModel().getColumn(0).setCellEditor(new EditdeleteEditor());
        tbl_profile.setRowHeight(edit_delete_renderer.getTableCellRendererComponent(tbl_profile, null, true, true, 0, 0).getPreferredSize().height);                                                                                            
                
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<ProfileItems> profile_items = sql_connection.getProfileData();
                       
        int columns = profile_items.size();                      
        for (int x = 0; x < columns; x++){                            
            profile_data_model.add(profile_items.get(x));
        }        
    }
    
    private void populate_cbo_rate() throws Exception {     
        ConnectSQLLite sql_connect = new ConnectSQLLite();        
        try {               
            ArrayList<RateItems> rate_items = sql_connect.PopulateRates();
            for (RateItems rate_item : rate_items) {
                cbo_profile_type.addItem(rate_item.rate_name);
            }
            
        } catch (Exception e) {  
            JOptionPane.showMessageDialog(null, "Failed to get rate data : " + e.toString());  
        }  
    }  
    
    public class ProfileTableModel extends AbstractTableModel {
        private ArrayList<ProfileItems> items;

        public ProfileTableModel() {
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
                    value = "Nama Profil";
                    break;                
                case 2:
                    value = "Persentase (%)";
                    break;               
            }
            return value;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class value = Object.class;
            switch (columnIndex) {                
                case 1:
                    // profile name //
                    value = String.class;
                    break;                
                case 2:
                    // rate value //
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
            ProfileItems obj = items.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:
                    break;                
                case 1:
                    value = obj.profile_name;
                    break;                
                case 2:
                    value = obj.rate_name;
                    break;
                
            }
            return value;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                ProfileItems value = items.get(rowIndex);
                if ("edit".equals(aValue)) {
                    edit(value);                    
                } else {
                    remove(value);                            
                }                                
            }
        }

        public void add(ProfileItems value) {
            int startIndex = getRowCount();
            items.add(value);
            fireTableRowsInserted(startIndex, getRowCount() - 1);
        }

        public void remove(ProfileItems value) {
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            boolean isSuccess = sql_connection.RemoveProfile(value);
            if(isSuccess){
                int startIndex = items.indexOf(value);                
                items.remove(value);
                fireTableRowsInserted(startIndex, startIndex);
                
                if(isUpdate){
                    // Reset text input //
                    txt_profile_name1.setText("");
                    cbo_profile_type.setSelectedIndex(0);
                    
                    // Reset button //
                    btn_add_profile1.setText("Simpan");
                    btn_add_profile1.setEnabled(false);
                    btn_cancel_add_profile.setEnabled(false);
                    
                    isUpdate = false;
                }else{
                    // Reset text input //
                    txt_profile_name1.setText("");
                    cbo_profile_type.setSelectedIndex(0);
                    
                    // Reset button //                    
                    btn_add_profile1.setEnabled(false);
                    btn_cancel_add_profile.setEnabled(false);                                        
                }
            }    
        }
        
        public void edit(ProfileItems value){
            isUpdate = true;
            profile_id = value.profile_id;
            txt_profile_name1.setText(value.profile_name);            
            cbo_profile_type.setSelectedItem(value.rate_name);
            
            btn_add_profile1.setText("Ubah");
            btn_add_profile1.setEnabled(true);
            btn_cancel_add_profile.setEnabled(true);
        }
        
            public void deleteData() {
                int rows = getRowCount();
                if (rows == 0) {
                    return;
                }                
                fireTableRowsDeleted(0, rows - 1);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_profile = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_profile_name1 = new javax.swing.JTextField();
        btn_add_profile1 = new javax.swing.JButton();
        btn_cancel_add_profile = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        cbo_profile_type = new javax.swing.JComboBox();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Profile data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        tbl_profile.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbl_profile);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));

        jLabel2.setText("Nama Profil :");

        txt_profile_name1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_profile_name1ActionPerformed(evt);
            }
        });
        txt_profile_name1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_profile_name1KeyReleased(evt);
            }
        });

        btn_add_profile1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_save_small.png"))); // NOI18N
        btn_add_profile1.setText("Simpan");
        btn_add_profile1.setIconTextGap(9);
        btn_add_profile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_add_profile1ActionPerformed(evt);
            }
        });

        btn_cancel_add_profile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_cancel_small.png"))); // NOI18N
        btn_cancel_add_profile.setText("Batal");
        btn_cancel_add_profile.setIconTextGap(9);
        btn_cancel_add_profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancel_add_profileActionPerformed(evt);
            }
        });

        jLabel5.setText("Persentase : ");

        cbo_profile_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Select Type --", "Profil Member", "Profil Non-Member / Umum", "Profil Cicilan 3 Bulan", "Profil Cicilan 6 Bulan", "Profil Cicilan 12 Bulan", "Profil Dana Talangan" }));

        jLabel1.setText("%");

        jLabel3.setText("Tipe : ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(btn_add_profile1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_cancel_add_profile, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cbo_profile_type, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1))
                            .addComponent(txt_profile_name1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_profile_name1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbo_profile_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_add_profile1)
                    .addComponent(btn_cancel_add_profile))
                .addGap(57, 57, 57))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Profil mesin MDR", jPanel1);

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

    private void txt_profile_name1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_profile_name1KeyReleased
        if(txt_profile_name1.getText().length() < 1){
            btn_add_profile1.setEnabled(false);
            btn_cancel_add_profile.setEnabled(false);
        }else{
            btn_add_profile1.setEnabled(true);
            btn_cancel_add_profile.setEnabled(true);
        }
    }//GEN-LAST:event_txt_profile_name1KeyReleased

    private void btn_add_profile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_add_profile1ActionPerformed
        if(isUpdate){
            ProfileItems items = new ProfileItems();
            items.setProfile_id(profile_id);
            items.setProfile_name(txt_profile_name1.getText());            
            items.setRate_id(cbo_profile_type.getSelectedIndex());

            ConnectSQLLite sql_connect = new ConnectSQLLite();
            boolean isSuccess = sql_connect.InsertProfile(items);
            if(isSuccess){
                // Reload table //
                Reload_Table();

                // Reset text input //
                txt_profile_name1.setText("");
                cbo_profile_type.setSelectedIndex(0);

                // Reset button //
                btn_add_profile1.setText("Simpan");
                btn_add_profile1.setEnabled(false);
                btn_cancel_add_profile.setEnabled(false);

                isUpdate = false;
            }
        }else{
            ProfileItems items = new ProfileItems();
            items.setProfile_name(txt_profile_name1.getText());            
            items.setRate_id(cbo_profile_type.getSelectedIndex());

            ConnectSQLLite sql_connect = new ConnectSQLLite();
            boolean isSuccess = sql_connect.InsertProfile(items);
            if(isSuccess){
                // Reload table //
                Reload_Table();

                // Reset text input //
                txt_profile_name1.setText("");
                cbo_profile_type.setSelectedIndex(0);

                // Reset button //
                btn_add_profile1.setEnabled(false);
                btn_cancel_add_profile.setEnabled(false);
            }
        }
    }//GEN-LAST:event_btn_add_profile1ActionPerformed

    private void btn_cancel_add_profileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancel_add_profileActionPerformed
        if(isUpdate){
            isUpdate = false;
            txt_profile_name1.setText("");            
            cbo_profile_type.setSelectedIndex(0);
            
            btn_add_profile1.setText("Simpan");
            btn_add_profile1.setEnabled(false);
            btn_cancel_add_profile.setEnabled(false);
        }else{
            txt_profile_name1.setText("");            
            cbo_profile_type.setSelectedIndex(0);
            
            btn_add_profile1.setEnabled(false);
            btn_cancel_add_profile.setEnabled(false);
        }
    }//GEN-LAST:event_btn_cancel_add_profileActionPerformed

    private void txt_profile_name1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_profile_name1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_profile_name1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add_profile1;
    private javax.swing.JButton btn_cancel_add_profile;
    private javax.swing.JComboBox cbo_profile_type;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbl_profile;
    private javax.swing.JTextField txt_profile_name1;
    // End of variables declaration//GEN-END:variables
}
