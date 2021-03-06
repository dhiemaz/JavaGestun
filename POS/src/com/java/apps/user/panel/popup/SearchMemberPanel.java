/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.java.apps.user.panel.popup;

import com.java.apps.database.ConnectSQLLite;
import com.java.apps.pojo.MemberItems;
import com.java.apps.pojo.ValueHolder;
import java.awt.Color;
import java.awt.Window;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ultimate
 */
public class SearchMemberPanel extends javax.swing.JPanel {
    
    DefaultTableModel member_data_model;
    private int member_id = 0;

    /**
     * Creates new form SearchMemberPanel
     */
    public SearchMemberPanel() {
        initComponents();
        
        // Set Property //
        btn_ok.setEnabled(false);
        
        ReloadTable();
    }
    
    private void ReloadTable(){
        member_data_model = new DefaultTableModel(){
            Class[] types = new Class[]{                
                java.lang.Integer.class, 
                java.lang.String.class                             
            };    
                
            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
               
        member_data_model.addColumn("Citizenship");
        member_data_model.addColumn("Fullname");        
        
        tbl_member_data.setModel(member_data_model);
        tbl_member_data.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                
        tbl_member_data.getColumnModel().getColumn(0).setPreferredWidth(150);   // citizenship //
        tbl_member_data.getColumnModel().getColumn(1).setPreferredWidth(195);   // fullname //        
        
        tbl_member_data.setRowSelectionAllowed(true);
        tbl_member_data.setSelectionBackground(Color.blue);
        ListSelectionModel rowSelectionModel = tbl_member_data.getSelectionModel();       
        rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        ConnectSQLLite sql_connection = new ConnectSQLLite();
        ArrayList<MemberItems> member_items = sql_connection.getMemberData();
        
        for (MemberItems item : member_items){
            member_data_model.addRow(new Object[]{item.citizenship,
                                                  item.fullname
                                                 });
                                                  
        }   
        
        rowSelectionModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                
                Object[] colData = new Object[tbl_member_data.getColumnCount()];  
                for(int i = 0; i < tbl_member_data.getColumnCount(); i++) {  
                        colData[i] = tbl_member_data.getValueAt(tbl_member_data.getSelectedRow(), i) == null ? " " : tbl_member_data.getValueAt(tbl_member_data.getSelectedRow(), i);  
                }  
                
                btn_ok.setEnabled(true);
                member_id = Integer.parseInt(colData[0].toString());                                 
                //ValueHolder.setMember_id(member_id);
            }
        });                    
    }   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_member_data = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txt_member_name = new javax.swing.JTextField();
        btn_cancel = new javax.swing.JButton();
        btn_ok = new javax.swing.JButton();

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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/member_icon1_small.png"))); // NOI18N
        jLabel1.setText("Member name : ");

        txt_member_name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_member_nameKeyReleased(evt);
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

        btn_ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_ok_small.png"))); // NOI18N
        btn_ok.setText("Ok");
        btn_ok.setIconTextGap(9);
        btn_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_okActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_member_name, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cancel))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_member_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancel)
                    .addComponent(btn_ok))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_okActionPerformed
        ValueHolder.setMember_id(member_id);
        ValueHolder.setPayment_member_id(member_id);
        ValueHolder.setSearch_payment_member_id(member_id);
        ValueHolder.setSearch_transaction_member_id(member_id);
        Window window = SwingUtilities.getWindowAncestor(this);
        window.setVisible(false);
    }//GEN-LAST:event_btn_okActionPerformed

    private void txt_member_nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_member_nameKeyReleased
        if(txt_member_name.getText().length() < 1){
             member_data_model.setRowCount(0);
             ReloadTable();
        }else{
            member_data_model.setRowCount(0);
            ConnectSQLLite sql_connection = new ConnectSQLLite();
            ArrayList<MemberItems> member_items = sql_connection.SearchMemberByName(txt_member_name.getText());
            
            for(MemberItems item : member_items){
                member_data_model.addRow(new Object[]{item.citizenship, 
                                                      item.fullname, 
                                                     });
            }  
        } 
    }//GEN-LAST:event_txt_member_nameKeyReleased

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelActionPerformed
        ValueHolder.setMember_id(0);
        ValueHolder.setPayment_member_id(0);
        ValueHolder.setSearch_payment_member_id(0);
        ValueHolder.setSearch_transaction_member_id(0);
        Window window = SwingUtilities.getWindowAncestor(this);
        window.setVisible(false);
    }//GEN-LAST:event_btn_cancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_ok;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_member_data;
    private javax.swing.JTextField txt_member_name;
    // End of variables declaration//GEN-END:variables
}
