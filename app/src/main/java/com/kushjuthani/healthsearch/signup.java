package com.kushjuthani.healthsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    CentralStorage cstorage ;

    TextView signup_dob,signup_genderTextView;
    RadioGroup signup_gender;
    RadioButton gender;
    EditText signup_username, signup_email, signup_password,
            signup_phone, signup_address, signup_pincode,
            signup_BG, signup_height, signup_weight;
    Button signup_buttom;
    TextView loginInstead;

    String singupUsername, signupEmail , singupPassword,signupDOB,
            signupGender, signPhone, signupAddress, signupPincode,
            signupBG, signupHeight, signupWieght;

    //int height,weaight,pincode;
    //long phoneNumber;
    int genderID=0;
    String userId="";

    DatePickerDialog.OnDateSetListener signupDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth=FirebaseAuth.getInstance();

        signup_username=findViewById(R.id.txt_username);
        signup_email=findViewById(R.id.txt_email);
        signup_password=findViewById(R.id.txt_password);
        signup_dob=findViewById(R.id.txt_dob);
        signup_genderTextView=findViewById(R.id.genderTextView);
        signup_gender=(RadioGroup) findViewById(R.id.gender);
        signup_phone = findViewById(R.id.txt_phoneno);
        signup_address = findViewById(R.id.addressMultiLine);
        signup_pincode = findViewById(R.id.txt_pincode);
        signup_BG=findViewById(R.id.txt_bloodgroup);
        signup_height=findViewById(R.id.txt_height);
        signup_weight=findViewById(R.id.txt_weight);

        signup_buttom=findViewById(R.id.signup_button);
        loginInstead=findViewById(R.id.loginInstead);

        signup_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(signup.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        (DatePickerDialog.OnDateSetListener)signupDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        signupDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String udate = dayOfMonth + " / " + month + " / " + year;
                signup_dob.setText(udate);
            }
        };

        signup_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                singupUsername=String.valueOf(signup_username.getText()).trim();
                signupEmail=String.valueOf(signup_email.getText()).trim();
                singupPassword=String.valueOf(signup_password.getText()).trim();
                signupDOB=String.valueOf(signup_dob.getText()).trim();
                signPhone=String.valueOf(signup_phone.getText());
                signupAddress=String.valueOf(signup_address.getText()).trim();
                signupPincode=String.valueOf(signup_pincode.getText());
                signupBG=String.valueOf(signup_BG.getText()).trim();
                signupHeight=String.valueOf(signup_height.getText());
                signupWieght=String.valueOf(signup_weight.getText());
                genderID=signup_gender.getCheckedRadioButtonId();
                if(genderID!=1000013||genderID!=1000057){
                    gender=(RadioButton) findViewById(genderID);
                    signupGender=String.valueOf(gender.getText());
                }
                else
                    signup_genderTextView.setError("Choose Gender");
                //Toast.makeText(signup.this,signupGender,Toast.LENGTH_LONG).show();

                int error = 0;
                //check for empty fields
                if(singupUsername.isEmpty()) {
                    signup_username.setError("Enter username");
                    error = 1;
                }if(singupPassword.isEmpty()){
                    signup_password.setError("Enter password");
                    error = 1;
                }
                if(signupEmail.isEmpty()){
                    signup_email.setError("Enter Email address");
                    error = 1;
                }
                if(signupDOB.isEmpty()){
                    signup_dob.setError("Enter Date of Birth");
                    error = 1;
                }
                if(signPhone.isEmpty()){
                    signup_phone.setError("Enter valid Phone Number");
                    error = 1;
                }
                if(signupAddress.isEmpty()){
                    signup_address.setError("Enter Address");
                    error = 1;
                }
                if(signupPincode.isEmpty()){
                    signup_pincode.setError("Enter Valid Pincode");
                    error = 1;
                }
                if(signupHeight.isEmpty())
                    signup_height.setText("0.0");
                if(signupWieght.isEmpty())
                    signup_weight.setText("0.0");
                if(signupBG.isEmpty())
                    signup_BG.setText("");

                if(error==0){
                    //adding in firebase
                    mAuth.createUserWithEmailAndPassword(signupEmail, singupPassword)
                            .addOnCompleteListener(signup.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // logic for success
                                        userId = mAuth.getUid();// retrieving autogenerated id for newly created user
                                        cstorage = new CentralStorage(signup.this);
                                        cstorage.setData("userid",userId);
                                        // optional , verify email address by sending verification link
                                        //mAuth.getCurrentUser().sendEmailVerification();
                                        // create a model class and save the data for profiling purpose
                                        //User user = new User(userId,name,email,contact,pass); // for example
                                        //createProfile(); // write a method to upload all data into respective database
                                    } else {
                                        //logic for failed
                                        Toast.makeText(
                                                signup.this,
                                                "Failed "+task.getException(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            String val;
                            while(true){
                                val = new CentralStorage(signup.this).getData("userid");
                                if(val.equals("")){
                                    CentralStorage loop = new CentralStorage(signup.this);
                                    loop.clearData();
                                    loop.setData("userid",mAuth.getUid());
                                }
                                else
                                    break;
                            }

                            //stroing data in firestore
                            db = FirebaseFirestore.getInstance();
                            // creating map variable to store data in key-value pair
                            Map<String,Object> newUser = new HashMap<>();

                            /* in case of having data from a model class
                             newUser.put(singupUsername,user.getUserName());
                             newUser.put(,user.getUserEmail());*/

                            // in case of not having a model class

                            newUser.put("Name",singupUsername);
                            newUser.put("Email",signupEmail);
                            newUser.put("UID",val);
                            newUser.put("Gender",signupGender);
                            newUser.put("DOB",signupDOB);
                            newUser.put("Phone",signPhone);
                            newUser.put("Address",signupAddress);
                            newUser.put("Pincode",signupPincode);
                            newUser.put("Blood Group",signupBG);
                            newUser.put("Height",signupHeight);
                            newUser.put("Weight",signupWieght);

                            //saving data in fireStore
                            db.collection("USER")
                            .document(val).set(newUser)  // in case of using map , if not using then write multiple set() to save data
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failure
                                    Toast.makeText(signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener <Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //success
                                    Toast.makeText(signup.this, "Signed in successfully",
                                            Toast.LENGTH_LONG).show();

                                    Intent in = new Intent(signup.this,MainActivity.class);
                                    startActivity(in);
                                    signup.this.finish();
                                }
                            }
                     );
                }
                else{
                    Toast.makeText(signup.this, "Enter all valid information",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        loginInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(signup.this,login_page.class);
                startActivity(in);
                signup.this.finish();
            }
        });
    }
}
