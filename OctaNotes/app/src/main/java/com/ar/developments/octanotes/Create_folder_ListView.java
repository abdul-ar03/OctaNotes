package com.ar.developments.octanotes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 12/10/2016.
 */
public class Create_folder_ListView extends ArrayAdapter {
    private Context context;
    ArrayList <Folder_details> values=new ArrayList <Folder_details>();
    ArrayList <Folder_details> selected_list=new ArrayList <Folder_details>();
    public Create_folder_ListView(Context context, ArrayList<Folder_details> values, ArrayList<Folder_details> selected_list) {
        super(context, 0, values);
        this.context=context;
        this.values=values;
        this.selected_list=selected_list;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = convertView;
        if (v == null) {
            v = Inflater.inflate(R.layout.layout_folder_list, null);
        } else {
            v = convertView;
        }
        Typeface font = Typeface.createFromAsset(context.getAssets(), "ShortStack-Regular.otf");
        Folder_details folder=values.get(position);
        TextView textView=(TextView)v.findViewById(R.id.folder_name_space);
        textView.setText(folder.getF_name());
        textView.setTypeface(font);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.list_body);
        if(selected_list.contains(folder)){
            layout.setBackgroundResource(R.color.grey2);
            layout.setTag("select");
        }
        else{
            layout.setBackgroundColor(Color.TRANSPARENT);        }
        return v;
    }


}
