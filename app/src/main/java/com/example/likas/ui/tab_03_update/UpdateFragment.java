package com.example.likas.ui.tab_03_update;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likas.databinding.NewsItemBinding;
import com.example.likas.databinding.PostItemBinding;
import com.example.likas.databinding.Tab03UpdateBinding;
import com.example.likas.models.News;
import com.example.likas.models.Post;
import com.example.likas.ui.tab_04_share.CommentActivity;
import com.example.likas.ui.tab_04_share.ShareFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFragment extends Fragment {

    private Tab03UpdateBinding binding;
    private List<String> spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private DatabaseReference db;
    private DatabaseReference dbSpinner;
    String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        spinner = new ArrayList<>();
        spinner.add("All");


        binding = Tab03UpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseDatabase.getInstance(url).getReference().child("News");

        binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.createNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),NewsActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();


        Query q = db.orderByChild("date");
        FirebaseRecyclerOptions<News> options = new FirebaseRecyclerOptions.Builder<News>().setQuery(q,News.class).build();
        FirebaseRecyclerAdapter<News, UpdateViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, UpdateViewHolder>(options) {

            @NonNull
            @Override
            public UpdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                NewsItemBinding newsItemBinding = NewsItemBinding.inflate(layoutInflater,parent,false);
                return new UpdateViewHolder(newsItemBinding);
            }

            @Override
            protected void onBindViewHolder(@NonNull UpdateViewHolder holder, int position, @NonNull News model) {
                String pos = getRef(position).getKey();
                holder.newsItemBinding.setNews(model);
                holder.newsItemBinding.executePendingBindings();

                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String temp = snapshot.child(pos).child("author").getValue(String.class);
                        if(!spinner.contains(temp)) {
                            spinner.add(temp);
                        }
                        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line,spinner) ;
                        binding.spinnerNewsLocation.setAdapter(spinnerAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                holder.newsItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),UpdateDetailActivity.class);
                        intent.putExtra("Position",pos);
                        startActivity(intent);
                    }
                });

            }
        };
        binding.newsRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public class UpdateViewHolder extends  RecyclerView.ViewHolder {

        private NewsItemBinding newsItemBinding;

        public UpdateViewHolder(@NonNull NewsItemBinding newsItemBinding){

            super(newsItemBinding.getRoot());
            this.newsItemBinding = newsItemBinding;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}