package com.kushjuthani.healthsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Type extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    //ListView listview;

    private FirestoreRecyclerAdapter adapter;
    private RecyclerView mFirestoreList;
    //ArrayAdapter<String> adapter;
    //List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        //listview = findViewById(R.id.listView);
        mFirestoreList = findViewById(R.id.firestore_list);
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Query
        Query query = firebaseFirestore.collection("types");

        //Recycler opitons
        FirestoreRecyclerOptions<TypeData> options = new FirestoreRecyclerOptions.Builder<TypeData>()
                .setLifecycleOwner(this)
                .setQuery(query, new SnapshotParser<TypeData>() {
                    @NonNull
                    @Override
                    public TypeData parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        TypeData productsModel = snapshot.toObject(TypeData.class);
                        String ID = snapshot.getId();
                        productsModel.setDocumentId(ID);
                        return productsModel;
                    }
                })
                .build();

        adapter = new FirestoreRecyclerAdapter<TypeData, TypeViewHolder>(options) {
            @NonNull
            @Override
            public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.type_view,parent, false);
                return new TypeViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull TypeViewHolder holder, int position, @NonNull TypeData model) {
                holder.listID.setText(model.getDocumentId());
                holder.listSci.setText(model.getSci());
                holder.listInfo.setText(model.getInfo());
            }

        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);


    }
    private  class TypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView listID,listSci,listInfo;
        private CardView cardviewType;
        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);

            listID = itemView.findViewById(R.id.list_ID);
            listSci = itemView.findViewById(R.id.list_Sci);
            listInfo = itemView.findViewById(R.id.list_Info);
            cardviewType = itemView.findViewById(R.id.card_view_type);

            cardviewType.setOnClickListener(this);


            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            /*
            DocumentSnapshot snapshot = null;

            TypeData productsModel = snapshot.toObject(TypeData.class);
            Log.d(String.valueOf("Type"), "onItemClick: "+getAdapterPosition()+" item ID : "+productsModel.getDocumentId());

             */


            Intent intent = getIntent();
            String type = intent.getStringExtra("main");
            String itemID = (listID).getText().toString();
            String text = " ID : "+itemID+" , "+type;
            Toast.makeText(Type.this,text,Toast.LENGTH_LONG).show();

            //Intent to list of types
            intent = new Intent(Type.this, listoftypes.class);
            intent.putExtra("type",itemID);
            intent.putExtra("main",type);
            startActivity(intent);
            //itemID pass to new activity
        }
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
/*
adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1);

        firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = firebaseFirestore.collection("types");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //list.add(document.getId());
                        adapter.add(document.getId());
                    }
                    //Log.d(String.valueOf(DoctorType.this), list.toString());
                } else {
                    Log.d(String.valueOf(Type.this), "Error getting documents: ", task.getException());

                    }
                }

        });
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = getIntent();
                String type = intent.getStringExtra("main");
                String itemID = ((TextView)view).getText().toString();
                //String text = "Item"+position+" ID : "+itemID+" , "+type;
                //Toast.makeText(Type.this,text,Toast.LENGTH_LONG).show();

                //Intent to list of types
                intent = new Intent(Type.this, listoftypes.class);
                intent.putExtra("type",itemID);
                intent.putExtra("main",type);
                startActivity(intent);
                //itemID pass to new activity
            }
        });
 */
