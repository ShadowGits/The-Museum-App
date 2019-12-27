/*
Copyright
Author : ITMR
Created on : June 2018
Class purpose : It initialize exammple.txt file and load id and model array then it opens up camera for scanning QR code
 */
package com.example.st01.arsample;

//0-gate
//1-pot

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.PendingIntent.getActivity;

//implementing onclicklistener
public class MainActivity extends AppCompatActivity implements DownloadFragment.DownloadCallbacks,View.OnClickListener {
    private int REQUEST_PERMISSIONS = 100;
    final String myfolder= "/storage/emulated/0"+"/"+"Saved_model";

    String PERMISSIONS_REQUIRED[] = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    GifView gifView;
    private static final String DOWNLOAD_FRAGMENT = "download_fragment";
    private DownloadFragment downloadFragment;
    private Button buttonScan;
    private static List<String> sampleList = new ArrayList<String>();
    public static final String FILE = "example.txt";
    public static String res = null,res_model = null,res_link = null;
    public static int index = 0;
    public static String[] id = new String[20];
    public static String[] model = new String[20];
    public static String[] link = new String[20];

    //qr code scanner object
    private IntentIntegrator qrScan;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.Saved_models:
                File directory = new File(myfolder);
                File[] contents = directory.listFiles();
                // the directory file is not really a directory..
                if (contents == null) {

                    Toast.makeText(MainActivity.this, " No saved models !!!", Toast.LENGTH_SHORT).show();


                }
                // Folder is empty
                else if (contents.length == 0) {

                    Toast.makeText(MainActivity.this, "No saved models !!!", Toast.LENGTH_SHORT).show();


                }
                // Folder contains files
                else {
                    //Toast.makeText(this, "Saved_models have files", Toast.LENGTH_SHORT).show();
                    Intent k = new Intent(MainActivity.this, activity_savedmodels.class);
                    startActivity(k);

                }
                return true;
            case R.id.Delete_models:
                File directory1 = new File(myfolder);
                File[] contents1 = directory1.listFiles();
                // the directory file is not really a directory..
                if (contents1 == null) {

                    Toast.makeText(MainActivity.this, " No models to delete !!!", Toast.LENGTH_SHORT).show();


                }
                // Folder is empty
                else if (contents1.length == 0) {

                    Toast.makeText(MainActivity.this, "No models to delete !!!", Toast.LENGTH_SHORT).show();


                }
                else{
                Intent k = new Intent(
                        MainActivity.this, DeleteActivity.class);
                startActivity(k);}
                return true;


            default:
                return false;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gifView = (GifView)findViewById(R.id.gif_view);
//        File gameDir = new File("/sdcard/Saved_model/" + res_model + "");
//        gameDir.mkdirs();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fm = getSupportFragmentManager();

        ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS);

        downloadFragment = (DownloadFragment) fm.findFragmentByTag(DOWNLOAD_FRAGMENT);
        //Download exxample.txt file from S3 which is our database file
        // if it's null, it was created, otherwise it was created and retained
        if (downloadFragment == null) {
            try {
                openFileInput(FILE);
            }catch (IOException e){}
            downloadFragment = new DownloadFragment();
            fm.beginTransaction().add(downloadFragment, DOWNLOAD_FRAGMENT).commit();
        }
        FileInputStream fis = null;
        try {
            //After Waiting 6 seconds it will come here till then download will be completed.
            //Read id and models from FILE(example.txt) into id and model array
            fis = openFileInput(FILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            while ((text = br.readLine()) != null) {
                sampleList.add(text);
                String[] si = sampleList.get(index).split(",");
                id[index] = si[0];
                model[index] = si[1];
                link[index] = si[2];
                si = new String[si.length];
                index++;
            }
                /*Toast.makeText(this, "Saved to" + getFilesDir() + "/" + FILE + "total  " + index+"1. " + id[1]+
                        " mo1 " + model[0]+ " mo2 " + model[1], Toast.LENGTH_LONG).show();*/
        } catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(fis != null ){
                try{
                    fis.close();
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
            }

        }

        final TextView textView = (TextView)findViewById(R.id.textView5);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){

                textView.setText("Now you can continue !!!");
                gifView.setVisibility(View.INVISIBLE);

            }
        },12000);


        //intializing scan object
        qrScan = new IntentIntegrator(this);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        //attaching onclick listener
        buttonScan.setOnClickListener(this);

        /*Button btnview =(Button)findViewById(R.id.btn_viewoffline);
        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File directory = new File(myfolder);
                File[] contents = directory.listFiles();
                // the directory file is not really a directory..
                if (contents == null) {

                    Toast.makeText(MainActivity.this, " No saved models !!!", Toast.LENGTH_SHORT).show();


                }
                // Folder is empty
                else if (contents.length == 0) {

                    Toast.makeText(MainActivity.this, "No saved models !!!", Toast.LENGTH_SHORT).show();


                }
                // Folder contains files
                else {
                    //Toast.makeText(this, "Saved_models have files", Toast.LENGTH_SHORT).show();
                    Intent k = new Intent(MainActivity.this, activity_savedmodels.class);
                    startActivity(k);

                }

            }
        });*/
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed() {
        finishAffinity();

        System.exit(0);
    }
    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(result.getContents().matches("[0-9]+")&& result.getContents().length() == 10) {
                        //If QR code contains correct input ,send that data to Main1Activity by putExtra
                        Intent intent = new Intent(MainActivity.this,Main1Activity.class);
                        intent.putExtra ( "nTextBox", result.getContents() );
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Please Enter correct input..!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }
    public void onPostExecute(String msg) {
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        removeDownloadFragment();
    }

    private void removeDownloadFragment() {
        FragmentManager fm = getSupportFragmentManager();
        downloadFragment = (DownloadFragment) fm.findFragmentByTag(DOWNLOAD_FRAGMENT);
        if (downloadFragment != null) {
            fm.beginTransaction()
                    .remove(downloadFragment)
                    .commit();
        }
    }
   /* public void onDelete(View view)
    {
        Intent k = new Intent(
                MainActivity.this, DeleteActivity.class);
        startActivity(k);
    }*/

}