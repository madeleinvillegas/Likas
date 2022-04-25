package com.example.likas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerEmail, registerPassword, registerName;
    private FirebaseAuth mAuth;
    String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerBtn = findViewById(R.id.register_btn);
        registerName = findViewById(R.id.register_username);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(view -> createAccount());

        TextView backToLogin = findViewById(R.id.login_here);
        backToLogin.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void createAccount() {
        String name = registerName.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "You forgot to add your email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "You forgot to add your password", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 5) {
            Toast.makeText(this, "Your password should be at least 5 characters long", Toast.LENGTH_SHORT).show();
        } else if (name.length() < 5) {
            Toast.makeText(this, "Your username should be at least 5 characters long", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(name, email, password);
        }
    }

    private void registerUser(String name, String email, String password) {
        Intent intent = new Intent(this, MainActivity.class);
        findViewById(R.id.register_btn).setClickable(false);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    UserProfileChangeRequest req = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                    user.updateProfile(req).addOnCompleteListener(task_update -> {
                        if (task_update.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            DatabaseReference usersdb = FirebaseDatabase.getInstance(url).getReference().child("Users").child(uid);

                            HashMap users = new HashMap();
                            users.put("name", name);
                            //users.put("admin","0");

                            usersdb.updateChildren(users).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Your account has been created", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            startActivity(intent);
                            finish();
                        } else {
                            String message = Objects.requireNonNull(task_update.getException()).getMessage();
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            findViewById(R.id.register_btn).setClickable(true);
                        }
                    });
                }
            } else {
                String message = Objects.requireNonNull(task.getException()).getMessage();
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                findViewById(R.id.register_btn).setClickable(true);
            }
        });
    }
}