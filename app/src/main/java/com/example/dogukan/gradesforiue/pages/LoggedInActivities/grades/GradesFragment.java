package com.example.dogukan.gradesforiue.pages.LoggedInActivities.grades;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dogukan.gradesforiue.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class GradesFragment extends Fragment {


    public GradesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grades, container, false);
    }

}
