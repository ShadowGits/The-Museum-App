/*
Copyright
Author : ITMR
Created on : June 2018
Class purpose : It plays mp4 file on screen on videoview
 */
package com.example.st01.arsample;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.st01.arsample.MainActivity.res_model;

public class animation4 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);
        VideoView myVideoView = (VideoView)findViewById(R.id.myvideoview);
        String SrcPath = "/sdcard/Download/video/"+res_model+"1.mp4";
        myVideoView.setVideoPath(SrcPath);
        myVideoView.setMediaController(new MediaController(animation4.this));
        myVideoView.requestFocus();
        myVideoView.start();
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(SrcPath);
        }catch (IOException e){
            e.printStackTrace();
        }
        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                File dir1 = new File("/sdcard/download/video/"+res_model+"1.mp4");
                DeleteRecursive(dir1);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {

                        Intent k = new Intent (animation4.this,ExampleLoadObjFile.class);
                        startActivity(k);
                    }
                },1000);
            }
        });

    }
    //All this onBackpressed function is override because when user hit back button
    //We will delete files of model from sdcard
    @Override
    public void onBackPressed() {
        File dir1 = new File("/sdcard/download/video/"+res_model+"1.mp4");
        DeleteRecursive(dir1);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                Intent k = new Intent (animation4.this,ExampleLoadObjFile.class);
                startActivity(k);
            }
        },1000);
    }

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