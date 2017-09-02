package caretaker.caretaker;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import android.media.*;
import android.net.Uri;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.IntentService;
/**
 * Created by Fusion on 03-Oct-16.
 */
public class Service_Data extends IntentService {
    public static String URL_Registration = "http://www.mspstracker.com/caretaker/servicedata.php";


    ScreenBroadcastReceiver1 m_receiver1;
    static Calendar calendar;
    static String hour;
    static String minute;
    static String yes;
    static String no;
    static NotificationManager notificationManager;
    static String NotificationTitle = "Medicine Reminder";
    static String NotificaitonID = "You to have to take medicine NOW";
    public static final String ACTION_YES = "YES";
    public static final String ACTION_NO = "NO";
    Intent intent_;
    public static Intent intent1;
    public static Intent intentGO;
    public static String  minutes_json;
    public static String  hours_json;





    public static ContentResolver cResolver2; //Content resolver used as a handle to the system's settings
    public static IntentFilter filterr;
    static MediaPlayer mp;

    public Service_Data() {
        super("Service_Data");


    }


    @Override
    public void onCreate() {

        m_receiver1 = new ScreenBroadcastReceiver1();
        filterr = new IntentFilter(Intent.ACTION_TIME_TICK);
        filterr.addAction(Intent.ACTION_TIME_CHANGED);
        filterr.addAction(Intent.ACTION_TIME_TICK);
        filterr.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(m_receiver1, filterr);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        registerReceiver(m_receiver1, filterr);

        // start();
        //Toast.makeText(getBaseContext(), "Brightness Service activated", Toast.LENGTH_SHORT).show();


        return START_STICKY;
    }


    @Override
    public void onDestroy() {

    }




    private class ScreenBroadcastReceiver1 extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, final Intent intent_broadcaster) {


            if (intent_broadcaster.getAction().equals(Intent.ACTION_TIME_CHANGED) || intent_broadcaster.getAction().equals(Intent.ACTION_TIME_TICK)) {
                IntentFilter batterylevelfilter = new IntentFilter(Intent.ACTION_TIME_TICK);
                registerReceiver(m_receiver1, batterylevelfilter);
                context.unregisterReceiver(this);

                calendar = Calendar.getInstance();
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                hour = String.valueOf(hours);
                minute = String.valueOf(minutes);

                ServerResponseRequest();



                // Toast.makeText(getApplicationContext(), "Time changed", Toast.LENGTH_SHORT).show();

                IntentFilter batterylevelfilter_1 = new IntentFilter(Intent.ACTION_TIME_TICK);
                registerReceiver(m_receiver1, batterylevelfilter_1);
            }



        }


    }

    public void ServerResponseRequest() {
        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_Registration, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject1 = new JSONObject(response);



                        JSONArray jsonarray=jsonObject1.getJSONArray("data");
                        hours_json=jsonarray.getString(0);
                        minutes_json=jsonarray.getString(1);

                        Toast.makeText(getApplicationContext(), hours_json+":" +minutes_json, Toast.LENGTH_SHORT).show();





                        intentGO = new Intent(getApplicationContext(),Medicine_Notification.class);
                        intentGO.putExtra("hours",hours_json);
                        intentGO.putExtra("minutes",minutes_json);
                        PendingIntent IntentP= PendingIntent.getActivity(getApplicationContext(), 0, intentGO, PendingIntent.FLAG_UPDATE_CURRENT);

                        Notification n  = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Medicine Timing")
                                .setContentText("It's medicine time.")
                                .setContentIntent(IntentP)
                                .setSmallIcon(R.drawable.ic_menu_share)// .setContentIntent(IntentP)
                                .setAutoCancel(true)
                                .addAction(R.drawable.ic_menu_share, "GO",IntentP )
                                .build();
                        notificationManager.notify(0, n);

                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                        mp = MediaPlayer.create(getApplicationContext(), notification);
                        mp.start();










                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error response " + error, Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
// Replace Passed_Email string below in put arugments
                params.put("hours", hour);
                params.put("minutes", minute);


                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, "req_register");


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onHandleIntent(Intent intent) {

    }
}