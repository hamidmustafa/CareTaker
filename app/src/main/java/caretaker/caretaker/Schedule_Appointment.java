package caretaker.caretaker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Schedule_Appointment extends AppCompatActivity implements DatePicker.OnDateChangedListener{
    static TextView time;
    public static String gethour;
    public static String getminutes;
    private static String Name_Receiver;
    private static String Email_Receiver;
    private static String Name_Sender;
    private static String Email_Sender;
    private static String Type;

    public DatePicker datePicker_appointment;
    public static int day;
    public static  int month;
    public static int year1;

    public static int day_;
    public static  int month_;
    public static int year_;
    public static String Request_Date;
    public static String Appointment_Date;
    public static String Appointment_Time;
    private ProgressDialog progressDialog;

    private  String URL_Registration = "http://mspstracker.com/caretaker/firebasefiles_caretaker/index_text_appointment.php";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule__appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);

        Calendar calendar=Calendar.getInstance();
        time= (TextView)findViewById(R.id.textView_time);
        Button Button_SendRequest=(Button)findViewById(R.id.btn_request_appointment);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        Request_Date = df.format(calendar.getTime());

        time.setText(Request_Date);


        Bundle bundle=getIntent().getExtras();
        Name_Receiver= bundle.getString("name_receiver");
        Email_Receiver=bundle.getString("email_receiver");
        Name_Sender= bundle.getString("name_sender");
        Email_Sender=bundle.getString("email_sender");
        Type=bundle.getString("type");


        getSupportActionBar().setTitle("Appointment to "+Name_Receiver);

        datePicker_appointment=(DatePicker)findViewById(R.id.datePicker_appointment);
        datePicker_appointment.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),  this);
        day=datePicker_appointment.getDayOfMonth();
        month=datePicker_appointment.getMonth()+1;
        year1=datePicker_appointment.getYear();

        Button_SendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
;
                Toast.makeText(getApplicationContext(), Name_Receiver+":"+
                        Email_Receiver+":"+Name_Sender+":"+Email_Sender, Toast.LENGTH_SHORT).show();
                StoreAppointmentData();

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

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {

        day_ = day;
        month_ = month + 1;
        year_ = year;

        Appointment_Date=day_+":"+month_+":"+year_;



 time.setText(Appointment_Date);


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
            Appointment_Time=gethour+":"+getminutes;
            time.setText(Appointment_Time);





        }


    }

    private void StoreAppointmentData() {

        progressDialog.setMessage("Sending appointment...");
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();


                // ChatListview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                // ChatListview.setStackFromBottom(true);
              //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);


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

                params.put("name_receiver", Name_Receiver);
                params.put("email_receiver", Email_Receiver);
                params.put("name_sender", Name_Sender);
                params.put("email_sender", Email_Sender);
                params.put("request_date", Request_Date);
                params.put("appointment_date", Appointment_Date);
                params.put("appointment", "appointment");







                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, "req_register");

    }
}
