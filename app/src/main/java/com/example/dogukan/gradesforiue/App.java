package com.example.dogukan.gradesforiue;

import android.app.Application;
import android.content.Context;

/**
 * Created by dogukan on 01.08.2016.
 */
public class App extends Application {
    public static Context getmContext() {
        return mContext;
    }

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

}
