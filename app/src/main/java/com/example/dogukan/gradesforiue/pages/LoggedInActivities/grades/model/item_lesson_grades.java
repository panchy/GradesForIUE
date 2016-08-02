package com.example.dogukan.gradesforiue.pages.LoggedInActivities.grades.model;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dogukan.gradesforiue.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dogukan on 02.08.2016.
 */
public class item_lesson_grades extends FrameLayout {

    @BindView(R.id.lessonTitle)
    TextView mTitle;

    @BindView(R.id.gradesContainer)
    LinearLayout mGradesContainer;

    public item_lesson_grades(Context context) {
        super(context);
    }

    public void init(String title, List<String> items)
    {
        View view = inflate(getContext(), R.layout.item_lesson_grades, this);
        ButterKnife.bind(this,view);

        mTitle.setText(title);

        for(String s:items)
        {
            TextView temp = new TextView(getContext());
            temp.setText(s);
            mGradesContainer.addView(temp);
        }

    }


}
