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
public class BankItems {
    
    public int bank_id;
    public String bank_name;    
    public int card_id;
    public int card_type;    
    public String card_name;
    public String card_type_name;
    public String expired;
    public String min_limit;
    public String max_limit;
    

    public void setBank_id(int bank_id) {
        this.bank_id = bank_id;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public void setCard_type(int card_type) {
        this.card_type = card_type;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public void setCard_type_name(String card_type_name) {
        this.card_type_name = card_type_name;
    }   

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public void setMin_limit(String min_limit) {
        this.min_limit = min_limit;
    }

    public void setMax_limit(String max_limit) {
        this.max_limit = max_limit;
    }       
}
