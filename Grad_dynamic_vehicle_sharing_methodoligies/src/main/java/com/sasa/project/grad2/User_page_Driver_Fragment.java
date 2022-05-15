package com.sasa.project.grad2;

import com.google.firebase.database.Query;
import com.google.firebase.database.DatabaseReference;

public class User_page_Driver_Fragment extends User_page_posts_layout {

    ///// user page post fragment
    public User_page_Driver_Fragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return  databaseReference.child("user-posts").child(super.User_id)  //rag3 el Driver posts bta3t el user
                .child("Driver_offers");
    }

}
