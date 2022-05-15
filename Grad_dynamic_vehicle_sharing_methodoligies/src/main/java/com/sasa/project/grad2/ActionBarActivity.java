package com.sasa.project.grad2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

///////////// gebto men https://www.uplabs.com/posts/navigationtabbar
public class ActionBarActivity {

    private NavigationTabBar Tabs;
    private Activity run_nav;
    private static ActionBarActivity actionbaractivity = null;
    private int INDEX;
    Handler handler;
    Runnable runnable;


    public static ActionBarActivity getActionbar(){
        if(actionbaractivity == null){
            actionbaractivity = new ActionBarActivity();
        }
        return actionbaractivity;
    }

    public void initUI(final Activity Nav_v){
//        final ViewPager viewPager = (run_nav) findViewById(R.id.NavB);
//        viewPager.setAdapter(new PagerAdapter() {

        this.run_nav = Nav_v;
        Tabs = run_nav.findViewById(R.id.NavB);
        ViewPager viewPager = run_nav.findViewById(R.id.NavBar);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            /////3adad el buttons fel navbar ely hast3mlha ta7t
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = LayoutInflater.from(
                        Nav_v.getBaseContext()).inflate(R.layout.action_bar, null, false);
//                final TextView txtPage = (TextView) view.findViewById(R.id.action_bar);
//                txtPage.setText(String.format("Page #%d", position));
                container.addView(view);
                return null;
            }
        });
// add el sora wel loon
        final String[] colors = run_nav.getResources().getStringArray(R.array.NavB_colors);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        run_nav.getResources().getDrawable(R.drawable.ppp),
                        Color.parseColor(colors[0]))
                        //  .title("Heart")
                        //   .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        run_nav.getResources().getDrawable(R.drawable.home_button),
                        Color.parseColor(colors[2]))
                        //  .title("Heart")
                        // .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        run_nav.getResources().getDrawable(R.drawable.a),
                        Color.parseColor(colors[1]))
                        //  .title("Heart")
                        // .badgeTitle("NTB")
                        .build()
        );


        Tabs.setModels(models);
        Tabs.setViewPager(viewPager, 2);
        Tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                Tabs.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        //////// failed msh h33rf aratb hena -------------------
//        navigationTabBar.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
//                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
//                    navigationTabBar.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            model.showBadge();
//                        }
//                    }, i * 100);
//                }
//            }
//        }, 500);
//    }
        /// tarteeb el navbar hift7 anhy page
        Tabs.setModels(models);
        if(run_nav instanceof User_page_Activity){
            Tabs.setViewPager(viewPager, 0);
        }else if(run_nav instanceof MainActivity) {
            Tabs.setViewPager(viewPager, 1);
        }
        else if(run_nav instanceof Display_organizations_Activity){
            Tabs.setViewPager(viewPager, 2);
        }

    }
    // on click
    public void Tab_click(final Activity current){
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                int index = ActionBarActivity.this.INDEX;
                if(index==0 && !(current instanceof User_page_Activity)){
                    Intent intent = new Intent(current, User_page_Activity.class);
                    current.startActivity(intent);
                }
                if(index==1 && !(current instanceof MainActivity)){
                    Intent intent = new Intent(current, MainActivity.class);
                    current.startActivity(intent);
                }

                if(index==2 && !(current instanceof Display_organizations_Activity)){
                    Intent intent = new Intent(current, Display_organizations_Activity.class);
                    current.startActivity(intent);
                }
            }
        };

        Tabs.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                ActionBarActivity.this.INDEX = index;
                System.out.println(index);
                handler.postDelayed(runnable, 300);
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {

            }
        });
    }
}
