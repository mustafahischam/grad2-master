package com.sasa.project.grad2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sasa.project.grad2.Firebase_DataObjects.Organization_DataObjects;

public abstract class User_page_organizations_layout extends Fragment {


    private DatabaseReference databaseReference;

    private FirebaseRecyclerAdapter<Organization_DataObjects, ViewHolder_OrganizationLayout> firebaseRecyclerAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    protected String User_id;
    public User_page_organizations_layout() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_all_organizations, container, false);


        if(getActivity() instanceof User_page_Activity){// bad2 fe user page activity
            this.User_id = getArguments().getString("uID");
            String user_name = getArguments().getString("userName");
        }
        else{
            this.User_id = getUid(); /// in case mafish org  or user need to be updated
        }


        databaseReference = FirebaseDatabase.getInstance().getReference();

        this.recyclerView = view.findViewById(R.id.sdl_list);
        this.recyclerView.setHasFixedSize(true);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);// up and down
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Query query = getQuery(databaseReference);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Organization_DataObjects, ViewHolder_OrganizationLayout>(Organization_DataObjects.class, R.layout.organization,
                ViewHolder_OrganizationLayout.class, query) {

            @Override/// multibly layout recycler view
            public void populateViewHolder(final ViewHolder_OrganizationLayout ViewHolder_OrganizationLayout, final Organization_DataObjects organizationDataObjects, final int position) {
                final DatabaseReference databaseReference1 = getRef(position);

                final String organizationKey = databaseReference1.getKey();/// on click for all organizations
                ViewHolder_OrganizationLayout.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //if want to join w eluser msh feha
                        Intent intent = new Intent(getActivity(), Join_organization_Activity.class);
                        intent.putExtra(Join_organization_Activity.Organization_Key, organizationKey);
                        startActivity(intent);
                    }
                });

                ViewHolder_OrganizationLayout.model(organizationDataObjects);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

}
