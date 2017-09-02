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

public class Reports_ToPatient_List extends AppCompatActivity implements ListAdapter_ForPatientReports.customButtonListener {
    private ListView ListView_PatientsReports;
    ListAdapter_ForPatientReports adapter;
    public static ArrayList<String>  ArrayList_Patient_Name_=null;
    public static ArrayList<String>  ArrayList_Patient_Email_=null;
    public static ArrayList<String>  ArrayList_Combine_=null;
    public static String URL_Registration_PatientList = "http://www.mspstracker.com/caretaker/list_patient_data.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports__to_patient__list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList_Patient_Name_=new ArrayList<String>();
       ArrayList_Patient_Email_=new ArrayList<String>();
        ArrayList_Combine_=new ArrayList<String>();



        ListView_PatientsReports = (ListView) findViewById(R.id.listView_patientReports);


        getPatientList();




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
    public void onButtonClickListner(int position, String value) {

        String Patient_Name=ArrayList_Patient_Name_.get(position);
        Intent intent =new Intent(Reports_ToPatient_List.this,Individual_Patient_Report.class);
        intent.putExtra("patient_name",Patient_Name);
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
                  //  boolean error=jsonObject.getBoolean("error");
                   // String error1=jsonObject.getString("error_msg");

                    JSONArray jsonArray_patient_name=jsonObject.getJSONArray("patient_name");
                    JSONArray jsonArray_patient_email=jsonObject.getJSONArray("patient_email");







                         for (int a = 0; a < jsonArray_patient_name.length(); a++) {
                             String dummy_patient_name = jsonArray_patient_name.getString(a);
                             String dummy_patient_email = jsonArray_patient_email.getString(a);


                             ArrayList_Patient_Name_.add(dummy_patient_name);
                             ArrayList_Patient_Email_.add(dummy_patient_email);

                             ArrayList_Combine_.add(ArrayList_Patient_Name_.get(a) + "\n" + ArrayList_Patient_Email_.get(a));


                         }

                    adapter = new ListAdapter_ForPatientReports(Reports_ToPatient_List.this, ArrayList_Combine_);
                    adapter.setCustomButtonListner(Reports_ToPatient_List.this);

                    ListView_PatientsReports.setAdapter(adapter);



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
