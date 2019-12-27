/*
Copyright
Author : ITMR
Created on : June 2018
Class purpose : It loads 3D object with camera background on screen
 */
package com.example.st01.arsample;

import min3d.core.Object3dContainer;
import min3d.core.RendererActivity;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;


import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.st01.arsample.MainActivity.res_model;


public class ExampleLoadObjFile extends RendererActivity implements View.OnClickListener {
    private Object3dContainer objModel;
    private Spinner c;
    @Override
    protected void glSurfaceViewConfig()
    {
        //It creates transparent background for 3D model
        _glSurfaceView.setEGLConfigChooser(8,8,8,8, 16, 0);
        _glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        _glSurfaceView.setZOrderOnTop(true);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //Camera preview instance
        CameraPreview mPreview = new CameraPreview(this);
        // Add camera preview to our layout
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        //Add preview on Z axis,0 is parameter for Z axis
        preview.addView(mPreview,0);
        // _glsurfaceView has our model loaded in it,we are just adding it to layout
        preview.addView(_glSurfaceView);
        // button for selecting spinner option
        Button b;
        b = (Button) this.findViewById(R.id.layoutCancel);
        b.setOnClickListener(this);
        //Spinner button for displaying options which are defined in res/values/strings.xml
        c = (Spinner) this.findViewById(R.id.layoutOkay);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = String.valueOf(c.getSelectedItem());
                //Based on selection it will go to respective activities
                if(temp.equals("Animation")) {
                    Intent k = new Intent(ExampleLoadObjFile.this, animation.class);
                    startActivity(k);
                }
                else if(temp.equals("Information")){

                    Intent k = new Intent(ExampleLoadObjFile.this, Info.class);
                    startActivity(k);
                }
//                else if(temp.equals("Model_animation")){
//
//                    Intent k = new Intent(ExampleLoadObjFile.this, animation2.class);
//                    startActivity(k);
//                }
                else if(temp.equals("Save")){

                    Intent k = new Intent(ExampleLoadObjFile.this, SaveActivity.class);
                    startActivity(k);
                }
            }
        });
    }
    @Override
    public void initScene() {
        // res_model contains model name we are using min3D library to load model object in objModel
        //Ontouchevents below is for controlling object
        scene.lights().add(new Light());
        scene.backgroundColor().setAll(0x00011111);
        Parser.sdcard = true;
        IParser parser;
        if(!Parser.sdcard)
        {
            parser = Parser.createParser(Parser.Type.OBJ,
                    getResources(), "com.example.st01.arsample:raw/camaro_obj", true);
        }
        else
        {
            parser = Parser.createParser(Parser.Type.OBJ, "/sdcard/Download/"+res_model+"/"+res_model+"/"+res_model+"_obj",
                    true);
        }
        parser.parse();
        objModel = parser.getParsedObject();
        objModel.scale().x = objModel.scale().y = objModel.scale().z = .5f;
        objModel.position().x = 0;
        objModel.position().z = 0;
        objModel.position().y = -20;
        scene.addChild(objModel);

        scene.camera().target = objModel.position();
        Parser.sdcard = false;
    }

    float touchedX, touchedY;
    float lastplanePosition,planePosition;
    float totalRatio = .9f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchedX = event.getX();
                touchedY = event.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    lastplanePosition = spacing(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                switch (event.getPointerCount()) {
                    case 1:
                        objModel.rotation().z += (touchedX - event.getX()) / 2f;
                        objModel.rotation().x -= (touchedY - event.getY()) / 2f;
                        touchedX = event.getX();
                        touchedY = event.getY();
                        break;
                    case 2:
                        planePosition = spacing(event);
                        float scaledRatio = planePosition/ lastplanePosition;
                        totalRatio = totalRatio *scaledRatio;

                        objModel.scale().x = objModel.scale().y = objModel.scale().z = totalRatio;
                        lastplanePosition = planePosition;
                        break;
                }
                break;
        }
        return true;

    }
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }
    //All this onBackpressed function is override because when user hit back button
    //We will delete files of model from sdcard
    @Override
    public void onBackPressed() {
        File dir1 = new File("/sdcard/Download/"+res_model+"/");
        DeleteRecursive(dir1);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent k = new Intent (ExampleLoadObjFile.this,MainActivity.class);
                startActivity(k);
            }
        },2000);
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

    @Override
    public void onClick(View view) {

    }
}