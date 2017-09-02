package caretaker.caretaker;

import android.app.Application;
import android.widget.Toast;
import android.content.Context;

/**
 * Created by Fusion on 06-Oct-16.
 */
public class Test extends Application{
public Context context_;
public Test(Context context)
{
    this.context_=context;

}


    public void test(Context context)
    {
        Toast.makeText(context," Yes Taking", Toast.LENGTH_SHORT).show();
    }

}
