package com.ar.developments.octanotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by Admin on 12/16/2016.
 */
public class Notes_Edit extends Activity {

    private String folder_name;
    private int folder_id;
    private Typeface font1;
    private Typeface font2;
    private Sql_db db;
    private  Bitmap thumbnail;
    private String N_name="Untitled Note";
    private String N_date;
    private String N_content;
    private int camera_request=9999;
    private int gallery_request=8888;
    private int crop_request=7777;
    private TextView textView;
    private EditText Current_Edittext;
    private EditText editText;
    private boolean isPanelShown;
    ArrayList<String> Image_src = new ArrayList<String>();
    ArrayList<String> Selected_src = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_edit);
        db = new Sql_db(this);
        initial_fun();
        ontouch_fun();


        Intent intent = getIntent();
        folder_id = intent.getIntExtra("Folder_id", 1);
        isPanelShown=false;




    }

    public void initial_fun(){
        font1 = Typeface.createFromAsset(getAssets(), "ShortStack-Regular.otf");
        font2 = Typeface.createFromAsset(getAssets(), "ShortStack-Regular.otf");

        TextView notes_name = (TextView)findViewById(R.id.note_name1);
        notes_name.setTypeface(font1);
        SpannableString content = new SpannableString("Untitled Note");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        notes_name.setText(content);

        // Tools
        textView= (TextView)findViewById(R.id.tool1_txt1);
        textView.setTypeface(font2);
        textView= (TextView)findViewById(R.id.tool1_txt2);
        textView.setTypeface(font2);
        textView= (TextView)findViewById(R.id.tool1_txt3);
        textView.setTypeface(font2);


        textView= (TextView)findViewById(R.id.initial_edit);
        textView.setTypeface(font2);
        textView= (TextView)findViewById(R.id.txt_smily);
        textView.setTypeface(font2);




        LinearLayout tool1=(LinearLayout)findViewById(R.id.tool1_body_div);
        tool1.setTag("close");
        LinearLayout tool2=(LinearLayout)findViewById(R.id.tool2_body_div);
        tool2.setTag("close");
    }

    public void back_fun(View view){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void rename(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.alert_editbox, null);
        builder.setView(v);
        final TextView notes_name = (TextView)findViewById(R.id.note_name1);
        String previous_name = notes_name.getText().toString();
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
                    SpannableString content = new SpannableString(name);
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    notes_name.setText(content);
                    alert.hide();
                }

            }
        });
    }

    public void save(View view){
        TextView name=(TextView)findViewById(R.id.note_name1);
        N_name=name.getText().toString();

        EditText editText=(EditText)findViewById(R.id.initial_edit);
        N_content=editText.getText().toString();

//        Editable editable=(Editable)findViewById(R.id.initial_edit);
//        N_content=editable.toString();

        N_date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Notes notes=new Notes(1,folder_id,N_name,N_date,N_content);
        db.add_Notes(notes);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
     }

    public  void tool1_click_fun(View view){
        TextView title1=(TextView)findViewById(R.id.tool1_txt1);
        TextView title2=(TextView)findViewById(R.id.tool1_txt2);
        TextView title3=(TextView)findViewById(R.id.tool1_txt3);
        title1.setTextColor(R.color.grey5);
        title2.setTextColor(R.color.grey5);
        title3.setTextColor(R.color.grey5);

        TextView bottom1=(TextView)findViewById(R.id.tool1_bottom1);
        TextView bottom2=(TextView)findViewById(R.id.tool1_bottom2);
        TextView bottom3=(TextView)findViewById(R.id.tool1_bottom3);
        bottom1.setBackgroundColor(Color.TRANSPARENT);
        bottom2.setBackgroundColor(Color.TRANSPARENT);
        bottom3.setBackgroundColor(Color.TRANSPARENT);


        LinearLayout layout1=(LinearLayout)findViewById(R.id.signs_collection);
        LinearLayout layout2=(LinearLayout)findViewById(R.id.symbols_collection);
        LinearLayout layout3=(LinearLayout)findViewById(R.id.others_collection);
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);

        switch (view.getId()){
            case R.id.tool1_txt1:
                title1.setTextColor(R.color.grey6);
                bottom1.setBackgroundColor(R.color.blue2);
                layout1.setVisibility(View.VISIBLE);
                break;
            case R.id.tool1_txt2:
                title2.setTextColor(R.color.grey6);
                bottom2.setBackgroundColor(R.color.blue2);
                layout2.setVisibility(View.VISIBLE);
                break;
            case R.id.tool1_txt3:
                title3.setTextColor(R.color.grey6);
                bottom3.setBackgroundColor(R.color.blue2);
                layout3.setVisibility(View.VISIBLE);
                break;

        }

    }

    public void ontouch_fun(){

        final ImageView tool1=(ImageView)findViewById(R.id.edt_tool1);
        tool1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }
                return false;
            }
        });
    }



    public void tool1_fun(View view){
        LinearLayout tool1=(LinearLayout)findViewById(R.id.tool1_body_div);
        LinearLayout tool2=(LinearLayout)findViewById(R.id.tool2_body_div);
        tool2.setTag("close");
        tool2.setVisibility(View.GONE);
        if(tool1.getTag()=="close")
        {
            tool1.setTag("open");
            tool1.setVisibility(View.VISIBLE);
        }
        else {
            tool1.setTag("close");
            tool1.setVisibility(View.GONE);
        }
    }

    public void symbole_fun(View view){
        String symbole = "";
        Current_Edittext=(EditText)findViewById(R.id.initial_edit);
        switch (view.getId()){
            case R.id.sym1_1:
                symbole="+";
                break;
            case R.id.sym1_2:
                symbole="⁻";
                break;
            case R.id.sym1_3:
                symbole="×";
                break;
            case R.id.sym1_4:
                symbole="÷";
                break;
            case R.id.sym1_5:
                symbole="%";
                break;
            case R.id.sym1_6:
                symbole="√";
                break;
            case R.id.sym1_7:
                symbole="∫";
                break;
            case R.id.sym1_8:
                symbole="<";
                break;
            case R.id.sym1_9:
                symbole=">";
                break;
            case R.id.sym1_10:
                symbole="=";
                break;
            case R.id.sym1_11:
                symbole="≠";
                break;
            case R.id.sym1_12:
                symbole="≈";
                break;
            case R.id.sym1_13:
                symbole="≤";
                break;
            case R.id.sym1_14:
                symbole="≥";
                break;
        }
        int start =Current_Edittext.getSelectionStart();
        Current_Edittext.getText().insert(start, symbole);
    }

    public void symbole_fun2(View view){
        String symbole = "";
        Current_Edittext=(EditText)findViewById(R.id.initial_edit);
        switch (view.getId()){
            case R.id.sym2_1:
                symbole="α";
                break;
            case R.id.sym2_2:
                symbole="β";
                break;
            case R.id.sym2_3:
                symbole="γ";
                break;
            case R.id.sym2_4:
                symbole="δ";
                break;
            case R.id.sym2_5:
                symbole="ε";
                break;
            case R.id.sym2_6:
                symbole="η";
                break;
            case R.id.sym2_7:
                symbole="μ";
                break;
            case R.id.sym2_8:
                symbole="φ";
                break;
            case R.id.sym2_9:
                symbole="σ";
                break;
            case R.id.sym2_10:
                symbole="τ";
                break;
            case R.id.sym2_11:
                symbole="ω";
                break;
            case R.id.sym2_12:
                symbole="ξ";
                break;
            case R.id.sym2_13:
                symbole="ψ";
                break;
            case R.id.sym2_14:
                symbole="ζ";
                break;
        }
        int start =Current_Edittext.getSelectionStart();
        Current_Edittext.getText().insert(start, symbole);
    }

    public void symbole_fun3(View view){
        String symbole = "";
        Current_Edittext=(EditText)findViewById(R.id.initial_edit);
        switch (view.getId()){
            case R.id.sym3_1:
                symbole="र";
                break;
            case R.id.sym3_2:
                symbole="$";
                break;
            case R.id.sym3_3:
                symbole="£";
                break;
            case R.id.sym3_4:
                symbole="♂";
                break;
            case R.id.sym3_5:
                symbole="♀";
                break;
            case R.id.sym3_6:
                symbole="♥";
                break;
            case R.id.sym3_7:
                symbole="★";
                break;
            case R.id.sym3_8:
                symbole="←";
                break;
            case R.id.sym3_9:
                symbole="↑";
                break;
            case R.id.sym3_10:
                symbole="↓";
                break;
            case R.id.sym3_11:
                symbole="→";
                break;
            case R.id.sym3_12:
                symbole="↔";
                break;
            case R.id.sym3_13:
                symbole="↕";
                break;
            case R.id.sym3_14:
                symbole="↻";
                break;
        }
        int start =Current_Edittext.getSelectionStart();
        Current_Edittext.getText().insert(start, symbole);
    }



    public void tool2_fun(View view){
        LinearLayout tool2=(LinearLayout)findViewById(R.id.tool2_body_div);
        LinearLayout tool1=(LinearLayout)findViewById(R.id.tool1_body_div);
        tool1.setTag("close");
        tool1.setVisibility(View.GONE);
        if(tool2.getTag()=="close")
        {
            tool2.setTag("open");
            tool2.setVisibility(View.VISIBLE);
        }
        else {
            tool2.setTag("close");
            tool2.setVisibility(View.GONE);
        }
    }

    public void smiley_fun(View view){
        String symbole = "";
        Current_Edittext=(EditText)findViewById(R.id.initial_edit);
        switch(view.getId()) {
            case R.id.smly1:
                symbole = "\uD83D\uDE00";
                break;
            case R.id.smly2:
                symbole = "\uD83D\uDE11";
                break;
            case R.id.smly3:
                symbole = "\uD83D\uDE27";
                break;
            case R.id.smly4:
                symbole = "\uD83D\uDE0D";
                break;
            case R.id.smly5:
                symbole = "\uD83D\uDE0A";
                break;
            case R.id.smly6:
                symbole = "\uD83D\uDE37";
                break;
            case R.id.smly7:
                symbole = "\uD83D\uDE35";
                break;
            case R.id.smly8:
                symbole ="\uD83D\uDE0C";
                break;
            case R.id.smly9:
                symbole = "\uD83D\uDE1F";
                break;
            case R.id.smly10:
                symbole = "\uD83D\uDE1B";
                break;
            case R.id.smly11:
                symbole = "\uD83D\uDE15";
                break;
            case R.id.smly12:
                symbole = "\uD83D\uDE26";
                break;
            case R.id.smly13:
                symbole = "\uD83D\uDE2E";
                break;
            case R.id.smly14:
                symbole = "\uD83D\uDE20";
                break;
            case R.id.smly15:
                symbole = "\uD83D\uDE15";
                break;
            case R.id.smly16:
                symbole = "\uD83D\uDE1C";
                break;
            case R.id.smly17:
                symbole = "\uD83D\uDE23";
                break;
            case R.id.smly18:
                symbole = "\uD83D\uDE0E";
                break;
            case R.id.smly19:
                symbole = "\uD83D\uDE22";
                break;
            case R.id.smly20:
                symbole = "\uD83D\uDE03";
                break;
            case R.id.smly21:
                symbole = "\uD83D\uDE09";
                break;

        }

        int start =Current_Edittext.getSelectionStart();
        Current_Edittext.getText().insert(start, symbole);
    }


    public void img_slideUpDown(View view){
        ViewGroup hiddenPanel = (ViewGroup)findViewById(R.id.Image_Collection);
        if(!isPanelShown) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);

            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
            isPanelShown = true;
        }
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.INVISIBLE);
            isPanelShown = false;
        }
    }

    public void tool4_fun(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.alert_image_add, null);
        textView= (TextView)v.findViewById(R.id.img_add_txt);
        textView.setTypeface(font2);
        textView= (TextView)v.findViewById(R.id.gallery_txt);
        textView.setTypeface(font2);
        textView= (TextView)v.findViewById(R.id.camera_txt);
        textView.setTypeface(font2);
        builder.setView(v);
        ImageView gallery =(ImageView)v.findViewById(R.id.gallery_img);
        ImageView camera =(ImageView)v.findViewById(R.id.camera_img);
        final AlertDialog alert = builder.create();
        alert.show();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, gallery_request);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, camera_request);

            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==camera_request && resultCode == RESULT_OK) {

            Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Bundle extras = data.getExtras();
            Bitmap thumbnail1 = (BitmapFactory.decodeFile(picturePath));
            Bitmap thumbnail2 = (Bitmap) data.getExtras().get("data");
            saveImage(thumbnail1, thumbnail2, extras);

        }
    }

    private void saveImage(Bitmap finalBitmap,Bitmap thumbnail,Bundle extras) {
        File myDir_img = new File(Environment.getExternalStorageDirectory()+"/Octa Notes", "Images");
        if (!myDir_img.exists()) {myDir_img.mkdirs();}

        File myDir_thumnails = new File(Environment.getExternalStorageDirectory()+"/Octa Notes/Images", "Thumbnails");
        if (!myDir_thumnails.exists()) {myDir_thumnails.mkdirs();}

        File file = new File(Environment.getExternalStorageDirectory()+"/Octa Notes/Images",System.currentTimeMillis() + ".jpg");
        File thumnail = new File(Environment.getExternalStorageDirectory()+"/Octa Notes/Images/Thumbnails",System.currentTimeMillis() + ".jpg");

        Image_src.add(file.toString());
        if (file.exists()) file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//            thumnail.createNewFile();
//            FileOutputStream fo = new FileOutputStream(thumnail);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }





        create_Img_Adapter();
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
//        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
//                new MediaScannerConnection.OnScanCompletedListener() {
//                    public void onScanCompleted(String path, Uri uri) {
//                        Log.i("ExternalStorage", "Scanned " + path + ":");
//                        Log.i("ExternalStorage", "-> uri=" + uri);
//                    }
//                });

//        String Image_path = Environment.getExternalStorageDirectory()+ "/Pictures/folder_name/"+iname;
//
//        File[] files = myDir.listFiles();
//        int numberOfImages=files.length;
//        System.out.println("Total images in Folder "+numberOfImages);
    }

    public void create_Img_Adapter(){
        GridView grid=(GridView)findViewById(R.id.img_coll_grid);
        Create_image_GridView img_adapter = new Create_image_GridView(Notes_Edit.this, Image_src, Selected_src);
        grid.setAdapter(img_adapter);
    }



    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

}
