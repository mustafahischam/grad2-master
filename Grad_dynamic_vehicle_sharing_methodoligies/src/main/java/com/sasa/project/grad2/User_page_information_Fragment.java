package com.sasa.project.grad2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sasa.project.grad2.Edit_user_information_Activity;
import com.sasa.project.grad2.R;


public class User_page_information_Fragment extends Fragment {

    private String Title;
    private Button Edit_button;
    private TextView view_title;
    public User_page_information_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        String firstName = getArguments().getString("userName");
        String User_id = getArguments().getString("uID");
        Title =   firstName + "'s Information";
        View view = inflater.inflate(R.layout.user_page_information_fragment, container, false);
        view_title = view.findViewById(R.id.User_Name_layout);
        view_title.setText(Title);

        if(User_id == getUid()){
            Edit_button = view.findViewById(R.id.edit_user_page_layout);
            Edit_button.setVisibility(View.VISIBLE);
            Edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), Edit_user_information_Activity.class));
                    getActivity().finish();
                }
            });
        }

        return view;
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
