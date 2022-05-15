package com.sasa.project.grad2;

import com.sasa.project.grad2.Firebase_DataObjects.Post_info_DataObjects;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class Last_Post_Fragment extends Load_posts_Fragment {

    public Last_Post_Fragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query query;


        /// el function btrg3 a5r 20 post et3mlo fe kol organization mn el firebase
        // passenger
        if(type == Post_info_DataObjects.Post_type.Passenger) {
            query = this.databaseReference.child("organization_posts").child(Load_under_org_ID())
                    .child("Passenger_Requests").limitToFirst(20);
        }
        // Driver
        else {
            query = this.databaseReference.child("organization_posts").child(Load_under_org_ID())
                    .child("Driver_offers").limitToFirst(20);
        }

        return query;
    }
}
