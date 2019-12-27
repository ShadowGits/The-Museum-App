/*
Copyright
Author : ITMR
Created on : June 2018
Class purpose : It Show txt file in scrollview type textview box.
 */
package com.example.st01.arsample;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


import static com.example.st01.arsample.MainActivity.res_model;

public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        final TextView myVideoView = (TextView) findViewById(R.id.myvideoview);
        File sdcard = new File("/sdcard/download/info/");
        File file = new File(sdcard,res_model+".txt");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        myVideoView.setText(text);
    }
    //All this onBackpressed function is override because when user hit back button
    //We will delete files of model from sdcard
    @Override
    public void onBackPressed() {
        File dir1 = new File("/sdcard/download/info/"+res_model+".txt");
        DeleteRecursive(dir1);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent k = new Intent (Main5Activity.this,ExampleLoadObjFile.class);
                startActivity(k);
            }
        },1000);
    }

    public void DeleteRecursive (File dir)
    {
        Log.d("DeleteRecursive", "DELETEPREVIOUS TOP" + dir.getPath());
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                File temp = new File(dir, children[i]);
                if (temp.isDirectory()) {
                    Log.d("DeleteRecursive", "Recursive Call" + temp.getPath());
                    DeleteRecursive(temp);
                } else {
                    Log.d("DeleteRecursive", "Delete File" + temp.getPath());
                    boolean b = temp.delete();
                    if (b == false) {
                        Log.d("DeleteRecursive", "DELETE FAIL");
                    }
                }
            }

        }
        dir.delete();
    }
}