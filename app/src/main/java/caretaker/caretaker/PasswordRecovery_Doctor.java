package caretaker.caretaker;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordRecovery_Doctor extends AppCompatActivity {
    private static EditText RegisteredEmail_Text;
    private Button Registered_Email_Button;
    private ProgressDialog progressDialog;
    private static String URL_Registration = "http://www.mspstracker.com/caretaker/passwordrecovery_doctor.php";
    private String RegisteredEmail;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery__doctor);
        RegisteredEmail_Text=(EditText)findViewById(R.id.editText_registeredemail);
        Registered_Email_Button=(Button)findViewById(R.id.btn_registeredemail);
        progressDialog=new ProgressDialog(this);


        Registered_Email_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisteredEmail=RegisteredEmail_Text.getText().toString();
                if(RegisteredEmail.matches(""))
                {
                    Toast.makeText(PasswordRecovery_Doctor.this, "Field can not be empty", Toast.LENGTH_SHORT).show();
                }

                else if(RegisteredEmail.matches(emailPattern))
                {

                    SendRequest_ReceiveReponse();
                }

                else
                {  Toast.makeText(getApplicationContext(), "Email address is not a valid email address", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private void SendRequest_ReceiveReponse() {
        // Tag used to cancel the request
        String tag_json_obj = "jason_obj_req";
        progressDialog.setMessage("Wait...");
        progressDialog.show();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                RegisteredEmail_Text.setText("");

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    boolean error=jsonObject.getBoolean("error");
                    if(!error)
                    {


                        String error1=jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                error1, Toast.LENGTH_LONG).show();


                    }

                    else
                    {  String error1=jsonObject.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                error1, Toast.LENGTH_LONG).show();

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

                params.put("registeredemail", RegisteredEmail);



                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");





    }
    }

