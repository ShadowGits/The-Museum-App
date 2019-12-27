/*It shows the information and text to speech functionality
Created by winter intern*/
package com.example.st01.arsample;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import static com.example.st01.arsample.MainActivity.res_model;

public class information extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private Button btnn;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        btnn=(Button)findViewById(R.id.btnn);

        TextView txtview = (TextView)findViewById(R.id.information);
        String path = "/storage/emulated/0" + "/" + "Saved_model/" +res_model+"/"+ res_model + ".txt";
        File file = new File(path);
        if(file.exists())
        {
            StringBuilder text = new StringBuilder();

            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line ;
                while ((line=br.readLine())!=null)
                {
                    text.append(line);
                    text.append('\n');

                }
            }

            catch (IOException e)
            {
                Toast.makeText(information.this,"No information present",Toast.LENGTH_LONG).show();

            }
            txtview.setText(text);

        }

        else
        {
            txtview.setText("Sorry, no information available!");
        }

        textView = (TextView) findViewById(R.id.information);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String data = textView.getText().toString();
                Log.i("TTS", "button clicked: " + data);
                int speechStatus = textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null);

                if (speechStatus == TextToSpeech.ERROR) {
                    Log.e("TTS", "Error in converting Text to Speech!");
                }
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
