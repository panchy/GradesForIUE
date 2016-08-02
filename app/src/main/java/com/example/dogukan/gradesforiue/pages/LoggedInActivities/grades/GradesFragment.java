package com.example.dogukan.gradesforiue.pages.LoggedInActivities.grades;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dogukan.gradesforiue.R;
import com.example.dogukan.gradesforiue.pages.LoggedInActivities.grades.model.LessonGrades;
import com.example.dogukan.gradesforiue.pages.LoggedInActivities.grades.model.item_lesson_grades;
import com.example.dogukan.gradesforiue.rest.RestClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.QueryMap;


public class GradesFragment extends Fragment {

    @BindView(R.id.cardsContainer)
    LinearLayout mCardsContainer;

    @BindView(R.id.fab)
    FloatingActionButton mFab;


    public GradesFragment() {
        // Required empty public constructor
    }

    public static GradesFragment newInstance() {

        Bundle args = new Bundle();

        GradesFragment fragment = new GradesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_grades, container, false);
        ButterKnife.bind(this,v);
        mFab.hide();
        renderFirstGrades();

        return v;
    }

    private void renderGrades(String year,String semester,String formposted)
    {

        RestClient.getService().getGradesSearch(year,semester,formposted).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


             try
             {
                 body=response.body().string();
                 body2=body;
                 new renderGradesAsyncTask().execute();
                 new renderTermButtonAsyncTask().execute();
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

    private void renderFirstGrades()
    {
        RestClient.getService().getGrades().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body()!=null)
                {
                    try
                    {
                        body=response.body().string();
                        body2=body;
                        new renderGradesAsyncTask().execute();
                        new renderTermButtonAsyncTask().execute();
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


    String body;
    String body2;

    private class renderGradesAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private List<LessonGrades> tempLessons;
        @Override
        protected Void doInBackground(Void... voids) {
            tempLessons=new ArrayList<>();


                Document doc = Jsoup.parse(body);
                Element gradesTable = doc.select("table.oasistable").last();
                if(gradesTable!=null)
                {
                    if(gradesTable.select("tbody").first().children().size()>1)
                    {
                        gradesTable.select("tr.title").remove();

                        Log.e("asda",String.valueOf(gradesTable.select("tbody").first().children().size()));

                        for (Element e:gradesTable.getElementsByAttributeValueContaining("class","dersback"))
                        {
                            LessonGrades temp = new LessonGrades();
                            String code = e.select("td").first().text();
                            e.select("td").first().remove();
                            String lesson =  e.select("td").first().text();
                            e.select("td").first().remove();
                            e.select("td").first().remove();
                            String instructor =  e.select("td").first().text();
                            e.select("td").first().remove();
                            temp.setLessonNameInfo(code + " | " + lesson + " | " + instructor);
                            temp.setGrades(new ArrayList<String>());
                            tempLessons.add(temp);
                            e.remove();
                        }

                        int counter=0;

                        Elements tr_grades = gradesTable.getElementsByClass("artema");
                        Log.e("asd",String.valueOf(tr_grades.size()));
                        for(Element e:tr_grades)
                        {
                            List<String> titleAndGrade = new ArrayList<>();

                            List<String> titles = new ArrayList<>();
                            for(Element title:e.select("tr").first().children())
                            {
                                titles.add(title.text());
                                //title.remove();
                            }
                            e.select("tr").first().remove();

                            List<String> grades = new ArrayList<>();
                            for(Element grade:e.select("tr").first().children())
                            {
                                grades.add(grade.text());
                               // grade.remove();
                            }
                            for(int i=0;i<titles.size();i++)
                            {
                                titleAndGrade.add(titles.get(i) + ": "+grades.get(i));
                            }
                            tempLessons.get(counter).setGrades(titleAndGrade);
                            counter++;
                        }

                    }
                }




            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mCardsContainer.removeAllViews();
            for(LessonGrades lg:tempLessons)
            {

                item_lesson_grades view = new item_lesson_grades(getActivity());
                view.init(lg.getLessonNameInfo(),lg.getGrades());
                mCardsContainer.addView(view);
            }

        }
    }


    private class renderTermButtonAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private List<String> years;
        private List<String> semesters;
        String selectedYear;
        String selectedSemester;
        String selectedSemesterValue;
        List<String> semesterValues;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mFab.show();
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final MaterialDialog dia = new MaterialDialog.Builder(getActivity())
                            .customView(R.layout.dialog_select,true)
                            .cancelable(true)
                            .title(R.string.selectys)
                            .positiveText(R.string.search)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    renderGrades(selectedYear,selectedSemesterValue,"+Search+");

                                }
                            })
                            .build();

                    final TextView currentYear = (TextView) dia.findViewById(R.id.selectedYear);
                    currentYear.setText(selectedYear);

                    final TextView currentSemester = (TextView) dia.findViewById(R.id.selectedSemester);
                    currentSemester.setText(selectedSemester);

                    Button setSemesterBtn = (Button) dia.findViewById(R.id.btnSelectSemester);
                    Button setYearBtn = (Button) dia.findViewById(R.id.btnSelectYear);



                    setSemesterBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new MaterialDialog.Builder(getActivity())
                                    .title("Select Semester")
                                    .items(semesters)
                                    .alwaysCallSingleChoiceCallback()
                                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                        @Override
                                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                            selectedSemester = semesters.get(which);
                                            selectedSemesterValue = semesterValues.get(which);

                                            currentSemester.setText(selectedSemester);


                                            return true;
                                        }
                                    })
                                    .show();


                        }
                    });

                    setYearBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new MaterialDialog.Builder(getActivity())
                                    .title("Select Year")
                                    .items(years)
                                    .alwaysCallSingleChoiceCallback()
                                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                        @Override
                                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                            selectedYear = years.get(which);


                                            currentYear.setText(selectedYear);


                                            return true;
                                        }
                                    })
                                    .show();

                        }
                    });

                    dia.show();

                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            years=new ArrayList<>();
            semesters=new ArrayList<>();
            semesterValues=new ArrayList<>();

             Document doc = Jsoup.parse(body2);
                Element yearsTable = doc.select("table.oasistable").first();

                for(Element y:yearsTable.getElementById("year").children())
                {
                    if(y.hasAttr("selected"))
                    {
                        selectedYear = y.text();
                    }

                    years.add(y.text());


                }

                for(Element s:yearsTable.getElementById("semester1").children())
                {

                    semesterValues.add(s.val());

                    if(s.hasAttr("selected"))
                    {
                        selectedSemester = s.text();
                        selectedSemesterValue = s.val();
                    }
                    semesters.add(s.text());


                }




            return null;
        }
    }

}
