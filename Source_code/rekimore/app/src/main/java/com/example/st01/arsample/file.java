package com.example.st01.arsample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class file extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<String>  filelist;
    public file(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);

        this.context=context;
        this.resource=resource;
        filelist=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View myView=null;

            myView=LayoutInflater.from(context).inflate(resource,parent,false);
             TextView textFileName=(TextView)myView.findViewById(R.id.textViewfile);
            String fileName=filelist.get(position);
            textFileName.setText(fileName);
            return  myView;
    }
}
