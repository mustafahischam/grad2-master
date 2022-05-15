package com.sasa.project.grad2;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasa.project.grad2.Firebase_DataObjects.Organization_DataObjects;

import java.util.HashMap;
import java.util.Map;


public class Join_organization_Activity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "Join_organization_Activity";
    private static final String REQUIRED = "Required";

    public static final String Organization_Key = "organization_key";


    private DatabaseReference databaseReference, organization_Reference;
    private String Organization_id;
    private TextView Organization_name,Organization_join_email,Organization_url,Organization_type,Organization_Description;
    private Button Join_organization_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_organization_activity);

        Organization_id = getIntent().getStringExtra(Organization_Key);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        organization_Reference = FirebaseDatabase.getInstance().getReference()
                .child("organizations").child(Organization_id);

        Organization_name = findViewById(R.id.organization_name);
        Organization_type = findViewById(R.id.organization_type);
        Organization_Description = findViewById(R.id.organization_description);
        Organization_url = findViewById(R.id.organization_url);
        Organization_join_email = (EditText) findViewById(R.id.organization_join_email);
        Join_organization_button = findViewById(R.id.join_organization_button);
        hide_join_button();


    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.join_organization_button) {
            join_button();
        }
    }

    @Override
    public void onStart() { // load if the data changed first
        super.onStart();

        organization_Reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {// firebase
                Organization_DataObjects organizationDataObjects = dataSnapshot.getValue(Organization_DataObjects.class);
                Organization_name.setText(organizationDataObjects.name);
                Organization_type.setText(organizationDataObjects.type);
                Organization_Description.setText(organizationDataObjects.description);
                Organization_url.setText(organizationDataObjects.url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError);
            }

        });
    }

    private void join_button()
    {
        final String Organization_Join_Email = Organization_join_email.getText().toString();
        if (TextUtils.isEmpty(Organization_Join_Email)) {
            Organization_join_email.setError(REQUIRED); // bta3 el user
            return;
        }

        // add new organization to database:
        Toast.makeText(this, "joining ..", Toast.LENGTH_SHORT).show();
        organization_add_user(Organization_Join_Email);
        Intent intent = new Intent(Join_organization_Activity.this, MainActivity.class);
        startActivity(intent);

    }

    // add user id/email to firebase/ organization
    private void organization_add_user(String user_is_mail) {

        Map<String, Object> user_is_organization = get_mail_name(user_is_mail, Organization_name.getText().toString());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user-organizations/" + getUid() + "/" + Organization_id, user_is_organization);

        databaseReference.updateChildren(childUpdates);

    }

    @Exclude
    public Map<String, Object> get_mail_name(String email_user, String organization_name)
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user 's mail", email_user);
        result.put("name", organization_name);

        return result;
    }
    public String getUid() {

        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    private void setCanJoin(boolean canJoin) {
        if (canJoin) {
            Join_organization_button.setOnClickListener(this);

        }
        else { // remove the option to join the organization:
            Organization_join_email.setVisibility(View.GONE);
            Join_organization_button.setVisibility(View.GONE);
            //if other two are invisible, that means user is part of the organization

        }
    }


    private void hide_join_button()
    {
/// bshof eldata bta3t el user ogoda wla la
        databaseReference.child("user-organizations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(getUid() + "/" + Organization_id)) {
                  //  Join_organization_button.setOnClickListener(this);
                    setCanJoin(false);
                }
                else {
                  //  Organization_join_email.setVisibility(View.GONE);
                 //   Join_organization_button.setVisibility(View.GONE);
                    setCanJoin(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}
