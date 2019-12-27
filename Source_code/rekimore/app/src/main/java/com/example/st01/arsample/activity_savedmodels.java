/*
This shows the saved offline models in list view
Created by winter intern
*/

package com.example.st01.arsample;

import android.content.Intent;
import android.net.Uri;
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
activity_savedmodels extends AppCompatActivity {

    file adapter;

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

    void initAdapter(){
        adapter=new file(this,R.layout.list_item_original,filelist);
        listView.setAdapter(adapter );
        listView.setOnItemClickListener(itemClickListener);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String myfolder= "/storage/emulated/0"+"/"+"Saved_model";


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedmodels);

        listView=(ListView)findViewById(R.id.listView);
        readFiles();
        initAdapter();

        TextView leftMemory=(TextView)findViewById(R.id.textView4);
        TextView fullMemory=(TextView)findViewById(R.id.textView6);
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
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String fileName = filelist.get(i);
            res_model=fileName;
            // Toast.makeText(activity_savedmodels.this,"You Clicked "+fileName,Toast.LENGTH_LONG).show();

            Intent k = new Intent(activity_savedmodels.this, saved_more.class);
            startActivity(k);

        }
    };

}




