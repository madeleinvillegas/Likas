package com.example.likas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerEmail, registerPassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerBtn = findViewById(R.id.register_btn);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(view -> {
            createAccount();
        });

        TextView backToLogin = findViewById(R.id.login_here);
        backToLogin.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void createAccount() {
        String email = registerEmail.getText().toString();
        String password = registerPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "You forgot to add your email", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "You forgot to add your password", Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < 8) {
            Toast.makeText(this, "Your password should be at least 8 characters long", Toast.LENGTH_SHORT).show();
        }
        else {
            registerUser(email, password);
        }
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
//                    startActivity(MainActivity);
                }
                else {
                    String message = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(RegisterActivity.this, "Your account has not been created due to " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}