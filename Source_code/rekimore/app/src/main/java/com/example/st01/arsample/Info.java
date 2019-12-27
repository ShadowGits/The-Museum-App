/*
Copyright
Author : ITMR
Created on : June 2018
Class purpose : It Downloads model.txt oin device Download/info folder.
 */
package com.example.st01.arsample;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.st01.arsample.MainActivity.res_model;

public class Info extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        File pa = new File("/sdcard/download/info/" + res_model + ".txt");
        if (!pa.exists()) {
            downloadByDownloadManager(
                    "https://s3.amazonaws.com/myhelenbucket/" + res_model+".txt", res_model+".txt");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Intent k = new Intent(Info.this, Main5Activity.class);
                    startActivity(k);
                }
            }, 3000);
        }
        else{
            Intent k = new Intent(Info.this, Main5Activity.class);
            startActivity(k);
        }
    }

        public void downloadByDownloadManager (String url, String outputFileName){
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDescription("A zip package with some files");
            request.setTitle("Zip package");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.allowScanningByMediaScanner();
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/info", outputFileName);

            Log.d("MainActivity: ", "download folder>>>>"
                    + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/info");

            // get download service and enqueue file
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        /*registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));*/
            manager.enqueue(request);
        }
    }