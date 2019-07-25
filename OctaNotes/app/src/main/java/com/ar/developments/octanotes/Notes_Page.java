package com.ar.developments.octanotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class Notes_Page extends Activity {

    private String folder_name;
    private int folder_id;
    private Typeface font1;
    private int long_hint=0;
    private int long_press=0;
    private TextView textView;
    private EditText editText;
    private ArrayList<Notes> Notes_list = new ArrayList<Notes>();
    private ArrayList<Notes> selected_list = new ArrayList<Notes>();
    private Create_Notes notes_Adapter;
    private Sql_db db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_page);
        db = new Sql_db(this);
        intial_fun();
        ontouch_fun();
        Notes_list=db.getNotes(folder_id);
        create_Adapter();
    }

    private void ontouch_fun() {
        font1 = Typeface.createFromAsset(getAssets(), "ShortStack-Regular.otf");
        TextView textView=(TextView)findViewById(R.id.ab_folder_name);
        textView.setTypeface(font1);
        textView.setText(folder_name);
    }

    private void intial_fun() {
        Intent intent = getIntent();
        folder_id = intent.getIntExtra("Folder_id", 1);
        folder_name = intent.getStringExtra("Folder_name");
        Log.d("fol0", folder_id + " " + folder_name);
    }

    private void create_Adapter() {
        LinearLayout empty=(LinearLayout)findViewById(R.id.empty_notes);
        GridView gridView=(GridView)findViewById(R.id.notes_grid);

        if(Notes_list.size()==0){
            empty.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        }
        else{
            empty.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            notes_Adapter = new Create_Notes(Notes_Page.this, Notes_list, selected_list);
            gridView.setAdapter(notes_Adapter);
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    LinearLayout body = (LinearLayout) view.findViewById(R.id.notes_body);
                    RelativeLayout top = (RelativeLayout) view.findViewById(R.id.notes_top_view);
                    LinearLayout bottom = (LinearLayout) view.findViewById(R.id.notes_bottom_view);
                    TextView textView=(TextView)view.findViewById(R.id.notes_txt_space);
                    ImageView tick=(ImageView)view.findViewById(R.id.notes_tick);
                    if (body.getTag() == null) {
                        long_hint = 1;
                        selected_list.add(Notes_list.get(position));
                        body.setTag("select");
                        bottom.setBackgroundResource(R.color.orange_light);
                        top.setBackgroundResource(R.color.grey4);
                        textView.setBackgroundResource(R.color.grey4);
                        tick.setVisibility(View.VISIBLE);
                        long_press = 1;
                        Log.d("add", "add");
                    } else if (body.getTag() == "select") {
                        long_hint = 1;
                        body.setTag(null);
                        bottom.setBackgroundResource(R.color.blue1);
                        top.setBackgroundResource(R.color.white);
                        textView.setBackgroundResource(R.color.white);
                        tick.setVisibility(View.GONE);
                        selected_list.remove(Notes_list.get(position));
                        Log.d("remove", "remove");
                    }
                    update_actionBar();
                    return false;
                }
            });
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (long_press == 1) {
                        LinearLayout body = (LinearLayout) view.findViewById(R.id.notes_body);
                        RelativeLayout top = (RelativeLayout) view.findViewById(R.id.notes_top_view);
                        LinearLayout bottom = (LinearLayout) view.findViewById(R.id.notes_bottom_view);
                        TextView textView=(TextView)view.findViewById(R.id.notes_txt_space);
                        ImageView tick=(ImageView)view.findViewById(R.id.notes_tick);

                        if (body.getTag() == null && long_hint == 0) {
                            body.setTag("select");
                            bottom.setBackgroundResource(R.color.orange_light);
                            top.setBackgroundResource(R.color.grey4);
                            textView.setBackgroundResource(R.color.grey4);
                            tick.setVisibility(View.VISIBLE);
                            selected_list.add(Notes_list.get(position));
                            long_press = 1;

                            Log.d("add", "add");
                        } else if (body.getTag() == "select" && long_hint == 0) {
                            body.setTag(null);
                            selected_list.remove(Notes_list.get(position));
                            bottom.setBackgroundResource(R.color.blue1);
                            top.setBackgroundResource(R.color.white);
                            textView.setBackgroundResource(R.color.white);
                            tick.setVisibility(View.GONE);
                            Log.d("remove", "remove");
                        }
                        long_hint = 0;
                        update_actionBar();
                    }
                    // list OnClick
                    else {
//                        Intent intent = new Intent(Home_Page.this, Notes_Page.class);
//                        Folder_details folder = (Folder_details) Folder_list.get(position);
//                        intent.putExtra("Folder_id", folder.getF_id());
//                        intent.putExtra("Folder_name", folder.getF_name());
//                        startActivityForResult(intent, 1111);
                    }


                }
            });

        }
    }

    private void update_actionBar() {
        LinearLayout add_layout = (LinearLayout) findViewById(R.id.ab_add2_div);
        LinearLayout edit_layout = (LinearLayout) findViewById(R.id.ab_edit2_div);
        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        ImageView edit1_img = (ImageView) findViewById(R.id.edit2_img);

        if (selected_list.size() == 0) {
            add_layout.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            edit_layout.setVisibility(View.GONE);
            edit1_img.setVisibility(View.GONE);
            long_press = 0;
        } else if (selected_list.size() == 1) {
            add_layout.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            edit_layout.setVisibility(View.VISIBLE);
            edit1_img.setVisibility(View.VISIBLE);
        } else {
            add_layout.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            edit_layout.setVisibility(View.VISIBLE);
            edit1_img.setVisibility(View.GONE);
        }
        final TextView select_txt=(TextView)findViewById(R.id.select_txt2);
        select_txt.setText(selected_list.size()+"  Selected");
    }

    public void add_notes(View view){
        Intent intent = new Intent(Notes_Page.this, Notes_Edit.class);
        intent.putExtra("Folder_id", folder_id);
        startActivityForResult(intent, 3333);
    }

    public void rename_folder_fun(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.alert_editbox, null);
        builder.setView(v);
        String previous_name = selected_list.get(0).getN_name().toString();
        final int N_id = selected_list.get(0).getN_id();
        textView = (TextView) v.findViewById(R.id.folder_name_txt);
        textView.setTypeface(font1);
        textView.setText("Rename :");

        editText = (EditText) v.findViewById(R.id.folder_name_edit);
        editText.setTypeface(font1);
        editText.setText(previous_name);

        final AlertDialog alert = builder.create();
        alert.show();

        Button button = (Button) v.findViewById(R.id.folder_create_btn);
        button.setText("Update");
        button.setTypeface(font1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                String name = editText.getText().toString();
                if (name == null) {
                    //Vibrate
                } else {
                    selected_list.clear();
                    long_press = 0;
                    db.rename_Notes_name(name,N_id);
                    alert.cancel();
                    Notes_list = db.getNotes(folder_id);
                    create_Adapter();
                    update_actionBar();
                }

            }
        });

    }

    public void delete_folder_fun(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.alert_textbox, null);
        builder.setView(v);
        textView = (TextView) v.findViewById(R.id.delete_txt);
        textView.setTypeface(font1);
        final AlertDialog alert = builder.create();
        Button cancel = (Button) v.findViewById(R.id.del_cancel);
        cancel.setTypeface(font1);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        Button ok = (Button) v.findViewById(R.id.del_ok);
        ok.setTypeface(font1);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete_Notes(selected_list);
                long_press = 0;
                for (int i = 0; i < selected_list.size(); i++) {
                    Notes_list.remove(selected_list.get(i));
                }
                selected_list.clear();
                update_actionBar();
                create_Adapter();

                alert.cancel();
            }
        });


        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 3333) {
            if(resultCode == Activity.RESULT_OK){
                Log.d("Result","ok");
                Notes_list=db.getNotes(folder_id);
                create_Adapter();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("Result","cancel");
            }
        }
    }
}
