package com.example.sony.snapnsplit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

public class Start_Up_Screen extends AppCompatActivity implements View.OnClickListener {
Button signup,login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__up__screen);
        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        flipper.startFlipping();
     signup= (Button) findViewById(R.id.signup);
        login= (Button) findViewById(R.id.login);
        signup.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==login)
        {
            Intent i=new Intent(this,ActivityLogin.class);
            startActivity(i);
        }
        if(v==signup)
        {
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
        }


    }
}
