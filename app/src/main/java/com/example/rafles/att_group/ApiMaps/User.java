package com.example.rafles.att_group.ApiMaps;

public class User {
    private String email,status,name;

    public User(){
    }
    public User(String email,String status){
        this.email=email;
        this.status=status;
//        this.name=name;
    }
    public String getEmail() {
        return email;
    }

//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

//    public void setStatus(String status) {
//        this.status = status;
//    }

}
