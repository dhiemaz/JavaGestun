/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.apps.frame;

import com.java.apps.admin.panel.BankPanel;
import com.java.apps.admin.panel.CardPanel;
import com.java.apps.admin.panel.ProfilePanel;
import com.java.apps.admin.panel.RatePanel;
import com.java.apps.admin.panel.UserPanel;
import com.java.apps.pojo.LoginData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Dimas.Yudha
 */
public class AdminFrame extends javax.swing.JFrame {
    
    private UserPanel user_panel;    
    private RatePanel rate_panel;    
    private ProfilePanel profile_panel;
    private BankPanel bank_panel;
    private CardPanel card_panel;

    /**
     * Creates new form AdminController
     */
    public AdminFrame() {
        initComponents();
        
        // Get today date //                      
        lbl_header_user_login_time_value.setText(LoginData.login_time);
        lbl_header_user_login_value.setText(LoginData.username);
        
        // Start timer //
        startTimer();
        
        menu_tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {                
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)menu_tree.getLastSelectedPathComponent();                
                if(selectedNode.getUserObject().equals("User Setup")){                    
                    content_panel.removeAll();
                    user_panel = new UserPanel();  
                    user_panel.setBounds(0, 0, 500, 530);
                    content_panel.add(user_panel);                    
                    setVisible(true);
                    content_panel.repaint();
                    content_panel.revalidate();                                                                        
                }else if(selectedNode.getUserObject().equals("Seting Rate Pejualan")){
                    content_panel.removeAll();
                    rate_panel = new RatePanel();
                    rate_panel.setBounds(0, 0, 500, 530);
                    content_panel.add(rate_panel);
                    setVisible(true);
                    content_panel.repaint();
                    content_panel.revalidate();
                }else if(selectedNode.getUserObject().equals("Seting mesin MDR")){
                    content_panel.removeAll();
                    profile_panel = new ProfilePanel();
                    profile_panel.setBounds(0, 0, 500, 530);
                    content_panel.add(profile_panel);
                    setVisible(true);
                    content_panel.repaint();
                    content_panel.revalidate();
                }else if(selectedNode.getUserObject().equals("Seting Bank")){
                    content_panel.removeAll();
                    bank_panel = new BankPanel();
                    bank_panel.setBounds(0, 0, 500, 530);
                    content_panel.add(bank_panel);
                    setVisible(true);
                    content_panel.repaint();
                    content_panel.revalidate();
                }else if(selectedNode.getUserObject().equals("Seting Kartu Kredit")){
                    content_panel.removeAll();
                    card_panel = new CardPanel();
                    card_panel.setBounds(0, 0, 500, 530);
                    content_panel.add(card_panel);
                    setVisible(true);
                    content_panel.repaint();
                    content_panel.revalidate();
                }else if(selectedNode.getUserObject().equals("Exit")){
                    int confirmed = JOptionPane.showConfirmDialog(null, "Exit Application ?", "Exit Application", JOptionPane.YES_NO_OPTION);
                    if (confirmed == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }else if(selectedNode.getUserObject().equals("Logout")){
                    LoginFrame loginfrm = new LoginFrame();
                    loginfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    loginfrm.setLocationRelativeTo(null);
                    loginfrm.setVisible(true);    
                    loginfrm.pack();                    
                    dispose();
                }else{
                    // Doing nothing //
                }
            }
        });
    }
    
    public final void startTimer(){
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String nol_jam = "", nol_menit = "",nol_detik = "";
                java.util.Date dateTime = new java.util.Date();
                int nilai_jam = dateTime.getHours();
                int nilai_menit = dateTime.getMinutes();
                int nilai_detik = dateTime.getSeconds();

                if(nilai_jam <= 9) nol_jam= "0";
                if(nilai_menit <= 9) nol_menit= "0";
                if(nilai_detik <= 9) nol_detik= "0";

                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                String date = sdf.format(new Date());    

                lbl_time.setText("Date : "+ date +", Time : "+jam+":"+menit+":"+detik);
            }
        };
        new Timer(1000, taskPerformer).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header_panel = new javax.swing.JPanel();
        lbl_header_application = new javax.swing.JLabel();
        lbl_header_login_user = new javax.swing.JLabel();
        lbl_header_user_login_value = new javax.swing.JLabel();
        lbl_header_user_login_time = new javax.swing.JLabel();
        lbl_header_user_login_time_value = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbl_time = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        menu_tree = new javax.swing.JTree();
        content_panel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        header_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbl_header_application.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lbl_header_application.setText("POINT OF SALES version 1.0");

        lbl_header_login_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_user_small1.png"))); // NOI18N
        lbl_header_login_user.setText("Login : ");

        lbl_header_user_login_value.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_header_user_login_value.setText("administrator");

        lbl_header_user_login_time.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_login_time_small.png"))); // NOI18N
        lbl_header_user_login_time.setText("Login Time : ");

        lbl_header_user_login_time_value.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_header_user_login_time_value.setText("13:00");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/java/apps/resources/icon_login_time_small.png"))); // NOI18N
        jLabel1.setText("Last login : ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("-");

        lbl_time.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout header_panelLayout = new javax.swing.GroupLayout(header_panel);
        header_panel.setLayout(header_panelLayout);
        header_panelLayout.setHorizontalGroup(
            header_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(header_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(header_panelLayout.createSequentialGroup()
                        .addComponent(lbl_header_login_user)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_header_user_login_value)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_header_user_login_time)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_header_user_login_time_value)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(header_panelLayout.createSequentialGroup()
                        .addComponent(lbl_header_application, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_time)))
                .addContainerGap())
        );
        header_panelLayout.setVerticalGroup(
            header_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(header_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_header_application)
                    .addComponent(lbl_time))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(header_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_header_login_user)
                    .addComponent(lbl_header_user_login_value)
                    .addComponent(lbl_header_user_login_time)
                    .addComponent(lbl_header_user_login_time_value)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Menu");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("User Management");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("User Setup");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Machine Management");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Seting Rate Pejualan");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Seting mesin MDR");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Seting Bank");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Seting Kartu Kredit");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Setting");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Application Setting");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Logout");
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Exit");
        treeNode1.add(treeNode2);
        menu_tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(menu_tree);

        javax.swing.GroupLayout content_panelLayout = new javax.swing.GroupLayout(content_panel);
        content_panel.setLayout(content_panelLayout);
        content_panelLayout.setHorizontalGroup(
            content_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 547, Short.MAX_VALUE)
        );
        content_panelLayout.setVerticalGroup(
            content_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(header_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(content_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(header_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                    .addComponent(content_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel content_panel;
    private javax.swing.JPanel header_panel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_header_application;
    private javax.swing.JLabel lbl_header_login_user;
    private javax.swing.JLabel lbl_header_user_login_time;
    private javax.swing.JLabel lbl_header_user_login_time_value;
    private javax.swing.JLabel lbl_header_user_login_value;
    private javax.swing.JLabel lbl_time;
    private javax.swing.JTree menu_tree;
    // End of variables declaration//GEN-END:variables
}
