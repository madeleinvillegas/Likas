package com.example.likas.ui.tab_03_update;

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

import com.example.likas.databinding.Tab03UpdateBinding;
import com.example.likas.models.News;

import java.util.List;

public class UpdateFragment extends Fragment {

    private Tab03UpdateBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UpdateViewModel updateViewModel = new ViewModelProvider(this).get(UpdateViewModel.class);

        binding = Tab03UpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        UpdateRecyclerAdapter updateRecyclerAdapter = new UpdateRecyclerAdapter();
        binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.newsRecyclerView.setAdapter(updateRecyclerAdapter);

        binding.createNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),NewsActivity.class);
                startActivity(intent);
            }
        });



        updateViewModel.getNews().observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                updateRecyclerAdapter.setNews(news);
            }
        });

        /*
        updateRecyclerAdapter.setListener(new UpdateRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                News news = updateRecyclerAdapter.getNewsAt(position);
                Intent intent = new Intent(getActivity(),UpdateDetailActivity.class);
                intent.putExtra(UpdateDetailActivity.TITLE, news.getTitle());
                intent.putExtra(UpdateDetailActivity.DESC,news.getDescription());
                intent.putExtra(UpdateDetailActivity.AUTHOR,news.getAuthor());
                startActivity(intent);
                Toast.makeText(getActivity(), news.getTitle(), Toast.LENGTH_SHORT).show();

            }
        });*/

        updateRecyclerAdapter.setListener(new UpdateRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(News news) {
                Intent intent = new Intent(getActivity(),UpdateDetailActivity.class);
                intent.putExtra(UpdateDetailActivity.TITLE, news.getTitle());
                intent.putExtra(UpdateDetailActivity.DESC,news.getDescription());
                intent.putExtra(UpdateDetailActivity.AUTHOR,news.getAuthor());
                startActivity(intent);
                Toast.makeText(getActivity(), news.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });



        //final TextView textView = binding.textUpdate;
        //locateViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}