package com.ar.developments.octanotes;

import java.io.Serializable;

/**
 * Created by Admin on 12/10/2016.
 */
public class Folder_details  implements Serializable {

    private int F_id;
    private String F_name;

    public Folder_details(int F_id, String F_name) {
        this.F_id=F_id;
        this.F_name=F_name;
    }

    public int getF_id() {
        return F_id;
    }

    public void setF_id(int f_id) {
        F_id = f_id;
    }

    public String getF_name() {
        return F_name;
    }

    public void setF_name(String f_name) {
        F_name = f_name;
    }
}
