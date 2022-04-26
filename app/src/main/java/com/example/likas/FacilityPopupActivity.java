package com.example.likas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FacilityPopupActivity extends AppCompatActivity {
    private static final String DB_URL = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_popup);
        intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference();

        // Back Button
        ImageView back = findViewById(R.id.user_back);
        back.setOnClickListener(view -> finish());

        // Facility Name
        ((TextView) findViewById(R.id.facility_name)).setText(intent.getStringExtra("name"));

        // Facility Type
        String type = intent.getStringExtra("type");
        ((TextView) findViewById(R.id.facility_type)).setText(type);

        // Google Maps
        Button maps = findViewById(R.id.redirect);
        maps.setOnClickListener(view -> {
            try {
                double lat = getIntent().getDoubleExtra("lat", 0);
                double lon = getIntent().getDoubleExtra("longitude", 0);
                Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + lat + "," + lon);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Google Maps is Not Available in Your Device", Toast.LENGTH_SHORT).show();
            }
        });

        // Slots Remaining
        TextView slotsRemain = findViewById(R.id.facility_slots);
        if (!type.equals("Evacuation Centers (Verified)")) {
            slotsRemain.setVisibility(View.INVISIBLE);
        } else {
            int taken = intent.getIntExtra("slotsTaken", 0);
            int max = intent.getIntExtra("slotsMax", 0);
            String slotsText = "Slots: " + taken + " / " + max;
            slotsRemain.setText(slotsText);
        }

        // Buttons
        buttons();
    }

    private void buttons() {
        Button admit = findViewById(R.id.admit);
        Button edit = findViewById(R.id.edit);
        Button delete = findViewById(R.id.delete);

        String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Log.e("UID: ", UID);
        mDatabase.child("admins").child(UID).get().addOnCompleteListener(task -> {
            Log.e("Admin", String.valueOf(task.getResult().getValue()));
            if (!String.valueOf(task.getResult().getValue()).equals("true")) {
                admit.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            } else {
                // Admit
                admit.setOnClickListener(view -> {
                    Intent newIntent = new Intent(getApplicationContext(), QRActivity.class);
                    newIntent.putExtra("key", intent.getStringExtra("key"));
                    startActivity(newIntent);
                });

                // Edit
                edit.setOnClickListener(view -> {
                    Intent newIntent = new Intent(getApplicationContext(), NewEvacActivity.class);
                    newIntent.putExtra("lat", intent.getDoubleExtra("lat", 0));
                    newIntent.putExtra("long", intent.getDoubleExtra("longitude", 0));
                    newIntent.putExtra("name", intent.getStringExtra("name"));
                    newIntent.putExtra("slotsTaken", intent.getIntExtra("slotsTaken", 0));
                    newIntent.putExtra("slotsMax", intent.getIntExtra("slotsMax", 0));
                    newIntent.putExtra("type", intent.getStringExtra("type"));
                    newIntent.putExtra("mode", intent.getStringExtra("modify"));
                    newIntent.putExtra("key", intent.getStringExtra("key"));
                    startActivity(newIntent);
                    finish();
                });

                // Delete
                delete.setOnClickListener(view -> {
                    mDatabase.child("facilities").child(intent.getStringExtra("key")).removeValue();
                    Toast.makeText(this, "Facility Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
}