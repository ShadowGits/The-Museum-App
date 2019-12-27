package com.example.st01.arsample;

/*
This shows the saved offline models in list view
Created by winter intern
*/



import android.content.Intent;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.st01.arsample.MainActivity.res_model;

public class
DeleteActivity extends AppCompatActivity {
    static TextView leftMemory;
    static TextView fullMemory;
    CustomAdapter adapter;

    ListView listView;
    ArrayList<String> filelist;

    void readFiles(){
        filelist=new ArrayList<>();
        String path = "/storage/emulated/0"+"/"+"Saved_model";

        File file = new File(path);
        String[] fileNames= file.list();
        if(fileNames!=null){
            for(String s : fileNames){
                if(!s.endsWith(".zip")&&s.endsWith("")){
                    filelist.add(s);
                }
            }
        }
    }

    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    void initAdapter(){
        adapter=new CustomAdapter(filelist,this);
        listView.setAdapter(adapter );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String myfolder= "/storage/emulated/0"+"/"+"Saved_model";


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedmodels);

        listView=(ListView)findViewById(R.id.listView);
        readFiles();
        initAdapter();

        leftMemory=(TextView)findViewById(R.id.textView4);
        fullMemory=(TextView)findViewById(R.id.textView6);
        updateMemory();

    }

    public static void updateMemory()
    {

        double d1=getAvailableInternalMemorySize();
        double d2=getTotalInternalMemorySize();
        double filled_storage=d1/d2;
        filled_storage*=100;
        String storage_string=Double.toString(filled_storage);
        Log.i("Infohhjjj",Double.toString(filled_storage));
        //Toast.makeText(getApplicationContext(),Double.toString(filled_storage),Toast.LENGTH_LONG).show();
        leftMemory.setText("Usable Memory :=");
        fullMemory.setText(storage_string.substring(0,7)+"% left");

    }



}





