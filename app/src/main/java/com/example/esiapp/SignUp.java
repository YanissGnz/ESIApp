package com.example.esiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    Button signUp;
    EditText fullname, email, password, confirmPassword;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //___________________________________Init__________________________________
        signUp = findViewById(R.id.next);
        fullname = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.password2);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.signup_progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { final String sEmail = email.getText().toString();
                String sPassword = password.getText().toString();
                final String username = fullname.getText().toString();
                FirebaseUser User =mAuth.getCurrentUser();
                assert User != null;
                final boolean UserEmailVerification = User.isEmailVerified();
                    if (validate()) {
                        signUp.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            sendEmailVerification();
                                            if(UserEmailVerification) {
                                                Toast.makeText(SignUp.this, "user created ", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), (Home.class)));
                                                FirebaseDatabase.getInstance().getReference().child("UserNames").push().child("Name").setValue(username);
                                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                finish();
                                            }
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(SignUp.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
            }
        });
            }

    //_______________________________________________________________________________________________
    private boolean validate() {
        boolean result;
        result = true;

        if (TextUtils.isEmpty(fullname.getText().toString())) {
            SignUp.this.fullname.setError("Enter your name");
            result = false;
        }  if (TextUtils.isEmpty(email.getText().toString())) {
            SignUp.this.email.setError("Email is required");
            result = false;
        }  if (!email.getText().toString().contains("esi-sba.dz")) {
            SignUp.this.email.setError("you are not an esi sba student");
            result = false;
        }  if (TextUtils.isEmpty(password.getText().toString())) {
            SignUp.this.password.setError("Password is required");
            result = false;
        } else if (password.getText().toString().length() < 8) {
            SignUp.this.password.setError("Password must be more than 8 characters");
            result = false;
        }  if (!(password.getText().toString().equals(confirmPassword.getText().toString()))) {
            SignUp.this.confirmPassword.setError("Password don't match. Try again");
            result = false;
        }
        return result;
    }

    public void sendEmailVerification() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        assert user != null;
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(SignUp.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SignUp.this, " Failed to sent Verification ", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}
