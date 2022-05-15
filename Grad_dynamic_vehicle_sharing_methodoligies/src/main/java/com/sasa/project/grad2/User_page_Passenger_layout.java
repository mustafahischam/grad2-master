package com.sasa.project.grad2;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class User_page_Passenger_layout extends User_page_posts_layout {


    public User_page_Passenger_layout() {}

///// user page post fragment
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return  databaseReference.child("user-posts").child(super.User_id)// rag3 el passenger posts bta3t el user
                .child("Passenger_Requests");
    }

}
