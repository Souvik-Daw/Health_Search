package com.kushjuthani.healthsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.cache.Weigher;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.UserDataReader;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.firestore.core.UserData;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class user_profile extends AppCompatActivity {

    private FirebaseFirestore db; // declaration
    CentralStorage storage;
    String value;

    TextView name;
    TextView email;
    TextView phone;
    TextView address;
    TextView pincode;
    TextView gender;
    TextView DOB;
    TextView weight;
    TextView height;
    TextView BG;

    Map <String,Object> map = new HashMap<>();
    String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        gender = findViewById(R.id.gender);
        DOB = findViewById(R.id.dob);
        BG = findViewById(R.id.bloodgroup);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);


        //centeral storage
        storage = new CentralStorage(user_profile.this);
        value = storage.getData("userid");

        //reading data from firestore

        db = FirebaseFirestore.getInstance(); // initialisation

        DocumentReference docRef = db.collection("USER").document(value);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        map =  document.getData();
                        Log.d(String.valueOf(user_profile.this),"DocumentSnapshot data: "+map);
                        //userinfo.setText( String.valueOf(info));
                        name.setText("Name : "+ String.valueOf(map.get("Name")));
                        email.setText("Email : "+ String.valueOf(map.get("Email")));
                        phone.setText("Phone : "+ String.valueOf(map.get("Phone")));
                        address.setText("Address : "+ String.valueOf(map.get("Address")));
                        pincode.setText("Pincode : "+ String.valueOf(map.get("Pincode")));
                        gender.setText("Gender : "+ String.valueOf(map.get("Gender")));
                        DOB.setText("Date Of Birth : "+ String.valueOf(map.get("DOB")));
                        BG.setText("Blood Group : "+ String.valueOf(map.get("Blood Group")));
                        weight.setText("Weight : "+ String.valueOf(map.get("Weight")));
                        height.setText("Height : "+ String.valueOf(map.get("Height")));


                        //.setText(info);

                    } else {
                        Log.d(String.valueOf(user_profile.this), "No such document"+value);
                    }
                } else {
                    Log.d(String.valueOf(user_profile.this), "get failed with ", task.getException());
                }
            }
        });
/*
        db.collection("<CollectionName>")
                .document("<documentId>")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //fetching result in document format
                            DocumentSnapshot snapshot = task.getResult();
                            // saving in a map variable
                            Map<String, Object> map = snapshot.getData();
                            // assigning in the model class to access
                            User user = new User(
                                    String.valueOf(map.get(user_profile.this))
                            );
                        }
                    }
                }*/


        //userinfo.setVisibility(View.VISIBLE);
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
                Toast.makeText(this, "Already in Profile", Toast.LENGTH_SHORT ).show();
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