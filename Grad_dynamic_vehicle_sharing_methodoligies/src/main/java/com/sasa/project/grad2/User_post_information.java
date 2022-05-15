package com.sasa.project.grad2;
import com.sasa.project.grad2.Firebase_DataObjects.User_info_DataObjects;


public class User_post_information {

    public static String User_post_name(User_info_DataObjects user_info_DataObjects_p_name) {
        String user_post_name = "";

        if (user_info_DataObjects_p_name.First_name != null && user_info_DataObjects_p_name.Last_name !=null ) {/// return user_info_DataObjects_p_name full name in post
            user_post_name += " "+ user_info_DataObjects_p_name.First_name +" "+ user_info_DataObjects_p_name.Last_name +" ";
        }
        else  {
            return "";
        }
        return user_post_name;
    }

    public String email;
    public User_post_information(String email){

        this.email = email;
    }
}
