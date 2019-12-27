/*
Copyright
Author : ITMR
Created on : June 2018
Class purpose : It Downloads model1.mp4 oin device Download/video folder.
 */
package com.example.st01.arsample;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.st01.arsample.MainActivity.res_model;

public class DownloadFragment3 extends Fragment {
    interface DownloadCallbacks {
        void onPostExecute(String msg);
    }

    private DownloadCallbacks mCallbacks;
    private DownloadTask mTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // Retain this fragment across configuration changes.

        File gameDir = new File("/sdcard/Download/video/");
        gameDir.mkdirs();

        // Create and execute the background task.
        File pa = new File("/sdcard/Download/video/"+res_model+"1.mp4");
        if (!pa.exists()) {
            mTask = new DownloadTask();
            mTask.execute("https://s3.amazonaws.com/myhelenbucket/"+res_model+"1.mp4",
                    "/sdcard/Download/video/" +res_model+"1.mp4");
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (DownloadCallbacks) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... args) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            String destinationFilePath = "";
            try {
                URL url = new URL(args[0]);
                destinationFilePath = args[1];

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                }

                // download the file
                input = connection.getInputStream();

                Log.d("DownloadFragment ", "destinationFilePath=" + destinationFilePath);
                new File(destinationFilePath).createNewFile();
                output = new FileOutputStream(destinationFilePath);

                byte data[] = new byte[4096];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }

            File f = new File(destinationFilePath);

            Log.d("DownloadFragment ", "f.getParentFile().getPath()=" + f.getParentFile().getPath());
            Log.d("DownloadFragment ", "f.getName()=" + f.getName().replace(".zip", ""));

            return "Download Completed!";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("DownloadFragment ", s);
            mCallbacks.onPostExecute(s);
        }
    }
}