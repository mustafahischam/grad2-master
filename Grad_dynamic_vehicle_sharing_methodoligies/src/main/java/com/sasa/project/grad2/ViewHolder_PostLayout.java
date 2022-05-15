package com.sasa.project.grad2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sasa.project.grad2.Firebase_DataObjects.Post_info_DataObjects;

public class ViewHolder_PostLayout extends RecyclerView.ViewHolder {

    public TextView Username;
    public TextView Destination,Source;
    public ImageView Post_background;
    public TextView Date,Time,text;
    public ViewHolder_PostLayout(View V) {
        super(V);

        Username = V.findViewById(R.id.postlayout_username);
        Destination = V.findViewById(R.id.destination_postlayout);
        Source = V.findViewById(R.id.source_postlayout);
        Date = V.findViewById(R.id.date_postlayout);
        Time = V.findViewById(R.id.time_postlayout);
        Post_background = V.findViewById(R.id.background_postlayout);
        text = V.findViewById(R.id.to_postlayout);
    }

    public void get_postData(String name, Post_info_DataObjects.Post_type postType, Post_info_DataObjects postInfo) {
        Username.setText(name);
        String to ="To";
        text.setText(to);
        Source.setText(postInfo.source);
        Destination.setText(postInfo.destination);

        if (postType == Post_info_DataObjects.Post_type.Passenger) {
            Post_background.setImageResource(R.drawable.passenger_background);
        }
        else if (postType == Post_info_DataObjects.Post_type.Driver) {
            Post_background.setImageResource(R.drawable.driver_background);
        }

        Date.setText(Post_date_time_information.post_date(postInfo));
        Time.setText("Arrive at " + Post_date_time_information.Arrive_before_time(postInfo));
    }


}
