package com.sasa.project.grad2.Firebase_DataObjects;

public class User_info_DataObjects {

    public String User_email, First_name, Last_name, Phone,default_organization;;
    public User_info_DataObjects() {}


    public User_info_DataObjects(String User_email, String first_name, String last_name, String phone) {

        this.User_email = User_email;
        this.First_name = first_name;
        this.Last_name = last_name;
        this.Phone = phone;
        this.default_organization = "";


    }
}

