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
public class AttachmentItem {
    
    public int member_id;
    public String name;
    public String type;
    public String path;
    public byte[] attachment;

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }      

    public void setPath(String path) {
        this.path = path;
    }        
}
