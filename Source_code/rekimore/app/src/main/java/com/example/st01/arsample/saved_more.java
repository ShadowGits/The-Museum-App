/*
It displays the different components of models like videos, 3D model and information files.
*/


package com.example.st01.arsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static com.example.st01.arsample.MainActivity.res_model;

public class saved_more extends AppCompatActivity {

        file adapter;


        ListView listView;
        ArrayList<String> filelist;

        void readFiles(){
            filelist=new ArrayList<>();
            String path = "/storage/emulated/0"+"/"+"Saved_model/"+res_model;

            File file = new File(path);
            String[] fileNames= file.list();
            if(fileNames!=null){
                for(String s : fileNames){
                    if((!s.endsWith(".zip")&&s.endsWith(""))||s.endsWith(".mp4")||s.endsWith(".txt")){
                        if(s.endsWith("")&&!s.endsWith(".mp4")&&!s.endsWith(".txt"))
                        {
                            s=res_model+ " 3d model";
                        }
                        else  if(s.endsWith(".mp4"))
                        {
                            s=res_model+ " video";
                        }
                        else if(s.endsWith(".txt"))
                        {
                            s=res_model+ " information";
                        }
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


        @Override
        protected void onCreate(Bundle savedInstanceState) {
           // String myfolder= "/storage/emulated/0"+"/"+"Saved_model/"+res_model+res_model;


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_saved_more);

            listView=(ListView)findViewById(R.id.listView_more);
            readFiles();
            initAdapter();

        }

        //It selects one of the component  from the video, 3D model and and text file.
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String fileName = filelist.get(i);
                int a = fileName.indexOf(' ');
                String word = fileName.substring(0, a);
                String lastWord = fileName.substring(fileName.lastIndexOf(" ")+1);
                res_model= word;



                //Toast.makeText(saved_more.this,"You Clicked "+fileName,Toast.LENGTH_LONG).show();

                if(lastWord.equals("video"))
                {
                    Intent k = new Intent(saved_more.this, options_saves.class);
                    startActivity(k);
                }
                else if(lastWord.equals("model"))
                {
                    Intent k = new Intent(saved_more.this, AfterSavingLoad.class);
                    startActivity(k);
                }
                else
                {
                    Intent k = new Intent(saved_more.this, information.class);
                    startActivity(k);
                }
            }
        };

}

