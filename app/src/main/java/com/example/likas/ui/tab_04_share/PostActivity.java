package com.example.likas.ui.tab_04_share;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.likas.R;
import com.example.likas.databinding.ActivityPostBinding;
import com.google.android.material.chip.Chip;

import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;
    List<String> tags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.inputTags.getText().toString();
                tags.add(text);
                Toast.makeText(PostActivity.this, tags.toString(), Toast.LENGTH_SHORT).show();
                setChips(text);
                binding.inputTags.setText("");
            }
        });
    }

    public void setChips(String e){
        final Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.single_chip,null,false);
        chip.setText(e);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.chipTags.removeView(chip);

            }
        });
        binding.chipTags.addView(chip);
    }
}