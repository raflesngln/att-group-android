package com.example.rafles.att_group.recyclerview_data;

//this is model for adapter to manage
public class ModelRecycler {

    private String username;
    private String fullname;
    private String iduser;

    public ModelRecycler(String iduser, String username, String fullname) {
        this.username = username;
        this.fullname = fullname;
        this.iduser = iduser;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }
}
