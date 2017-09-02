package caretaker.caretaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Appointment_ToDoctor_List extends AppCompatActivity implements ListAdapter_ForAppointment.customButtonListener {
    private ListView ListView_Appointments;
     ListAdapter_ForAppointment adapter;
    public static ArrayList<String>  ArrayList_Doctor_Name_=null;
    public static ArrayList<String>  ArrayList_Doctor_Email_=null;
    public static ArrayList<String>  ArrayList_Combine_=null;
    public static String URL_Registration_DoctorList = "http://www.mspstracker.com/caretaker/list_doctor_data.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment__to_doctor__list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        stopService(new Intent(this,Chat_Service.class));

          ArrayList_Doctor_Name_=new ArrayList<String>();
          ArrayList_Doctor_Email_=new ArrayList<String>();
         ArrayList_Combine_=new ArrayList<String>();
         ListView_Appointments = (ListView) findViewById(R.id.listView_Appointments);




        getDoctorList();





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


       // String Patient_Name=ArrayList_Doctor_Name_.get(position);
       // Toast.makeText(getApplicationContext(), Patient_Name, Toast.LENGTH_SHORT).show();




         String Name_Receiver=ArrayList_Doctor_Name_.get(position);
        String Email_Receiver=ArrayList_Doctor_Email_.get(position);

        DBHelper.getCursorData_Patient();
        DBHelper.LoopingData_Patient();
        String Name_Sender=DBHelper.ArrayList_Name_Patient.get(0);
        String Email_Sender=DBHelper.ArrayList_Email_Patient.get(0);


        Intent intent=new Intent(Appointment_ToDoctor_List.this, Schedule_Appointment.class);
        intent.putExtra("name_receiver",Name_Receiver);
        intent.putExtra("email_receiver",Email_Receiver);
        intent.putExtra("name_sender",Name_Sender);
        intent.putExtra("email_sender",Email_Sender);
        intent.putExtra("type","patient");
        intent.putExtra("appointment","appointment");

        startActivity(intent);

    }
    public void getDoctorList()
    {   // Tag used to cancel the request


        String tag_json_obj = "jason_obj_req";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration_DoctorList, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {




                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray_doctor_name=jsonObject.getJSONArray("doctor_name");
                    JSONArray jsonArray_doctor_email=jsonObject.getJSONArray("doctor_email");

                    for (int a = 0; a < jsonArray_doctor_name.length(); a++) {
                        String dummy_doctor_name = jsonArray_doctor_name.getString(a);
                        String dummy_doctor_email = jsonArray_doctor_email.getString(a);


                        ArrayList_Doctor_Name_.add(dummy_doctor_name);
                        ArrayList_Doctor_Email_.add(dummy_doctor_email);

                        ArrayList_Combine_.add(ArrayList_Doctor_Name_.get(a) + "\n" + ArrayList_Doctor_Email_.get(a));


                    }


                    adapter = new ListAdapter_ForAppointment(Appointment_ToDoctor_List.this, ArrayList_Combine_);
                    adapter.setCustomButtonListner(Appointment_ToDoctor_List.this);
                    ListView_Appointments.setAdapter(adapter);


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
                params.put("doctors","list");


                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");}
}
