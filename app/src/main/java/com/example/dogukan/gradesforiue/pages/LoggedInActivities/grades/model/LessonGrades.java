package com.example.dogukan.gradesforiue.pages.LoggedInActivities.grades.model;

import java.util.List;

/**
 * Created by dogukan on 02.08.2016.
 */
public class LessonGrades {

    private List<String> grades;
    private String lessonNameInfo;

    public List<String> getGrades() {
        return grades;
    }

    public void setGrades(List<String> grades) {
        this.grades = grades;
    }

    public String getLessonNameInfo() {
        return lessonNameInfo;
    }

    public void setLessonNameInfo(String lessonNameInfo) {
        this.lessonNameInfo = lessonNameInfo;
    }
}
