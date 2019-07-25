package com.ar.developments.octanotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Home_Page extends Activity {

    private Typeface font1;
    private TextView textView;
    private EditText editText;
    private Sql_db db;
    private int View_no=0;
    private int Sort_no=0;
    private int long_press = 0;
    private int long_hint = 0;
    private Create_folder_ListView list_adapter;
    private Create_folder_GridView grid_adapter;

    ArrayList<Folder_details> Folder_list = new ArrayList<Folder_details>();
    ArrayList<Folder_details> selected_list = new ArrayList<Folder_details>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        font1 = Typeface.createFromAsset(getAssets(), "ShortStack-Regular.otf");
        db = new Sql_db(this);
        intial_fun();
        ontouch_fun();
        create_Adapter(View_no);
    }

    public void intial_fun() {
        App_details details = new App_details(0, 0, 0);
        details = db.getDetails();
        View_no = details.getView();
        Sort_no=details.getSort1();
        Folder_list = db.getFolder(Sort_no);
        Log.d("mainnnnn", View_no + "");

        File Octa_notes = new File(Environment.getExternalStorageDirectory(), "Octa Notes");
        File images = new File(Environment.getExternalStorageDirectory()+"/Octa Notes", "Images");
        File thumnails = new File(Environment.getExternalStorageDirectory()+"/Octa Notes/Images", "Thumbnails");
        File pdf = new File(Environment.getExternalStorageDirectory()+"/Octa Notes", "PDF");
        File backup = new File(Environment.getExternalStorageDirectory()+"/Octa Notes", "Backup");

        if (!Octa_notes.exists()) {
            if (!Octa_notes.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
        if (!images.exists()) {
            if (!images.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
        if (!thumnails.exists()) {
            if (!thumnails.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
        if (!pdf.exists()) {
            if (!pdf.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
        if (!backup.exists()) {
            if (!backup.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
    }

    public void create_Adapter(int View_no) {
        Log.d("inside", "ib" + Folder_list.size());
        final LinearLayout empty = (LinearLayout) findViewById(R.id.folder_emptyview);
        final ListView listView = (ListView) findViewById(R.id.folder_listview);
        final GridView gridView = (GridView) findViewById(R.id.folder_gridview);
        if (Folder_list.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
        }
        else if (View_no == 1) {
            empty.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            list_adapter = new Create_folder_ListView(Home_Page.this, Folder_list, selected_list);
            listView.setAdapter(list_adapter);
            final TextView select_txt=(TextView)findViewById(R.id.select_txt1);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    LinearLayout layout = (LinearLayout) view.findViewById(R.id.list_body);
                    if (layout.getTag() == null) {
                        layout.setTag("select");
                        long_hint = 1;
                        layout.setBackgroundResource(R.drawable.border_folder_select);
                        selected_list.add(Folder_list.get(position));
                        long_press = 1;
                        Log.d("add", "add");
                    } else if (layout.getTag() == "select") {
                        long_hint = 1;
                        layout.setTag(null);
                        selected_list.remove(Folder_list.get(position));
                        layout.setBackgroundColor(Color.TRANSPARENT);
                        Log.d("remove", "remove");
                    }
                    update_actionBar();
                    return false;
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (long_press == 1) {
                        LinearLayout layout = (LinearLayout) view.findViewById(R.id.list_body);
                        if (layout.getTag() == null && long_hint == 0) {
                            layout.setTag("select");
                            layout.setBackgroundResource(R.drawable.border_folder_select);
                            selected_list.add(Folder_list.get(position));
                            long_press = 1;

                            Log.d("add", "add");
                        } else if (layout.getTag() == "select" && long_hint == 0) {
                            layout.setTag(null);
                            selected_list.remove(Folder_list.get(position));
                            layout.setBackgroundColor(Color.TRANSPARENT);
                            Log.d("remove", "remove");
                        }
                        long_hint = 0;
                        update_actionBar();
                    }
                    // list OnClick
                    else{
                        Intent intent = new Intent(Home_Page.this, Notes_Page.class);
                        Folder_details folder=(Folder_details)Folder_list.get(position);
                        intent.putExtra("Folder_id",folder.getF_id());
                        intent.putExtra("Folder_name",folder.getF_name());
                        startActivityForResult(intent, 1111);
                    }




                }
            });
        }
        else if (View_no == 2) {
            empty.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            grid_adapter = new Create_folder_GridView(Home_Page.this, Folder_list, selected_list);
            gridView.setAdapter(grid_adapter);
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageView layout = (ImageView) view.findViewById(R.id.grid_body);
                    if (layout.getTag() == null) {
                        layout.setTag("select");
                        long_hint = 1;
                        layout.setBackgroundResource(R.drawable.border_folder_select);
                        selected_list.add(Folder_list.get(position));
                        long_press = 1;
                        Log.d("add", "add");
                    } else if (layout.getTag() == "select") {
                        long_hint = 1;
                        layout.setTag(null);
                        selected_list.remove(Folder_list.get(position));
                        layout.setBackgroundResource(R.color.grey1);
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
                        ImageView layout = (ImageView) view.findViewById(R.id.grid_body);
                        if (layout.getTag() == null && long_hint == 0) {
                            layout.setTag("select");
                            layout.setBackgroundResource(R.drawable.border_folder_select);
                            selected_list.add(Folder_list.get(position));
                            long_press = 1;

                            Log.d("add", "add");
                        } else if (layout.getTag() == "select" && long_hint == 0) {
                            layout.setTag(null);
                            selected_list.remove(Folder_list.get(position));
                            layout.setBackgroundResource(R.color.grey1);
                            Log.d("remove", "remove");
                        }
                        long_hint = 0;
                    }
                    update_actionBar();


                }
            });
        }

    }

    private void update_actionBar() {
        LinearLayout add_layout = (LinearLayout) findViewById(R.id.ab_add1_div);
        LinearLayout edit_layout = (LinearLayout) findViewById(R.id.ab_edit1_div);
        ImageView edit1_img = (ImageView) findViewById(R.id.edit1_img);

        if (selected_list.size() == 0) {
            add_layout.setVisibility(View.VISIBLE);
            edit_layout.setVisibility(View.GONE);
            edit1_img.setVisibility(View.GONE);
            long_press = 0;
        } else if (selected_list.size() == 1) {
            add_layout.setVisibility(View.GONE);
            edit_layout.setVisibility(View.VISIBLE);
            edit1_img.setVisibility(View.VISIBLE);
        } else {
            add_layout.setVisibility(View.GONE);
            edit_layout.setVisibility(View.VISIBLE);
            edit1_img.setVisibility(View.GONE);
        }
        final TextView select_txt=(TextView)findViewById(R.id.select_txt1);
        select_txt.setText(selected_list.size()+"  Selected");
    }

    public void rename_folder_fun(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.alert_editbox, null);
        builder.setView(v);
        String previous_name = selected_list.get(0).getF_name().toString();
        final int F_id = selected_list.get(0).getF_id();
        textView = (TextView) v.findViewById(R.id.folder_name_txt);
        textView.setTypeface(font1);
        textView.setText("Rename :");

        editText = (EditText) v.findViewById(R.id.folder_name_edit);
        editText.setTypeface(font1);
        editText.setHint(previous_name);

        final AlertDialog alert = builder.create();
        alert.show();

        Button button = (Button) v.findViewById(R.id.folder_create_btn);
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
                    db.rename_Folder_name(name, F_id);
                    alert.cancel();
                    Folder_list = db.getFolder(Sort_no);
                    create_Adapter(View_no);
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
                db.delete_folder(selected_list);
                long_press = 0;
                for (int i = 0; i < selected_list.size(); i++) {
                    Folder_list.remove(selected_list.get(i));
                }
                selected_list.clear();
                update_actionBar();
                create_Adapter(1);

                alert.cancel();
            }
        });


        alert.show();
    }

    public void add_folder_fun(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.alert_editbox, null);
        builder.setView(v);
        textView = (TextView) v.findViewById(R.id.folder_name_txt);
        textView.setTypeface(font1);
        editText = (EditText) v.findViewById(R.id.folder_name_edit);
        editText.setTypeface(font1);
        final AlertDialog alert = builder.create();
        alert.show();

        Button button = (Button) v.findViewById(R.id.folder_create_btn);
        button.setTypeface(font1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                String name = editText.getText().toString();
                if (name == null) {
                    //Vibrate
                } else {
                    db.add_folder(name);
                    Folder_list = db.getFolder(Sort_no);
                    alert.cancel();
                    create_Adapter(View_no);
                }

            }
        });

    }

    public void ontouch_fun() {
        textView = (TextView) findViewById(R.id.add_folder_txt);
        textView.setTypeface(font1);
        textView = (TextView) findViewById(R.id.app_name1);
        textView.setTypeface(font1);
        textView = (TextView) findViewById(R.id.sort_txt1);
        textView.setTypeface(font1);
        textView = (TextView) findViewById(R.id.asc_txt1);
        textView.setTypeface(font1);
        textView = (TextView) findViewById(R.id.desc_txt_1);
        textView.setTypeface(font1);
        textView = (TextView) findViewById(R.id.date_txt1);
        textView.setTypeface(font1);
        textView = (TextView) findViewById(R.id.view_txt1);
        textView.setTypeface(font1);
        textView = (TextView) findViewById(R.id.list_txt1);
        textView.setTypeface(font1);
        textView = (TextView) findViewById(R.id.grid_txt1);
        textView.setTypeface(font1);
        textView = (TextView) findViewById(R.id.receive_txt1);
        textView.setTypeface(font1);
        textView = (TextView) findViewById(R.id.abt_us_txt1);
        textView.setTypeface(font1);
        textView = (TextView) findViewById(R.id.select_txt1);
        textView.setTypeface(font1);


        DrawerLayout mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int i) {
                LinearLayout asc1_layout = (LinearLayout) findViewById(R.id.asc1_lay);
                LinearLayout des1_layout = (LinearLayout) findViewById(R.id.desc1_lay);
                LinearLayout dat1_layout = (LinearLayout) findViewById(R.id.dat1_lay);
                asc1_layout.setBackgroundColor(Color.TRANSPARENT);
                des1_layout.setBackgroundColor(Color.TRANSPARENT);
                dat1_layout.setBackgroundColor(Color.TRANSPARENT);

                LinearLayout listview = (LinearLayout) findViewById(R.id.list1_lay);
                LinearLayout gridview = (LinearLayout) findViewById(R.id.grid1_lay);
                listview.setBackgroundColor(Color.TRANSPARENT);
                gridview.setBackgroundColor(Color.TRANSPARENT);

                LinearLayout receiver = (LinearLayout) findViewById(R.id.receive1_lay);
                LinearLayout about_us = (LinearLayout) findViewById(R.id.about1_lay);
                receiver.setBackgroundColor(Color.TRANSPARENT);
                about_us.setBackgroundColor(Color.TRANSPARENT);
            }
        });






        LinearLayout asc1_layout = (LinearLayout) findViewById(R.id.asc1_lay);
        asc1_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundResource(R.drawable.border_drawer_select_div);
                        Log.d("down", "down");
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        Log.d("up", "up");
                        break;

                }
                return false;
            }
        });


        LinearLayout desc1_layout = (LinearLayout) findViewById(R.id.desc1_lay);
        desc1_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundResource(R.drawable.border_drawer_select_div);
                        Log.d("down", "down");
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        Log.d("up", "up");
                        break;

                }
                return false;
            }
        });

        LinearLayout date1_layout = (LinearLayout) findViewById(R.id.dat1_lay);
        date1_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundResource(R.drawable.border_drawer_select_div);
                        Log.d("down", "down");
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        Log.d("up", "up");
                        break;

                }
                return false;
            }
        });


        //    List &&&& Grid View

        LinearLayout listview = (LinearLayout) findViewById(R.id.list1_lay);
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundResource(R.drawable.border_drawer_select_div);
                        Log.d("down", "down");
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        Log.d("up", "up");
                        break;

                }
                return false;
            }
        });

        LinearLayout gridview = (LinearLayout) findViewById(R.id.grid1_lay);
        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundResource(R.drawable.border_drawer_select_div);
                        Log.d("down", "down");
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        Log.d("up", "up");
                        break;

                }
                return false;
            }
        });


