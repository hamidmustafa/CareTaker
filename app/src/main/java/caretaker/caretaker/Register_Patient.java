package caretaker.caretaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import caretaker.caretaker.Utilities.ConnectionDetector;
import caretaker.caretaker.service.MyFirebaseInstanceIdService;

public class Register_Patient extends AppCompatActivity {
    private Button RegisterButton;
    private EditText FullName_EditText;
    private EditText Email_EditText;
    private EditText Password_EditText;
    private EditText Phone_EditText;
    private EditText Country_EditText;
    private RadioGroup RadioSexGroup;
    private RadioButton RadioSexButton;
    private TextView AlreadyRegistered_TextView;
    private ConnectionDetector conn_detect;



    private static String FullName;
    private static String Email;
    private static String Password;
    private static String Phone;
    private static String Country;
    private static String CheckedRadioButtonValue;
    private ProgressDialog progressDialog;
    public static String URL_Registration = "http://www.mspstracker.com/caretaker/upload_patient_reg.php";
    private static boolean isInternetConnected;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static String ttoken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__patient);
        conn_detect=new ConnectionDetector(getApplicationContext());

        RegisterButton = (Button) findViewById(R.id.btn_registeredemail);
        FullName_EditText = (EditText) findViewById(R.id.editText_fullname);
        Email_EditText = (EditText) findViewById(R.id.editText_registeredemail);
        Password_EditText = (EditText) findViewById(R.id.editText_password);
        Phone_EditText = (EditText) findViewById(R.id.editText_phone);
        Country_EditText = (EditText) findViewById(R.id.editText_country);
        RadioSexGroup = (RadioGroup) findViewById(R.id.radiogroup_sex);

        AlreadyRegistered_TextView=(TextView) findViewById(R.id.textView_alreadyregistered);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        //getting device registration token
        MyFirebaseInstanceIdService myFirebaseInstanceIdService=new MyFirebaseInstanceIdService();
         ttoken=  myFirebaseInstanceIdService.token();

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isInternetConnected=conn_detect.isConnectingToInternet();
                if(isInternetConnected==true) {


                    if (FullName_EditText.getText().toString().equals("")
                            || Email_EditText.getText().toString().equals("")
                            || Password_EditText.getText().toString().equals("")
                            || Phone_EditText.getText().toString().equals("")
                            || Country_EditText.getText().toString().equals("")

                            ) {
                        Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
                    }

                    else if(Email_EditText.getText().toString().matches(emailPattern))
                    {

                        FullName = FullName_EditText.getText().toString();
                        Email = Email_EditText.getText().toString();
                        Password = Password_EditText.getText().toString();
                        Phone = Phone_EditText.getText().toString();
                        Country = Country_EditText.getText().toString();
                        int checkedbuttonid = RadioSexGroup.getCheckedRadioButtonId();
                        RadioSexButton = (RadioButton) findViewById(checkedbuttonid);
                        CheckedRadioButtonValue = RadioSexButton.getText().toString();
                        registerUser(); }

                    else {


                        Toast.makeText(getApplicationContext(), "Email address is not a valid email address", Toast.LENGTH_SHORT).show();


                    }


                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlreadyRegistered_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Register_Patient.this, Login_Patient.class);
                startActivity(intent);
            }
        });
    }
    private void registerUser() {
        // Tag used to cancel the request
        String tag_json_obj = "jason_obj_req";
        progressDialog.setMessage("Registering as a Patient...");
        progressDialog.show();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("Registration", "Register response " + response);
                progressDialog.dismiss();
                FullName_EditText.setText("");
                Email_EditText.setText("");
                Password_EditText.setText("");
                Phone_EditText.setText("");
                Country_EditText.setText("");
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean error=jsonObject.getBoolean("error");
                    if(error)
                    {


                        String error1=jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                error1, Toast.LENGTH_LONG).show();


                    }

                    else
                    {  String error1=jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                error1, Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(Register_Patient.this,Login_Patient.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error response " +error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();

                params.put("firebase_regId", ttoken);
                params.put("name", FullName);
                params.put("email", Email);
                params.put("password", Password);
                params.put("phone", Phone);
                params.put("country", Country);
                params.put("checkedradiobuttonvalue", CheckedRadioButtonValue);


                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");





    }
}
