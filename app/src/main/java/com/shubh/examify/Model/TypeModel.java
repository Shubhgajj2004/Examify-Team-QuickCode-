package com.shubh.examify.Model;

import androidx.fragment.app.Fragment;

//Model used by TabLayout
public class TypeModel {

    Fragment fm ;
    String title;

    public TypeModel(Fragment fm, String title) {
        this.fm = fm;
        this.title = title;
    }

    public Fragment getFm() {
        return fm;
    }

    public void setFm(Fragment fm) {
        this.fm = fm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
