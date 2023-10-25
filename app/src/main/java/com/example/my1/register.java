package com.example.my1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.my1.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {
    ActivityRegisterBinding registerBinding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(registerBinding.getRoot());

        initVars();

        registerBinding.contiregButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = registerBinding.nameText.getText().toString();
                String mobile = registerBinding.mobileText.getText().toString();
                String email = registerBinding.emailRegText.getText().toString();
                String pass = registerBinding.passRegText.getText().toString();
                String pass_cnf = registerBinding.passCnfRegText.getText().toString();

                if (!pass.equals(pass_cnf)){
                    Toast.makeText(register.this, "Password not matched", Toast.LENGTH_SHORT).show();
                }

                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(register.this, Login.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void initVars() {
        firebaseAuth = FirebaseAuth.getInstance();
    }
}