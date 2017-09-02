package caretaker.caretaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import caretaker.caretaker.Utilities.Sessions;

public class Patient_Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public Sessions sessions1;
    public WebView webView1;
    TextView Nav_Patient_Name;
    TextView Nav_Patient_Email;
    private static String Passed_Email;
    ProgressDialog progressDialog;
    public static String URL_Registration = "http://www.mspstracker.com/caretaker/fetch_patient_data.php";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startService(new Intent(this, Chat_Service.class));
        startService(new Intent(this, appointment_service.class));


        progressDialog=new ProgressDialog(Patient_Dashboard.this);
        sessions1=new Sessions(getApplicationContext());
        webView1=(WebView)findViewById(R.id.webview_browser_patientnews);
        webView1.loadUrl("http://healthy-tips-for-a-healthy-lifestyle.blogspot.com/");
        webView1.getSettings().setBuiltInZoomControls(true);
        webView1.getSettings().setDisplayZoomControls(false);
        webView1.setWebViewClient(new WebViewController());


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view=navigationView.getHeaderView(0);
        Nav_Patient_Name=(TextView)view.findViewById(R.id.textView_nav_name);
        Nav_Patient_Email=(TextView)view.findViewById(R.id.textView_nav_email);



       Bundle bundle=getIntent().getExtras();
        Passed_Email=bundle.getString("email");



        retrieveData();





    }

    public void retrieveData()
    {

        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("Registration", "Register response " + response);
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");

                      String Name=jsonArray.getString(0);
                      String Email=jsonArray.getString(1);

                    Nav_Patient_Email.setText(Email);
                    Nav_Patient_Name.setText(Name);





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
// Replace Passed_Email string below in put arugments
                params.put("email", Passed_Email);

                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");




    }
    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient__dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addmedicine) {
            Intent intent=new Intent(Patient_Dashboard.this, Add_Medicine.class);
            startActivity(intent);
        } else if (id == R.id.nav_Generate_Report) {
            Intent intent=new Intent(Patient_Dashboard.this, Report_Main.class);
            startActivity(intent);

        } else if (id == R.id.nav_changepassword) {
            Intent intent=new Intent(Patient_Dashboard.this,ChangePassword_Patient.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_appointments) {
            Intent intent=new Intent(Patient_Dashboard.this,Appointment_ToDoctor_List.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_chat) {
            Intent intent=new Intent(Patient_Dashboard.this,Chat_ToDoctor_List.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_logout) {


            sessions1.setLogin(false);
            stopService(new Intent(Patient_Dashboard.this, Service_Data.class));
            getContentResolver().delete(ContentProvider_Patient.CONTENT_URI_Patient,null,null);
            Intent intent =new Intent(Patient_Dashboard.this, Login_Patient.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
