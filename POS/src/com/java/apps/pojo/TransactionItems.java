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
public class TransactionItems {
    
    public int transaction_id;
    public String transaction_date;
    public String fullname;
    public int profile;
    public String profile_name;
    public String percentage;
    public String nominal_transaction;
    public String nominal_accepted;
    public String total_settle;
    public String cash_amount;
    public String bank_charge;
    public int card_id;
    public String note;

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public void setNominal_transaction(String nominal_transaction) {
        this.nominal_transaction = nominal_transaction;
    }

    public void setNominal_accepted(String nominal_accepted) {
        this.nominal_accepted = nominal_accepted;
    }

    public void setTotal_settle(String total_settle) {
        this.total_settle = total_settle;
    }

    public void setCash_amount(String cash_amount) {
        this.cash_amount = cash_amount;
    }

    public void setBank_charge(String bank_charge) {
        this.bank_charge = bank_charge;
    }
    
    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public void setNote(String note) {
        this.note = note;
    }    

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }        
}
