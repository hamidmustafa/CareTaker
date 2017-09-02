package caretaker.caretaker.Utilities;

/**
 * Created by g50 on 4/2/2016.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    private Context contextobj;

    public ConnectionDetector(Context context)
    {this.contextobj=context;}

    public boolean isConnectingToInternet()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)contextobj.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {
            NetworkInfo networksInfo=connectivityManager.getActiveNetworkInfo();
            if(networksInfo!=null)
            {
                    if(networksInfo.getState()==NetworkInfo.State.CONNECTED);
                      return  true;
            }
        }
        return false;
    }
}

