package com.sasa.project.grad2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.annotation.NonNull;
import android.util.Log;

import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasa.project.grad2.Firebase_DataObjects.User_info_DataObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Edit_user_information_Activity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "Edit_user_information_Activity";

    private DatabaseReference databaseReference, organization_Reference;
    private User_info_DataObjects currentUserInfo;
    private EditText First_name;
    private EditText Last_name;
    private EditText Phone_register;
    private String userID;
    private Button Signup_button;
    String Organization_Name;

    private Spinner Choose_organization;
    private ArrayAdapter<String> List_organization;
    private HashMap<String, String> Org_id_name;/// 3shan a store id w name m3 b3d

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_information_activity);

        Signup_button = findViewById(R.id.signup_button);

        First_name = findViewById(R.id.first_name);
        Last_name = findViewById(R.id.last_name);
        Phone_register = findViewById(R.id.phone_register);
        Choose_organization = findViewById(R.id.choose_organization);

        Signup_button.setOnClickListener(this);

    }

    private void update_user_information(String first_name, String last_name, String phone_register, String default_organization) {
        HashMap<String, Object> userInformation = new HashMap<>();

        userInformation.put("default_organization", default_organization);/// as choosen from spiner
        userInformation.put("First_name", first_name);
        userInformation.put("Last_name", last_name);
        userInformation.put("phoneNumber", phone_register);


        databaseReference.updateChildren(userInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }
//// function btl3 el organization ely el user da5lha
    private void load_user_organization() {
        Org_id_name = new HashMap<>();

        organization_Reference = FirebaseDatabase.getInstance().getReference()
                .child("user-organizations").child(getUid());

        organization_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> all_organizations = new ArrayList<String>();

                String defaultOrgName = "";
                for (DataSnapshot orgSnapshot: dataSnapshot.getChildren()) {

                   Organization_Name = orgSnapshot.child("name").getValue(String.class);
                    all_organizations.add(Organization_Name);
                   // 7ot el id bta3 el organization m3 elname 3shan el posts
                    String orgId = orgSnapshot.getKey();
                    Org_id_name.put(Organization_Name, orgId);

                    // bi5ly el defualt sabta when refresh
                    if (currentUserInfo.default_organization.equals(orgId)) {
                        defaultOrgName = Organization_Name;
                    }
                }


                List_organization = new ArrayAdapter<String>(Edit_user_information_Activity.this,
                        android.R.layout.simple_spinner_item, all_organizations);
                List_organization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);/// put fel spinner
                Choose_organization.setAdapter(List_organization);
                Choose_organization.setSelection(List_organization.getPosition(defaultOrgName));// make el selected default
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void onStart() {
        super.onStart();

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentFirebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Organization_DataObjects object and use the values to update the UI
                currentUserInfo = dataSnapshot.getValue(User_info_DataObjects.class);
                // [START_EXCLUDE]
                First_name.setText(currentUserInfo.First_name);
                Last_name.setText(currentUserInfo.Last_name);
                Phone_register.setText(currentUserInfo.Phone);
                load_user_organization();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
                Toast.makeText(Edit_user_information_Activity.this, "Failed to load user.",
                        Toast.LENGTH_SHORT).show();
            }
        });///////////////// standars
    }


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_button:
                String f_name = First_name.getText().toString();
                String l_name = Last_name.getText().toString();
                String p_register = Phone_register.getText().toString();

                String organizationId = "";
                if (Choose_organization.getSelectedItem() != null) {
                    String organizationName = Choose_organization.getSelectedItem().toString();
                    organizationId = Org_id_name.get(organizationName);
                }
                update_user_information(f_name, l_name, p_register, organizationId);
                break;

        }
        Toast.makeText(this, "Information Saved..", Toast.LENGTH_SHORT).show();
    }
}
