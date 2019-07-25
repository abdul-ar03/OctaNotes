package com.ar.developments.octanotes;

import java.io.Serializable;

/**
 * Created by Admin on 12/9/2016.
 */
public class App_details implements Serializable {

    private int sort1;
    private int sort2;
    private int view;

    public App_details(int sort1,int sort2,int view){
        this.sort1=sort1;
        this.sort2=sort2;
        this.view=view;

    }

    public int getSort1() {
        return sort1;
    }

    public void setSort1(int sort1) {
        this.sort1 = sort1;
    }

    public int getSort2() {
        return sort2;
    }

    public void setSort2(int sort2) {
        this.sort2 = sort2;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}
