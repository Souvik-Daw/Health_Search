package com.kushjuthani.healthsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class showHospitalData extends AppCompatActivity {

    TextView hosName,hosAddress,hosMail,hosNumber;//hosType;
    Button hosSchedule;
    String hosmailid,hosPhoneNumber;
    String TAG  = "showHospitalData";
    Map<String,Object> map = new HashMap<>();

    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_hospital_data);

        hosName = findViewById(R.id.HosName);
        hosAddress = findViewById(R.id.HosAddress);
        hosMail = findViewById(R.id.HosMail);
        hosNumber = findViewById(R.id.HosNumber);
        //hosType = findViewById(R.id.HosType);
        hosSchedule = findViewById(R.id.HosSchedule);


        Intent in = getIntent();

        final String hosID = in.getStringExtra("dataID");
        Log.d(TAG, "HosID : "+hosID);
        DocumentReference db =  firebaseFirestore.collection("hospitals").document(hosID);
        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        map = document.getData();
                        //add all fileds to get data
                        hosName.setText(String.valueOf(map.get("name")));
                        hosAddress.setText(String.valueOf(map.get("address")));
                        hosNumber.setText(String.valueOf(map.get("phone number")));
                        hosmailid = String.valueOf(map.get("mail"));
                        hosMail.setText(hosmailid);
                        //ArrayList arr = (ArrayList) map.get("type");
                        //String arrText = arr.toString();
                        //hosType.setText(arrText.substring(1,arrText.length()-1));

                    }

                }
            }
        });
        hosSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_SEND);
                in.setData(Uri.parse("mailto:"));
                in.setType("text/plain");
                in.putExtra(Intent.EXTRA_EMAIL,new String[]{hosmailid});
                in.putExtra(Intent.EXTRA_SUBJECT,"eMAIL SUBJECT");
                in.putExtra(Intent.EXTRA_TEXT,"user data mail body");
                startActivity(in);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.threedotsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(this,user_profile.class));
                return true;

            case R.id.aboutus:
                startActivity(new Intent(this,AboutUs.class));
                return true;

            case R.id.help:
                startActivity(new Intent(this,Help.class));
                return true;

            case R.id.logout:
                Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT ).show();
                CentralStorage cs = new CentralStorage(this);
                cs.clearData();
                cs.removeData("userid");
                startActivity(new Intent(this,login_page.class));
                this.finish();
                return true;

            default: return super.onOptionsItemSelected(item);
        }

    }
}