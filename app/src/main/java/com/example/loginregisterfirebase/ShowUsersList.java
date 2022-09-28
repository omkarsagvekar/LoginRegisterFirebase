package com.example.loginregisterfirebase;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfirebase.Model.MainModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class ShowUsersList extends AppCompatActivity {
    MainAdapter mainAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_recycler);

        recyclerView = findViewById(R.id.item_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users"), MainModel.class)
                        .build();
        mainAdapter = new MainAdapter(options, ShowUsersList.this);
        recyclerView.setAdapter(mainAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                textSearch(str);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String str) {
                textSearch(str);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void textSearch(String str){
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").orderByChild("fullname").startAt(str).endAt(str+"~"), MainModel.class)
                        .build();
        MainAdapter mainAdapter = new MainAdapter(options, ShowUsersList.this);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);


    }
}
