package com.sasa.project.grad2;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasa.project.grad2.Firebase_DataObjects.Post_info_DataObjects;
import com.sasa.project.grad2.Firebase_DataObjects.User_info_DataObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Passenger_Driver_Main_Activity extends MainActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseReference databaseReference;
    private static final String TAG = "Passenger_Driver_Main_Activity";
    public static final String POST_TYPE = "postType";
    private Post_info_DataObjects.Post_type type;
    private User_info_DataObjects user_infoDataObjects;
    private HashMap<String, String> Organization_Name_ID;
    private String organization_id_selected;
    private ArrayAdapter<String> stringArrayAdapter;


    private Spinner spinner;



    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        type = Post_info_DataObjects.Post_type.valueOf(getIntent().getStringExtra(Passenger_Driver_Main_Activity.POST_TYPE));

        // If showing Ride Request Posts, then user is a Driver:
        if (type == Post_info_DataObjects.Post_type.Passenger) { /// Passenger post appear in driver section
            setContentView(R.layout.create_post_activity_d);

            findViewById(R.id.new_drive_offer_post).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Passenger_Driver_Main_Activity.this, Create_post_Activity.class);
                   intent.putExtra("Driver_offers", true);
                    startActivity(intent);
                }
            });
        }
        else {
            setContentView(R.layout.create_post_activity_p);    /// Driver post appear in pssenger section
            findViewById(R.id.new_ride_request_post).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Passenger_Driver_Main_Activity.this, Create_post_Activity.class);
                   intent.putExtra("Driver_offers", false);
                    startActivity(intent);
                }
            });
        }
        spinner = findViewById(R.id.choose_organization);
        load_organization_underUserID_inSpinner();

    }



    private void onClick_load_spinner() {

        Load_posts_Fragment loadpostsfragment = (Load_posts_Fragment) getSupportFragmentManager().findFragmentById(R.id.posts_activity);
        getSupportFragmentManager().beginTransaction().detach(loadpostsfragment).attach(loadpostsfragment).commit();
    }

    private void load_organization_underUserID_inSpinner(){// if user joined or deleted one ( update ya3ny
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String User_id = user.getUid();

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users").child(User_id);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_infoDataObjects = dataSnapshot.getValue(User_info_DataObjects.class);
                LoadUser_organizations();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }

        });
    }

    private void LoadUser_organizations() {
        Organization_Name_ID = new HashMap<>();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                .child("user-organizations").child(getUid());

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> organizations_names = new ArrayList<String>();
                String default_name = "",default_id = "";;
                for (DataSnapshot Organization_name: dataSnapshot.getChildren()) {
                    String organization_name = Organization_name.child("name").getValue(String.class);// load name bta3 user organizations
                    organizations_names.add(organization_name);


                    String organization_id = Organization_name.getKey();// hash map
                    Organization_Name_ID.put(organization_name, organization_id);

                    if (user_infoDataObjects.default_organization.equals(organization_id))
                    {/// load el default
                        default_name = organization_name;
                        default_id = organization_id;
                    }
                }

                stringArrayAdapter = new ArrayAdapter<>(Passenger_Driver_Main_Activity.this,
                        android.R.layout.simple_spinner_item, organizations_names);
                stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                System.out.println("orgSpinner = " + spinner);
                spinner.setAdapter(stringArrayAdapter);
                spinner.setSelection(stringArrayAdapter.getPosition(default_name));/// add defualt to load in spinner auto
                organization_id_selected = default_id;

                spinner.setOnItemSelectedListener(Passenger_Driver_Main_Activity.this);//on click

                GetData_Posts_fragment();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void GetData_Posts_fragment() {
        Bundle bundle = new Bundle();// pass data
        bundle.putString(Load_posts_Fragment.Post_Type, type.name());
        bundle.putString(Load_posts_Fragment.Organization_ID, organization_id_select());
        /// load data
        Fragment posts = new Last_Post_Fragment();
        posts.setArguments(bundle);


        getSupportFragmentManager().beginTransaction().add(R.id.posts_activity, posts, "PostsList").commit();
    }


    public String organization_id_select() {
        return organization_id_selected;
    }


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using parent.getItemAtPosition(pos).

        String clicked_organization_name = (String) parent.getItemAtPosition(pos);
        organization_id_selected = Organization_Name_ID.get(clicked_organization_name);
        onClick_load_spinner();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}
