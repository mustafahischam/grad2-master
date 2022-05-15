package com.sasa.project.grad2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sasa.project.grad2.Firebase_DataObjects.User_info_DataObjects;

public class User_page_Activity extends BaseActivity {


    private Tabs_adapter List_Adpater;

    private User_info_DataObjects userInfo;
    private DatabaseReference databaseReference;
    private TextView Display_username;
    final Bundle bundle = new Bundle();/// transport data

    @Override
    protected void onCreate(Bundle savedInstanceState) {// 3shan ye save el 7agat el adima also fel profile informatin
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        this.Display_username = this.findViewById(R.id.display_username);
        String uID = getUid();

        if(uID == getUid()){// userInfo data fragment
            ImageButton Signout_button = this.findViewById(R.id.signout_button);
            Signout_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v)
                {
                    FirebaseAuth.getInstance().signOut();
                    // from userInfo page to first page (sgnining)
                    startActivity(new Intent(User_page_Activity.this, Sign_In_Activity.class));
                    finish(); // activity shutdown
                }
            });
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        bundle.putString("uID", uID);
        this.call_uID(uID);
        setActionBarActivity(this);
    }

    private void Display_fragment(){
        List_Adpater = new Tabs_adapter(getSupportFragmentManager()){
            private final Fragment[] fragment_count = new Fragment[] {
///              new User_Organizations_Fragment.getchild(organizationId),
// /              new User_page_Passenger_layout.getchild(organizationId),
                    new User_Organizations_Fragment(),
                    new User_page_Passenger_layout(),
                    new User_page_Driver_Fragment(),
                    new User_page_information_Fragment(),
            };
            @Override
            public Fragment getItem(int position) {
                fragment_count[position].setArguments(User_page_Activity.this.bundle);
                return fragment_count[position];
            }
            @Override
            public int getCount() {
                /// to stop open different fragment
                return fragment_count.length;
            }
        };
        
        /// display el fragment
        ViewPager viewpager = findViewById(R.id.container);
        viewpager.setAdapter(List_Adpater);////3shan a display fe list
        TabLayout tabLayout = findViewById(R.id.tabs);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpager));
    }

    
    public static class Tabs_adapter extends FragmentPagerAdapter {
        public Tabs_adapter(FragmentManager x) {
            super(x);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
            // for the counted fragment
        }
        @Override
        public int getCount() {
            return 3;
        }
    }
    public static class PlaceholderFragment extends Fragment {

        private static final String tab_number = "section_number";
        public PlaceholderFragment() {}

        public static PlaceholderFragment newInstance(int i) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(tab_number, i);
            fragment.setArguments(args);
            return fragment;
        }

    }

    public void call_uID(String User_id) {

        // System.out.println(" updaaaaaaaaaate ");

        databaseReference.child("users").child(User_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(User_info_DataObjects.class);
                String userName = userInfo.First_name + " " + userInfo.Last_name;
                User_page_Activity.this.Display_username.setText(userName);
                bundle.putString("userName", userInfo.First_name);
                User_page_Activity.this.Display_fragment();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
