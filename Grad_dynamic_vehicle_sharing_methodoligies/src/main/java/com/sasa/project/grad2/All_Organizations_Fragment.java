package com.sasa.project.grad2;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class All_Organizations_Fragment extends User_page_organizations_layout {

    public All_Organizations_Fragment() {}
    /// el function btrg3  a5r 20 et3mlo organization mn el firebase
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query First20 = databaseReference.child("organizations")
                .limitToFirst(20);

        return First20;
    }
}
