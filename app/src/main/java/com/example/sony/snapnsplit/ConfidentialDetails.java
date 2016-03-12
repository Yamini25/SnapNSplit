package com.example.sony.snapnsplit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfidentialDetails extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextName;
    private EditText editTextPhoneno;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextReEnterPassword;
    private EditText editTextIFSC;
    private EditText editTextAccountno;


    private Button buttonLogin;
    private Button buttonSubmit;
    private TextView textView;
    private static final String REGISTER_URL = "http://snapnsplit.esy.es/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confidential_details);

        textView = (TextView) findViewById(R.id.textViewName);

        Intent intent = getIntent();

        String user_name = intent.getStringExtra(MainActivity.NAME);

        textView.setText("Welcome User " + user_name);
        //editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPhoneno = (EditText) findViewById(R.id.editTextPhoneno);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        // editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextAccountno= (EditText) findViewById(R.id.editTextAccountNo);
        editTextIFSC= (EditText) findViewById(R.id.editTextIFSC);
        editTextReEnterPassword= (EditText) findViewById(R.id.editTextReEnterPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonLogin.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
    }
    public void onBackPressed() {
        Intent intent1 = new Intent(this,MainActivity.class);
        startActivity(intent1);
    }
    @Override
    public void onClick(View v) {
        if(v == buttonSubmit){
            if(editTextPhoneno.length()!= 10)
            {
                editTextPhoneno.setError("Invalid Number");
                editTextPhoneno.setText(null);
            }
            if(editTextPassword.length()< 6)
            {
                editTextPassword.setError("Minimum 6 characters");
                editTextPassword.setText(null);
            }
            if(! editTextReEnterPassword.getText().toString().equals(editTextPassword.getText().toString())  )
            {
                editTextReEnterPassword.setError("Incorrect match");
                editTextReEnterPassword.setText(null);
            }

            if(! isValidIFSC(editTextIFSC.getText().toString()))
            {
                editTextIFSC.setError("Invalid IFSC");
                editTextIFSC.setText(null);
            }
            if(! isValidAcc(editTextAccountno.getText().toString()))
            {
                editTextAccountno.setError("Invalid Account Number");
                editTextAccountno.setText(null);
            }

            if (isValidAcc(editTextPhoneno.getText().toString()) && editTextPassword.getText().toString().length() >= 6
                    && editTextReEnterPassword.getText().toString().equals(editTextPassword.getText().toString())
                    && editTextAccountno.getText().toString().length() > 0 && editTextIFSC.getText().toString().length() > 0
                    && isValidIFSC(editTextIFSC.getText().toString()) && isValidAcc(editTextAccountno.getText().toString())  ) {
                registerUser();
            }
            else {
                Toast.makeText(this, "All fields are mandatory",
                        Toast.LENGTH_LONG).show();
            }
        }
        if(v==buttonLogin)
        {
            Intent i=new Intent(this,ActivityLogin.class);
            startActivity(i);
        }

    }

    private boolean isValidIFSC(String IFSC) {
        String PATTERN = "[A-Z|a-z]{4}[0]\\d{6}";

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(IFSC);
        return matcher.matches();
    }
    private boolean isValidAcc(String Acc) {
        String PATTERN = "[0-9]{10}";

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(Acc);
        return matcher.matches();
    }


    private void registerUser() {
        Intent intent = getIntent();
        String name=" ";

        String email=" ";
        if(MainActivity.x==1) {
            MainActivity.x=0;
            name = intent.getStringExtra(MainActivity.NAME_G);
            //    phoneno = intent.getStringExtra(MainActivity.PHONENO);
            email = intent.getStringExtra(MainActivity.EMAIL_G);

        }
        else if(MainActivity.y==1)
        {
            MainActivity.y=0;
            name = intent.getStringExtra(MainActivity.NAME);
            //   phoneno = intent.getStringExtra(MainActivity.PHONENO);
            email = intent.getStringExtra(MainActivity.EMAIL);

        }
        String phoneno = editTextPhoneno.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();
        String ReEnterPassword=editTextReEnterPassword.getText().toString().trim().toLowerCase();
        String AccNo=editTextAccountno.getText().toString().trim().toLowerCase();
        String IFSCcode=editTextIFSC.getText().toString().trim().toLowerCase();
        register(name,phoneno,password,email,AccNo,IFSCcode);
        if(password.equals(ReEnterPassword))
        {

        }
        register(name, phoneno, password, email, AccNo, IFSCcode);


        if(password.equals(ReEnterPassword))
        {

        }


    }

    private void register(String name, String phoneno, String password, String email,String AccNo,String IFSCcode) {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ConfidentialDetails.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("phoneno",params[1]);
                data.put("password",params[2]);
                data.put("email",params[3]);
                data.put("AccNo",params[4]);
                data.put("IFSCcode",params[5]);
                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name,phoneno,password,email,AccNo,IFSCcode);
    }
}
