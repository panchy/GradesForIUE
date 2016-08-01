package com.example.dogukan.gradesforiue.pages.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dogukan.gradesforiue.R;
import com.example.dogukan.gradesforiue.pages.LoggedInActivities.MainActivity;
import com.example.dogukan.gradesforiue.rest.RestClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.content)
    FrameLayout mContent;

    @BindView(R.id.username)
    EditText mUsername;

    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.rememberme)
    CheckBox mRememberMe;

    @BindView(R.id.btnlogin)
    Button mBtnLogin;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        
        Bundle args = new Bundle();
        
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,v);
        mBtnLogin.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnlogin:
                loginFirstStep();
            break;
        }
    }

    private void loginFirstStep()
    {
        String userid = mUsername.getText().toString();
        String pass = mPassword.getText().toString();
        boolean rememberme = mRememberMe.isChecked();
        RestClient.getService().loginFirstPart(userid,pass,"Login").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body()!=null)
                {
                    try
                    {
                        Document doc = Jsoup.parse(response.body().string());
                        if(!doc.select("input[name=ltype]").isEmpty())
                        {
                            loginSecondStep(doc.select("input[name=ltype]").attr("value"));
                        }
                        else
                        {
                            Snackbar.make(mContent,getString(R.string.couldnt_login),Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    catch (IOException d)
                    {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void loginSecondStep(final String ltype)
    {
        new MaterialDialog.Builder(getActivity())
                .input(getString(R.string.typeyourpinhere), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        RestClient.getService().loginSecondPart(input.toString(),ltype,"Login").enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.body()!=null)
                                {
                                    try
                                    {
                                        Document doc = Jsoup.parse(response.body().string());
                                        if(doc.title().toLowerCase().contains("student"))
                                        {
                                            Intent loggedIn = new Intent(getActivity(), MainActivity.class);
                                            getActivity().startActivity(loggedIn);
                                            getActivity().finish();
                                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                        }
                                        else
                                        {
                                            loginSecondStep(ltype);
                                            Snackbar.make(mContent,getString(R.string.couldnt_login),Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (IOException d)
                                    {

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }).build().show();
    }
}
