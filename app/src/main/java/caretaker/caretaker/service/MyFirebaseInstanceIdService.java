package caretaker.caretaker.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import caretaker.caretaker.AppController;
import caretaker.caretaker.MainActivity_CareTaker;
import caretaker.caretaker.app.Config;
/**
 * Created by Fusion on 13-Nov-16.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService
{
    private static final String TAG = MyFirebaseInstanceIdService.class.getSimpleName();
    public static String refreshedToken;
    public static String refreshedToken_2;


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

       // refreshedToken = FirebaseInstanceId.getInstance().getToken();
            // refreshedToken=token();

   refreshedToken= token();

        // Saving reg id to shared preferences
       // storeRegIdInPref(refreshedToken);



        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }


public String token()
{        refreshedToken_2 = FirebaseInstanceId.getInstance().getToken();
          // sendRegistrationToServer(refreshedToken_2);
    return refreshedToken_2;
}





    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }





}
