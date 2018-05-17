package com.example.rafles.att_group.Model;

public class ModelData {
    String iduser,name,address,department;
    public ModelData(){}

    public ModelData(String iduser,String name,String address,String department){
        this.iduser=iduser;
        this.name=name;
        this.address=address;
        this.department=department;
    }


    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
