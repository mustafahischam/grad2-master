package com.sasa.project.grad2;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasa.project.grad2.Firebase_DataObjects.Post_info_DataObjects;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public abstract class User_page_posts_layout extends Load_posts_Fragment {

    protected String User_id;
    private RecyclerView recyclerView1;
    private LinearLayout linearLayout;
    private String Driver,Passenger;
    protected DatabaseReference databasereference;
    public User_page_posts_layout() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,// initiate layout im container 
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_page_post_layout, container, false);
        linearLayout = view.findViewById(R.id.fragment_all_posts_linearlayout);
        this.User_id = getArguments().getString("uID");
        String User_name = getArguments().getString("userName");
        Driver = User_name +  "'s Driver Posts";
        Passenger = User_name  + "'s Passenger Posts";
        System.out.println("uID: " + User_id);
//// put user title 
        if(this instanceof User_page_Driver_Fragment){
            this.Title(this.Driver);
            type = Post_info_DataObjects.Post_type.Driver;
        }
        else{
            this.Title(this.Passenger);
            type = Post_info_DataObjects.Post_type.Passenger;
        }
        databasereference = FirebaseDatabase.getInstance().getReference();
   
     //// on post view source / destination
        recyclerView1 = view.findViewById(R.id.sdl_list);
        recyclerView1.setHasFixedSize(true);

        return view;
    }
    private TextView view_title;
    private void Title(String title){
        view_title = new TextView(getActivity());/// wrap content 3shan yeb2a ad el parent goah ell container
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        view_title.setLayoutParams(layoutParams);
        view_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        view_title.setPadding(12, 12, 12, 12);
        view_title.setText(title);
        linearLayout.addView(view_title, 0);
    }

    @Override
    /// after on create finish
    public void onActivityCreated(Bundle savedInstanceState) {/// efdel recall
        super.databaseReference = this.databasereference;
        super.recyclerView = this.recyclerView1;// always active
        super.onActivityCreated(savedInstanceState);
    }

    public abstract Query getQuery(DatabaseReference databaseReference);
}
