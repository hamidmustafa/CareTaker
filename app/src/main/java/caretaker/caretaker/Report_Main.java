package caretaker.caretaker;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Report_Main extends AppCompatActivity {
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
    public static String URL_Registration = "http://www.mspstracker.com/caretaker/get_report_data.php";
    public static String URL_Registration_GettingName = "http://www.mspstracker.com/caretaker/getting_name.php";
    public TextView ReportTextView;
    public TextView PatientNameTextView;

    private Button  Button_GenerateReport;
    public ArrayList<String> ArrayList_Patient_Name_;
    public ArrayList<String> ArrayList_Medicine_Name_;
    public ArrayList<String> ArrayList_Medicine_Doctor_;
    public ArrayList<String> ArrayList_Hours_;
    public ArrayList<String> ArrayList_Minutes_;
    public ArrayList<String> ArrayList_Day_;
    public ArrayList<String> ArrayList_Month_;
    public ArrayList<String> ArrayList_Year_;
    public ArrayList<String> ArrayList_YesNo_;










    static TextView time;
    public static String emailfromdata;
    public static String Name_Retrieved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList_Patient_Name_=new ArrayList<String>();
        ArrayList_Medicine_Name_=new ArrayList<String>();
        ArrayList_Medicine_Doctor_=new ArrayList<String>();
        ArrayList_Hours_=new ArrayList<String>();
        ArrayList_Minutes_=new ArrayList<String>();
        ArrayList_Day_=new ArrayList<String>();
        ArrayList_Month_=new ArrayList<String>();
        ArrayList_Year_=new ArrayList<String>();
        ArrayList_YesNo_=new ArrayList<String>();

        progressDialog=new ProgressDialog(Report_Main.this);
        ReportTextView=(TextView)findViewById(R.id.textView_Report);
        PatientNameTextView=(TextView)findViewById(R.id.textView_NameOfPatient);

        ReportTextView.setMovementMethod(new ScrollingMovementMethod());
        Button_GenerateReport=(Button)findViewById(R.id.btn_patient_report);
        Button_GenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportTextView.setTextColor(Color.parseColor("#696969"));
                ReportTextView.setText("");
              getName();

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

    public void ReportData()
    {   // Tag used to cancel the request


        String tag_json_obj = "jason_obj_req";
        progressDialog.setMessage("Generating "+ Name_Retrieved+"'s" + " Report...");
        progressDialog.show();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();



                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray_patient_name=jsonObject.getJSONArray("patient_name");
                    JSONArray jsonArray_medicine_name=jsonObject.getJSONArray("medicine_name");
                    JSONArray jsonArray_medicine_doctor=jsonObject.getJSONArray("medicine_doctor");
                    JSONArray jsonArray_hours=jsonObject.getJSONArray("hours");
                    JSONArray jsonArray_minutes=jsonObject.getJSONArray("minutes");
                    JSONArray jsonArray_day=jsonObject.getJSONArray("day");
                    JSONArray jsonArraye_month=jsonObject.getJSONArray("month");
                    JSONArray jsonArray_year=jsonObject.getJSONArray("year");
                    JSONArray jsonArray_yesno=jsonObject.getJSONArray("yesno");

                    for(int a=0; a<jsonArray_patient_name.length();a++)
                       {
                           String dummy_patient_name=jsonArray_patient_name.getString(a);
                           String dummy_medicine_name=jsonArray_medicine_name.getString(a);
                           String dummy_medicine_doctor=jsonArray_medicine_doctor.getString(a);
                           String dummy_hours=jsonArray_hours.getString(a);
                           String dummy_minutes=jsonArray_minutes.getString(a);
                           String dummy_day=jsonArray_day.getString(a);
                           String dummy_month=jsonArraye_month.getString(a);
                           String dummy_year=jsonArray_year.getString(a);
                           String dummy_yesno=jsonArray_yesno.getString(a);

                           ArrayList_Patient_Name_.add(dummy_patient_name);
                           ArrayList_Medicine_Name_.add(dummy_medicine_name);
                           ArrayList_Medicine_Doctor_.add(dummy_medicine_doctor);
                           ArrayList_Hours_.add(dummy_hours);
                           ArrayList_Minutes_.add(dummy_minutes);
                           ArrayList_Day_.add(dummy_day);
                           ArrayList_Month_.add(dummy_month);
                           ArrayList_Year_.add(dummy_year);
                           ArrayList_YesNo_.add(dummy_yesno);

                           ReportTextView.append("Medicine Name: "+ArrayList_Medicine_Name_.get(a)+"\n"+"Medicine Doctor: "+ArrayList_Medicine_Doctor_.get(a)+"\n"
                           +"Taking time: "+ArrayList_Hours_.get(a)+":"+ArrayList_Minutes_.get(a)+"\n"+
                             "Medicine status: "+ArrayList_YesNo_.get(a)+"\n"+
                              "Taken/Skipped Date: "+ArrayList_Day_.get(a)+"-"+ArrayList_Month_.get(a)+"-"+ArrayList_Year_.get(a)
                           );
                           ReportTextView.append("\n\n");
                       }

                    if(ArrayList_Patient_Name_.size()==0)
                    {  Toast.makeText(getApplicationContext(), "Sorry! no record found", Toast.LENGTH_SHORT).show();
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







                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");}




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
            Toast.makeText(Report_Main.this, "Sorry patient db empty", Toast.LENGTH_SHORT).show();
        }

        String tag_json_obj = "jason_obj_req";




        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration_GettingName, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                // Log.d("Registration", "Register response " + response);




                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                    Name_Retrieved=jsonArray.getString(0);
                   Toast.makeText(Report_Main.this, "Received Name: "+ Name_Retrieved, Toast.LENGTH_SHORT).show();
                    PatientNameTextView.setText(Name_Retrieved+"'s"+" " +"Medicine Report");

                    ReportData();


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
