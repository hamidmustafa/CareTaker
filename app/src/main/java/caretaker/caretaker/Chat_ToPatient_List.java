package caretaker.caretaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chat_ToPatient_List extends AppCompatActivity implements ListAdapterForPatientChat.customButtonListener{
    private ListView ListView_Patients;
    ListAdapterForPatientChat adapter;
    public static ArrayList<String>  ArrayList_Patient_Name_=null;
    public static ArrayList<String>  ArrayList_Patient_Email_=null;
    public static ArrayList<String>  ArrayList_Combine_=null;
    public static String URL_Registration_PatientList = "http://www.mspstracker.com/caretaker/list_patient_data.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__to_patient__list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList_Patient_Name_=new ArrayList<String>();
        ArrayList_Patient_Email_=new ArrayList<String>();
        ArrayList_Combine_=new ArrayList<String>();

        ListView_Patients = (ListView) findViewById(R.id.listView_chatpatients);

        getPatientList();







    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {

            finish();
            // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onButtonClickListner(int position, String value) {
        String Name_Receiver=ArrayList_Patient_Name_.get(position);
        String Email_Receiver=ArrayList_Patient_Email_.get(position);

        DBHelper.getCursorData_Doctor();
        DBHelper.LoopingData_Doctor();
        String Name_Sender=DBHelper.ArrayList_Name_Doctor.get(0);
        String Email_Sender=DBHelper.ArrayList_Email_Doctor.get(0);


        Intent intent=new Intent(Chat_ToPatient_List.this, ChatScreen.class);
        intent.putExtra("name_receiver",Name_Receiver);
        intent.putExtra("email_receiver",Email_Receiver);
        intent.putExtra("name_sender",Name_Sender);
        intent.putExtra("email_sender",Email_Sender);
        intent.putExtra("type","patient");
        startActivity(intent);

    }
    public void getPatientList()
    {   // Tag used to cancel the request


        String tag_json_obj = "jason_obj_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration_PatientList, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {




                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray_patient_name=jsonObject.getJSONArray("patient_name");
                    JSONArray jsonArray_patient_email=jsonObject.getJSONArray("patient_email");

                    for (int a = 0; a < jsonArray_patient_name.length(); a++) {
                        String dummy_patient_name = jsonArray_patient_name.getString(a);
                        String dummy_patient_email = jsonArray_patient_email.getString(a);


                        ArrayList_Patient_Name_.add(dummy_patient_name);
                        ArrayList_Patient_Email_.add(dummy_patient_email);
                        ArrayList_Combine_.add(ArrayList_Patient_Name_.get(a) + "\n" + ArrayList_Patient_Email_.get(a));


                    }

                    adapter = new ListAdapterForPatientChat(Chat_ToPatient_List.this, ArrayList_Combine_);
                    adapter.setCustomButtonListner(Chat_ToPatient_List.this);
                    ListView_Patients.setAdapter(adapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error response " +error, Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                params.put("patients","list");








                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");}



}
