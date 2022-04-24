package com.example.likas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class FacilityPopupActivity extends AppCompatActivity {
    private static final String DB_URL = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_popup);
        Intent intent = getIntent();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        ImageView back = findViewById(R.id.user_back);
        back.setOnClickListener(view -> finish());

        ((TextView) findViewById(R.id.facility_name)).setText(intent.getStringExtra("name"));

        HashMap<String, String> types = new HashMap<>();
        types.put("Fire", "Fire Station");
        String type = intent.getStringExtra("type");
        ((TextView) findViewById(R.id.facility_type)).setText(types.get(type));

        TextView slotsRemain = findViewById(R.id.facility_slots);
        if (!type.equals("Evac")) {
            slotsRemain.setVisibility(View.INVISIBLE);
        } else {
            int taken = intent.getIntExtra("slotsTaken", 0);
            int max = intent.getIntExtra("slotsMax", 0);
            int remain = max - taken;
            String slotsText = "Slots Remaining: " + remain;
            slotsRemain.setText(slotsText);
        }

        Button admit = findViewById(R.id.admit);
        Button edit = findViewById(R.id.edit);
        Button delete = findViewById(R.id.delete);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference();
        String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mDatabase.child("admins").child(UID).get().addOnCompleteListener(task -> {
            if (!String.valueOf(task.getResult().getValue()).equals("true")) {
                admit.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            } else {
                Log.e("TAG", "Admin");
            }
        });

//        sign_out.setOnClickListener(view -> {
//            mAuth.signOut();
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        });
    }
}