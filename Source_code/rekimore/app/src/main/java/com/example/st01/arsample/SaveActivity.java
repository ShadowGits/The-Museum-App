/*It opens the save activity from the list of options shown after scanning and loading of model (online)
-created by winter intern*/

package com.example.st01.arsample;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.example.st01.arsample.MainActivity.res_model;
import static com.example.st01.arsample.MainActivity.res_link;



public class SaveActivity extends AppCompatActivity  {

    public  static  final  int  DIALOG_DOWNLOAD_PROGRESS=1;
    private ProgressDialog progressDialog;
    private static  String file_url="https://s3.amazonaws.com/myhelenbucket/ "+res_model+".zip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        //This button shows the already present offline models.
        Button btnview =(Button)findViewById(R.id.btnview);
        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File directory = new File(myfolder);
                File[] contents = directory.listFiles();
                // the directory file is not really a directory..
                if (contents == null) {

                    Toast.makeText(SaveActivity.this, " No saved models !!!", Toast.LENGTH_SHORT).show();


                }
                // Folder is empty
                else if (contents.length == 0) {

                    Toast.makeText(SaveActivity.this, "No saved models !!!", Toast.LENGTH_SHORT).show();


                }
                // Folder contains files
                else {
                    //Toast.makeText(this, "Saved_models have files", Toast.LENGTH_SHORT).show();
                    Intent k = new Intent(SaveActivity.this, activity_savedmodels.class);
                    startActivity(k);

                }

            }
        });

        //This button  saves the model and other related information for offline view.

        Button btnsave =(Button)findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File gameDir = new File("/sdcard/Saved_model/"+res_model+"/" + res_model + "");


                if(!gameDir.exists())
                {
                    gameDir.mkdirs();
                    startDownload();


                }

                else
                {
                    Toast.makeText(SaveActivity.this, res_model+" already exits", Toast.LENGTH_SHORT).show();
                }



            }

        });




    }
    @Override
    public void onBackPressed() {


                Intent k = new Intent (SaveActivity.this,MainActivity.class);
                startActivity(k);

    }
    private boolean unpackZip(String filePath) {

        InputStream is;
        ZipInputStream zis;
        try {

            File zipfile = new File(filePath);
            String parentFolder = zipfile.getParentFile().getPath();
            String filename;

            is = new FileInputStream(filePath);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                if (ze.isDirectory()) {
                    File fmd = new File(parentFolder + "/" + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(parentFolder + "/" + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
        Toast.makeText(SaveActivity.this,res_model+" Model Saved !!!", Toast.LENGTH_SHORT).show();

        return true;
    }

    private  void startDownload(){
        String url1="https://s3.amazonaws.com/myhelenbucket/"+res_model+".zip";
        String url2="https://s3.amazonaws.com/myhelenbucket/"+res_model+".txt";
        String url3="https://s3.amazonaws.com/myhelenbucket/"+res_model+".mp4";
        new DownloadFileAsync().execute(url1,url2,url3);

    }

    protected  Dialog onCreateDialog(int id){
        switch (id){
            case DIALOG_DOWNLOAD_PROGRESS:
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Saving Model...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                return progressDialog;
            default:
                return  null;
        }
    }

    class DownloadFileAsync extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            int count;
            try {
                URL url0 = new URL(params[0]);
                URL url1= new URL(params[1]);
                URL url2= new URL(params[2]);
                URLConnection conexion0 = url0.openConnection();
                URLConnection conexion1 = url1.openConnection();
                URLConnection conexion2 = url2.openConnection();
                conexion0.connect();
                conexion1.connect();
                conexion2.connect();

                int lengthOfFile = conexion0.getContentLength()+conexion1.getContentLength()+conexion2.getContentLength();
                Log.d("ANDRO_ASYNC", "Length Of File: " + lengthOfFile);

                InputStream input0 = new BufferedInputStream(url0.openStream());
                InputStream input1= new BufferedInputStream(url1.openStream());
                InputStream input2= new BufferedInputStream(url2.openStream());
                OutputStream output0 = new FileOutputStream("/storage/emulated/0" + "/" + "Saved_model/" +res_model+"/"+ res_model + ".zip");
                OutputStream output1 = new FileOutputStream("/storage/emulated/0" + "/" + "Saved_model/" +res_model+"/"+ res_model + ".txt");
                OutputStream output2 = new FileOutputStream("/storage/emulated/0" + "/" + "Saved_model/" +res_model+"/"+ res_model + ".mp4");

                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input0.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output0.write(data, 0, count);
                }

                while ((count = input1.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output1.write(data, 0, count);
                }

                while ((count = input2.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    output2.write(data, 0, count);
                }
                output0.flush();
                output0.close();
                input0.close();
                output1.flush();
                output1.close();
                input1.close();
                output2.flush();
                output2.close();
                input2.close();
            } catch (Exception e) {

            }
            return "Download Completed!";

        }
        @Override
        protected void onPostExecute(String result)
        {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            unpackZip("/sdcard/Saved_model"+"/"+res_model+"/"+res_model+".zip");

        }

        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected void onProgressUpdate(String... values){
            Log.d("ANDRO_SYNC",values[0]);
            progressDialog.setProgress(Integer.parseInt(values[0]));
        }


    }



    String myfolder= "/storage/emulated/0"+"/"+"Saved_model/"+res_model;
    public void onStart(){
        super.onStart();
        createFolder("Saved_model");
    }


    public void createFolder(String fname){
//        String myfolder= "/storage/emulated/0"+"/"+fname;
        File f=new File(myfolder);
       /* if(!f.exists())
            if(!f.mkdir()){
               Toast.makeText(this, myfolder+" can't be created.", Toast.LENGTH_SHORT).show();
            }
            else
               Toast.makeText(this, myfolder+" can be created.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, myfolder+" already exits.", Toast.LENGTH_SHORT).show();*/
    }






}
