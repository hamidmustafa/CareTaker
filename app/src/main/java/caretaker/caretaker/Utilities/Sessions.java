package caretaker.caretaker.Utilities;


import android.content.*;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by hamid on 8/24/16.
 */
public class Sessions {
    public String TAG= Sessions.class.getSimpleName();
    public SharedPreferences sharedPreferences;
    public Editor editor;
    public Context context_;
    private int  PRIVATE_MODE=0;
    // shared preference file names
    private static final String preference_file_name="CareTakerLogin";
    private static final String Key_Logged_In="isLoggedIn";

    public Sessions(Context context) {
        this.context_ = context;
        sharedPreferences = context_.getSharedPreferences(preference_file_name, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

        public void setLogin(boolean isLoggedIn)
    {
        editor.putBoolean(Key_Logged_In,isLoggedIn);
        editor.commit();
        Log.d(TAG,"User login session modified");
    }
   public Boolean isLoggedIn()
   {return sharedPreferences.getBoolean(Key_Logged_In,false);}


}
