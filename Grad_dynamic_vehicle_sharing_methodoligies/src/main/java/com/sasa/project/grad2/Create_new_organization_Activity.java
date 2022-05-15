package com.sasa.project.grad2;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sasa.project.grad2.Firebase_DataObjects.Organization_DataObjects;

import java.util.HashMap;
import java.util.Map;


public class Create_new_organization_Activity extends BaseActivity {


    private EditText name_org;
    private EditText type_org;
    private EditText description_org;
    private EditText email_org;
    private EditText url_org;
    private FloatingActionButton add_org;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_organization);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        name_org = findViewById(R.id.name_org);
        type_org = findViewById(R.id.type_organization);
        description_org = findViewById(R.id.description_org);
        email_org = findViewById(R.id.email_org);
        url_org = findViewById(R.id.url_org);
        add_org = findViewById(R.id.add_org);
        add_org.setOnClickListener(new View.OnClickListener() {public void onClick(View v)
        { Create(); }
        });
    }

    private void Create() {
        final String org_name = name_org.getText().toString();
        final String org_type = type_org.getText().toString();
        final String org_description = description_org.getText().toString();
        final String org_email = email_org.getText().toString();
        final String org_url = url_org.getText().toString();
        if (TextUtils.isEmpty(org_name)) {
            name_org.setError("Missing");
            return;
        }
        if (TextUtils.isEmpty(org_type)) {
            name_org.setError("Missing");
            return;
        }

        if (TextUtils.isEmpty(org_description)) {
            name_org.setError("Missing");
            return;
        }


        if (TextUtils.isEmpty(org_email)) {
            name_org.setError("Missing");
            return;
        }

        if (TextUtils.isEmpty(org_url)) {
            name_org.setError("Missing");
            return;
        }


        setEditingEnabled(false);
        Toast.makeText(this, "Adding Organization_DataObjects...", Toast.LENGTH_SHORT).show();
        organization_add_database(new Organization_DataObjects(org_name, org_type, org_description, org_email, org_url));
      setEditingEnabled(true);
        finish();
    }
/// check en feeh 7aga na2sa
    private void setEditingEnabled(boolean enabled) {
        name_org.setEnabled(enabled);
        type_org.setEnabled(enabled);
        description_org.setEnabled(enabled);
        email_org.setEnabled(enabled);
        url_org.setEnabled(enabled);

        if (enabled) {
            add_org.setVisibility(View.VISIBLE);
        } else {
            add_org.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void organization_add_database(Organization_DataObjects organizationDataObjects) {
        String ord_id = databaseReference.child("organizations").push().getKey();
        Map<String, Object> organizationValues = organizationDataObjects.OrganizationObjects();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/organizations/" + ord_id, organizationValues);
        // make it default
        databaseReference.child("users").child(getUid()).child("default_organization").setValue(ord_id);
        databaseReference.updateChildren(childUpdates);
    }

}
