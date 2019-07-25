package com.ar.developments.octanotes;

import java.io.Serializable;

/**
 * Created by Admin on 12/16/2016.
 */
public class Notes  implements Serializable {

    private int N_id;
    private int N_folder;
    private String  N_name;
    private String N_date;
    private String N_content;

    public Notes(int N_id,int N_folder,String N_name,String N_date,String N_content){
        this.N_id=N_id;
        this.N_folder=N_folder;
        this.N_name=N_name;
        this.N_date=N_date;
        this.N_content=N_content;

    }
    public Notes(int N_id,String N_name,String N_date,String N_content){
        this.N_id=N_id;
        this.N_folder=N_folder;
        this.N_name=N_name;
        this.N_date=N_date;
        this.N_content=N_content;

    }

    public int getN_id() {
        return N_id;
    }

    public void setN_id(int n_id) {
        N_id = n_id;
    }

    public int getN_folder() {
        return N_folder;
    }

    public void setN_folder(int n_folder) {
        N_folder = n_folder;
    }

    public String getN_name() {
        return N_name;
    }

    public void setN_name(String n_name) {
        N_name = n_name;
    }

    public String getN_date() {
        return N_date;
    }

    public void setN_date(String n_date) {
        N_date = n_date;
    }

    public String getN_content() {
        return N_content;
    }

    public void setN_content(String n_content) {
        N_content = n_content;
    }
}
