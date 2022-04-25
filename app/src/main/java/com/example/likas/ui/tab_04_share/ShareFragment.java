package com.example.likas.ui.tab_04_share;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likas.R;
import com.example.likas.databinding.PostItemBinding;
import com.example.likas.databinding.Tab04ShareBinding;
import com.example.likas.models.Post;
import com.example.likas.ui.tab_03_update.NewsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShareFragment extends Fragment {

    List<String> tags = new ArrayList<>();

    private Tab04ShareBinding binding;
    private DatabaseReference db,dblikes;
    private String uid;
    private boolean liked;
    String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ShareViewModel shareViewModel = new ViewModelProvider(this).get(ShareViewModel.class);

        binding = Tab04ShareBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseDatabase.getInstance(url).getReference().child("Posts");
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


        binding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
            }
        });

        binding.addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.inputTags.getText().toString();
                if(!text.trim().isEmpty()) {
                    tags.add(text.trim());
                    Toast.makeText(getActivity(), tags.toString(), Toast.LENGTH_SHORT).show();
                    setChips(text.trim());
                    binding.inputTags.setText("");
                }

            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query q = db.orderByChild("date");

        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(q,Post.class).build();
        FirebaseRecyclerAdapter<Post, ShareViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, ShareViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ShareViewHolder holder, int position, @NonNull Post model) {
                String pos = getRef(position).getKey();

                holder.postItemBinding.setPost(model);
                holder.postItemBinding.executePendingBindings();
                holder.setLikeBtn(pos);
                holder.postItemBinding.likeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        liked = true;
                        dblikes = FirebaseDatabase.getInstance(url).getReference().child("Posts").child(pos).child("Likes");
                        dblikes.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(liked){
                                    if(snapshot.hasChild(uid)) {
                                        dblikes.child(uid).removeValue();
                                    }
                                    else{
                                        dblikes.child(uid).setValue(true);
                                    }
                                    liked = false;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                holder.postItemBinding.commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"Posted", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), CommentActivity.class);
                        intent.putExtra("Position", pos);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                PostItemBinding postItemBinding = PostItemBinding.inflate(layoutInflater,parent,false);
                return new ShareViewHolder(postItemBinding);
            }
        };
        binding.postsRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class ShareViewHolder extends RecyclerView.ViewHolder{
        private PostItemBinding postItemBinding;
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";

        public ShareViewHolder(@NonNull PostItemBinding postItemBinding){
            super(postItemBinding.getRoot());
            this.postItemBinding = postItemBinding;

        }

        public void setLikeBtn(String pos) {

            DatabaseReference dblikes = FirebaseDatabase.getInstance(url).getReference().child("Posts").child(pos).child("Likes");
            dblikes.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    String likes = String.valueOf(snapshot.getChildrenCount());
                    postItemBinding.upvotes.setText(likes);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setChips(String e){
        final Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.single_chip,null,false);
        chip.setText(e);

        String tag = e;

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tags.remove(tag);
                binding.chipTags.removeView(chip);
            }
        });
        binding.chipTags.addView(chip);
    }
}

