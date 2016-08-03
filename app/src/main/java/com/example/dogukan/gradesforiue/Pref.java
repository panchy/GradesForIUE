package com.example.dogukan.gradesforiue;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dogukan on 01.08.2016.
 */
public class Pref {
    private static SharedPreferences pref;
    private static SharedPreferences getSettings()
    {
        if(pref==null)
        {
            pref= App.getmContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        }
        return pref;
    }

    public static void setPHPSESSIDcookie(String val)
    {
        getSettings().edit().putString("phpsessid",val).apply();
    }

    public static String getPHPSESSIDcookie()
    {
       return getSettings().getString("phpsessid","!");
    }

    public static void setUsername(String val)
    {
        getSettings().edit().putString("username",val).apply();
    }

    public static String getUsername()
    {
        return getSettings().getString("username","!");
    }

    public static void setPassword(String val)
    {
        getSettings().edit().putString("password",val).apply();
    }

    public static String getPassword()
    {
        return getSettings().getString("password","!");
    }
    public static void setPin(String val)
    {
        getSettings().edit().putString("pin",val).apply();
    }

    public static String getPin()
    {
        return getSettings().getString("pin","!");
    }



}
