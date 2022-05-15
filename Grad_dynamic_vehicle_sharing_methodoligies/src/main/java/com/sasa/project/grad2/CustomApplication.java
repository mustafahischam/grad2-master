package com.sasa.project.grad2;

import android.app.Application;
import com.google.android.libraries.places.api.Places;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDbJdB5ZPbyVqbFMehjikoLLjjSwXFYhLk");
        }
    }
}
