package com.example.dogukan.gradesforiue.pages.LoggedInActivities.inbox.model;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dogukan.gradesforiue.R;
import com.example.dogukan.gradesforiue.rest.RestClient;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dogukan on 03.08.2016.
 */
public class item_message extends LinearLayout {

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.content)
    TextView mContent;

    public item_message(Context context) {
        super(context);
    }

    public void init(String _title, String _content, final boolean isRead,final String id)
    {
        View view = inflate(getContext(), R.layout.item_message, this);
        ButterKnife.bind(this,view);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               if(mContent.getVisibility() == VISIBLE)
               {
                   mContent.setVisibility(GONE);
               }
                else
               {
                   mContent.setVisibility(VISIBLE);
               }
                if(!isRead)
                {

                    Map<String,String> q = new HashMap<String, String>();
                    q.put("read",id);
                    q.put("type","in");
                    RestClient.getService().queryMessages(q).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            mTitle.setTextColor(getResources().getColor(R.color.primary_text));
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                }
            }
        });

        mContent.setText(_content);
        mTitle.setText(_title);

        if(!isRead)
            mTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        else
            mTitle.setTextColor(getResources().getColor(R.color.primary_text));
    }

}
