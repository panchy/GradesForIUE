package com.example.dogukan.gradesforiue.pages.LoggedInActivities.inbox;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dogukan.gradesforiue.R;
import com.example.dogukan.gradesforiue.events.MessaegePageSwitchedEvent;
import com.example.dogukan.gradesforiue.pages.LoggedInActivities.inbox.model.MessageModel;
import com.example.dogukan.gradesforiue.pages.LoggedInActivities.inbox.model.PageModel;
import com.example.dogukan.gradesforiue.pages.LoggedInActivities.inbox.model.item_message;
import com.example.dogukan.gradesforiue.pages.LoggedInActivities.inbox.model.item_page;
import com.example.dogukan.gradesforiue.rest.RestClient;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutboxFragment extends Fragment {

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindView(R.id.pages)
    LinearLayout mPages;

    @BindView(R.id.messagesContainer)
    LinearLayout mMessagesContainer;

    @BindView(R.id.outbox)
    Button mOutbox;

    @BindView(R.id.inbox)
    Button mInbox;

    @BindView(R.id.compose)
    Button mCompose;


    private String sender="";
    private String subject="";
    private String page="1";
    private String message="";


    public OutboxFragment() {
        // Required empty public constructor
    }

    public static OutboxFragment newInstance() {

        Bundle args = new Bundle();

        OutboxFragment fragment = new OutboxFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this,v);

        setOnClickListeners();
        LoadMessages();

        return v;
    }

    private void setOnClickListeners()
    {

        mOutbox.setTextColor(getResources().getColor(R.color.colorAccent));

        mInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessaegePageSwitchedEvent("inbox"));
            }
        });

        mCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessaegePageSwitchedEvent("compose"));
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final MaterialDialog dia = new MaterialDialog.Builder(getActivity())
                        .title(R.string.search_)
                        .customView(R.layout.dialog_search_messages,false)
                        .positiveText(R.string.search__)
                        .build();

                final EditText sender_ = (EditText) dia.getCustomView().findViewById(R.id.sender);
                final EditText subject_ = (EditText) dia.getCustomView().findViewById(R.id.subject);
                final EditText message_ = (EditText) dia.getCustomView().findViewById(R.id.message);

                dia.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sender=sender_.getText().toString();
                        message=message_.getText().toString();
                        subject=subject_.getText().toString();
                        LoadMessages();
                        dia.hide();
                    }
                });
                dia.show();

            }
        });
    }


    private void LoadMessages()
    {
        RestClient.getService().getOutbox(page,sender,subject,message).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body()==null)
                return;

                try
                {
                    body=response.body().string();
                    new getMessagesAsyncTask().execute();
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


    private String body;
    private class getMessagesAsyncTask extends AsyncTask<Void,Void,Void>
    {
        List<MessageModel> _models;
        List<String> _messages;
        List<String> _titles;
        List<Boolean> _isReads;
        List<String> _ids;
        List<PageModel> _pages;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mMessagesContainer.removeAllViews();
            mPages.removeAllViews();

            for(MessageModel m:_models)
            {
                item_message message = new item_message(getActivity());
                message.init(m.getTitle(),m.getContent(),m.isRead(),m.getId());
                mMessagesContainer.addView(message);
            }

            for(PageModel p:_pages)
            {
                final item_page _page=new item_page(getActivity());
                _page.init(p);
                _page.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        page=_page.getNavTo();
                        LoadMessages();
                    }
                });
                mPages.addView(_page);
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {

            _models=new ArrayList<>();
            _ids=new ArrayList<>();
            _isReads=new ArrayList<>();
            _messages=new ArrayList<>();
            _titles=new ArrayList<>();
            _pages = new ArrayList<>();

            Document doc = Jsoup.parse(body);

            if(doc.getElementById("list")!=null)
            {



            Element messagesTable = doc.getElementById("list").select("tbody").first();
            messagesTable.select("tr").first().remove();
            messagesTable.select("tr").last().remove();
            messagesTable.select("tr").last().remove();
            messagesTable.select("tr").last().select("td > div").first().remove();

            for(Element div:messagesTable.select("tr").last().select("td").first().children())
            {
                PageModel temp = new PageModel();
                temp.setCurrent(div.className().contains("cur"));
                temp.setNavTo(div.attr("onclick").replaceAll("\\D+",""));
                temp.setPage(div.text());
                _pages.add(temp);

            }

            Log.e("asd",messagesTable.toString());
            messagesTable.select("tr").last().remove();
            Log.e("asd",messagesTable.toString());
            messagesTable.select("tr").last().remove();
            messagesTable.select("input[id=cur_page]").last().remove();



            for(Element m:messagesTable.getElementsByClass("mes_text"))
            {

                _messages.add(m.select("td").text());
                m.remove();
            }

            for(Element t:messagesTable.children())
            {
                if(t.className().contains("unread"))
                {
                    _isReads.add(false);
                }
                else
                {
                    _isReads.add(true);
                }

                String id= t.select("td").first().select("input").attr("name").replaceAll("\\D+","");
                t.select("td").first().remove();
                _ids.add(id);
                String title=  t.select("td").first().text();
                t.select("td").first().remove();
                title += " - "+ t.select("td").first().text();
                t.select("td").first().remove();
                title += " - " + t.select("td").first().text();
                t.select("td").first().remove();
                _titles.add(title);
            }

            for(int i=0;i<_titles.size();i++)
            {
                MessageModel temp = new MessageModel();
                temp.setContent(_messages.get(i));
                temp.setId(_ids.get(i));
                temp.setRead(_isReads.get(i));
                temp.setTitle(_titles.get(i));
                _models.add(temp);
            }
            }


            return null;
        }
    }

}
