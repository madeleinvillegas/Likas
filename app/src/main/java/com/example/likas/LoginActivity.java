package com.example.likas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText loginEmail = findViewById(R.id.login_email);
        EditText loginPassword = findViewById(R.id.login_password);

        findViewById(R.id.login_btn).setOnClickListener(view -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String email = loginEmail.getText().toString().trim();
            String pass = loginPassword.getText().toString().trim();
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String message = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        findViewById(R.id.register_here).setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }
}