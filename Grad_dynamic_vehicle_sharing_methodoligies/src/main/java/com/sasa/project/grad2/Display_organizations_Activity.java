package com.sasa.project.grad2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;


public class Display_organizations_Activity extends BaseActivity {

    private FragmentPagerAdapter fragmentPagerAdapter; //calling fragments
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_organizations_activity);
        setActionBarActivity(this);
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] fragments = new Fragment[] {
                    new All_Organizations_Fragment(),
                    new User_Organizations_Fragment()
            };
            private final String[] Names_Fragments = new String[] {
                    getString(R.string.all_organizations_fragment),
                    getString(R.string.user_organization_fragment)
            };
            public Fragment getItem(int position) {
                return fragments[position];
            }
            public int getCount() {
                return fragments.length;
            }
            public CharSequence getPageTitle(int position) {
                return Names_Fragments[position];
            }
        };
        viewPager = findViewById(R.id.container);/// search in main
        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        /// display create organization button
        findViewById(R.id.create_new_organization_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Display_organizations_Activity.this, Create_new_organization_Activity.class));
            }
        });
    }

}
