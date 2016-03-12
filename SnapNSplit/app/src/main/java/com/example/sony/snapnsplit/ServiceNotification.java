package com.example.sony.snapnsplit;

/**
 * Created by gvsyl on 09-03-16.
 */
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
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
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Intent.getIntent;
import static com.google.android.gms.internal.zzip.runOnUiThread;

public class ServiceNotification extends IntentService {



    String myJSON;
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
    private static final String TAG_STATUS ="status";
    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;


    SessionManagement session;
    String username="";
    int i=0;





    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.example.gvsyl.serviceex2";

    public ServiceNotification() {
        super("DownloadService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {


        username = intent.getStringExtra(ActivityLogin.USER_NAME);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        i++;
                        peoples = null;
                        getData();
                        final Toast toast = Toast.makeText(
                                getApplicationContext(),""+i,
                                Toast.LENGTH_SHORT);
                        //toast.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                toast.cancel();

                            }
                        }, 5000);

                    }
                });
            }
        }, 0, 5000);


    }









    public void getData(){
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




            if(!(Snap_Split.p.equals(c1.getString(TAG_PHONENO))))
            {
                Toast.makeText(this, "Changed", Toast.LENGTH_SHORT).show();
                String tittle="This is Title";
                String subject="This is subject";
                String body="This is notification body";
                NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify=new Notification(R.drawable.noti,tittle,System.currentTimeMillis());
                PendingIntent pending= PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);

                notify.setLatestEventInfo(getApplicationContext(),subject,body,pending);
                notif.notify(0, notify);

                Snap_Split.p=c1.getString(TAG_PHONENO);
            }
            else
            {
                Toast.makeText(this,"Not Changed",Toast.LENGTH_SHORT).show();
            }





} catch (JSONException e) {
        e.printStackTrace();
        }

        }

@Override



public void onDestroy() {
        super.onDestroy();


        }
        }