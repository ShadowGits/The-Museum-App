/*It loads the 3D model
 Created by winter intern*/
package com.example.st01.arsample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;

import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import min3d.core.Object3dContainer;
import min3d.core.RendererActivity;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.example.st01.arsample.MainActivity.res_model;
import static min3d.Min3d.TAG;

public class AfterSavingLoad extends RendererActivity implements View.OnClickListener {

    private Button takePictureButton;
    private Object3dContainer objModel;
    private Uri file;
    private Spinner c;
    String pathToFile;
    @Override
    protected void glSurfaceViewConfig()
    {
        //It creates transparent background for 3D model
        _glSurfaceView.setEGLConfigChooser(8,8,8,8, 16, 0);
        _glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        _glSurfaceView.setZOrderOnTop(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_saving_load);
        //Camera preview instance
        CameraPreview mPreview = new CameraPreview(this);
        // Add camera preview to our layout
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_layout);
        //Add preview on Z axis,0 is parameter for Z axis
        preview.addView(mPreview, 0);
        //_glsurfaceView has our model loaded in it,we are just adding it to layout
        preview.addView(_glSurfaceView);


      /*  takePictureButton=(Button)findViewById(R.id.button3);
        //camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }*/

     /* if(Build.VERSION.SDK_INT>=23)
      {
          requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2 );
      }
       dispatchCamera();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==1)
            {
                Bitmap bitmap= BitmapFactory.decodeFile(pathToFile);
            }
        }
    }

    public void dispatchCamera()
    {
        Intent openCamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCamera.putExtra("android.intent.extras.CAMERA_FACING", 1);

        if(openCamera.resolveActivity(getPackageManager())!=null)
        {
            File photo=null;

                photo=createPhotoFile();
                if(photo!=null)
                {
                    pathToFile=photo.getAbsolutePath();
                    Uri photoUri= FileProvider.getUriForFile(this,"com.example.st01.arsample.fileprovider",photo);
                    openCamera.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                    startActivityForResult(openCamera,1);
                }

        }
    }

    private File createPhotoFile() {
        String name=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir=getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=null;
        try {
            image=File.createTempFile(name,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }






    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }


    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }


    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }*/



    @Override
    public void initScene() {
        // res_model contains model name we are using mi n3D library to load model object in objModel
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
            parser = Parser.createParser(Parser.Type.OBJ, "/sdcard/Saved_model/"+res_model+"/"+res_model+"/"+res_model+"_obj",
                    true);
         //  Toast.makeText(AfterSavingLoad.this, "parsed from "+"/sdcard/Saved_model"+res_model+"/"+res_model+"_obj", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onClick(View view) {

    }
}
