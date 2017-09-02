package caretaker.caretaker;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.text.SimpleDateFormat;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Medicine_Notification extends AppCompatActivity {
    private ProgressDialog progressDialog;
    public static String URL_Registration = "http://www.mspstracker.com/caretaker/medicine_update.php";
    public static String hours_passed;
    public  static String minutes_passed;
    public static SimpleDateFormat Day;
    public static SimpleDateFormat  Month;
    public static SimpleDateFormat   Year;
    public static String Day_;
    public static String Month_;
    public static String Year_;
    public static  Calendar calendar;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine__notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Button YESButton=(Button)findViewById(R.id.btn_yes);
        final Button NOButton=(Button)findViewById(R.id.btn_no);
        progressDialog=new ProgressDialog(this);



        Bundle bundle=getIntent().getExtras();
        hours_passed=bundle.getString("hours");
        minutes_passed=bundle.getString("minutes");


        calendar=Calendar.getInstance();







        YESButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)

            {
                //final Calendar c = Calendar.getInstance();

                //int hour = c.get(Calendar.HOUR_OF_DAY);

                // int minute = c.get(Calendar.MINUTE);
                Day=new SimpleDateFormat("d");
                Month=new SimpleDateFormat("M");
                Year=new SimpleDateFormat("yyyy");

                 Day_=Day.format(calendar.getTime());  // at which day
                 Month_=Month.format(calendar.getTime());  // in which month
                 Year_=Year.format(calendar.getTime());


                NOButton.setEnabled(false);
                YESRequest();

               YESButton.setEnabled(false);
                /*finish();
                Intent intent=new Intent(Medicine_Notification.this, Add_Medicine.class);
                startActivity(intent);*/


            }
        });
        NOButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // int minute = c.get(Calendar.MINUTE);
                Day=new SimpleDateFormat("d");
                Month=new SimpleDateFormat("M");
                Year=new SimpleDateFormat("yyyy");

                 Day_=Day.format(calendar.getTime());  // at which day
                 Month_=Month.format(calendar.getTime());  // in which month
                 Year_=Year.format(calendar.getTime());

                YESButton.setEnabled(false);
                NORequest();
                NOButton.setEnabled(false);
               /* finish();
                Intent intent=new Intent(Medicine_Notification.this, Add_Medicine.class);
                startActivity(intent);*/




            }
        });




    }

    public void YESRequest()
    {   String tag_string_req = "req_login";
        progressDialog.setMessage("Saving 'YES'...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("Registration", "Register response " + response);
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error=jsonObject.getBoolean("error");
                    String error1=jsonObject.getString("error_msg");
                    if(!error)
                    {                    Toast.makeText(getApplicationContext(), error1, Toast.LENGTH_LONG).show();
                    }


                    // boolean error=jsonObject.getBoolean("error");

                    //if(error==true)
                    //{ }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error response " + error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();

               params.put("hours_passed", hours_passed);
               params.put("minutes_passed", minutes_passed);
               params.put("Received","Taken");
               params.put("day",Day_) ;
                params.put("month",Month_) ;
                params.put("year",Year_) ;





                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");

    }

    public void NORequest()
    {   String tag_string_req = "req_login";
        progressDialog.setMessage("Saving 'NO'...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("Registration", "Register response " + response);
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error=jsonObject.getBoolean("error");
                    String error1=jsonObject.getString("error_msg");



                    if(!error)
                    {
                        Toast.makeText(getApplicationContext(), error1, Toast.LENGTH_LONG).show();
                    }

                    // boolean error=jsonObject.getBoolean("error");

                    //if(error==true)
                    //{ }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error response " + error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();


                params.put("hours_passed", hours_passed);
                params.put("minutes_passed", minutes_passed);
                params.put("Received","Skipped");
                params.put("day",Day_) ;
                params.put("month",Month_) ;
                params.put("year",Year_) ;


                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            this.finish();

            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
           // System.exit(0);;// close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        this.finish();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);

    }
}
