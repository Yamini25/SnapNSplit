package com.example.sony.snapnsplit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class Tag_Friends extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    static String amt="Please snap the bill first";

    String myJSON;
    public static int i=0;
    int j,f=0;
    int flag =0;
    EditText inputSearch;
    AutoCompleteTextView myAutoComplete;
    String[] item,test;
    ListView lv;
    ArrayList<String> selectedItems = new ArrayList<String>();
    //ListAdapter adapter;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_PHONENO = "PhoneNo";
    private static final String TAG_NAME = "Name";
    private static final String TAG_IFSC = "IFSC";
    private static final String TAG_ACCNO = "AccountNumber";
    private static final String TAG_EMAIL = "Email";
    private static final String TAG_PASSWORD = "Password";
    JSONArray peoples = null;
    ArrayList<String> itemsList = new ArrayList<String>();

    ArrayAdapter<String> adapter;
    ArrayList<HashMap<String, String>> personList;
TextView Amount;
    ListView list;
    Button proceed;
    Button Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag__friends);
        amt = getIntent().getStringExtra("Amount");

        Add = (Button)findViewById(R.id.addbutton);
        lv = (ListView)findViewById(R.id.outputList);
        Amount=(TextView)findViewById(R.id.textV);
        amt = getIntent().getStringExtra("Amount");
        Amount.setText("Total Amount : " + amt );
        myAutoComplete = (AutoCompleteTextView)findViewById(R.id.myautocomplete);
        myAutoComplete.addTextChangedListener(this);
        //list.setClickable(true);
        // inputSearch = (EditText) mainView.findViewById(R.id.inputSearch);
        proceed = (Button)findViewById(R.id.testbutton);
        // list.setOnItemClickListener(this);
        personList = new ArrayList<HashMap<String, String>>();
        Add.setOnClickListener(this);
        proceed.setOnClickListener(this);
        getData();


    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            int f = 0;

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String name = c.getString(TAG_NAME);


                itemsList.add(name);

            }
            adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.name, itemsList);
            myAutoComplete.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://snapnsplit.esy.es/update_tag.php");

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
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
    public static String[] outputStrArr;



            @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {

        if(v==Add){

            if(myAutoComplete.length()!=0) {
                //j=test.length;
                String str= myAutoComplete.getText().toString().trim();
                /*while(j>=0)
                {
                    Toast.makeText(getActivity(),test[j],Toast.LENGTH_SHORT).show();
                    if(str==test[j--])
                    {
                        f=1;
                    }

                }*/


                selectedItems.add(str);
                Toast.makeText(this, myAutoComplete.getText().toString().trim() + " added", Toast.LENGTH_SHORT).show();
                myAutoComplete.setText("");


                outputStrArr = new String[selectedItems.size()];
                if (flag == 0) {
                    proceed.setVisibility(View.VISIBLE);
                    flag = 1;

                }


                for (int i = 0; i < selectedItems.size(); i++) {
                    outputStrArr[i] = selectedItems.get(i);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, outputStrArr);
                lv.setAdapter(adapter);


            }
        }
        if(v == proceed) {


            outputStrArr = new String[selectedItems.size()];

            for (int i = 0; i < selectedItems.size(); i++) {
                outputStrArr[i] = selectedItems.get(i);
            }

            //    Intent intent = new Intent(getApplicationContext(),
            //          Confirm_Tag.class);

            // Create a bundle object
            Bundle b = new Bundle();
            b.putStringArray("selectedItems", outputStrArr);

           Intent i2=new Intent(this,Confirm.class);
            startActivity(i2);

                // set the toolbar title
                // ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Confirm Tags");
            }
        }


        }

