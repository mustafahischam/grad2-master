package com.sasa.project.grad2.Firebase_DataObjects;

public class Comments_DataObjects {
    public Comments_DataObjects() { }
    public String Comment;
    public String User_id;
    public String User_name;


    public Comments_DataObjects(String User_id, String User_name, String Comment) {
        this.User_id = User_id;
        this.User_name = User_name;
        this.Comment = Comment;
    }

}

