package com.example.esiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button login;
    TextView signUp, forgetPassword;
    TextInputLayout Email,Password;
    private FirebaseAuth fAuth;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init

        login = findViewById(R.id.login);
        signUp = findViewById(R.id.sign_up);
        forgetPassword = findViewById(R.id.forget_password);
        fAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.login_email);
        Password = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        //____________________________________Login Button______________________________________
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = Email.getEditText().getText().toString().trim();
                String sPassword = Password.getEditText().getText().toString().trim();
                if (validate()  ) {
                    login.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    fAuth.signInWithEmailAndPassword(sEmail,sPassword)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                if (fAuth.getCurrentUser().isEmailVerified()) {
                                    Toast.makeText(Login.this, "signInWithEmail:success  ", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, Home.class));
                                    finish();
                                }else{
                                    Toast.makeText(Login.this, "verification!!  ", Toast.LENGTH_SHORT).show();


                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                login.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(Login.this, "Email or Password incorrect or verify your network connection ", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });

        //____________________________________Password reset____________________________________
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PasswordReset.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        //____________________________________Create new account_________________________________
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }
    private boolean validate() {
        boolean result ;
        String SPassword = Password.getEditText().getText().toString().trim();
        String SEmail = Email.getEditText().getText().toString().trim();
        result = true;

        if (TextUtils.isEmpty(SEmail))
        {
            Login.this.Email.setError("Email is required");
            result = false;
        }
        else if (!SEmail.contains("esi-sba.dz"))
        {
            Login.this.Email.setError("You are not an esi sba student");
            result=false;
        }
        if (TextUtils.isEmpty(SPassword))
        {
            Login.this.Password.setError("Password is required");
            result = false;
        }
        return result;
    }

}