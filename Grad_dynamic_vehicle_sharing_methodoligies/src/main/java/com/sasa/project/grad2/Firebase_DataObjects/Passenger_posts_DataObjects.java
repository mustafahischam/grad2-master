package com.sasa.project.grad2.Firebase_DataObjects;

import java.util.HashMap;
import java.util.Map;


public class Passenger_posts_DataObjects extends Post_info_DataObjects {

    public Passenger_posts_DataObjects() {}
    public Passenger_posts_DataObjects(String User_id, String User_name, String source, String destination
            , int leave_time, int arrivalTime, int tripDate){
        super(User_id, User_name, source, destination, leave_time, arrivalTime, tripDate);
        this.Type = Post_type.Passenger;
    }

    /// add Passenger post data including type and organization to firebase
    public Map<String, Object> PassengerObjects(){
        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("User_id", User_id);
        objectHashMap.put("Type", Type);
        objectHashMap.put("organizationId", organizationId);
        objectHashMap.put("postId", postId);
        objectHashMap.put("User_name", User_name);
        objectHashMap.put("source", source);
        objectHashMap.put("destination", destination);
        objectHashMap.put("leave_time", leave_time);
        objectHashMap.put("arrivalTime", arrivalTime);
        objectHashMap.put("tripDate", tripDate);
        return objectHashMap;
    }

}
