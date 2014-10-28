/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.java.apps.pojo;

/**
 *
 * @author Ultimate
 */
public class LoginData {
    
    public static String username;
    public static String login_time;

    public static void setUsername(String username) {
        LoginData.username = username;
    }

    public static void setLogin_time(String login_time) {
        LoginData.login_time = login_time;
    }           
}
