package caretaker.caretaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import caretaker.caretaker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import caretaker.caretaker.app.Config;
import caretaker.caretaker.service.MyFirebaseInstanceIdService;
import caretaker.caretaker.utils.NotificationUtils;
import caretaker.caretaker.R;

public class ChatScreen extends AppCompatActivity {
    EditText  Message_Input_Area;
    ListView ChatListview;
    private static String Name_Receiver;
    private static String Email_Receiver;
    private static String Name_Sender;
    private static String Email_Sender;
    private static String Type;
    private static String Message_Text;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    TextView Message_TextView;
    private  String URL_Registration = "http://mspstracker.com/caretaker/firebasefiles_caretaker/index_text.php";
    public static String URL_Registration_ListMessage = "http://mspstracker.com/caretaker/list_messages.php";
    public ArrayList<String> ArrayList_Receiver_Name_;
    public ArrayList<String> ArrayList_Receiver_Email_;
    public ArrayList<String> ArrayList_Messasge_;
    public ArrayList<String> ArrayList_Sender_Name_;
    public ArrayList<String> ArrayList_Sender_Email_;
    public ArrayList<String> ArrayList_Combine_Data;
    public static ArrayAdapter<String> arrayAdapter_Messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        ArrayList_Receiver_Name_=new ArrayList<String>();
        ArrayList_Receiver_Email_=new ArrayList<String>();
        ArrayList_Messasge_= new ArrayList<String>();
        ArrayList_Sender_Name_=new ArrayList<String>();
        ArrayList_Sender_Email_=new ArrayList<String>();
        ArrayList_Combine_Data=new ArrayList<String>();
        Message_Input_Area = (EditText) findViewById(R.id.input);
        ChatListview= (ListView)findViewById(R.id.list_of_messages);
        //Message_TextView= (TextView)findViewById(R.id.textView_message);
        Bundle bundle=getIntent().getExtras();
        Name_Receiver= bundle.getString("name_receiver");
        Email_Receiver=bundle.getString("email_receiver");
        Name_Sender= bundle.getString("name_sender");
        Email_Sender=bundle.getString("email_sender");
        Type=bundle.getString("type");
        String checkk=bundle.getString("check");

            getSupportActionBar().setTitle(Name_Receiver);

        MyFirebaseInstanceIdService myFirebaseInstanceIdService=new MyFirebaseInstanceIdService();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Message_Input_Area.getText().toString().matches(""))

                {
                    Toast.makeText(getApplicationContext(), "Type some message", Toast.LENGTH_SHORT).show();
                }
                else {
                    Message_Text = Message_Input_Area.getText().toString();
                    StoreMessageRequest();
                    Message_Input_Area.setText("");
                    ChatListview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    ChatListview.setStackFromBottom(true);

                }



            }
        });

      /*  mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                     String sender=intent.getStringExtra("is_background");

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                //    Message_TextView.setText(message);
                }
            }
        };*/
        ListChatMessage();
        arrayAdapter_Messages=new ArrayAdapter<String>(this, R.layout.simplerow,R.id.rowTextView,ArrayList_Combine_Data);
        ChatListview.setAdapter(arrayAdapter_Messages);


       ChatListview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        ChatListview.setStackFromBottom(true);


    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
      /* LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));*/

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        Log.d(" "," ");
      super.onPause();
    }

    private void StoreMessageRequest() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                ListChatMessage();


               // ChatListview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
              // ChatListview.setStackFromBottom(true);
               // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


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
                params.put("message", Message_Text);
                params.put("type", Type);




                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, "req_register");

    }


    public void ListChatMessage()
    {   // Tag used to cancel the request



        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration_ListMessage, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                ArrayList_Receiver_Name_.clear();
                ArrayList_Receiver_Email_.clear();
                ArrayList_Messasge_.clear();
                ArrayList_Sender_Name_.clear();
                ArrayList_Sender_Email_.clear();
                ArrayList_Combine_Data.clear();



                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray_receiver_name=jsonObject.getJSONArray("receiver_name");
                    JSONArray jsonArray_receiver_email=jsonObject.getJSONArray("receiver_email");
                    JSONArray jsonArray_message=jsonObject.getJSONArray("message");
                    JSONArray jsonArray_sender_name=jsonObject.getJSONArray("sender_name");
                    JSONArray jsonArray_sender_email=jsonObject.getJSONArray("sender_email");


                    for(int a=0; a<jsonArray_receiver_name.length();a++)
                    {
                        String dummy_receiver_name=jsonArray_receiver_name.getString(a);
                        String dummy_receiver_email=jsonArray_receiver_email.getString(a);
                        String dummy_message=jsonArray_message.getString(a);
                        String dummy_sender_name=jsonArray_sender_name.getString(a);
                        String dummy_sender_email=jsonArray_sender_email.getString(a);

                        ArrayList_Receiver_Name_.add(dummy_receiver_name);
                        ArrayList_Receiver_Email_.add(dummy_receiver_email);
                        ArrayList_Messasge_.add( dummy_message);
                        ArrayList_Sender_Name_.add(dummy_sender_name);
                        ArrayList_Sender_Email_.add(dummy_sender_email);
                        ArrayList_Combine_Data.add("Sender: "+ArrayList_Sender_Name_.get(a)+"\n"+"Receiver: "+ArrayList_Receiver_Name_.get(a)
                        +"\n\n"+"Message: "+ArrayList_Messasge_.get(a));

                    }

                    arrayAdapter_Messages.notifyDataSetChanged();




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

                params.put("email_receiver", Email_Receiver);
                params.put("email_sender", Email_Sender);
                params.put("name_receiver", Name_Receiver);
                params.put("name_sender", Name_Sender);









                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");}


    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

}
