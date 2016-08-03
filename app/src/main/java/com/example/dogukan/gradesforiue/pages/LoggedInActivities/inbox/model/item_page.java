package com.example.dogukan.gradesforiue.pages.LoggedInActivities.inbox.model;

import android.content.Context;
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
public class item_page extends LinearLayout {

    @BindView(R.id.pageTxt)
    TextView mPage;

    private String navto;

    public item_page(Context context) {
        super(context);
    }

    public void init(PageModel model)
    {
        View view = inflate(getContext(), R.layout.item_page, this);
        ButterKnife.bind(this,view);

        mPage.setText(model.getPage());
        navto = model.getNavTo();

        if(model.isCurrent())
            mPage.setTextColor(getResources().getColor(R.color.colorAccent));
        else
            mPage.setTextColor(getResources().getColor(R.color.secondary_text));

    }

    public String getNavTo()
    {
        return navto;
    }

}
