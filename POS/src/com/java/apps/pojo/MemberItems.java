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
public class MemberItems {
        
    public String fullname;
    public int citizenship;
    public String address;
    public String city;
    public String zipcode;
    public String phone;
    public String mobile;
    public String email;
    public String registered;
    public String updated;    
    public byte[] member_picture;
   
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setCitizenship(int citizenship) {
        this.citizenship = citizenship;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }
            
    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public void setMember_picture(byte[] member_picture) {
        this.member_picture = member_picture;
    } 

    public void setUpdated(String updated) {
        this.updated = updated;
    }    
}
