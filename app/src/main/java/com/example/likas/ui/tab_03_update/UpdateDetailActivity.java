package com.example.likas.ui.tab_03_update;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.likas.R;
import com.example.likas.databinding.UpdateDetailBinding;

public class UpdateDetailActivity extends AppCompatActivity {

    public static final String TITLE = "Test";
    public static final String AUTHOR = "author";

    public static final String DESC = "desc";



    private UpdateDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.update_detail);

        binding = UpdateDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //replace with
        //uid = getIntent().getExtras().get("Position").toString();
        //postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(uidOfPost);

        Intent intent = getIntent();
        binding.newsTitle.setText(intent.getStringExtra(TITLE));
        binding.newsAuthor.setText(intent.getStringExtra(AUTHOR));
        binding.newsContent.setText(intent.getStringExtra(DESC));

    }
}