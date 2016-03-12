package com.example.sony.snapnsplit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Profile extends AppCompatActivity {
    EditText name,accnum,ph,ifsc,password,mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name =(EditText)findViewById(R.id.name);
        ph =(EditText)findViewById(R.id.phone);
        accnum =(EditText)findViewById(R.id.accno);
        ifsc =(EditText)findViewById(R.id.ifsc);
        password =(EditText)findViewById(R.id.password);
        mail =(EditText)findViewById(R.id.email);

        Snap_Split.sharedpreferences = this.getSharedPreferences(Snap_Split.MyPREFERENCES, 0);

        String res = Snap_Split.sharedpreferences.getString("name", "couldn't load").toString();
        name.setText(res);
        res = Snap_Split.sharedpreferences.getString("phone", "couldn't load").toString();
        ph.setText(res);
        res = Snap_Split.sharedpreferences.getString("acc", "couldn't load").toString();
        accnum.setText(res);
        res = Snap_Split.sharedpreferences.getString("IFSC", "couldn't load").toString();ifsc.setText(res);
        res = Snap_Split.sharedpreferences.getString("mail", "couldn't load").toString();
        mail.setText(res);
        //res = SnapNSplit.sharedpreferences.getString("logstatus", "couldn't load").toString();
        //password.setText(res);

        name.setKeyListener(null);
        mail.setKeyListener(null);
        ifsc.setKeyListener(null);
        accnum.setKeyListener(null);
        ph.setKeyListener(null);

    }
}
