package com.example.likas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FacilityPopupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_account);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            ((TextView) findViewById(R.id.user_info_name)).setText(user.getDisplayName());
            ((TextView) findViewById(R.id.user_info_email)).setText(user.getEmail());
            ((TextView) findViewById(R.id.user_info_id)).setText(user.getUid());
        }

        ImageView back = findViewById(R.id.user_back);
        back.setOnClickListener(view -> finish());

        Button sign_out = findViewById(R.id.sign_out);
        sign_out.setOnClickListener(view -> {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}