package com.example.likas.ui.tab_04_share;

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
import com.example.likas.models.Comment;
import com.example.likas.models.News;
import com.example.likas.models.Post;
import com.example.likas.ui.tab_03_update.UpdateRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShareRecyclerAdapter extends RecyclerView.Adapter<ShareRecyclerAdapter.ShareViewHolder> {
    List<Post> posts;
    OnItemClickListener listener;

    public ShareRecyclerAdapter(List<Post> posts){
        this.posts = posts;
    }
    public ShareRecyclerAdapter(){}

    public interface OnItemClickListener{
        void onItemClick(Post post);
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

        public ShareViewHolder(@NonNull PostItemBinding postItemBinding){
            super(postItemBinding.getRoot());
            this.postItemBinding = postItemBinding;

            postItemBinding.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(listener!=null && pos!= RecyclerView.NO_POSITION){
                        listener.onItemClick(posts.get(pos));
                    }

                }
            });

        }
    }
}
