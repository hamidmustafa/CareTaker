package caretaker.caretaker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

import caretaker.caretaker.app.Config;
import caretaker.caretaker.utils.NotificationUtils;

/**
 * Created by Fusion on 29-Nov-16.
 */

public class appointment_service extends IntentService {
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
    public static Intent intentChat;
    public static String  minutes_json;
    public static String  hours_json;
    private BroadcastReceiver mRegistrationBroadcastReceiver;









    public static IntentFilter filterr;

    public appointment_service() {
        super("Appointment_Service");


    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

       /* filterr = new IntentFilter(Intent.ACTION_TIME_TICK);
        filterr.addAction(Intent.ACTION_TIME_CHANGED);
        filterr.addAction(Intent.ACTION_TIME_TICK);
        filterr.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), "i am in appointment service", Toast.LENGTH_SHORT).show();

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message_appointment = intent.getStringExtra("message1");

                    if(message_appointment==null)
                    {
                        Toast.makeText(getApplicationContext(), "message appoint: "+message_appointment, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    Toast.makeText(getApplicationContext(), message_appointment, Toast.LENGTH_LONG).show();

                    intentChat = new Intent(getApplicationContext(),ChatScreen.class);
                    intentChat.putExtra("appointment",message_appointment);

                    PendingIntent IntentP= PendingIntent.getActivity(getApplicationContext(), 0, intentChat, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification n  = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Appointment Request")
                            .setContentText(message_appointment)
                            .setContentIntent(IntentP)
                            .setSmallIcon(R.mipmap.ic_launcher)// .setContentIntent(IntentP)
                            .setAutoCancel(true)
                            .addAction(R.drawable.ic_menu_share, "Chat",IntentP )
                            .build();
                    notificationManager.notify(0, n);

                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();

                }}
            }
        };




        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

*/


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //  registerReceiver(m_receiver1, filterr);


        // start();
        //Toast.makeText(getBaseContext(), "Brightness Service activated", Toast.LENGTH_SHORT).show();


        return START_STICKY;
    }


    @Override
    public void onDestroy() {

    }

}
