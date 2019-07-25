package com.ar.developments.octanotes;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;



public class Sql_db extends SQLiteOpenHelper {

    private App_details details;
    private Object folder;

    public Sql_db(Context context) {
        super(context, "Octa_Notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String Details = "Create table Details (D_id INTEGER DEFAULT 1 ,Sort1 INTEGER DEFAULT 1,Sort2 INTEGER DEFAULT 1,View INTEGER DEFAULT 1)";
        db.execSQL(Details);

        String Folder = "Create table Folder (F_id INTEGER PRIMARY KEY AUTOINCREMENT ,F_name TEXT)";
        db.execSQL(Folder);

        String Notes = "Create table Notes (N_id INTEGER PRIMARY KEY AUTOINCREMENT ,N_folder INTEGER ,N_name TEXT, N_date TEXT, N_content TEXT)";
        db.execSQL(Notes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Details");
        db.execSQL("DROP TABLE IF EXISTS Folder");
        db.execSQL("DROP TABLE IF EXISTS Notes");
        onCreate(db);
    }

    public App_details getDetails() {
        App_details details=new App_details(0,0,0);
        String  selectQuery = "select * from Details";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("anssss", "up");

        if (cursor.moveToFirst()) {
            Log.d("anssss","if");
            do {
                details.setSort1(cursor.getInt(1));
                details.setSort2(cursor.getInt(2));
                details.setView(cursor.getInt(3));
                Log.d("anssss"," "+cursor.getInt(1)+cursor.getInt(2)+cursor.getInt(3)+"");
            }
            while (cursor.moveToNext());
        }
        else {
            Log.d("anssss","else");
            String sql = "insert into Details (D_id,Sort1,Sort1,View ) values('1','3','1','1')";
            details.setSort1(1);
            details.setSort2(1);
            details.setView(1);
            db.execSQL(sql);
        }
        return details;
    }

    public void add_folder(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "insert into Folder (F_name) values('"+name+"')";
        db.execSQL(sql);
    }

    public ArrayList getFolder(int no) {
        ArrayList<Folder_details> Folder_list = new ArrayList<Folder_details>();
        String selectQuery = null;
        if(no==1){
            selectQuery = "select * from Folder order by F_name ASC ";
        }
        else if(no==2){
            selectQuery = "select * from Folder order by F_name DESC ";
        }
        else{
            selectQuery = "select * from Folder ";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("anssss", "up");

        if (cursor.moveToFirst()) {
            Log.d("anssss","if");
            do {
                Folder_details folder=new Folder_details(0,"");
                folder.setF_id(cursor.getInt(0));
                folder.setF_name(cursor.getString(1));
                Folder_list.add(folder);
                Log.d("anssss"," "+cursor.getString(1)+"");
            }
            while (cursor.moveToNext());
        }


        return Folder_list;
    }

    public void rename_Folder_name(String name, int f_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE Folder set F_name ='"+name+"' where F_id="+f_id;
        db.execSQL(sql);
    }

    public void delete_folder(ArrayList<Folder_details> selected_list) {
        SQLiteDatabase db = this.getWritableDatabase();
        for(int i=0;i<selected_list.size();i++){
            Folder_details folder=(Folder_details)selected_list.get(i);
            String sql = "DELETE from Folder where F_id="+folder.getF_id();
            db.execSQL(sql);
        }
    }

    public void folder_sortNo(int no) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE Details set Sort1 ="+no+" where D_id=1";
        db.execSQL(sql);
    }

    public void folder_viewNo(int view_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE Details set View ="+view_no+" where D_id=1";
        db.execSQL(sql);
    }



    // Notes :
    public void add_Notes(Notes notes){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "insert into Notes (N_folder,N_name,N_date,N_content) values("+notes.getN_folder()+",'"+notes.getN_name()+"','"+notes.getN_date()+"','"+notes.getN_content()+"')";
        db.execSQL(sql);
    }

    public ArrayList<Notes> getNotes(int folder_id){
        ArrayList<Notes> Notes_list = new ArrayList<Notes>();
        String selectQuery = null;
        selectQuery = "select * from Notes where N_folder="+folder_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("anssss", "up");

        if (cursor.moveToFirst()) {
            Log.d("anssss","if");
            do {
                Notes notes=new Notes(0,null,null,null);
                notes.setN_id(cursor.getInt(0));
                notes.setN_name(cursor.getString(2));
                notes.setN_date(cursor.getString(3));
                notes.setN_content(cursor.getString(4));

                Notes_list.add(notes);
//                Log.d("anssss"," "+cursor.getString(1)+"");
            }
            while (cursor.moveToNext());
        }


        return Notes_list;
    }

    public void rename_Notes_name(String name, int n_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE Notes set N_name ='"+name+"' where N_id="+n_id;
        db.execSQL(sql);
    }

    public void delete_Notes(ArrayList<Notes> selected_list) {
        SQLiteDatabase db = this.getWritableDatabase();
        for(int i=0;i<selected_list.size();i++){
            Notes folder=(Notes)selected_list.get(i);
            String sql = "DELETE from Notes where N_id="+folder.getN_id();
            db.execSQL(sql);
        }
    }
}