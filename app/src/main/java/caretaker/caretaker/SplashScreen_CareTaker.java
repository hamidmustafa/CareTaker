package caretaker.caretaker;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import caretaker.caretaker.Utilities.Sessions;

public class SplashScreen_CareTaker extends AppCompatActivity {
    public Sessions sessions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen__care_taker);
        getSupportActionBar().hide();
        sessions=new Sessions(getApplicationContext());

        if(sessions.isLoggedIn())
        {  DBHelper.getCursorData_Patient();
            DBHelper.LoopingData_Patient();
            int sizeOfarray=DBHelper.ArrayList_Email_Patient.size();
            if(sizeOfarray!=1)
            {
                DBHelper.getCursorData_Doctor();
                DBHelper.LoopingData_Doctor();
                String emailfromdata=DBHelper.ArrayList_Email_Doctor.get(0);
                Intent intent= new Intent(getApplicationContext(), Docter_Dashboard.class);
                intent.putExtra("email",emailfromdata);
                startActivity(intent);
                finish();
            }

            else


            {
               // startService(new Intent(getBaseContext(), Service_Data.class));
                String emailfromdata=DBHelper.ArrayList_Email_Patient.get(0);
                Intent intent= new Intent(getApplicationContext(), Patient_Dashboard.class);
                intent.putExtra("email",emailfromdata);
                startActivity(intent);
                finish();
            }
        }

        else
        {




        new CountDownTimer(3000,1000) {

            @Override
            public void onFinish() {
                Intent intent = new Intent(getBaseContext(), MainActivity_CareTaker.class);
                startActivity(intent);

                finish();

            }


            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();}

    }


    }

