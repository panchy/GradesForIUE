package com.example.dogukan.gradesforiue.pages.LoggedInActivities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.dogukan.gradesforiue.Pref;
import com.example.dogukan.gradesforiue.R;
import com.example.dogukan.gradesforiue.pages.LoggedInActivities.grades.GradesFragment;
import com.example.dogukan.gradesforiue.pages.login.LoginActivity;
import com.example.dogukan.gradesforiue.pages.login.LoginFragment;
import com.example.dogukan.gradesforiue.rest.RestClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.DrawerLayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.fragmentContainer)
    FrameLayout mFragmentContainer;

    private NavigationView mNavView=null;

    private TextView mLogout;

    private MaterialDialog dialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dialogProgress=new MaterialDialog.Builder(this)
                .progress(true,0)
                .cancelable(false)
                .title(getResources().getString(R.string.waitabit))
                .content(getResources().getString(R.string.workingonlogin))
                .build();

        initDrawer();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, GradesFragment.newInstance())
                .commit();


    }

    private void enableUI(boolean isEnabled)
    {
        if(isEnabled)
        {
            dialogProgress.hide();
        }
        else
        {
            dialogProgress.show();
        }
    }

    private void initDrawer()
    {

        mNavView =(NavigationView) mDrawerLayout.findViewById(R.id.nav_view);
        mLogout = (TextView) mNavView.findViewById(R.id.nav_logout);

        mNavView.setNavigationItemSelectedListener(this);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableUI(false);
                RestClient.getService().logout().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        enableUI(true);
                        Pref.setPassword("!");
                        Pref.setUsername("!");
                        Pref.setPin("!");
                        Pref.setPHPSESSIDcookie("!");
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().hide();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().show();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        final View header = mNavView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.nav_header_username)).setText(Pref.getUsername());
        final CircleImageView avatar = (CircleImageView) header.findViewById(R.id.avatar);

        RestClient.getService().getProfile().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try
                {
                    Document doc = Jsoup.parse(response.body().string());
                    String img = doc.select("a[href*=change_photo] > img").attr("src");
                    img = "https://odin-oasis.izmirekonomi.edu.tr/"+img.replaceAll("../../../","");
                    Glide.with(MainActivity.this).load(img).into(avatar);
                }
                catch (IOException d)
                {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(GravityCompat.START);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        closeDrawer();

        switch (item.getItemId())
        {
            case R.id.drawer_grades_btn:

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, GradesFragment.newInstance())
                        .commit();

                break;

        }

        return false;
    }
}
