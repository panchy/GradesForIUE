package com.example.dogukan.gradesforiue.events;

/**
 * Created by dogukan on 03.08.2016.
 */
public class MessaegePageSwitchedEvent {
    private String page;

    public MessaegePageSwitchedEvent(String page) {
        this.page = page;
    }

    public String getPage() {

        return page;
    }
}
