package com.sasa.project.grad2.Firebase_DataObjects;

import java.util.HashMap;
import java.util.Map;

public class Driver_posts_DataObjects extends Post_info_DataObjects {
    public Driver_posts_DataObjects() {}

    public int Num_of_passengers;
    public Driver_posts_DataObjects(String User_id, String User_name, String source, String destination, int NumOfPassenger,
                                    int leave_time, int arrive_time, int tripDay){
        super(User_id, User_name, source, destination, leave_time, arrive_time, tripDay);
        this.Num_of_passengers = NumOfPassenger;
        this.Type = Post_type.Driver;
    }

/// add driver post data including type and organization to firebase
    public  Map<String, Object> DriverObjects(){
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
        objectHashMap.put("passengerCount", Num_of_passengers);
        return objectHashMap;
    }

}
