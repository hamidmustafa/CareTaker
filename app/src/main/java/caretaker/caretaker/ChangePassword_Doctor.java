package caretaker.caretaker;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

public class ChangePassword_Doctor extends AppCompatActivity {
    private EditText CurrentPassword_Text;
    private EditText NewPassword_Text;
    private EditText ConfirmNewPassword_Text;
    private Button UpdatePassword_Button;
    private static String CurrentPassword;
    private static String NewPassword;
    private static String ConfirmNewPassword;
    private ProgressDialog progressDialog;
    public static String URL_Registration = "http://www.mspstracker.com/caretaker/update_password_doctor.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password__doctor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CurrentPassword_Text=(EditText)findViewById(R.id.editText_currentpassword);
        NewPassword_Text=(EditText)findViewById(R.id.editText_newpassword);
        ConfirmNewPassword_Text=(EditText)findViewById(R.id.editText_confirm_newpassword);
        UpdatePassword_Button=(Button)findViewById(R.id.btn_updatepassword);
        progressDialog=new ProgressDialog(this);
        UpdatePassword_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentPassword=CurrentPassword_Text.getText().toString();
                NewPassword=NewPassword_Text.getText().toString();
                ConfirmNewPassword=ConfirmNewPassword_Text.getText().toString();

                if (CurrentPassword.equals("")
                        || NewPassword.equals("")
                        || ConfirmNewPassword.equals("")
                        )
                {     Toast.makeText(getApplicationContext(), "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
                else if(NewPassword.matches(ConfirmNewPassword))
                {
                    SendRequest_ReceiveReponse();
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "New password and Confirmed new passsword do not match", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void SendRequest_ReceiveReponse() {
        // Tag used to cancel the request
        String tag_json_obj = "jason_obj_req";
        progressDialog.setMessage("Updating new password...");
        progressDialog.show();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                CurrentPassword_Text.setText("");
                NewPassword_Text.setText("");
                ConfirmNewPassword_Text.setText("");

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

                params.put("currentpassword", CurrentPassword);
                params.put("newpassword", NewPassword);




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
}
