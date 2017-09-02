package caretaker.caretaker;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import caretaker.caretaker.Utilities.Sessions;
import caretaker.caretaker.service.MyFirebaseInstanceIdService;

public class Login_Patient extends AppCompatActivity {
    private TextView Patient_TextView;
    private Button Patient_Login_Button;
    private ProgressDialog progressDialog;
    public Sessions sessions;
    private EditText Email;
    private EditText Password;
    public static String EmailFromField;
    public static   String PasswordFromField;
    public static String URL_Registration = "http://www.mspstracker.com/caretaker/login_patient.php";
    public static String URL_Registration_Update = "http://www.mspstracker.com/caretaker/update_regtoken_patient.php";

    private JSONObject jsonObject;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Button GoToMain;
    private TextView ForgotPassword_TextView;
    private static String ttoken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__patient);
     // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Patient_TextView=(TextView)findViewById(R.id.textView_RegisterPatient);
        ForgotPassword_TextView=(TextView) findViewById(R.id.textView_ForgotPassword);
        Patient_Login_Button=(Button)findViewById(R.id.btn_login_patient);
        Email=(EditText)findViewById(R.id.editText_registeredemail) ;
        Password=(EditText)findViewById(R.id.editText_password) ;
        GoToMain=(Button)findViewById(R.id.btn_gotomain);

        //getting device registration token
        MyFirebaseInstanceIdService myFirebaseInstanceIdService=new MyFirebaseInstanceIdService();
         ttoken=  myFirebaseInstanceIdService.token();


        GoToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent=new Intent(Login_Patient.this, MainActivity_CareTaker.class);
                startActivity(intent);
            }
        });



        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        sessions=new Sessions(getApplicationContext());

        if(sessions.isLoggedIn())
        {
            DBHelper.getCursorData_Patient();
            DBHelper.LoopingData_Patient();
            String emailfromdata=DBHelper.ArrayList_Email_Patient.get(0);
            Intent intent= new Intent(getApplicationContext(), Patient_Dashboard.class);
            intent.putExtra("email",emailfromdata);
            startActivity(intent);
            finish();

        }
        ForgotPassword_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login_Patient.this,PasswordRecovery_Patient.class);
                startActivity(intent);
            }
        });

        Patient_Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EmailFromField=Email.getText().toString();
                PasswordFromField=Password.getText().toString();

                if (EmailFromField.matches("") && PasswordFromField.matches("")) {
                    Toast.makeText(getApplicationContext(),
                            "email or password fields are empty", Toast.LENGTH_LONG)
                            .show();
                }


                else if(EmailFromField.matches(emailPattern)) {

                    checkLogin(EmailFromField,PasswordFromField);

                }

                else {Toast.makeText(getApplicationContext(),
                        "This is not a valid email address", Toast.LENGTH_LONG)
                        .show();}


                /*Intent intent= new Intent(Login_Doctor.this, Docter_Dashboard.class);
                startActivity(intent);*/
            }
        });

        Patient_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), Register_Patient.class);
                startActivity(intent);
            }
        });




    }
    private void checkLogin(final String email , final String password) {
        String tag_string_req = "req_login";
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("Registration", "Register response " + response);
                progressDialog.dismiss();

                try {
                    JSONObject   jsonObject = new JSONObject(response);
                    JSONArray jsonArray_patient_name=jsonObject.getJSONArray("name_patient");
                    //getting doctor name from server
                    String dummy_patient_name = jsonArray_patient_name.getString(0);

                    String YESNO=jsonArray_patient_name.getString(1);
                    String Message__=jsonArray_patient_name.getString(2);

                    Toast.makeText(getApplicationContext(), "name patient: "+ dummy_patient_name, Toast.LENGTH_SHORT).show();

                    if(YESNO.matches("YES"))
                    {
                        sessions.setLogin(true);
                        startService(new Intent(getBaseContext(), Service_Data.class));
                        ContentValues contentValues=new ContentValues();
                        contentValues.put(DBHelper.FIELD_EmailPatient, EmailFromField);
                        contentValues.put(DBHelper.FIELD_NamePatient, dummy_patient_name);
                        getContentResolver().insert(ContentProvider_Patient.CONTENT_URI_Patient,contentValues);

                        Intent intent = new Intent(getApplicationContext(),
                                Patient_Dashboard.class);
                       intent.putExtra("email", EmailFromField);
                        startActivity(intent);
                        finish();

                        //updating device registration token
                        Update_DeviceRegToken();

                    }

                     if(YESNO.matches("NO"))

                    {
                        Toast.makeText(getApplicationContext(),
                                Message__, Toast.LENGTH_LONG).show();

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error response " + error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("password", password);



                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");
    }


    public void Update_DeviceRegToken()
    {
        final String getemail=Email.getText().toString();

        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration_Update, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("Response", "Token Updating Response " + response);

                try {
                    JSONObject   jsonObject = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error response " + error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();

                params.put("email", getemail);
                params.put("firebase_regId",ttoken);



                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");





    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {

        super.onBackPressed();
        this.finish();

    }
}
