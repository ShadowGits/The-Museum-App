/*
Copyright
Author : ITMR
Created on : June 2018
Class purpose : It creates camera preview
 */
package com.example.st01.arsample;


import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements Callback {
    private Camera camera;
    //This is a standard camera preview file.
    public CameraPreview( Context context ) {
        super( context );
        getHolder().addCallback( this );
        getHolder().setType( SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS );
    }

    public void surfaceCreated( SurfaceHolder holder ) { camera = Camera.open(); }

    // get full details from http://geekonjava.blogspot.com/2016/05/monster-overlay-on-android-camera-using-jpct.html

    public void surfaceChanged( SurfaceHolder holder, int format, int width, int height ) {
        Camera.Parameters p = camera.getParameters();
        p.setPreviewSize( 640, 480 );
        camera.setParameters( p );

        try {
            camera.setPreviewDisplay( holder );
        } catch( IOException e ) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    public void surfaceDestroyed( SurfaceHolder holder ) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}