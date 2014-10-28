/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.java.apps.pojo;

import java.io.Serializable;

/**
 *
 * @author Ultimate
 */
public class RateItems implements Serializable{
    
    public int rate_id;
    public String rate_name;
    public String rate_value;
        
    public void setRate_id(int rate_id) {
        this.rate_id = rate_id;
    }

    public void setRate_name(String rate_name) {
        this.rate_name = rate_name;
    }

    public void setRate_value(String rate_value) {
        this.rate_value = rate_value;
    }            
}
