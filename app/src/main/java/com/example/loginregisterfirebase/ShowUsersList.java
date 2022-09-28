package com.example.loginregisterfirebase;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfirebase.Model.MainModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class ShowUsersList extends AppCompatActivity {
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_recycler);

        RecyclerView recyclerView = findViewById(R.id.item_recyclerView);
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
}
