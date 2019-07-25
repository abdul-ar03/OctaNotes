package com.ar.developments.octanotes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 12/16/2016.
 */
public class Create_Notes extends ArrayAdapter {
    private Context context;
    ArrayList <Notes> values=new ArrayList <Notes>();
    ArrayList <Notes> selected_list=new ArrayList <Notes>();

    public Create_Notes(Context context, ArrayList<Notes> values, ArrayList<Notes> selected_list) {
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
            v = Inflater.inflate(R.layout.layout_notes, null);
        } else {
            v = convertView;
        }
        Typeface font = Typeface.createFromAsset(context.getAssets(), "ShortStack-Regular.otf");
        Typeface font1 = Typeface.createFromAsset(context.getAssets(), "ShortStack-Regular.otf");
        Notes notes=values.get(position);

        TextView name=(TextView)v.findViewById(R.id.note_name_space);
        name.setText(notes.getN_name());
        name.setTypeface(font);

        TextView date=(TextView)v.findViewById(R.id.note_date_space);
        date.setText(notes.getN_date());
        date.setTypeface(font);

        TextView content=(TextView)v.findViewById(R.id.notes_txt_space);
        content.setText(notes.getN_content());
        content.setTypeface(font1);

        LinearLayout body = (LinearLayout) v.findViewById(R.id.notes_body);
        RelativeLayout top = (RelativeLayout) v.findViewById(R.id.notes_top_view);
        LinearLayout bottom = (LinearLayout) v.findViewById(R.id.notes_bottom_view);
        ImageView tick=(ImageView)v.findViewById(R.id.notes_tick);
        if(selected_list.contains(notes)){
            body.setTag("select");
            bottom.setBackgroundResource(R.color.orange_light);
            top.setBackgroundResource(R.color.grey4);
            content.setBackgroundResource(R.color.grey4);
            tick.setVisibility(View.VISIBLE);
        }
        else{
            body.setTag(null);
            bottom.setBackgroundResource(R.color.blue1);
            top.setBackgroundResource(R.color.white);
            content.setBackgroundResource(R.color.white);
            tick.setVisibility(View.GONE);
        }


        return v;
    }


}
