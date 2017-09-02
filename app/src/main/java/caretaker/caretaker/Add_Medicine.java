package caretaker.caretaker;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.*;
import java.util.*;
import java.util.jar.Attributes;

import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.widget.*;
import android.view.View;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Add_Medicine extends AppCompatActivity {
    Button Button_SetTime;
    public static String gethour;
    public static String getminutes;
    EditText Medicine_Name;
    EditText Times_A_Day;
    EditText Medicine_Doctor;
    Button Button_Add_Medicine;
    ProgressDialog progressDialog;
   private static String MedicineName;
    private  static String TimesADay;
    private static String MedicineDoctor;
    public static String URL_Registration = "http://www.mspstracker.com/caretaker/add_medicine_upload.php";
    public static String URL_Registration_GettingName = "http://www.mspstracker.com/caretaker/getting_name.php";


    static TextView time;
    public  static String emailfromdata;
    public static String Name_Retrieved;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__medicine);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
       // startService(new Intent(getBaseContext(), Service_Data.class));
        /* Calendar calendar=Calendar.getInstance();
        int hours= calendar.get(Calendar.HOUR_OF_DAY);
        int minutes=calendar.get(Calendar.MINUTE);*/

time= (TextView)findViewById(R.id.textView_time);
        //time.setText(String.valueOf(hours)+":"+String.valueOf(minutes));
        Medicine_Name=(EditText)findViewById(R.id.editText_medicine_name);
        Medicine_Doctor=(EditText)findViewById(R.id.editText_medicine_doctor);

        Button_Add_Medicine=(Button)findViewById(R.id.btn_add_medicine);
        Button_Add_Medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicineName=Medicine_Name.getText().toString();
                MedicineDoctor=Medicine_Doctor.getText().toString();
                if(MedicineName.matches("")  || MedicineDoctor.matches(""))
                {
                    Toast.makeText(Add_Medicine.this, "You cannot leave the field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {getName();}


            }
        });




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
public void timepickercall(View v)
{
    DialogFragment newFragment = new TimePickerFragment();

    newFragment.show(getFragmentManager(), "timePicker");

}

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {


        @Override

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker

            final Calendar c = Calendar.getInstance();

            int hour = c.get(Calendar.HOUR_OF_DAY);

            int minute = c.get(Calendar.MINUTE);


            // Create a new instance of TimePickerDialog and return it

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));

        }


        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            gethour=String.valueOf(hourOfDay);
            getminutes=String.valueOf(minute);
           time.setText(gethour+":"+getminutes);





        }


    }


    public void AddMedicine()
    {   // Tag used to cancel the request

      // getName();
        String tag_json_obj = "jason_obj_req";
        progressDialog.setMessage("Adding medicine and schedule...");
        progressDialog.show();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

               // Log.d("Registration", "Register response " + response);
                progressDialog.dismiss();
                Medicine_Name.setText("");
                Medicine_Doctor.setText("");

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

                params.put("name", Name_Retrieved);
                params.put("medicine_name", MedicineName);
                params.put("medicine_doctor", "Dr. "+MedicineDoctor);
                params.put("hours",gethour);
                params.put("minutes", getminutes);






                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");
    }




    public void getName()
    {   // Tag used to cancel the request

        DBHelper.getCursorData_Patient();
        DBHelper.LoopingData_Patient();
        int sizeOfarray=DBHelper.ArrayList_Email_Patient.size();
        if(sizeOfarray>0) {
            emailfromdata = DBHelper.ArrayList_Email_Patient.get(0);
        }

        else
        {
            Toast.makeText(Add_Medicine.this, "no data found", Toast.LENGTH_SHORT).show();
        }

        String tag_json_obj = "jason_obj_req";
        progressDialog.setMessage("Wait...");
        progressDialog.show();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration_GettingName, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                // Log.d("Registration", "Register response " + response);
                progressDialog.dismiss();
                Medicine_Name.setText("");
                Medicine_Doctor.setText("");



                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                     Name_Retrieved=jsonArray.getString(0);
                    Toast.makeText(Add_Medicine.this, "Name Received: "+Name_Retrieved, Toast.LENGTH_SHORT).show();
                    AddMedicine();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

                params.put("email", emailfromdata);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");}



}