//  ******  Receiver &&&&  About us  *****

        LinearLayout receive = (LinearLayout) findViewById(R.id.receive1_lay);
        receive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundResource(R.drawable.border_drawer_select_div);
                        Log.d("down", "down");
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        Log.d("up", "up");
                        break;

                }
                return true;
            }
        });

        LinearLayout about_us = (LinearLayout) findViewById(R.id.about1_lay);
        about_us.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundResource(R.drawable.border_drawer_select_div);
                        Log.d("down", "down");
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        Log.d("up", "up");
                        break;

                }
                return true;
            }
        });

        // Add img
        final ImageView add_button=(ImageView)findViewById(R.id.add1_img);
        add_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
//                        add_button.setBackgroundColor(R.color.orange);
                        add_button.setBackgroundColor(getResources().getColor(R.color.orange));
                        Log.d("down", "down");
                        break;
                    case MotionEvent.ACTION_UP:
                        add_button.setBackgroundColor(Color.TRANSPARENT);
                        Log.d("up", "up");
                        break;

                }
                return false;
            }
        });

    }

    public void sort_fun(View view){
        switch (view.getId()) {
            case R.id.asc1_lay:
                Sort_no = 1;
                break;
            case R.id.desc1_lay:
                Sort_no = 2;
                break;
            case R.id.dat1_lay:
                Sort_no = 3;
                break;

        }
        db.folder_sortNo(Sort_no);
        Folder_list.clear();
        Folder_list=db.getFolder(Sort_no);
        create_Adapter(View_no);
        DrawerLayout mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(Gravity.START);
    }

    public void view_fun(View view){
        switch (view.getId()) {
            case R.id.list1_lay:
                View_no = 1;
                break;
            case R.id.grid1_lay:
                View_no = 2;
                break;
        }
        db.folder_viewNo(View_no);
        selected_list.clear();
        update_actionBar();
        create_Adapter(View_no);
        DrawerLayout mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(Gravity.START);

    }

    public void cancel_selected(View view){
        selected_list.clear();
        update_actionBar();
        create_Adapter(View_no);
    }

    @Override
    public void onBackPressed() {
        if(long_press==1){
            long_press=0;
            selected_list.clear();
            update_actionBar();
            create_Adapter(View_no);
        }
        else{
            finish();
        }
    }
}
