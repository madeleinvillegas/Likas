package com.example.likas.ui.tab_04_share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likas.databinding.CommentItemBinding;
import com.example.likas.databinding.PostItemBinding;
import com.example.likas.databinding.Tab04ShareBinding;
import com.example.likas.models.Comment;
import com.example.likas.models.News;
import com.example.likas.models.Post;
import com.example.likas.ui.tab_03_update.NewsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShareRecyclerAdapter extends RecyclerView.Adapter<ShareRecyclerAdapter.ShareViewHolder> {
    List<Post> posts;
    OnItemClickListener listener;
    String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private Tab04ShareBinding binding;

    public ShareRecyclerAdapter(List<Post> posts){
        this.posts = posts;
    }
    public ShareRecyclerAdapter(){
        posts = new ArrayList<>();
    }

    public interface OnItemClickListener{
        void onItemClick(Post post,String pos,int button);
    }

    public void setListener(ShareRecyclerAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PostItemBinding postItemBinding = PostItemBinding.inflate(layoutInflater,parent,false);
        return new ShareViewHolder(postItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareViewHolder holder, int position) {
        //assigning values to the views
        //based on position of recycler view
        final Post post = posts.get(position);
        final String key = posts.get(position).getKey();
        Log.e("TAG",posts.get(position).toString()) ;
        holder.setLikeBtn(key);

        holder.setKey(key,post);

        holder.postItemBinding.setPost(post);
        holder.postItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<Post> posts){
        this.posts = posts;
        notifyDataSetChanged();
    }

    public class ShareViewHolder extends RecyclerView.ViewHolder{
        private PostItemBinding postItemBinding;
        public String pos;
        public Post post;

        public void setKey(String pos, Post post){
            this.pos = pos;
            this.post = post;
        }




        public String getPos(){
            return pos;
        }

        public Post getPost(){
            return post;
        }

        public ShareViewHolder(@NonNull PostItemBinding postItemBinding){
            super(postItemBinding.getRoot());
            this.postItemBinding = postItemBinding;

            postItemBinding.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(listener!=null && pos!= RecyclerView.NO_POSITION){
                        listener.onItemClick(posts.get(pos),posts.get(pos).getKey().toString(),1);
                    }

                }
            });

            postItemBinding.likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(listener!=null && pos!= RecyclerView.NO_POSITION){
                        listener.onItemClick(posts.get(pos),posts.get(pos).getKey().toString(),0);
                    }
                }
            });
        }

        public void setLikeBtn(String pos) {
            Log.e("ERR",pos+"<- where");
            DatabaseReference dblikes = FirebaseDatabase.getInstance(url).getReference().child("Posts").child(pos).child("Likes");
            dblikes.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String likes = String.valueOf(snapshot.getChildrenCount());
                        postItemBinding.upvotes.setText(likes);
                    }
                    else{
                        postItemBinding.upvotes.setText("0");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}