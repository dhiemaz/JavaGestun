/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.java.apps;

import com.java.apps.frame.LoginFrame;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Dimas.Yudha
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {           
            UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
            LoginFrame loginfrm = new LoginFrame();
            loginfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginfrm.setResizable(false);
            loginfrm.setLocationRelativeTo(null);
            loginfrm.setVisible(true);    
            loginfrm.pack();
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    } 
}
