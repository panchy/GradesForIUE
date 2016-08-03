package com.example.dogukan.gradesforiue.pages.LoggedInActivities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import com.example.dogukan.gradesforiue.events.MessaegePageSwitchedEvent;
import com.example.dogukan.gradesforiue.pages.LoggedInActivities.grades.GradesFragment;
import com.example.dogukan.gradesforiue.pages.LoggedInActivities.inbox.MessagesFragment;
import com.example.dogukan.gradesforiue.pages.LoggedInActivities.inbox.OutboxFragment;
import com.example.dogukan.gradesforiue.pages.login.LoginActivity;
import com.example.dogukan.gradesforiue.pages.login.LoginFragment;
import com.example.dogukan.gradesforiue.rest.RestClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

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


        Fragment fragment = GradesFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();

       /* getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, GradesFragment.newInstance())
                .commit();*/

        if(!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }

    }

    @Override
    protected void onDestroy() {

        if(EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }

        super.onDestroy();
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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

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


        final View header = mNavView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.nav_header_username)).setText(Pref.getUsername());
        final CircleImageView avatar = (CircleImageView) header.findViewById(R.id.avatar);
        final TextView mrealname = (TextView) header.findViewById(R.id.nav_header_realname);

        RestClient.getService().getProfile().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try
                {
                    Document doc = Jsoup.parse(response.body().string());
                    String img = doc.select("a[href*=change_photo] > img").attr("src");
                    img = "https://odin-oasis.izmirekonomi.edu.tr/"+img.replaceAll("../../../","");
                    Glide.with(MainActivity.this).load(img).into(avatar);

                    String realname = doc.select("a[href=/oasis/student/profile/index.php]").last().text();
                    mrealname.setText(realname);

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

        if(getFragmentManager().findFragmentById(R.id.fragmentContainer)!=null)
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragmentContainer)).commit();




        switch (item.getItemId())
        {
            case R.id.drawer_grades_btn:

                Fragment fragment = GradesFragment.newInstance();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.commit();

                break;
            case R.id.drawer_inbox_btn:

                Fragment fragment2 = MessagesFragment.newInstance();
                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction transaction2 = fm2.beginTransaction();
                transaction2.replace(R.id.fragmentContainer, fragment2);
                transaction2.commit();

                break;

        }

        return false;
    }

    @Subscribe public void onSwitchedEvent(MessaegePageSwitchedEvent event)
    {
        if(getFragmentManager().findFragmentById(R.id.fragmentContainer)!=null)
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.fragmentContainer)).commit();


        if(event.getPage().equals("inbox"))
        {
            Fragment fragment = MessagesFragment.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();

        }
        else if(event.getPage().equals("outbox"))
        {
            Fragment fragment = OutboxFragment.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();
        }
        else if(event.getPage().equals("compose"))
        {

        }
    }
}
