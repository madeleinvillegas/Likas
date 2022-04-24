package com.example.likas.ui.tab_04_share;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likas.databinding.CommentItemBinding;
import com.example.likas.models.Comment;
import com.example.likas.models.News;
import com.example.likas.ui.tab_03_update.UpdateRecyclerAdapter;

import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentViewHolder> {

    List<Comment> comments;

    public CommentRecyclerAdapter(List<Comment> comments){
        this.comments = comments;
    }
    public CommentRecyclerAdapter(){}



    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CommentItemBinding commentItemBinding = CommentItemBinding.inflate(layoutInflater,parent,false);
        return new CommentViewHolder(commentItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        //assigning values to the views
        //based on position of recycler view
        final Comment comment = comments.get(position);
        holder.commentItemBinding.setComment(comment);
        holder.commentItemBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        //return number of item
        return comments.size();
    }

    public void setComments(List<Comment> comments){
        this.comments = comments;
        notifyDataSetChanged();
    }

    public static class CommentViewHolder extends  RecyclerView.ViewHolder{

        private CommentItemBinding commentItemBinding;

        //grabbing views from recycler
        //similar to onCreate()

        public CommentViewHolder(@NonNull CommentItemBinding commentItemBinding){
            super(commentItemBinding.getRoot());
            this.commentItemBinding = commentItemBinding;
        }
    }
}
