package com.ar.developments.octanotes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class Create_image_GridView extends ArrayAdapter {
    private Context context;
    ArrayList<String> src_list=new ArrayList <String>();
    ArrayList <String> selected_list=new ArrayList <String>();

    public Create_image_GridView (Context context, ArrayList<String> values, ArrayList<String> selected_list) {
        super(context, 0, values);
        this.context=context;
        this.src_list=values;
        this.selected_list=selected_list;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = convertView;
        if (v == null) {
            v = Inflater.inflate(R.layout.layout_img_grid, null);
        } else {
            v = convertView;
        }
        ImageView img = (ImageView) v.findViewById(R.id.imgView_grid);
        File auxFile = new File(String.valueOf(Uri.parse(src_list.get(position))));
        img.setImageBitmap(decodeFile(auxFile));

        return v;
    }
    private Bitmap decodeFile(File f){
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.decodeStream(fis, null, o);
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int scale = 1;
        if (o.outHeight > 500 || o.outWidth > 500) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(500 /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        b = BitmapFactory.decodeStream(fis, null, o2);
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b;
    }


}
