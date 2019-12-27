/*
Copyright
Author : ITMR
Created on : June 2018
Class purpose : It Downloads model.mp4 oin device Download/video folder.
 */
package com.example.st01.arsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;


public class animation extends AppCompatActivity implements DownloadFragment2.DownloadCallbacks {
    private static final String DOWNLOAD_FRAGMENT = "download_fragment";
    private DownloadFragment2 downloadFragment2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emp);
        FragmentManager fm = getSupportFragmentManager();
        downloadFragment2 = (DownloadFragment2) fm.findFragmentByTag(DOWNLOAD_FRAGMENT);

        // if it's null, it was created, otherwise it was created and retained
        downloadFragment2 = new DownloadFragment2();
        fm.beginTransaction().add(downloadFragment2, DOWNLOAD_FRAGMENT).commit();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent k = new Intent (animation.this,animation1.class);
                startActivity(k);
            }
        },20000);

    }
    public void onPostExecute(String msg) {
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        removeDownloadFragment();
    }

    private void removeDownloadFragment() {
        FragmentManager fm = getSupportFragmentManager();
        downloadFragment2 = (DownloadFragment2) fm.findFragmentByTag(DOWNLOAD_FRAGMENT);
        if (downloadFragment2 != null) {
            fm.beginTransaction()
                    .remove(downloadFragment2)
                    .commit();
        }
    }
}