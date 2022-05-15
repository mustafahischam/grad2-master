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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sasa.project.grad2.Firebase_DataObjects.Post_info_DataObjects;
import com.sasa.project.grad2.Firebase_DataObjects.User_info_DataObjects;

//// load posts in recycler
public abstract class Load_posts_Fragment extends Fragment {

    public static final String Post_Type = "postType";
    public static final String Organization_ID = "organizationId";
    private FirebaseRecyclerAdapter<Post_info_DataObjects, ViewHolder_PostLayout> adapter;
    protected RecyclerView recyclerView;// for scrolling view
    protected LinearLayoutManager linearLayoutManager;

    protected Post_info_DataObjects.Post_type type;

    protected DatabaseReference databaseReference;
    private String organization;
    View view;

   /// https://stackoverflow.com/questions/25183400/datepickedialog-displays-one-month-greater-in-android  not solved yet
    public Load_posts_Fragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        type = Post_info_DataObjects.Post_type.valueOf(getArguments().getString(Load_posts_Fragment.Post_Type));/// load in case of post type  and organization
        organization = getArguments().getString(Load_posts_Fragment.Organization_ID);

        // view all post in container
        view = inflater.inflate(R.layout.user_page_post_layout, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.sdl_list);
        recyclerView.setHasFixedSize(true);
        
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Load_posts_inCase_ofUser();

    }


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String Load_under_org_ID() {

        if (getActivity() instanceof Passenger_Driver_Main_Activity) {
            return ((Passenger_Driver_Main_Activity)getActivity()).organization_id_select();
        }
        else {
            return organization;
        }
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    public void Load_posts_inCase_ofUser() {

        databaseReference.child("users").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                view_posts_load();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private String user_name;
    private void view_posts_load() {
        Query postsQuery = getQuery(databaseReference);

        adapter = new FirebaseRecyclerAdapter<Post_info_DataObjects, ViewHolder_PostLayout>(Post_info_DataObjects.class, R.layout.post,
                ViewHolder_PostLayout.class, postsQuery) {
            @Override
            protected void populateViewHolder(final ViewHolder_PostLayout viewHolder, final Post_info_DataObjects Type, final int i) {
                final DatabaseReference databaseReference = getRef(i);

                System.out.println("Type.postType = " + Type.Type);
                if (Type.Type != null ) {
                    type = Type.Type;
                }


                final String PostId = databaseReference.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch Post_information_Activity
                        Intent intent = new Intent(getActivity(), Post_information_Activity.class);
                        intent.putExtra(Post_information_Activity.Post_Id, PostId);
System.out.println("Sasaaaaaaaaaaaaaaa");/// error
                        intent.putExtra("Post-type", Type.Type.name());

                        startActivity(intent);
                    }
                });


                DatabaseReference databaseReference1 =
                        FirebaseDatabase.getInstance().getReference().child("users").child(Type.User_id);

                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get Organization_DataObjects object and use the values to update the UI
                        User_info_DataObjects postUserInfo = dataSnapshot.getValue(User_info_DataObjects.class);

                        user_name = User_post_information.User_post_name(postUserInfo);

                        viewHolder.get_postData(user_name, Type.Type, Type);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

}
