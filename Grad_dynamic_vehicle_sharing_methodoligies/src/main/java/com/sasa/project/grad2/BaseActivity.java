package com.sasa.project.grad2;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;



public class BaseActivity extends AppCompatActivity {// suppor el navbar

    private ActionBarActivity actionBarActivity;
    @Override
    protected void attachBaseContext(Context newBase) {
//        Context context = MyContextWrapper.wrap(newBase, lang);
//        super.attachBaseContext(context);
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));// 3shan ye suppor ai language
    }

    public void setActionBarActivity(Activity activity){
        this.actionBarActivity = ActionBarActivity.getActionbar();
        actionBarActivity.initUI(activity);
        actionBarActivity.Tab_click(activity);
    }

    public String getUid() {

        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}
