package caretaker.caretaker;

import com.firebase.client.Firebase;

/**
 * Created by Fusion on 16-Nov-16.
 */


public class MainApplication extends AppController

{
    @Override
    public void onCreate() {
        super.onCreate();
        //Initializing firebase
        Firebase.setAndroidContext(getApplicationContext());
    }
}
