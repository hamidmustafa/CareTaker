package caretaker.caretaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import caretaker.caretaker.service.MyFirebaseInstanceIdService;

public class MainActivity_CareTaker extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity__care_taker);
        ImageView imageViewdoctor=(ImageView)findViewById(R.id.imageViewDoctor);
        ImageView imageViewpatient=(ImageView)findViewById(R.id.imageViewPatient);

        imageViewdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent= new Intent(MainActivity_CareTaker.this, Login_Doctor.class);
                startActivity(intent);



            }
        });

        imageViewpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent= new Intent(MainActivity_CareTaker.this, Login_Patient.class);
                startActivity(intent);



            }
        });
    }
}
