/*
        Copyright
        Author : ITMR
        Created on : June 2018
        Class purpose : It checks model corresponding to QR code is available or not
        */
package com.example.st01.arsample;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.st01.arsample.MainActivity.*;
public class Main1Activity extends AppCompatActivity implements DownloadFragment1.DownloadCallbacks{

    private static final String DOWNLOAD_FRAGMENT = "download_fragment";
    private DownloadFragment1 downloadFragment1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        //Get the incoded data using intent into re
        Intent iin1= getIntent();
        Bundle b1 = iin1.getExtras();
        if(b1!=null) {
            res =  b1.getString("nTextBox");
        }
        //Search if res is in id array then assign corresponding model to res_model
        res_model = null;

        /*res = "1234567892";
        res_model = "gun";
        res_link = "https://s3.ap-south-1.amazonaws.com/pivda/gun.zip";*/
        for(int j=0; j < index; j++)
        {
            if(res.equals(id[j]))
            {
                res_link = link[j];
                res_model=model[j];
            }
        }
        //If res_model is not empty means id found in id array
        if(res_model != null && !res_model.isEmpty()) {
            if( Arrays.asList(model).contains(res_model)) {
                //Fist download zip file of model then
                //load model with camera background by ExampleLoadObjFile class
                FragmentManager fm = getSupportFragmentManager();
                downloadFragment1 = (DownloadFragment1) fm.findFragmentByTag(DOWNLOAD_FRAGMENT);

                // if it's null, it was created, otherwise it was created and retained
                    downloadFragment1 = new DownloadFragment1();
                    fm.beginTransaction().add(downloadFragment1, DOWNLOAD_FRAGMENT).commit();
               }
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                Intent k = new Intent(Main1Activity.this,ExampleLoadObjFile.class);
                startActivity(k);
                    }
                },25000);
        }
        else {
            Intent k = new Intent(Main1Activity.this,MainActivity.class);
            startActivity(k);
        }
        }
    public void onPostExecute(String msg) {
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        removeDownloadFragment();
    }
    private void removeDownloadFragment() {
        FragmentManager fm = getSupportFragmentManager();
        downloadFragment1 = (DownloadFragment1) fm.findFragmentByTag(DOWNLOAD_FRAGMENT);
        if (downloadFragment1 != null) {
            fm.beginTransaction()
                    .remove(downloadFragment1)
                    .commit();
        }
    }
    //All this onBackpressed function is override because when user hit back button
    //We will delete files of model from sdcard
    @Override
    public void onBackPressed() {
        File dir1 = new File("/sdcard/Download/"+res_model+"/");
        DeleteRecursive(dir1);
        Intent k = new Intent(Main1Activity.this, MainActivity.class);
        startActivity(k);
    }
    //Used for deleting folder recursively.
    public void DeleteRecursive(File dir)
    {
        Log.d("DeleteRecursive", "DELETEPREVIOUS TOP" + dir.getPath());
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                File temp = new File(dir, children[i]);
                if (temp.isDirectory())
                {
                    Log.d("DeleteRecursive", "Recursive Call" + temp.getPath());
                    DeleteRecursive(temp);
                }
                else
                {
                    Log.d("DeleteRecursive", "Delete File" + temp.getPath());
                    boolean b = temp.delete();
                    if (b == false)
                    {
                        Log.d("DeleteRecursive", "DELETE FAIL");
                    }
                }
            }

        }
        dir.delete();
    }
}