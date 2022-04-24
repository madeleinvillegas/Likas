package com.example.likas.ui.tab_03_update;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likas.databinding.CommentItemBinding;
import com.example.likas.databinding.NewsItemBinding;
import com.example.likas.models.Comment;
import com.example.likas.models.News;

import java.util.ArrayList;
import java.util.List;

public class UpdateRecyclerAdapter extends RecyclerView.Adapter<UpdateRecyclerAdapter.UpdateViewHolder> {

    List<News> news;
    private OnItemClickListener listener;

    public UpdateRecyclerAdapter(List<News> news){
        this.news = news;
    }
    public UpdateRecyclerAdapter(){ }

    /*public interface OnItemClickListener {
        void onClick(View view, int position);
    }*/

    public interface OnItemClickListener{
        void onItemClick(News news);
    }

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }
    
    /*public News getNewsAt(int pos){
        return news.get(pos);
    }*/

    @NonNull
    @Override
    public UpdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        NewsItemBinding newsItemBinding = NewsItemBinding.inflate(layoutInflater,parent,false);
        return new UpdateViewHolder(newsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateViewHolder holder, int position) {
        //assigning values to the views
        //based on position of recycler view
        final News news1 = news.get(position);
        holder.newsItemBinding.setNews(news1);
        holder.newsItemBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        //return number of item
        return news.size();
    }

    public void setNews(List<News> news){
        this.news = news;
        notifyDataSetChanged();
    }

    public class UpdateViewHolder extends  RecyclerView.ViewHolder {

        private NewsItemBinding newsItemBinding;

        //grabbing views from recycler
        //similar to onCreate()

        /*
        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }*/

        public UpdateViewHolder(@NonNull NewsItemBinding newsItemBinding){

            super(newsItemBinding.getRoot());
            this.newsItemBinding = newsItemBinding;
            //this.newsItemBinding.getRoot().setOnClickListener(this);

            newsItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Test","Clicked");
                    int position = getAdapterPosition();
                    if(listener!= null && position!= RecyclerView.NO_POSITION){
                        listener.onItemClick(news.get(position));
                    }
                }
            });
            this.newsItemBinding = newsItemBinding;
        }
    }






}
