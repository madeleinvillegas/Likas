package com.example.likas.ui.tab_03_update;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.likas.models.News;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdateViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<News>> news;
    //private ItemListener listener;

    public UpdateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Update Fragment");
        news = new MutableLiveData<>();
        List<News> news = new ArrayList<>();
        news.add(new News("title","author",new Date(),"description"));
        news.add(new News("title","author",new Date(),"description"));
        news.add(new News("title","author",new Date(),"description"));
        news.add(new News("title","author",new Date(),"description"));
        news.add(new News("title","author",new Date(),"description"));
        news.add(new News("title","author",new Date(),"description"));
        news.add(new News("title","author",new Date(),"description"));
        news.add(new News("title","author",new Date(),"description"));
        news.add(new News("title","author",new Date(),"description"));
        this.news.setValue(news);
    }

    public LiveData<String> getText() {
        return mText;
    }

    /*public void setListener(ItemListener listener){
        this.listener = listener;
    }*/

    public void setNews(){
        List<News> news = new ArrayList<>();
        news.add(new News("title","author",new Date(),"description"));
        news.add(new News("title","author",new Date(),"description"));
        news.add(new News("title","author",new Date(),"description"));
        this.news.setValue(news);
    }

    public void addNews(News news){

    }

    public void deleteNews(News news){

    }

    public void updateNews(News news){

    }

    /*public void itemClick(News news){
        listener.onItemClicked(news);
    }*/


    public MutableLiveData<List<News>> getNews() {
        return news;
    }
}