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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class showDoctorData extends AppCompatActivity {

    TextView docName,docMail,docNumber,docType;
    Button callNow,bookAppointment;
    ArrayAdapter<String> degree;

    String docmailid,docPhoneNumber;
    String TAG  = "showDoctorData";
    Map<String,Object> map = new HashMap<>();

    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_doctor_data);

        docName = findViewById(R.id.DocName);
        docMail = findViewById(R.id.DocMail);
        docNumber = findViewById(R.id.DocNumber);
        docType = findViewById(R.id.DocDegree);
        callNow = findViewById(R.id.callnow);
        bookAppointment = findViewById(R.id.bookappointment);



        //degree = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1);
        Intent in = getIntent();
        final String docID = in.getStringExtra("dataID");
        Log.d(TAG, "DocID : "+docID);

        DocumentReference db =  firebaseFirestore.collection("doctors").document(docID);
        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        map = document.getData();
                        //add all fileds to get data
                        docName.setText("Dr. "+String.valueOf(map.get("name")));
                        docmailid = String.valueOf(map.get("mail"));
                        docMail.setText("Mail : "+docmailid);
                        docPhoneNumber = String.valueOf(map.get("number"));
                        docNumber.setText("Phone : "+docPhoneNumber);
                        ArrayList arr = (ArrayList) map.get("degree");
                        //for(int  i = 0 ; i < arr.size();i++)
                            //degree.add(arr.get(i).toString());
                        //docDegree.setAdapter(degree);
                        String arrText = arr.toString();
                        docType.setText("Degrees : "+arrText.substring(1,arrText.length()-1));
                    }

                }
            }
       });

        callNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent caller = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+docPhoneNumber));
                startActivity(caller);
            }
        });

        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Intent.ACTION_SEND);
                in.setData(Uri.parse("mailto:"));
                in.setType("text/plain");
                in.putExtra(Intent.EXTRA_EMAIL,new String[]{docmailid});
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