package com.example.sony.snapnsplit;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class AnswersActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    String[] questions= {"What is M-pin", "how to snap a bill", "how to tag friends","how to tag friends","how to snap a bill","how to snap a bill"};
    String[] keys= {"M-pin","snap a bill","tag friends","friends","snap","bill"};
    private TextToSpeech tts;
    String a="";
    int flag=0;
    String speakt;
    String[] answer = {"I don't know what is m pin. I added this just because mounica asked me to.",
            "Open the camera in the navigation drawer, take a snap shot of the bill and copy the total amount",
            "Go to tag friends in navigation drawer and type in your friends names in the auto suggest text view",
            "Go to tag friends in navigation drawer and type in your friends names in the auto suggest text view",
            "Open the camera in the navigation drawer, take a snap shot of the bill and copy the total amount",
            "Open the camera in the navigation drawer, take a snap shot of the bill and copy the total amount"};
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tts = new TextToSpeech(this, this);


        txt= (TextView) findViewById(R.id.ans);
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        String que = bundle.getString("stuff");

        for(int i=0;i<questions.length;i++)
        {
            if(que.contains(keys[i])){
                a = questions[i]+"?\nA. "+answer[i];
                speakt=answer[i];
                txt.setText(a);
                speakOut();
                flag =1;
                break;

            }
        }

        if(flag != 1){

            for(int i=0;i<questions.length;i++){
                a = a.concat(questions[i]+"?\nA. "+answer[i]+"\n\n");
            }
            flag=0;


            txt.setText("We didn't get you.. \n\nDid you mean any of the following?\n\n"+a);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {

                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }
    private void speakOut() {



        tts.speak(speakt, TextToSpeech.QUEUE_FLUSH, null);
    }
}
