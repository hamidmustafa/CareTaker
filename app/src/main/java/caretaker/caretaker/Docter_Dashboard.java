package caretaker.caretaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.graphics.Bitmap;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import caretaker.caretaker.Utilities.Sessions;

public class Docter_Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public Sessions sessions1;
    private ProgressDialog progressDialog;
    private ArrayAdapter<String> arrayAdapter;
    private String [] Array1 = {"1", "2","3"};
    public  static RelativeLayout rl;
    public  static LayoutInflater layoutInflater;
    public WebView webView1;
    TextView Nav_Doctor_Name;
    TextView Nav_Doctor_Email;
    private static String Passed_Email;
    public static String URL_Registration = "http://www.mspstracker.com/caretaker/fetch_doctor_data.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docter__dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startService(new Intent(this, Chat_Service.class));
        startService(new Intent(this, appointment_service.class));


        sessions1=new Sessions(getApplicationContext());
        webView1=(WebView)findViewById(R.id.webview_browser_doctornews);
        webView1.loadUrl("http://healthy-tips-for-a-healthy-lifestyle.blogspot.com/");
        webView1.getSettings().setBuiltInZoomControls(true);
        webView1.getSettings().setDisplayZoomControls(false);
        webView1.setWebViewClient(new WebViewController());
        progressDialog=new ProgressDialog(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view=navigationView.getHeaderView(0);
        Nav_Doctor_Name=(TextView)view.findViewById(R.id.textView_nav_name);
        Nav_Doctor_Email=(TextView)view.findViewById(R.id.textView_nav_email);



        Bundle bundle=getIntent().getExtras();
        Passed_Email=bundle.getString("email");
        retrieveDataDoctor();
    }
    public void retrieveDataDoctor()
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

                    Nav_Doctor_Email.setText(Email);
                    Nav_Doctor_Name.setText(Name);





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

        if(webView1.canGoBack())
        {
            webView1.goBack();}
        else {


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.docter__dashboard, menu);
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

       if (id == R.id.nav_changepassword) {

            Intent intent=new Intent(Docter_Dashboard.this,ChangePassword_Doctor.class);
            startActivity(intent);

        } else if (id == R.id.nav_ManageAppointments) {




        }
       else if (id == R.id.nav_chatDoctor) {

           Intent intent=new Intent(Docter_Dashboard.this,Chat_ToPatient_List.class);
           startActivity(intent);


       }
       else if (id == R.id.nav_PatientReports) {

           Intent intent=new Intent(Docter_Dashboard.this,Reports_ToPatient_List.class);
           startActivity(intent);


       }

        else if (id == R.id.nav_logout) {


           sessions1.setLogin(false);
            getContentResolver().delete(ContentProvider_Doctor.CONTENT_URI_Doctor,null,null);
            Intent intent =new Intent(Docter_Dashboard.this, Login_Doctor.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
