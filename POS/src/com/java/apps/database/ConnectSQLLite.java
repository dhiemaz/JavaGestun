/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.java.apps.database;

import com.java.apps.config.Configuration;
import com.java.apps.pojo.AdditionalRateItems;
import com.java.apps.pojo.AttachmentItem;
import com.java.apps.pojo.BankItems;
import com.java.apps.pojo.CardMemberItems;
import com.java.apps.pojo.MemberItems;
import com.java.apps.pojo.ProfileItems;
import com.java.apps.pojo.RateItems;
import com.java.apps.pojo.TransactionItems;
import com.java.apps.pojo.UserItems;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Ultimate
 */
public class ConnectSQLLite {
    Connection connect;
    PreparedStatement ps;
    Statement stmt;
    
    private String urlDatabase = Configuration.jdbcURI;
    
    public boolean InsertRate(RateItems items){ 
    
        System.out.println("Rate name = " + items.rate_name);
        System.out.println("Percentage (%) = " + items.rate_value);
        
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="INSERT INTO table_rate (rate_name, percentage) VALUES (?, ?)"; 
            ps = connect.prepareStatement(query);        
            ps.setString(1, items.rate_name);
            ps.setFloat(2, Float.parseFloat(items.rate_value));
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
            
            System.out.println("Insert rate data successfully!");                                       
            JOptionPane.showMessageDialog(null, "Rate save successfully", "Notification", 1);
            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException | NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Failed to save rate : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    
    public boolean InsertRateTransaction(RateItems items){ 
    
        System.out.println("Rate name = " + items.rate_name);
        System.out.println("Percentage (%) = " + items.rate_value);
        
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="INSERT INTO table_rate_transaction (rate_name, percentage) VALUES (?, ?)"; 
            ps = connect.prepareStatement(query);        
            ps.setString(1, items.rate_name);
            ps.setFloat(2, Float.parseFloat(items.rate_value));
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
            
            System.out.println("Insert rate transaction data successfully!");                                       
            JOptionPane.showMessageDialog(null, "Rate transaction save successfully", "Notification", 1);
            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException | NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Failed to save rate transaction : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public boolean InsertAdditionalCharge(AdditionalRateItems items){ 
                    
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="INSERT INTO table_additional_charge (charge_name, amount) VALUES (?, ?)"; 
            ps = connect.prepareStatement(query);        
            ps.setString(1, items.rate_name);
            ps.setString(2, items.amount);
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
            
            System.out.println("Insert additional charge data successfully!");                                       
            JOptionPane.showMessageDialog(null, "Additional charge save successfully", "Notification", 1);
            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException | NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Failed to save additional charge : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public boolean InsertProfile(ProfileItems items){                    
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="INSERT INTO table_profile (profile_name, rate_id) VALUES (?, ?)"; 
            ps = connect.prepareStatement(query);        
            ps.setString(1, items.profile_name);            
            ps.setInt(4, items.rate_id);
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
            
            System.out.println("Insert profile data successfully!");                                       
            JOptionPane.showMessageDialog(null, "Profile save successfully", "Notification", 1);
            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to save profile : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean RemoveRate(RateItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_rate WHERE rate_id = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, value.rate_id);            
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
            
            System.out.println("Remove rate data successfully!");                                       
            JOptionPane.showMessageDialog(null, "Rate remove successfully", "Notification", 1);
            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove rate : " + e.toString()); 
            
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public boolean RemoveAdditionalRate(AdditionalRateItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_rate_additional_charge WHERE rate_id = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, value.rate_id);            
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
                                                               
            JOptionPane.showMessageDialog(null, "Additional rate remove successfully", "Notification", 1);
            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove additional rate : " + e.toString()); 
            
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    
    public boolean RemoveTransactionRate(RateItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_rate_transaction WHERE rate_id = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, value.rate_id);            
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
            
            System.out.println("Remove rate data successfully!");                                       
            JOptionPane.showMessageDialog(null, "Rate remove successfully", "Notification", 1);
            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove rate : " + e.toString()); 
            
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public RateItems getRateProfilePercentage(int profile_id){
        
        ResultSet rs = null;  
        RateItems item = new RateItems();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // get rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT rate_id from table_profile where profile_id = " + profile_id);
            while(rs.next()){                              
               item.setRate_id(rs.getInt("rate_id"));               
            }   
            System.out.println("rate id : " + item.rate_id);
            
            rs = stmt.executeQuery("SELECT percentage from table_rate where rate_id = " + item.rate_id);
            while(rs.next()){                              
               item.setRate_value(Float.toString(rs.getFloat("percentage")));               
            }  
            
            System.out.println("percentage : " + item.rate_value);
            
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get rate data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return item;
    } 
    
    public RateItems getRateTransactionPercentage(int rate_id){
        
        ResultSet rs = null;  
        RateItems item = new RateItems();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // get rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT percentage from table_rate_transaction where rate_id = " + rate_id);
            while(rs.next()){                              
               item.setRate_value(Float.toString(rs.getFloat("percentage")));               
            }                                                                             
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get rate data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return item;
    } 
    
    public ArrayList<RateItems> getRateTransactionData(){
        
        ResultSet rs = null;  
        ArrayList<RateItems> rate_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * from table_rate_transaction");
            while(rs.next()){
               RateItems item = new RateItems();
               item.setRate_id(rs.getInt("rate_id"));
               item.setRate_name(rs.getString("rate_name"));
               item.setRate_value(Float.toString(rs.getFloat("percentage")));
               rate_items.add(item);
            }  
            
            System.out.println("get rate data successfully!");                                                   
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get rate data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return rate_items;
    } 
    
    public ArrayList<RateItems> getRateData(){
        
        ResultSet rs = null;  
        ArrayList<RateItems> rate_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * from table_rate");
            while(rs.next()){
               RateItems item = new RateItems();
               item.setRate_id(rs.getInt("rate_id"));
               item.setRate_name(rs.getString("rate_name"));
               item.setRate_value(Float.toString(rs.getFloat("percentage")));
               rate_items.add(item);
            }  
            
            System.out.println("get rate data successfully!");                                                   
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get rate data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return rate_items;
    } 
    
    public ArrayList<AdditionalRateItems> getAdditionalChargeData(){
        
        ResultSet rs = null;  
        ArrayList<AdditionalRateItems> rate_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * from table_additional_charge");
            while(rs.next()){
               AdditionalRateItems item = new AdditionalRateItems();
               item.setRate_id(rs.getInt("rate_id"));
               item.setRate_name(rs.getString("charge_name"));
               item.setAmount(rs.getString("amounts"));
               rate_items.add(item);
            }  
            
            System.out.println("get additional rate data successfully!");                                                   
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get additional rate data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return rate_items;
    } 
    
    
    public ArrayList<RateItems> PopulateRates(){
        
        ResultSet rs = null;  
        ArrayList<RateItems> rate_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT rate_id, rate_name from table_rate");
            while(rs.next()){
               RateItems item = new RateItems();
               item.setRate_id(rs.getInt("rate_id"));
               item.setRate_name(rs.getString("rate_name"));               
               rate_items.add(item);
            }  
            
            System.out.println("rate items : " + rate_items.size());                                                   
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get rate data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return rate_items;
    } 
    
    public ArrayList<ProfileItems> getProfileData(){
        
        ResultSet rs = null;  
        ArrayList<ProfileItems> profile_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT a.profile_id, a.profile_name, "
                    + "b.rate_name, b.percentage from table_profile a JOIN table_rate b "
                    + "ON a.rate_id = b.rate_id");
            
            while(rs.next()){
               ProfileItems item = new ProfileItems();
               item.setProfile_id(rs.getInt("profile_id"));
               item.setProfile_name(rs.getString("profile_name"));               
               item.setRate_name(rs.getString("rate_name"));               
               item.setRate_value(rs.getString("percentage"));               
               profile_items.add(item);
            }  
            
            System.out.println("get profile data successfully!");                                                   
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get profile data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        return profile_items;
    } 
    
    public boolean RemoveProfile(ProfileItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_profile WHERE profile_id = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, value.profile_id);            
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
            
            JOptionPane.showMessageDialog(null, "Profile remove successfully", "Notification", 1);            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove profile : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean UpdateProfile(ProfileItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            int updateCount = stmt.executeUpdate("UPDATE table_profile SET profile_name = '"+ value.profile_name
                              +"', rate_id = " + value.rate_id +" WHERE profile_id = " + value.profile_id);
            
            if(updateCount > 0){
                JOptionPane.showMessageDialog(null, "Profile updated successfully", "Notification", 1);            
                result =  true;
            }else{
                JOptionPane.showMessageDialog(null, "Profile not updated!", "Notification", 1);            
                result =  false;
            }
            
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to update profile : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean UpdateAdditionalCharge(AdditionalRateItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            int updateCount = stmt.executeUpdate("UPDATE table_additional_charge SET charge_name = '"+ value.rate_name
                              +"', amount = '"+ value.amount +"' WHERE id = " + value.rate_id);
            
            if(updateCount > 0){
                JOptionPane.showMessageDialog(null, "Additional charge update successfully", "Notification", 1);            
                result =  true;
            }else{
                JOptionPane.showMessageDialog(null, "Failed update additional charge", "Notification", 1);            
                result =  false;
            }
            
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed update additional charge : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean UpdateTransactionRate(RateItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            int updateCount = stmt.executeUpdate("UPDATE table_rate_transaction SET rate_name = '"+ value.rate_name
                              +"', percentage = "+ value.rate_value+ " WHERE rate_id = " + value.rate_id);
            
            if(updateCount > 0){
                JOptionPane.showMessageDialog(null, "Rate transaction update successfully", "Notification", 1);            
                result =  true;
            }else{
                JOptionPane.showMessageDialog(null, "Rate transaction not updated!", "Notification", 1);            
                result =  false;
            }
            
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to update rate transaction : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean UpdateRate(RateItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            int updateCount = stmt.executeUpdate("UPDATE table_rate SET rate_name = '"+ value.rate_name
                              +"', percentage = "+ value.rate_value+ " WHERE rate_id = " + value.rate_id);
            
            if(updateCount > 0){
                JOptionPane.showMessageDialog(null, "Rate update successfully", "Notification", 1);            
                result =  true;
            }else{
                JOptionPane.showMessageDialog(null, "Rate not updated!", "Notification", 1);            
                result =  false;
            }
            
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to update rate : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public ArrayList<UserItems> getUserData(){
        
        ResultSet rs = null;  
        ArrayList<UserItems> user_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * FROM table_user");
            
            while(rs.next()){
               UserItems item = new UserItems();
               item.setUser_id(rs.getInt("user_id"));
               item.setUsername(rs.getString("username"));
               item.setFullname(rs.getString("fullname"));
               item.setAddress(rs.getString("address"));
               item.setCity(rs.getString("city"));
               item.setZipcode(rs.getString("zipcode"));
               item.setPhone(rs.getString("phone"));
               item.setMobile(rs.getString("mobile"));
               item.setEmail(rs.getString("email"));
               item.setUser_type(rs.getString("role"));
               item.setPassword(rs.getString("password")); 
               item.setUser_picture(rs.getBytes("picture"));
               user_items.add(item);
            }  
            
            System.out.println("get user data successfully!");                                                   
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get user data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return user_items;
    }
    
    public boolean isAuthenticate(String username, String password, String role){
        
        ResultSet rs = null;  
        int found = 0;
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(user_id) FROM table_user where username = '" + username 
                                   + "' AND password = '" + password + "' AND role = '" + role +"'");                    
            
            while(rs.next()){
               found = rs.getInt("COUNT(user_id)");
            }
            
            if(found > 0){
                result = true;
            }else{
                result = false;
            }
                                                                          
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to authenticate user : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return result;
    } 
    
    public boolean InsertUser(UserItems items){                    
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            String query = "INSERT INTO table_user (username, fullname, address, "
                           + "city, zipcode, phone, mobile, email, picture, role, "
                           + "password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
            
            ps = connect.prepareStatement(query);        
            ps.setString(1, items.username);
            ps.setString(2, items.fullname);
            ps.setString(3, items.address);
            ps.setString(4, items.city);
            ps.setString(5, items.zipcode);
            ps.setString(6, items.phone);
            ps.setString(7, items.mobile);
            ps.setString(8, items.email);
            ps.setBytes(9, items.user_picture);
            ps.setString(10, items.user_type);
            ps.setString(11, items.password);                        
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
                                                               
            JOptionPane.showMessageDialog(null, "User save successfully", "Notification", 1);            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to save user : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean RemoveUser(UserItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_user WHERE user_id = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, value.user_id);            
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
            
            JOptionPane.showMessageDialog(null, "User remove successfully", "Notification", 1);            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove user : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean UpdateUser(UserItems items){                    
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query = "UPDATE table_user set username = '"+ items.username +", fullname = '"+items.fullname 
                           + ", address = '" + items.address +", city = '" + items.city +", zipcode = '" + items.zipcode
                           + ", phone = '" + items.phone +", mobile = '" + items.mobile + ", email = '" + items.email 
                           + ", picture = " + items.user_picture + ", role = '"+ items.user_type+ ", password = '" + items.password;
            
            // insert rate data //
            stmt = connect.createStatement();
            int updateCount = stmt.executeUpdate(query);
            
            if(updateCount > 0){
                JOptionPane.showMessageDialog(null, "User update successfully", "Notification", 1);            
                result =  true;
            }else{
                JOptionPane.showMessageDialog(null, "User not updated!", "Notification", 1);            
                result =  false;
            }                                                                                                
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to update user : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public ArrayList<MemberItems> getMemberData(){
        
        ResultSet rs = null;  
        ArrayList<MemberItems> member_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * FROM table_member");
            
            while(rs.next()){
               MemberItems item = new MemberItems();               
               item.setFullname(rs.getString("fullname"));
               item.setCitizenship(rs.getInt("citizenship"));               
               item.setAddress(rs.getString("address"));               
               item.setCity(rs.getString("city"));
               item.setZipcode(rs.getString("zipcode"));               
               item.setMobile(rs.getString("mobile"));               
               item.setRegistered(rs.getString("registered")); 
               item.setUpdated(rs.getString("updated"));                
               member_items.add(item);
            }  
                        
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get user data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return member_items;
    } 
    
    public MemberItems getSingleMemberData(int citizenship){
        
        ResultSet rs = null;
        MemberItems item = new MemberItems();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * FROM table_member where citizenship = " + citizenship);
            
            while(rs.next()){        
               item.setCitizenship(rs.getInt("citizenship"));
               item.setFullname(rs.getString("fullname"));                                                                      
            }  
                        
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get user data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return item;
    } 
    
    public byte[] getMemberPicture(int member_id){
        ResultSet rs = null;  
        MemberItems item = new MemberItems();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT member_picture FROM table_member WHERE member_id = " + member_id);
            
            while(rs.next()){                              
               item.setMember_picture(rs.getBytes("member_picture"));               
            }  
                        
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get member picture : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        
        return item.member_picture;
    }
    
    public ArrayList<MemberItems> SearchMemberByName(String fullname){
        
        ResultSet rs = null;  
        ArrayList<MemberItems> member_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * FROM table_member WHERE fullname LIKE '%" + fullname + "%'");
            
            while(rs.next()){
               MemberItems item = new MemberItems(); 
               item.setCitizenship(rs.getInt("citizenship"));
               item.setFullname(rs.getString("fullname"));
               item.setAddress(rs.getString("address"));               
               item.setCity(rs.getString("city"));
               item.setZipcode(rs.getString("zipcode"));               
               item.setMobile(rs.getString("mobile"));               
               item.setEmail(rs.getString("email"));     
               item.setRegistered(rs.getString("registered")); 
               item.setUpdated(rs.getString("updated"));                
               member_items.add(item);
            }  
                        
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get user data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return member_items;
    } 
    
    
    public ArrayList<MemberItems> SearchMemberByCitizenship(String citizenship){
        
        ResultSet rs = null;  
        ArrayList<MemberItems> member_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * FROM table_member WHERE citizenship ='" + citizenship + "'");
            
            while(rs.next()){
               MemberItems item = new MemberItems();  
               item.setCitizenship(rs.getInt("citizenship"));
               item.setFullname(rs.getString("fullname"));
               item.setAddress(rs.getString("address"));               
               item.setCity(rs.getString("city"));
               item.setZipcode(rs.getString("zipcode"));               
               item.setMobile(rs.getString("mobile"));               
               item.setEmail(rs.getString("email"));     
               item.setRegistered(rs.getString("registered")); 
               item.setUpdated(rs.getString("updated"));                
               member_items.add(item);
            }  
                        
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get user data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return member_items;
    } 
    
    public ArrayList<MemberItems> SearchMemberByNameAndCitizenship(String fullname, String citizenship){
        
        ResultSet rs = null;  
        ArrayList<MemberItems> member_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * FROM table_member WHERE fullname LIKE '%" + fullname + "%' AND citizenship = '" + citizenship +"'");
            
            while(rs.next()){
               MemberItems item = new MemberItems(); 
               item.setCitizenship(rs.getInt("citizenship"));
               item.setFullname(rs.getString("fullname"));
               item.setAddress(rs.getString("address"));               
               item.setCity(rs.getString("city"));
               item.setZipcode(rs.getString("zipcode"));               
               item.setMobile(rs.getString("mobile"));               
               item.setEmail(rs.getString("email"));     
               item.setRegistered(rs.getString("registered")); 
               item.setUpdated(rs.getString("updated"));                
               member_items.add(item);
            }  
                        
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get user data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return member_items;
    } 
    
    public boolean InsertMember(MemberItems items){                    
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query = "INSERT INTO table_member (fullname, citizenship, address, "
                           + "city, zipcode, mobile, email, registered, "
                           + "updated) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
            
            ps = connect.prepareStatement(query);        
            ps.setString(1, items.fullname);
            ps.setInt(2, items.citizenship);
            ps.setString(3, items.address);
            ps.setString(4, items.city);
            ps.setString(5, items.zipcode);
            ps.setString(6, items.mobile);            
            ps.setString(7, items.email);
            ps.setString(8, items.registered);
            ps.setString(9, items.updated);
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
                                                               
            JOptionPane.showMessageDialog(null, "Member save successfully", "Notification", 1);            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Member with same citizen id already registered");
            e.printStackTrace();
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean RemoveMember(MemberItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_member WHERE citizenship = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, value.citizenship);            
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
            
            JOptionPane.showMessageDialog(null, "Member remove successfully", "Notification", 1);            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove member : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    }
        
    public boolean UpdateMember(MemberItems items){                    
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");  
                        
            String query = "UPDATE table_member set fullname = '"+ items.fullname +"', citizenship = "+items.citizenship 
                           + ", address = '" + items.address +"', city = '" + items.city +"', zipcode = '" + items.zipcode
                           + "', mobile = '" + items.mobile +"', email = '" + items.email +"', updated = '" + items.updated+"'";
                        
            // insert rate data //
            stmt = connect.createStatement();
            int updateCount = stmt.executeUpdate(query);
            
            if(updateCount > 0){
                JOptionPane.showMessageDialog(null, "Member update successfully", "Notification", 1);            
                result =  true;
            }else{
                JOptionPane.showMessageDialog(null, "Member not updated!", "Notification", 1);            
                result =  false;
            }                                                                                                
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to update member : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    } 
    
    public boolean UpdateCardMember(ArrayList<CardMemberItems> items){                    
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");  
            
            stmt = connect.createStatement();
            for(CardMemberItems item : items){
                String query = "UPDATE table_card_member SET card_id = " + item.card_id + " WHERE member_id = " + item.member_id;                 
                stmt.executeUpdate(query);
            }
            
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to update member : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    } 
    
     
    public boolean InsertFieldUpdated(String updated, int member_id){                    
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");  
            
            stmt = connect.createStatement();
            String query = "UPDATE table_member SET updated = '" + updated + "' WHERE citizenship = " + member_id;                 
            stmt.executeUpdate(query);
            
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to update member : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    } 
    
    public int GetCardMemberCount(int member_id){
        
        ResultSet rs = null; 
        int count = 0;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT count(*) FROM table_card_member WHERE member_id = " + member_id);
            
            while(rs.next()){
                count = rs.getInt("count(*)");
            }  
                        
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get member card count : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return count;
    } 
    
    public int GetCardIDfromCardName(String card_name){
        
        ResultSet rs = null; 
        int count = 0;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT card_id FROM table_card WHERE card_name = '"+ card_name +"'");
            
            while(rs.next()){
                count = rs.getInt("card_id");
            }  
                        
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get card id : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return count;
    } 
    
    public boolean isAttachmentExist(int card_id, int member_id){
        
        ResultSet rs = null; 
        int count = 0;
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT count(*) FROM table_card_member WHERE member_id = " + member_id +" AND card_id = " + card_id);
            
            while(rs.next()){
                count = rs.getInt("count(*)");
            } 
            
            if(count > 0){
                result = true;
            }
                        
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get member card count : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    }
            
    public boolean InsertCardMember(ArrayList<CardMemberItems> card_member){                    
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");
                        
            for (CardMemberItems item : card_member){
                // insert rate data //
                String query = "INSERT INTO table_card_member (id, member_id, card_id, expired, min_limit, max_limit) VALUES (?, ?, ?, ?, ?, ?)"; 

                ps = connect.prepareStatement(query);        
                ps.setInt(1, item.card_number);
                ps.setInt(2, item.member_id);
                ps.setInt(3, item.card_id);                
                ps.setString(4, item.expired);
                ps.setString(5, item.min_limit);
                ps.setString(6, item.max_limit);
                ps.addBatch();
                ps.executeBatch();
            }
            
            ps.close();                                                                                      
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to save card : " + e.toString());
            e.printStackTrace();
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean InsertSingleCardMember(CardMemberItems item){                    
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");
                                    
            String query = "INSERT INTO table_card_member (id, member_id, card_id, expired, min_limit, max_limit) VALUES (?, ?, ?, ?, ?, ?)"; 

            ps = connect.prepareStatement(query);        
            ps.setInt(1, item.card_number);
            ps.setInt(2, item.member_id);
            ps.setInt(3, item.card_id); 
            ps.setString(4, item.expired);
            ps.setString(5, item.min_limit);
            ps.setString(6, item.max_limit);
            ps.addBatch();
            ps.executeBatch();
            
            ps.close();                                                                                      
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to save card : " + e.toString());
            e.printStackTrace();
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean RemoveCardsMember(int member_id){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_card_member WHERE member_id = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, member_id);            
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
            
            JOptionPane.showMessageDialog(null, "Card member remove successfully", "Notification", 1);            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove cards : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean RemoveSingleCardMember(CardMemberItems item){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_card_member WHERE member_id = ? AND card_id = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, item.member_id); 
            ps.setInt(2, item.card_id); 
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
                                   
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove cards : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean RemoveAttachmentsMember(int member_id){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_attachment_member WHERE member_id = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, member_id);            
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
            
            JOptionPane.showMessageDialog(null, "Attachment member remove successfully", "Notification", 1);            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove attachments : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean RemoveSingleAttachmentMember(AttachmentItem item){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_attachment_member WHERE member_id = ? AND name = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, item.member_id);            
            ps.setString(2, item.name);
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
                                   
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove attachments : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    }
                  
    public boolean InsertBank(BankItems items){ 
                   
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query = "INSERT INTO table_bank (bank_name) VALUES (?)"; 
            ps = connect.prepareStatement(query);        
            ps.setString(1, items.bank_name);            
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
                                                              
            JOptionPane.showMessageDialog(null, "Bank save successfully", "Notification", 1);            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to save bank : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public ArrayList<BankItems> getCardMemberData(int member_id){
        
        ResultSet rs = null;  
        ArrayList<BankItems> card_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT b.id, a.card_name, a.card_type, b.expired, b.min_limit, b.max_limit "
                                  + "from table_card a JOIN table_card_member b ON a.card_id = b.card_id "
                                  + "AND b.member_id = " + member_id);
            
            while(rs.next()){
               BankItems item = new BankItems();
               item.setCard_id(rs.getInt("id"));
               item.setCard_name(rs.getString("card_name"));
               item.setCard_type_name(rs.getString("card_type"));
               item.setExpired(rs.getString("expired"));
               item.setMin_limit(rs.getString("min_limit"));
               item.setMax_limit(rs.getString("max_limit"));
               card_items.add(item);
            }                                                                           
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get card member data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return card_items;
    } 
    
    public ArrayList<BankItems> getCardMemberDataByName(String fullname){
        
        ResultSet rs = null;  
        ArrayList<BankItems> card_items = new ArrayList<>();
        int citizenship = 0, count = 0;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(citizenship), citizenship FROM table_member WHERE fullname = '" + fullname +"'");
            while(rs.next()){
                count = rs.getInt("COUNT(citizenship)");
                citizenship = rs.getInt("citizenship");                                                
            }
            
            if(count > 0){
                rs = stmt.executeQuery("SELECT a.card_id, a.card_name, a.card_type from table_card a "
                                  + "JOIN table_card_member b ON a.card_id = b.card_id "
                                  + "AND b.member_id = " + citizenship);
            
                while(rs.next()){
                   BankItems item = new BankItems();
                   item.setCard_id(rs.getInt("card_id"));
                   item.setCard_name(rs.getString("card_name"));
                   item.setCard_type_name(rs.getString("card_type"));               
                   card_items.add(item);
                }   
            }
                                                                                    
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get card member data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return card_items;
    }
            
    public ArrayList<AttachmentItem> getAttachmentsMemberData(int member_id){
        
        ResultSet rs = null;  
        ArrayList<AttachmentItem> attachments = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT name, type, path from table_attachment_member "
                                  + "WHERE member_id = " + member_id);
            
            while(rs.next()){
               AttachmentItem item = new AttachmentItem();
               item.setName(rs.getString("name"));
               item.setType(rs.getString("type"));
               item.setPath(rs.getString("path"));                                           
               attachments.add(item);
            }                                                                           
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get card attachment data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return attachments;
    } 
    
    public boolean RemoveBank(BankItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_bank WHERE bank_id = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, value.bank_id);            
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
                                                              
            JOptionPane.showMessageDialog(null, "Bank remove successfully", "Notification", 1);            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove bank : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public ArrayList<BankItems> getBankData(){
        
        ResultSet rs = null;  
        ArrayList<BankItems> bank_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * from table_bank");
            while(rs.next()){
               BankItems item = new BankItems();
               item.setBank_id(rs.getInt("bank_id"));
               item.setBank_name(rs.getString("bank_name"));               
               bank_items.add(item);
            }                                                                           
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get bank data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return bank_items;
    } 
    
    public boolean UpdateBank(BankItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            String query_update = "UPDATE table_bank SET bank_name = '"+ value.bank_name
                              +"' WHERE bank_id = " + value.bank_id;
            
            System.out.println("query update : " + query_update);
            
            stmt = connect.createStatement();
            int updateCount = stmt.executeUpdate(query_update);
            
            if(updateCount > 0){
                JOptionPane.showMessageDialog(null, "Bank update successfully", "Notification", 1);            
                result =  true;
            }else{
                JOptionPane.showMessageDialog(null, "Bank not updated!", "Notification", 1);            
                result =  false;
            }
            
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to update bank : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    }
    
    public boolean InsertCard(BankItems items){ 
                   
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query = "INSERT INTO table_card (card_name, card_type, bank_id) VALUES (?, ?, ?)"; 
            ps = connect.prepareStatement(query);        
            ps.setString(1, items.card_name);            
            ps.setString(2, items.card_name);  
            ps.setInt(3, items.bank_id);  
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
                                                              
            JOptionPane.showMessageDialog(null, "Card save successfully", "Notification", 1);            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to save card : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public boolean RemoveCard(BankItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query="DELETE FROM table_card WHERE card_id = ?"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, value.card_id);            
            ps.addBatch();
            ps.executeBatch();
            ps.close(); 
                                                              
            JOptionPane.showMessageDialog(null, "Card remove successfully", "Notification", 1);            
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to remove card : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        
        return result;
    }
    
    public ArrayList<BankItems> getCardData(){
        
        ResultSet rs = null;  
        ArrayList<BankItems> bank_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT a.card_id, a.card_name, a.card_type, b.bank_name from table_card a "
                    + "JOIN table_bank b ON a.bank_id = b.bank_id");
            while(rs.next()){
               BankItems item = new BankItems();
               item.setCard_id(rs.getInt("card_id"));
               item.setCard_name(rs.getString("card_name"));
               item.setCard_type_name(rs.getString("card_type"));  
               item.setBank_name(rs.getString("bank_name"));
               bank_items.add(item);
            }                                                                           
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get card data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return bank_items;
    }
    
    public BankItems getCardIDbyCardName(String card_name){
        ResultSet rs = null;  
        BankItems item = new BankItems();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT card_id FROM table_card WHERE card_name = '"+ card_name +"'");
            
            while(rs.next()){               
               item.setCard_id(rs.getInt("card_id"));                           
            }                                                                           
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get card data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return item;
    }
    
    public ProfileItems getProfileIDbyProfileName(String profile_name){
        ResultSet rs = null;  
        ProfileItems item = new ProfileItems();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT profile_id FROM table_profile WHERE profile_name = '"+ profile_name +"'");
            
            while(rs.next()){               
               item.setProfile_id(rs.getInt("profile_id"));                           
            }                                                                           
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get profile data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return item;
    }
    
    public ArrayList<BankItems> getCardDataByIssuer(int bank_id){
        
        ResultSet rs = null;  
        ArrayList<BankItems> bank_items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT a.card_id, a.card_name, a.card_type, b.bank_name from table_card a "
                    + "JOIN table_bank b ON a.bank_id = b.bank_id AND a.bank_id = " + bank_id);
            
            while(rs.next()){
               BankItems item = new BankItems();
               item.setCard_id(rs.getInt("card_id"));
               item.setCard_name(rs.getString("card_name"));
               item.setCard_type_name(rs.getString("card_type"));  
               item.setBank_name(rs.getString("bank_name"));
               bank_items.add(item);
            }                                                                           
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get card data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return bank_items;
    } 
    
    public BankItems getSingleCardData(int card_id){
        
        ResultSet rs = null;          
        BankItems item = new BankItems();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT a.card_id, a.card_name, a.card_type from table_card a "
                    + "JOIN table_bank b ON a.bank_id = b.bank_id AND a.card_id = " + card_id);
            while(rs.next()){               
               item.setCard_id(rs.getInt("card_id"));
               item.setCard_name(rs.getString("card_name"));
               item.setCard_type_name(rs.getString("card_type"));                              
            }                                                                           
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to get card data : " + e.toString());  
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return item;
    } 
    
    public boolean UpdateCard(BankItems value){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    
            
            String query_update = "UPDATE table_card SET card_name = '"+ value.card_name
                              +"', card_type = '" + value.card_type_name 
                              +"', bank_id = " + value.bank_id 
                              +" WHERE card_id = " + value.card_id;
            
            System.out.println("query update : " + query_update);
            
            stmt = connect.createStatement();
            int updateCount = stmt.executeUpdate(query_update);
            
            if(updateCount > 0){
                JOptionPane.showMessageDialog(null, "Card update successfully", "Notification", 1);            
                result =  true;
            }else{
                JOptionPane.showMessageDialog(null, "Card not updated!", "Notification", 1);            
                result =  false;
            }
            
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to update card : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
        return result;
    } 
    
    public boolean InsertAttachment(ArrayList<AttachmentItem> items){ 
                   
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            for (AttachmentItem item : items){
                String query = "INSERT INTO table_attachment_member (member_id, name, type, path, attachment) VALUES (?, ?, ?, ?, ?)"; 
                ps = connect.prepareStatement(query);        
                ps.setInt(1, item.member_id);            
                ps.setString(2, item.name);  
                ps.setString(3, item.type);  
                ps.setString(4, item.path);
                ps.setBytes(5, item.attachment);
                ps.addBatch(); 
                ps.executeBatch();
            }            
            ps.close();                                                                                       
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to save Attachment : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public boolean InsertSingleAttachment(int member_id, AttachmentItem item, byte[] file){ 
                   
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            String query = "INSERT INTO table_attachment_member (member_id, name, type, path, attachment) VALUES (?, ?, ?, ?, ?)"; 
            ps = connect.prepareStatement(query);        
            ps.setInt(1, member_id);            
            ps.setString(2, item.name);  
            ps.setString(3, item.type);  
            ps.setString(4, item.path);
            ps.setBytes(5, file);
            ps.addBatch(); 
            ps.executeBatch();           
            ps.close();                                                                                       
            result =  true;            
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to save Attachment : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public boolean InsertTransaction(TransactionItems items){ 
                   
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query = "INSERT INTO table_transaction (transaction_date, "
                                                        + "fullname,"
                                                        + "profile_id,"
                                                        + "percentage,"
                                                        + "nominal_transaction,"
                                                        + "nominal_accepted,"
                                                        + "settle_amount,"
                                                        + "cash_amount,"
                                                        + "bank_charge,"
                                                        + "card_id,"
                                                        + "note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
            ps = connect.prepareStatement(query);        
            ps.setString(1, items.transaction_date);            
            ps.setString(2, items.fullname);            
            ps.setInt(3, items.profile);
            ps.setString(4, items.percentage);
            ps.setString(5, items.nominal_transaction);
            ps.setString(6, items.nominal_accepted);
            ps.setString(7, items.total_settle);
            ps.setString(8, items.cash_amount);
            ps.setString(9, items.bank_charge);
            ps.setInt(10, items.card_id);
            ps.setString(11, items.note);
            ps.addBatch();
            ps.executeBatch();
            ps.close();                                                                                       
            result =  true;
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to save Transaction : " + e.toString());             
            result = false;
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public ArrayList<TransactionItems> GetTransactionData(){ 
        ResultSet rs = null;
        ArrayList<TransactionItems> items = new ArrayList<>();
        try{
            Class.forName("org.sqlite.JDBC").newInstance();        
            connect = DriverManager.getConnection(urlDatabase);
            System.out.println("Opened database successfully");    

            // insert rate data //
            String query = "SELECT a.transaction_date, a.fullname, b.profile_name, c.percentage, a.note " +
                           "FROM table_transaction a INNER JOIN table_profile b INNER JOIN table_rate c " +
                           "ON a.profile_id = b.profile_id AND b.rate_id = c.rate_id"; 
            stmt = connect.createStatement();
            rs = stmt.executeQuery(query);
            
            while(rs.next()){ 
               TransactionItems item = new TransactionItems();
               item.setTransaction_date(rs.getString("transaction_date"));
               item.setFullname(rs.getString("fullname"));
               item.setProfile_name(rs.getString("profile_name"));
               item.setPercentage(rs.getString("percentage"));
               item.setNote(rs.getString("note"));
               items.add(item);
            } 
            
        }catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e){
            JOptionPane.showMessageDialog(null, "Failed to save Transaction : " + e.toString());                         
        }finally{
            try{            
                connect.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return items;
    }    
}
