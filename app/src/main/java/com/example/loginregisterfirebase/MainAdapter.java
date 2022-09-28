package com.example.loginregisterfirebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfirebase.Model.MainModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.ViewHolder>{

    private Context context;
    public MainAdapter(FirebaseRecyclerOptions<MainModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_items, parent, false);
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull MainModel model) {
        holder.tvFullName.setText(model.getFullname());
        holder.tvEmail.setText(model.getEmail());
        holder.tvPassword.setText(model.getPassword());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Delete data can't be undo.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("users").child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("FullName", holder.tvFullName.getText().toString());
                bundle.putString("Email", holder.tvEmail.getText().toString());
                bundle.putString("Password", holder.tvPassword.getText().toString());
                bundle.putString("MobileNum", getRef(position).getKey());

                FragmentActivity fragmentActivity = (FragmentActivity)(context);
                FragmentManager fm = fragmentActivity.getSupportFragmentManager();
                UpdateDialog updateDialog = new UpdateDialog();
                updateDialog.setArguments(bundle);
                updateDialog.show(fm,"Dialog fragment");

            }
        });
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFullName, tvEmail, tvPassword;
        private Button btnDelete, btnUpdate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tv_fullName);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvPassword = itemView.findViewById(R.id.tv_password);

            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnUpdate = itemView.findViewById(R.id.btn_update);


        }
    }
}
