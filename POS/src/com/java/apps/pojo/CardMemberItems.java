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
public class CardMemberItems {
    
    public int card_number;
    public int member_id;
    public int card_id;
    public String expired;
    public String min_limit;
    public String max_limit;

    public void setCard_number(int card_number) {
        this.card_number = card_number;
    }
    
    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
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
