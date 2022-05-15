package com.sasa.project.grad2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sasa.project.grad2.Firebase_DataObjects.Post_info_DataObjects;
import java.util.HashMap;
import java.util.Map;




public class  MainActivity extends BaseActivity {
    private DatabaseReference databaseReference;
    private ImageButton Sign_out_button;
 //   String play ;


    protected void onCreate(Bundle bundle) {
        String User_id = getUid();
        super.onCreate(bundle);
        setContentView(R.layout.mainactivity);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.driver_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, Passenger_Driver_Main_Activity.class);
                intent.putExtra(Passenger_Driver_Main_Activity.POST_TYPE, Post_info_DataObjects.Post_type.Passenger.name());
                startActivity(intent);
            }
        });

        findViewById(R.id.passenger_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, Passenger_Driver_Main_Activity.class);
                intent.putExtra(Passenger_Driver_Main_Activity.POST_TYPE, Post_info_DataObjects.Post_type.Driver.name());
                startActivity(intent);
            }
        });
        Update_Activity();
        setActionBarActivity(this);

        if(User_id == getUid()){
            Sign_out_button = this.findViewById(R.id.signout_button);
            Sign_out_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    FirebaseAuth.getInstance().signOut();
                    // roo7 mn el main page lel sign in page lw el user das signout
                    startActivity(new Intent(MainActivity.this, Sign_In_Activity.class));
                    finish();
                }
            });
        }


    }
/// update activity type
    public void Update_Activity(){
        Map<String, Object> hashMap = new HashMap<>();
        databaseReference.updateChildren(hashMap);
    }

}
