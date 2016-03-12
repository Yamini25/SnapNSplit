package com.example.sony.snapnsplit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;



import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Snap_Split extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    TextView textView;
Button pay,request1;

    String myJSON;


    static String p=" ";

    static String amt="Please snap the bill first";
    public static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private static final String TAG_RESULTS="result";
    private static final String TAG_PHONENO = "PhoneNo";
    private static final String TAG_NAME = "Name";
    private static final String TAG_IFSC ="IFSC";
    private static final String TAG_ACCNO ="AccountNumber";
    private static final String TAG_EMAIL ="Email";
    private static final String TAG_PASSWORD1 ="Password";
    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;


    SessionManagement session;
    String username="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap__split);

    pay=(Button)findViewById(R.id.pay);
        request1=(Button)findViewById(R.id.request1);
        pay.setOnClickListener(this);
        request1.setOnClickListener(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        getData();

        // textView = (TextView) findViewById(R.id.textViewUserName);

        Intent intent = getIntent();

        username = intent.getStringExtra(ActivityLogin.USER_NAME);

        //textView.setText("Welcome User " + username);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Intent i1=new Intent(this,ServiceNotification.class);
        startService(i1);
        Toast.makeText(this,"yes!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent1 = new Intent(Intent.ACTION_MAIN);
            intent1.addCategory(Intent.CATEGORY_HOME);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.snap__split, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
       // Fragment fragment = new About_Us_Fragment();
        //String title = getString(R.string.app_name);

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true; */

        switch (id) {
            case R.id.nav_history:
          //      fragment = new About_Us_Fragment();
            //    title = "About Us";
                Intent i=new Intent(this,Tansaction_History.class);
                startActivity(i);
                break;
            case R.id.nav_profile:
              //  fragment = new Profile_Fragment();
                //title = "Profile";
                Intent p=new Intent(this,Profile.class);
                startActivity(p);
                break;
            case R.id.nav_sync:
                //fragment = new Snap_Bill_Fragment();
                Intent s=new Intent(this,Sync_Contacts.class);
                startActivity(s);
                /*PackageManager pm=this.getPackageManager();
                String packageName="edu.sfsu.cs.orange.ocr";
                Intent launchIntent=pm.getLaunchIntentForPackage(packageName);
                startActivity(launchIntent); */

                //title = "Snap the Bill";
                break;
            case R.id.nav_changempin:
                //amt="";
                Intent m=new Intent(this,Change_mpin.class);
                startActivity(m);
                //amt = getIntent().getStringExtra("Amount");
                //fragment = new Tag_Friends_Fragment();
                //title = "Tag Friends";
                break;

            case R.id.nav_help:
                //fragment = new Help_Fragment();
                //title = "Help";
                Intent h=new Intent(this,Help_Activity.class);
                startActivity(h);
                break;

            case R.id.nav_logout:
//finish();
                 /* Intent intent1 = new Intent(Intent.ACTION_MAIN);
                  intent1.addCategory(Intent.CATEGORY_HOME);
                  intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(intent1); */
                // fragment = new Tag_Friends_Fragment();
                //   title = "Tag Friends";
//                  Intent i1=new Intent(this,Start_Up.class);
                //                startActivity(i1);

                session.logoutUser();
                break;
            case R.id.nav_share:
                //fragment = new Share_Fragment();
                //title = "Share";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Check out Snap 'N' Split http://www.google.com");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this App!");
                startActivity(Intent.createChooser(intent, "Share"));
                break;
            default:
                break;


        }

        /* if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        } */


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getData(){
        //Toast.makeText(getApplicationContext(),"getData",Toast.LENGTH_SHORT).show();
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://snapnsplit.esy.es/getAllData.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    protected void showList(){
        try {
           // Toast.makeText(getApplicationContext(),"showlist",Toast.LENGTH_SHORT).show();

            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);




            int f=0;
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String email = c.getString(TAG_EMAIL);

               // Toast.makeText(getApplicationContext(),"showlist"+email+" "+i,Toast.LENGTH_SHORT).show();

                if(email.equals(username))
                {
                    f=i;
                }

            }
            JSONObject c1 = peoples.getJSONObject(f);


            String phone = c1.getString(TAG_PHONENO);
            p=phone;
            String name = c1.getString(TAG_NAME);
            String email1=c1.getString(TAG_EMAIL);
            String accno = c1.getString(TAG_ACCNO);
            String ifsc = c1.getString(TAG_IFSC);
            String password = c1.getString(TAG_PASSWORD1);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("acc", accno);
            editor.apply();
            editor.putString("phone", phone);
            editor.apply();
            editor.putString("IFSC", ifsc);
            editor.apply();
            editor.putString("name", name);
            editor.apply();
            editor.putString("mail", email1);
            editor.apply();
            editor.putString("logstatus", "1");
            editor.apply();




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        if(v==pay)
        {
           /* Intent i1=new Intent(this,ServiceNotification.class);
            startService(i1);
            Toast.makeText(this,"yes!",Toast.LENGTH_SHORT).show();*/
        }
        if(v==request1)
        {
            PackageManager pm=this.getPackageManager();
            String packageName="edu.sfsu.cs.orange.ocr";
            Intent launchIntent=pm.getLaunchIntentForPackage(packageName);
            startActivity(launchIntent);
        }
    }
}

