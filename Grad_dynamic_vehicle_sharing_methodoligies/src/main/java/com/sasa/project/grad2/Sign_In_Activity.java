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

public class Sign_In_Activity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Sign_In_Activity";

    private EditText Email_signin;
    private EditText Password_signin;
    private Button Signin_button;
    private Button Signup_button2;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance(); /// 3shan a check el user logged in or not
        ///////// layout
        setContentView(R.layout.activity_sign_in);
        Email_signin = findViewById(R.id.email_signin);
        Password_signin = findViewById(R.id.password_signin);
        Signin_button = findViewById(R.id.signin_button);
        Signup_button2 = findViewById(R.id.signup2);

        Signin_button.setOnClickListener(this);
        Signup_button2.setOnClickListener(this);
    }

    ///// lw el user signed in before signin 3latol
    public void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            onSuccess(firebaseAuth.getCurrentUser());
        }
    }

    private void Sign_in() {
        Log.d(TAG, "Sign_in");
        if (check_write_correct()== false) {
            // lw el validate form lsa false dont Sign In
            return;
        }
        String email = Email_signin.getText().toString();
        String password = Password_signin.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "Sign in success" + task.isSuccessful());
                        //hideProgressDialog();
                        if (task.isSuccessful()) {
                            onSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(Sign_In_Activity.this, "Sign inn failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onSuccess(FirebaseUser user) {
        // Go to MainActivity page
        startActivity(new Intent(Sign_In_Activity.this, MainActivity.class));
        finish();
    }


    /// in case el user 3aml 7aga 8alt aw mad5lsh el el needed information
    private boolean check_write_correct() {
        boolean result = true;
        if (TextUtils.isEmpty(Password_signin.getText().toString())) {
            Password_signin.setError("Missing");
            result = false;
        } else {
            Password_signin.setError(null);
        }
        if (TextUtils.isEmpty(Email_signin.getText().toString())) {
            Email_signin.setError("Missing");
            result = false;
        } else {
            Email_signin.setError(null);
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signin_button) {
            Sign_in();
        } else if(i == R.id.signup2){// got so registration page
                startActivity(new Intent(Sign_In_Activity.this,  Register_Activity.class));
        }
    }
}
