package com.example.my1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my1.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    ActivityLoginBinding loginBinding;
    Button mbutton;
    TextView forgettext;
    FirebaseAuth firebaseAuth;

    public class LoginActivity extends Activity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();

            Thread welcomeThread = new Thread() {
                @Override
                public void run() {
                    try {
                        super.run();
                        sleep(10000); // Delay of 10 seconds
                    } catch (Exception e) {
                    } finally {
                        Intent i = new Intent(LoginActivity.this, Home1.class);
                        startActivity(i);
                        finish();
                    }
                }
            };
            welcomeThread.start();
        }
    }


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mbutton=findViewById(R.id.login_btn);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Home1.class));
            }
        });


        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        initVars();

        loginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginBinding.emailText.getText().toString();
                String pass = loginBinding.passwordText.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Successful login
                                startActivity(new Intent(Login.this, Home1.class));
                                finish();
                            } else {
                                // Failed login
                                Toast.makeText(Login.this, "Login failed. Check your credentials.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Login.this, "Please enter valid email and password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initVars() {
        firebaseAuth = FirebaseAuth.getInstance();
    }
}