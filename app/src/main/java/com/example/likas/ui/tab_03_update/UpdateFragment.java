package com.example.likas.ui.tab_03_update;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likas.R;
import com.example.likas.databinding.NewsItemBinding;
import com.example.likas.databinding.PostItemBinding;
import com.example.likas.databinding.Tab03UpdateBinding;
import com.example.likas.models.News;
import com.example.likas.models.Post;
import com.example.likas.ui.tab_04_share.CommentActivity;
import com.example.likas.ui.tab_04_share.ShareFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class UpdateFragment extends Fragment {

    private Tab03UpdateBinding binding;
    private List<String> spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private DatabaseReference db;
    private boolean admin = false;
    private FirebaseRecyclerAdapter<News, UpdateViewHolder> firebaseRecyclerAdapter;
    String url = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            switch(direction) {
                case ItemTouchHelper.LEFT:
                    Toast.makeText(getActivity(), "Deleting", Toast.LENGTH_SHORT).show();
                    ((UpdateViewHolder) viewHolder).deleteItem();
                    break;
                case ItemTouchHelper.RIGHT:
                    Toast.makeText(getActivity(), "Editing", Toast.LENGTH_SHORT).show();
                    ((UpdateViewHolder) viewHolder).editItem();
                    break;
            }
        }

        @Override
        public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            String pos = ((UpdateViewHolder) viewHolder).pos;
            String[] arr = pos.split(" ");
            String uid = arr[0];
            if(!uid.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) return 0;
            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(),R.color.prim))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(),R.color.google_blue))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance(url).getReference();
        spinner = new ArrayList<>();
        spinner.add("All");

        binding = Tab03UpdateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        try {
            String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            DatabaseReference mdb = FirebaseDatabase.getInstance(url).getReference();
            mdb.child("admins").child(UID).get().addOnCompleteListener(task -> {
                if (String.valueOf(task.getResult().getValue()).equals("true")) {
                    admin = true;
                }
                else{
                    admin = false;
                }
            });
        } catch (NullPointerException e) {
            // None
            admin = false;
        }

        binding.setAdmin(admin);

        db = FirebaseDatabase.getInstance(url).getReference().child("News");
        binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.newsRecyclerView);

        binding.createNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),NewsActivity.class);
                startActivity(intent);
            }
        });

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item:snapshot.getChildren()){
                    String temp = item.child("author").getValue(String.class);
                    if(!spinner.contains(temp)){
                        spinner.add(temp);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,spinner) ;
        binding.spinnerNewsLocation.setAdapter(spinnerAdapter);

        binding.spinnerNewsLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String author = spinner.get(i);
                Query q = db.orderByChild("date");
                if(!author.equals("All")) {

                    q = db.orderByChild("author").startAt(author).endAt(author);
                }
                else{
                    q = db.orderByChild("date");
                }

                FirebaseRecyclerOptions<News> options = new FirebaseRecyclerOptions.Builder<News>().setQuery(q, News.class).build();
                firebaseRecyclerAdapter.updateOptions(options);
                firebaseRecyclerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query q = db.orderByChild("date");
        FirebaseRecyclerOptions<News> options = new FirebaseRecyclerOptions.Builder<News>().setQuery(q,News.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<News, UpdateViewHolder>(options) {

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

                db.child(pos).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            final News news = snapshot.getValue(News.class);
                            holder.setKey(pos,news);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                holder.newsItemBinding.clickNews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),UpdateDetailActivity.class);
                        intent.putExtra("Position",pos);
                        startActivity(intent);
                    }
                });
                holder.newsItemBinding.executePendingBindings();
            }
        };
        binding.newsRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
        firebaseRecyclerAdapter.startListening();
    }

    public class UpdateViewHolder extends  RecyclerView.ViewHolder {
        private NewsItemBinding newsItemBinding;
        private String pos;
        private News news;

        public UpdateViewHolder(@NonNull NewsItemBinding newsItemBinding){
            super(newsItemBinding.getRoot());
            this.newsItemBinding = newsItemBinding;
        }

        public void setKey(String pos, News news){
            this.pos = pos;
            this.news = news;
        }

        public void editItem(){
            Intent intent = new Intent(getActivity(),NewsActivity.class);
            intent.putExtra("EditMode",true);
            intent.putExtra("title",news.getTitle());
            intent.putExtra("description",news.getDescription());
            intent.putExtra("key",pos);
            startActivity(intent);
        }

        public void deleteItem(){
            db.child(pos).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(binding.newsRecyclerView,"News Deleted",Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    HashMap temp = new HashMap();
                                    temp.put("title",news.getTitle());
                                    temp.put("author",news.getAuthor());
                                    temp.put("date",news.getDate());
                                    temp.put("description",news.getDescription());

                                    db.child(pos).updateChildren(temp);
                                }
                            }).show();
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}