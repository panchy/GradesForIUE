package com.example.dogukan.gradesforiue.pages.LoggedInActivities.inbox.model;

/**
 * Created by dogukan on 03.08.2016.
 */
public class PageModel {

    private String page;
    private String navTo;
    private boolean isCurrent;

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getNavTo() {
        return navTo;
    }

    public void setNavTo(String navTo) {
        this.navTo = navTo;
    }
}
