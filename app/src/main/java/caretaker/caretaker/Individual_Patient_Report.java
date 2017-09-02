package caretaker.caretaker;

import android.app.ProgressDialog;
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

import static java.lang.Boolean.FALSE;

public class Individual_Patient_Report extends AppCompatActivity {
    public static String Patient_Name_FromExtra;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual__patient__report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle=getIntent().getExtras();
         Patient_Name_FromExtra=bundle.getString("patient_name");

        ArrayList_Patient_Name_=new ArrayList<String>();
        ArrayList_Medicine_Name_=new ArrayList<String>();
        ArrayList_Medicine_Doctor_=new ArrayList<String>();
        ArrayList_Hours_=new ArrayList<String>();
        ArrayList_Minutes_=new ArrayList<String>();
        ArrayList_Day_=new ArrayList<String>();
        ArrayList_Month_=new ArrayList<String>();
        ArrayList_Year_=new ArrayList<String>();
        ArrayList_YesNo_=new ArrayList<String>();

        progressDialog=new ProgressDialog(Individual_Patient_Report.this);
        ReportTextView=(TextView)findViewById(R.id.textView_Report);
        PatientNameTextView=(TextView)findViewById(R.id.textView_NameOfPatient);

        ReportTextView.setMovementMethod(new ScrollingMovementMethod());
        Button_GenerateReport=(Button)findViewById(R.id.btn_patient_report);
        Button_GenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportTextView.setTextColor(Color.parseColor("#696969"));
                ReportTextView.setText("");
                PatientNameTextView.setText(Patient_Name_FromExtra+"'s"+" " +"Medicine Report");

                ReportData();


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
        progressDialog.setMessage("Generating "+ Patient_Name_FromExtra+"'s" + " Report...");
        progressDialog.show();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();



                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error=FALSE;
                   // error=jsonObject.getBoolean("error");
                   // String error1=jsonObject.getString("error_msg");


                        JSONArray jsonArray_patient_name = jsonObject.getJSONArray("patient_name");
                        JSONArray jsonArray_medicine_name = jsonObject.getJSONArray("medicine_name");
                        JSONArray jsonArray_medicine_doctor = jsonObject.getJSONArray("medicine_doctor");
                        JSONArray jsonArray_hours = jsonObject.getJSONArray("hours");
                        JSONArray jsonArray_minutes = jsonObject.getJSONArray("minutes");
                        JSONArray jsonArray_day = jsonObject.getJSONArray("day");
                        JSONArray jsonArraye_month = jsonObject.getJSONArray("month");
                        JSONArray jsonArray_year = jsonObject.getJSONArray("year");
                        JSONArray jsonArray_yesno = jsonObject.getJSONArray("yesno");

                        for (int a = 0; a < jsonArray_patient_name.length(); a++) {
                            String dummy_patient_name = jsonArray_patient_name.getString(a);
                            String dummy_medicine_name = jsonArray_medicine_name.getString(a);
                            String dummy_medicine_doctor = jsonArray_medicine_doctor.getString(a);
                            String dummy_hours = jsonArray_hours.getString(a);
                            String dummy_minutes = jsonArray_minutes.getString(a);
                            String dummy_day = jsonArray_day.getString(a);
                            String dummy_month = jsonArraye_month.getString(a);
                            String dummy_year = jsonArray_year.getString(a);
                            String dummy_yesno = jsonArray_yesno.getString(a);

                            ArrayList_Patient_Name_.add(dummy_patient_name);
                            ArrayList_Medicine_Name_.add(dummy_medicine_name);
                            ArrayList_Medicine_Doctor_.add(dummy_medicine_doctor);
                            ArrayList_Hours_.add(dummy_hours);
                            ArrayList_Minutes_.add(dummy_minutes);
                            ArrayList_Day_.add(dummy_day);
                            ArrayList_Month_.add(dummy_month);
                            ArrayList_Year_.add(dummy_year);
                            ArrayList_YesNo_.add(dummy_yesno);

                            ReportTextView.append("Medicine Name: " + ArrayList_Medicine_Name_.get(a) + "\n" + "Medicine Doctor: " + ArrayList_Medicine_Doctor_.get(a) + "\n"
                                    + "Taking time: " + ArrayList_Hours_.get(a) + ":" + ArrayList_Minutes_.get(a) + "\n" +
                                    "Medicine status: " + ArrayList_YesNo_.get(a) + "\n" +
                                    "Taken/Skipped Date: " + ArrayList_Day_.get(a) + "-" + ArrayList_Month_.get(a) + "-" + ArrayList_Year_.get(a)
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

                params.put("name", Patient_Name_FromExtra);







                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");}
}
