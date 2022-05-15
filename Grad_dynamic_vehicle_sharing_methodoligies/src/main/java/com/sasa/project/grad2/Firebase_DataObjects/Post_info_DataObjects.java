package com.sasa.project.grad2.Firebase_DataObjects;

public class Post_info_DataObjects {

    public String User_id,User_name,source,destination;

    public int leave_time = 0,arrivalTime = 0,tripDate = 0;
    public String organizationId;
    public String postId;

    public enum Post_type {
         Passenger, Driver,
    }
    public Post_type Type;

    public Post_info_DataObjects() { }

/// to user post information database
    public Post_info_DataObjects(String User_id, String User_name, String source, String destination,
                                 int departure_time, int arrivalTime, int date) {
        this.User_id = User_id;
        this.User_name = User_name;
        this.source = source;
        this.destination = destination;
        this.leave_time = departure_time;
        this.arrivalTime = arrivalTime;
        this.tripDate = date;
    }

}

