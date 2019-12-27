/*
Copyright
Author : ITMR
Created on : June 2018
Class purpose : Spalsh screen when we start application
 */
package com.example.st01.arsample;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main3Activity extends AppCompatActivity {
private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //Create a splash screen for SPLASH_TIME_OUT this time
        new Handler().postDelayed(new Runnable(){
        @Override
            public void run(){
            Intent k = new Intent(Main3Activity.this, MainActivity.class);
            startActivity(k);
        }
        },SPLASH_TIME_OUT);
    }
}
