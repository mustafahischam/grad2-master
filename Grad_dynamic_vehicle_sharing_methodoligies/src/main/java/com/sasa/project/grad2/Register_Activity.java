package com.sasa.project.grad2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sasa.project.grad2.Firebase_DataObjects.User_info_DataObjects;


/**
 * Created by Marta on 10/8/17.
 */

public class Register_Activity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "Register_Activity";

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Button Signup_button;
    private EditText First_Name, Last_Name,Email_register, Password_register, Phone_register;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        ///////// layout
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Email_register = findViewById(R.id.email_register);
        Password_register = findViewById(R.id.password_register);
        First_Name = findViewById(R.id.first_name);
        Last_Name = findViewById(R.id.last_name);
        Phone_register = findViewById(R.id.phone_register);
        Signup_button = findViewById(R.id.signup_button);
        Signup_button.setOnClickListener(this);


    }
    ///// lw el user signed in before signin 3latol
    public void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            onSuccess(firebaseAuth.getCurrentUser());
        }
    }

    private void Sign_up() {
        Log.d(TAG, "signUp");
        if (!check_write_correct()) {
            return;
        }
        String email = Email_register.getText().toString();
        String password = Password_register.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onSuccess(task.getResult().getUser());
                } else {
                    Toast.makeText(Register_Activity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean check_write_correct() {
        boolean result = true;
        if (TextUtils.isEmpty(Email_register.getText().toString())) {
            Email_register.setError("Missing");
            result = false;
        } else {
            Email_register.setError(null);
        }

        if (TextUtils.isEmpty(Password_register.getText().toString())) {
            Password_register.setError("Missing");
            result = false;
        } else {
            Password_register.setError(null);
        }
        return result;
    }
    private void onSuccess(FirebaseUser user) {
        String first = First_Name.getText().toString();
        String last = Last_Name.getText().toString();
        String phone = "";
        if (Phone_register != null) {
            phone = Phone_register.getText().toString();
        }

        firebase_add(user.getUid(),user.getEmail(), first, last, phone);
        //lw success = true yro7 lel MainActivity page
        startActivity(new Intent(Register_Activity.this,  MainActivity.class));
        finish();
    }
    /// in case el user 3aml 7aga 8alt aw mad5lsh el el needed information


    //7ot eldata bta3t el user fel firbase
    private void firebase_add(String userId, String email, String first, String last, String phone) {
        User_info_DataObjects userInfo = new User_info_DataObjects(email, first, last, phone);
        databaseReference.child("users").child(userId).setValue(userInfo);
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_button:
                Sign_up();
                break;

        }
    }
}

