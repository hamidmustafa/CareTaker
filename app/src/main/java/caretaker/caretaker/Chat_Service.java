package caretaker.caretaker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.SendException;

import java.util.Calendar;

import caretaker.caretaker.app.Config;
import caretaker.caretaker.utils.NotificationUtils;

/**
 * Created by Fusion on 24-Nov-16.
 */

public class Chat_Service extends IntentService {

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









    public static ContentResolver cResolver2; //Content resolver used as a handle to the system's settings
    public static IntentFilter filterr;
    static MediaPlayer mp;

    public Chat_Service() {
        super("Chat_Service");


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

        filterr = new IntentFilter(Intent.ACTION_TIME_TICK);
        filterr.addAction(Intent.ACTION_TIME_CHANGED);
        filterr.addAction(Intent.ACTION_TIME_TICK);
        filterr.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
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


                    int startIndex_Message = message.indexOf("^");
                    int endIndex_Message = message.indexOf("+");

                    String MESSAGE=message.substring(startIndex_Message+1, endIndex_Message);

                    int startIndex_SenderName = message.indexOf("-");
                    int endIndex_SenderName = message.indexOf("*");

                    String Sender_Name=message.substring(startIndex_SenderName+1, endIndex_SenderName);


                    int startIndexOf_SenderEmail = message.indexOf("+");
                    int endIndexOf_SenderEmail = message.indexOf("-");

                    String Sender_Email=message.substring(startIndexOf_SenderEmail+1, endIndexOf_SenderEmail);


                    int startIndexOf_RecevierName = message.indexOf("*");
                    int endIndexOf_Receiver_Name = message.indexOf("$");

                    String Receiver_Name=message.substring(startIndexOf_RecevierName+1, endIndexOf_Receiver_Name);

                    int startIndexOf_RecevierEmail = message.indexOf("$");
                    int endIndexOf_Receiver_Email = message.indexOf("!");

                    String Receiver_Email=message.substring(startIndexOf_RecevierEmail+1, endIndexOf_Receiver_Email);



                    int startIndexOf_type = message.indexOf("!");
                    int endIndexOf_Receiver_type = message.indexOf("%");

                    String Type=message.substring(startIndexOf_type+1, endIndexOf_Receiver_type);




                    //    Toast.makeText(getApplicationContext(), MESSAGE+" : "+ Sender_Name+" : "+Sender_Email+" : "+Receiver_Name+" : "
                    //  +Receiver_Email+" : "+Type, Toast.LENGTH_LONG).show();

                    intentChat = new Intent(getApplicationContext(),ChatScreen.class);
                    intentChat.putExtra("name_receiver",Receiver_Name);
                    intentChat.putExtra("email_receiver",Receiver_Email);
                    intentChat.putExtra("name_sender", Sender_Name);
                    intentChat.putExtra("email_sender",Sender_Email);
                    intentChat.putExtra("type",Type);



                    PendingIntent IntentP= PendingIntent.getActivity(getApplicationContext(), 0, intentChat, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification n  = new Notification.Builder(getApplicationContext())
                            .setContentTitle(Sender_Name)
                            .setContentText(MESSAGE)
                            .setContentIntent(IntentP)
                            .setSmallIcon(R.mipmap.ic_launcher)// .setContentIntent(IntentP)
                            .setAutoCancel(true)
                            .addAction(R.drawable.ic_menu_share, "Chat",IntentP )
                            .build();
                    notificationManager.notify(0, n);

                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();

                }
            }
        };




        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));




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
