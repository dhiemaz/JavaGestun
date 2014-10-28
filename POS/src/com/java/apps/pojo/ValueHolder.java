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
public class ValueHolder {
    
    private static int member_id;
    private static int payment_member_id;
    private static int search_payment_member_id;
    private static int search_transaction_member_id;

    public static int getSearch_payment_member_id() {
        return search_payment_member_id;
    }

    public static void setSearch_payment_member_id(int search_payment_member_id) {
        ValueHolder.search_payment_member_id = search_payment_member_id;
    }
        
    public static int getPayment_member_id() {
        return payment_member_id;
    }

    public static void setPayment_member_id(int payment_member_id) {
        ValueHolder.payment_member_id = payment_member_id;
    }
        
    public static int getMember_id() {
        return member_id;
    }

    public static void setMember_id(int member_id) {
        ValueHolder.member_id = member_id;
    }

    public static int getSearch_transaction_member_id() {
        return search_transaction_member_id;
    }

    public static void setSearch_transaction_member_id(int search_transaction_member_id) {
        ValueHolder.search_transaction_member_id = search_transaction_member_id;
    }       
}
