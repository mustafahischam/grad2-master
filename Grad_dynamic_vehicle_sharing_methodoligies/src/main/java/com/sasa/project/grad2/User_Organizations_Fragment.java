package com.sasa.project.grad2;


import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;
import android.view.View;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class User_Organizations_Fragment extends User_page_organizations_layout {

    private LinearLayout linearLayout;

    private String organization;

    public User_Organizations_Fragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if(getActivity() instanceof User_page_Activity){
            String userName = getArguments().getString("userName");
            this.organization = userName + "'s Organizations";
            this.linearLayout = view.findViewById(R.id.user_organization_fragment_layout);
            this.Title(organization);
        }
        return view;
    }
    private TextView view_title;
    private void Title(String title){
        view_title = new TextView(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        view_title.setLayoutParams(layoutParams);
        view_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        view_title.setPadding(12, 12, 12, 12);
        view_title.setText(title);
        this.linearLayout.addView(this.view_title, 0);
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("user-organizations")
                .child(super.User_id);
    }
}
