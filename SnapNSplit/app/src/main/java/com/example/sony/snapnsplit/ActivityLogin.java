package com.example.sony.snapnsplit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {


    String myJSON;
    public static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private static final String TAG_RESULTS = "result";
    private static final String TAG_PHONENO = "PhoneNo";
    private static final String TAG_NAME = "Name";
    private static final String TAG_IFSC = "IFSC";
    private static final String TAG_ACCNO = "AccountNumber";
    private static final String TAG_EMAIL = "Email";
    private static final String TAG_PASSWORD1 = "Password";
    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;
    public static final String USER_NAME = "USER_NAME";

    public static final String PASSWORD = "PASSWORD";

    private static final String LOGIN_URL = "http://snapnsplit.esy.es/login.php";

    private EditText editTextUserName;
    private EditText editTextPassword;

    private Button buttonLogin, Register;

    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activity_login);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        // Session Management
        session = new SessionManagement(getApplicationContext());

        personList = new ArrayList<HashMap<String, String>>();

        //getData();


        editTextUserName = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);

        buttonLogin = (Button) findViewById(R.id.buttonUserLogin);

        buttonLogin.setOnClickListener(this);
        //Register = (Button) findViewById(R.id.button2);
       // Register.setOnClickListener(this);

    }

    String username_sh = "";

    private void login() {
        username_sh = editTextUserName.getText().toString().trim();
        String username = editTextUserName.getText().toString().trim();

        String password = editTextPassword.getText().toString().trim();
        userLogin(username, password);
    }

    private void userLogin(final String username, final String password) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityLogin.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {



                    Intent intent = new Intent(ActivityLogin.this, Snap_Split.class);
                    intent.putExtra(USER_NAME, username);
                    startActivity(intent);
                } else {
                    Toast.makeText(ActivityLogin.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("username", params[0]);
                data.put("password", params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL, data);
                session.createLoginSession("unser_name", "email_id", "phone_no");

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username, password);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            login();
        }

        //if (v == Register) {
          //  Intent i = new Intent(this, MainActivity.class);
            //startActivity(i);
        //}
    }


}