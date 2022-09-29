package com.example.loginregisterfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregisterfirebase.Model.MainModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login extends AppCompatActivity {
    private EditText etPhone, etPassword;
    private Button btnLogin, btnAdminLogin;
    private TextView tvRegister;
    private MainAdapter mainAdapter;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myfirstproject-5e907-default-rtdb.firebaseio.com/");

    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initObj();
        allListeners();

        //code for push notification.
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initObj() {
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        btnAdminLogin = findViewById(R.id.btn_adminLogin);

    }

    private void allListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneText = etPhone.getText().toString();
                String passwordText = etPassword.getText().toString();
                
                if(phoneText.isEmpty() || passwordText.isEmpty()){
                    Toast.makeText(Login.this, "Please enter your mobile or password", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //we checking the existing password matching to the password which entering.
                            if (snapshot.hasChild(phoneText)){
                                String existingPswd = snapshot.child(phoneText).child("password").getValue(String.class);
                                if (passwordText.equals(existingPswd)){
                                    Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(Login.this, "wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(Login.this, "wrong password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAdminDialog();

            }
            private void showAdminDialog() {

                dialog = new Dialog(Login.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.admin_bottomsheet_layout);

                EditText etAdminName = dialog.findViewById(R.id.adminName);
                EditText etAdminPassword = dialog.findViewById(R.id.adminPassword);
                Button btnLogin = dialog.findViewById(R.id.adminDialogLogin);

                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etAdminName.getText().toString().isEmpty() || etAdminPassword.getText().toString().isEmpty()){
                            Toast.makeText(Login.this, "Fields should not be empty", Toast.LENGTH_SHORT).show();
                        }else if (etAdminName.getText().toString().equals("admin") && etAdminPassword.getText().toString().equals("admin123")){
                            startActivity(new Intent(Login.this, ShowUsersList.class));
                            finish();
                        }else{
                            Toast.makeText(Login.this, "Invalid name or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);

            }

        });
    }

    @Override
    protected void onPause() {
        Toast.makeText(this, "OnPause!", Toast.LENGTH_SHORT).show();
        super.onPause();
        dialog.dismiss();
    }
}