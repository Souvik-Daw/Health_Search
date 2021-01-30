package com.kushjuthani.healthsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login_page extends AppCompatActivity {

    private FirebaseAuth mAuth;

    ImageView ambulance;
    Button login_button;
    EditText username;
    EditText password;
    TextView forgotpassword;
    TextView signup;

    CentralStorage cstorage ;
    static final String ADMIN = "admin";
    String un,pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mAuth = FirebaseAuth.getInstance();
        login_button=findViewById(R.id.login_button);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forgotpassword = findViewById(R.id.forgetpassword);
        signup = findViewById(R.id.signup);
        ambulance=findViewById(R.id.ambulance);

        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent caller = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:102"));
                startActivity(caller);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                un=String.valueOf(username.getText()).trim();
                if(un.isEmpty())
                    username.setError("Enter valid Email Address");
                else {
                    mAuth.sendPasswordResetEmail(un).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                                Toast.makeText(login_page.this, "Mail sent to reset password",
                                        Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(login_page.this, task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();

                        }
                    });
                }

            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                un=String.valueOf(username.getText()).trim();
                pw=String.valueOf(password.getText()).trim();
                if(un.isEmpty()){
                    username.setError("Enter valid Email Address");
                }
                else if(pw.isEmpty()){
                    password.setError("Enter valid password");
                }
                /*
                else if(un.equalsIgnoreCase(ADMIN) && pw.equals(ADMIN) ){
                    Toast.makeText(login_page.this, "LOGIN DONE !", Toast.LENGTH_SHORT).show();
                    cstorage = new CentralStorage(login_page.this);
                    cstorage.setData("USER",un.toString());
                    Intent in = new Intent(login_page.this,MainActivity.class);
                    startActivity(in);
                    login_page.this.finish();
                }
                else {
                    Toast.makeText(login_page.this, "Invalid user/password", Toast.LENGTH_SHORT).show();
                }*/



                mAuth.signInWithEmailAndPassword(un, pw)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    /** if if have used email verification
                                     * then
                                     * check email verified or not
                                     * otherwise skip this part, directly inform user or route to dashboard
                                     */
                                    FirebaseUser fUser = mAuth.getCurrentUser();
                                    String userID = mAuth.getUid(); // get whose email to verify
                                    //boolean check = fUser.isEmailVerified();
                                    //if (check) {
                                        // if email is verified

                                        /**
                                         if required, here you need to store the user for session purpose
                                         write up the code yourself accordingly
                                         */
                                        Toast.makeText(login_page.this, "LOGIN DONE !", Toast.LENGTH_SHORT).show();
                                        cstorage = new CentralStorage(login_page.this);
                                        cstorage.setData("userid",userID);
                                        Intent in = new Intent(login_page.this,MainActivity.class);
                                        startActivity(in);
                                        login_page.this.finish();
                                    /*} else {
                                        // .. in case email is not verified
                                        Toast.makeText(login_page.this,
                                                "Please verify your email first!!!",
                                                Toast.LENGTH_LONG).show();
                                    }*/
                                } else {
                                    Toast.makeText(login_page.this,
                                            "SignIn failed",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                }
            });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(login_page.this,signup.class);
                startActivity(in);
                login_page.this.finish();
            }
        });
    }

}