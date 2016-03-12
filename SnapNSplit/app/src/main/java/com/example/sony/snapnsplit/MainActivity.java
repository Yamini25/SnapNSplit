package com.example.sony.snapnsplit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnConnectionFailedListener,ConnectionCallbacks{
    public static final String NAME = "NAME";
    public static final String PHONENO = "PHONENO";
    public static final String EMAIL = "EMAIL";
    public static final String NAME_G = "NAME";
    public static final String PHONENO_G = "PHONENO";
    public static final String EMAIL_G = "EMAIL";
    private EditText editTextName;
    private EditText editTextPhoneno;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextReEnterPassword;
    private EditText editTextIFSC;
    private EditText editTextAccountno;

    private Button buttonRegister;
    private Button buttonLogin;

    GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;
    SignInButton signIn_btn;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;
    private boolean is_intent_inprogress;
    private boolean is_signInBtn_clicked;
    private int request_code;
    ProgressDialog progress_dialog;

    public static int x=0,y=0;
    private static final String REGISTER_URL = "http://snapnsplit.esy.es/register.php";
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buidNewGoogleApiClient();
        //Customize sign-in button.a red button may be displayed when Google+ scopes are requested
        custimizeSignBtn();
        setBtnClickListeners();
        progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Signing in....");

        editTextName = (EditText) findViewById(R.id.editTextName);
        //editTextPhoneno = (EditText) findViewById(R.id.editTextPhoneno);
        // editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
//editTextAccountno= (EditText) findViewById(R.id.editTextAccountNo);
        // editTextIFSC= (EditText) findViewById(R.id.editTextIFSC);
        // editTextReEnterPassword= (EditText) findViewById(R.id.editTextReEnterPassword);
        userEmail = editTextEmail.getText().toString().trim();
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
       // buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister.setOnClickListener(this);
        //buttonLogin.setOnClickListener(this);
    }

    /*
      Set on click Listeners on the sign-in sign-out and disconnect buttons
     */

    private void setBtnClickListeners(){
        // Button listeners
        signIn_btn.setOnClickListener(this);
        //       findViewById(R.id.sign_out_button).setOnClickListener(this);
        //     findViewById(R.id.disconnect_button).setOnClickListener(this);
    }


    protected void onStart() {
        super.onStart();
        google_api_client.connect();
    }

    protected void onStop() {
        super.onStop();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }

    protected void onResume(){
        super.onResume();
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /*
    create and  initialize GoogleApiClient object to use Google Plus Api.
    While initializing the GoogleApiClient object, request the Plus.SCOPE_PLUS_LOGIN scope.
    */

    private void buidNewGoogleApiClient(){

        google_api_client =  new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API,Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    /*
      Customize sign-in button. The sign-in button can be displayed in
      multiple sizes and color schemes. It can also be contextually
      rendered based on the requested scopes. For example. a red button may
      be displayed when Google+ scopes are requested, but a white button
      may be displayed when only basic profile is requested. Try adding the
      Plus.SCOPE_PLUS_LOGIN scope to see the  difference.
    */

    private void custimizeSignBtn(){

        signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn_btn.setSize(SignInButton.SIZE_STANDARD);
        signIn_btn.setScopes(new Scope[]{Plus.SCOPE_PLUS_LOGIN});

    }

    @Override
    public void onClick(View v) {
        if(v== signIn_btn) {

            Toast.makeText(this, "start sign process", Toast.LENGTH_SHORT).show();
            gPlusSignIn();

        }

        if(v==buttonRegister)
        {

            if (isEmailValid(editTextEmail.getText().toString()) == false)
            {
                editTextEmail.setError("Invalid Email Address");
                editTextEmail.setText(null);
            }
            if (editTextName.getText().toString().length() > 0 && editTextEmail.getText().toString().length() > 0)
                registerUser();
            else {
                Toast.makeText(this, "All fields are mandatory",
                        Toast.LENGTH_LONG).show();
            }

        }
       // if(v==buttonLogin)
       // {
        //    Intent i=new Intent(this,ActivityLogin.class);
          //  startActivity(i);
        //}
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    } // end of email matcher

    private void registerUser() {
        String name = editTextName.getText().toString().trim().toLowerCase();
        //String phoneno = editTextPhoneno.getText().toString().trim().toLowerCase();

        //String password = editTextPassword.getText().toString().trim().toLowerCase();

        String email = editTextEmail.getText().toString().trim().toLowerCase();


//String ReEnterPassword=editTextReEnterPassword.getText().toString().trim().toLowerCase();
        //      String AccNo=editTextAccountno.getText().toString().trim().toLowerCase();
        //    String IFSCcode=editTextIFSC.getText().toString().trim().toLowerCase();
        //register(name,phoneno,email);
        //  if(password.equals(ReEnterPassword))
        //{

        //}
        Intent intent = new Intent(this,ConfidentialDetails.class);
        intent.putExtra(NAME,name);
        // intent.putExtra(PHONENO,phoneno);
        intent.putExtra(EMAIL,email);
        y=1;
        startActivity(intent);
    }

    /*
      Will receive the activity result and check which request we are responding to

     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        // Check which request we're responding to
        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (responseCode != RESULT_OK) {
                is_signInBtn_clicked = false;
                progress_dialog.dismiss();

            }

            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        is_signInBtn_clicked = false;
        // Get user's information and set it into the layout
        getProfileInfo();
        // Update the UI after signin
        ///changeUI(true);

    }

    @Override
    public void onConnectionSuspended(int i) {
        google_api_client.connect();
        //changeUI(false);

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            google_api_availability.getErrorDialog(this, result.getErrorCode(),request_code).show();
            return;
        }

        if (!is_intent_inprogress) {

            connection_result = result;

            if (is_signInBtn_clicked) {

                resolveSignInError();
            }
        }

    }



        /*
      Sign-in into the Google + account
     */

    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected","connected");
            is_signInBtn_clicked = true;
            progress_dialog.show();
            resolveSignInError();

        }
    }

    /*
      Method to resolve any signin errors
     */

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }
    }

    /*
     get user's information name, email, profile pic,Date of birth,tag line and about me
     */

    private void getProfileInfo() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
                setPersonalInfo(currentPerson);

            } else {
                Toast.makeText(getApplicationContext(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     set the User information into the views defined in the layout
     */

    private void setPersonalInfo(Person currentPerson){

        String personName = currentPerson.getDisplayName();
        String personPhotoUrl = currentPerson.getImage().getUrl();
        String email_g = Plus.AccountApi.getAccountName(google_api_client);
        //TextView   user_name = (TextView) findViewById(R.id.userName);
        //user_name.setText("Name: "+personName);
        String name=""+personName;
        //TextView gemail_id = (TextView)findViewById(R.id.emailId);
        //gemail_id.setText("Email Id: " +email);
        String email=""+email_g;
        // TextView dob = (TextView)findViewById(R.id.dob);
        //dob.setText("DOB: "+currentPerson.getBirthday());
        //TextView tag_line = (TextView)findViewById(R.id.tag_line);
        //tag_line.setText("Tag Line: " +currentPerson.getTagline());
        //TextView about_me = (TextView)findViewById(R.id.about_me);
        //about_me.setText("About Me: "+currentPerson.getAboutMe());
        //setProfilePic(personPhotoUrl);
        progress_dialog.dismiss();
        Toast.makeText(this, "Person information is shown!", Toast.LENGTH_LONG).show();

        x=1; //for indicating that these are g+ values
        Intent intent = new Intent(this,ConfidentialDetails.class);
        intent.putExtra(NAME_G,name);
        intent.putExtra(EMAIL_G, email);
        startActivity(intent);

    }


}

   /* private void register(String name, String phoneno,String email) {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait",null, true, true);
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
                //data.put("password",params[2]);
                data.put("email",params[3]);
                //data.put("AccNo",params[4]);
                //data.put("IFSCcode",params[5]);
                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name,phoneno,email);
    } */
//}