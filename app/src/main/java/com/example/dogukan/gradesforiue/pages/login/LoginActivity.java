package com.example.dogukan.gradesforiue.pages.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dogukan.gradesforiue.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, LoginFragment.newInstance())
                .commit();

    }
}
