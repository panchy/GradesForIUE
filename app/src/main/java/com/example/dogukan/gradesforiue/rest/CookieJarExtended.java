package com.example.dogukan.gradesforiue.rest;

import com.example.dogukan.gradesforiue.Pref;
import com.example.dogukan.gradesforiue.models.CookieModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by dogukan on 01.08.2016.
 */
public class CookieJarExtended implements CookieJar {


    private static List<Cookie> localcookies=new ArrayList<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

        if(!cookies.isEmpty())
        {
            for(Cookie c:cookies)
            {
                if(c.name().equals("PHPSESSID"))
                {
                    CookieModel temp = new CookieModel();
                    temp.setDomain(c.domain());
                    temp.setExpiresAt(c.expiresAt());
                    temp.setHostOnly(c.hostOnly());
                    temp.setName(c.name());
                    temp.setPath(c.path());
                    temp.setValue(c.value());
                    temp.setSecure(c.secure());
                    temp.setHttpOnly(c.httpOnly());
                    temp.setPersistent(c.persistent());
                    Pref.setPHPSESSIDcookie(new Gson().toJson(temp));
                }

            }
        }

        localcookies.clear();
        if(!Pref.getPHPSESSIDcookie().equals("!"))
        {
            CookieModel c = new Gson().fromJson(Pref.getPHPSESSIDcookie(),CookieModel.class);
            Cookie ck = new Cookie.Builder()
                    .name(c.getName())
                    .domain(c.getDomain())
                    .expiresAt(c.getExpiresAt())
                    .path(c.getPath())
                    .value(c.getValue())
                    .build();
            localcookies.add(ck);
        }


    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return localcookies;
    }
}
