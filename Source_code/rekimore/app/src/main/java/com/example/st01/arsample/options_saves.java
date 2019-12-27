/*
    It plays the video of the selected model.
    Created by winter intern
*/

package com.example.st01.arsample;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import static com.example.st01.arsample.MainActivity.res_model;

public class options_saves extends AppCompatActivity {

    Button play;
    VideoView videoView;
    MediaController mediac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_saves);
        play = (Button)findViewById(R.id.play);
        videoView = (VideoView)findViewById(R.id.videoview);
        mediac = new MediaController(this);


    }

    public void  videoplay( View v)
    {

        String videopath = "/storage/emulated/0" + "/" + "Saved_model/" +res_model+"/"+ res_model + ".mp4";
        Uri uri = Uri.parse(videopath);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediac);
        mediac.setAnchorView(videoView);
        videoView.start();
        //      Toast.makeText(options_saves.this,videopath,Toast.LENGTH_LONG).show();

    }
}
