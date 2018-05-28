package com.example.rafles.att_group.crud_firebase;

import java.io.Serializable;

public class Usermodel implements Serializable {
    private String idusers;
    private String username;
    private String password;
    private String fullname;
    private String phone;
    private String address;
    private String key;

    public Usermodel(){

    }

    public String getIdusers() {
        return idusers;
    }

    public void setIdusers(String idusers) {
        this.idusers = idusers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    @Override
    public String toString() {
        return " "+idusers+"\n" +
                " "+username +"\n" +
                " "+password +"\n" +
                " "+fullname +"\n" +
                " "+phone +"\n" +
                " "+address;
    }
    public Usermodel(String idusers, String username, String password, String fullname, String phone, String address){
        this.idusers = idusers;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
    }

}
