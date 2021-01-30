package com.kushjuthani.healthsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.DocumentCollections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class listoftypes extends AppCompatActivity {

    String TAG ="listoftypes";

    TextView infoText,sciText,typeidText;
    ListView listView;

    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebaseFirestore.collection("types");

    ArrayAdapter<String> adapter;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listoftypes);


        infoText = findViewById(R.id.infoTextView);
        sciText = findViewById(R.id.sciTextView);
        typeidText = findViewById(R.id.tpyeidTxtView);
        listView = findViewById(R.id.arrayList);

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1);



        Intent intent = getIntent();
        final String typeID = intent.getStringExtra("type");
        final String type = intent.getStringExtra("main");
        typeidText.setText(typeID+" from "+type);

        final DocumentReference documentReference = firebaseFirestore.collection("types").document(typeID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String ,Object> map = new HashMap<>();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        map = document.getData();
                        if(type.equals("doctors")) {
                            ArrayList arr = (ArrayList) map.get("doctors");
                            for (Object i : arr) {
                                adapter.add(i.toString());
                                //listView.setText(i.toString());
                                Log.d(TAG, "Doctor : " + i.toString());
                            }
                        }
                        else if(type.equals("hospitals")) {
                            ArrayList arr = (ArrayList) map.get("hospitals");
                            for (Object i : arr) {
                                adapter.add(i.toString());
                                //listView.setText(i.toString());
                                Log.d(TAG, "Hospitals : " + i.toString());
                            }
                        }
                        listView.setAdapter(adapter);
                        infoText.setText(String.valueOf(map.get("info")));
                        sciText.setText(String.valueOf(map.get("sci")));
                        //loadData(typeID);
                    }
                    else
                        Log.d(TAG, "No Documnet exists : "+typeID);
                }
                else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String dataID = ((TextView)view).getText().toString();
                Log.d(TAG, "dataID : "+dataID);
                Intent intent = getIntent();
                String type = intent.getStringExtra("main");
                if(type.equals("doctors")) {
                    intent = new Intent( listoftypes.this,showDoctorData.class);
                    intent.putExtra("dataID",dataID);
                }
                else if(type.equals("hospitals")) {
                    intent = new Intent( listoftypes.this,showHospitalData.class);
                    intent.putExtra("dataID",dataID);
                }
                startActivity(intent);
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

    /*public void loadData(final String TYPEID){
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            TypeData data1 = documentSnapshot.toObject(TypeData.class);
                            data1.setDocumentId(TYPEID);
                            String documentId = data1.getDocumentId();
                            data += "ID: " + documentId+"sci : "+data1.getSci()+"info : "+data1.getInfo();
                            /*for (String tag : data1.getdoctor()) {
                                data += "\n-" + tag;
                            }*
                            data += "\n\n";
                        }
                        listView.setText(data);
                    }
                });
    }*/