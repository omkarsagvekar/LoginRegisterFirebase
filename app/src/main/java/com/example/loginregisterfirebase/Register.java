package com.example.loginregisterfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myfirstproject-5e907-default-rtdb.firebaseio.com/");
    private EditText etFullname, etMobile, etEmail, etPassword, etConfPassword;
    private Button btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initObj();
        allListeners();
    }

    private void initObj() {
        etFullname = findViewById(R.id.et_fullName);
        etMobile = findViewById(R.id.et_regPhone);
        etEmail = findViewById(R.id.et_regEmail);
        etPassword = findViewById(R.id.et_regPassword);
        etConfPassword = findViewById(R.id.et_confPassword);

        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_loginNow);

    }

    private void allListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullNameTxt = etFullname.getText().toString();
                String mobileTxt = etMobile.getText().toString();
                String emailTxt = etEmail.getText().toString();
                String passwordTxt = etPassword.getText().toString();
                String confPasswordTxt = etConfPassword.getText().toString();

                if (fullNameTxt.isEmpty() || mobileTxt.isEmpty() ||  emailTxt.isEmpty() || passwordTxt.isEmpty() || confPasswordTxt.isEmpty()){
                    Toast.makeText(Register.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }else if (!passwordTxt.equals(confPasswordTxt)){
                    Toast.makeText(Register.this, "Password not matching", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if phone is not registered before
                            if (snapshot.hasChild(mobileTxt)){
                                Toast.makeText(Register.this, "phone is already registered", Toast.LENGTH_SHORT).show();
                            }else{

                                //sending data to firebase realtime database.
                                //we are using phone number as unique identity of every user.
                                //so all the other details of user comes under phone number.
                                databaseReference.child("users").child(mobileTxt).child("fullname").setValue(fullNameTxt);
                                databaseReference.child("users").child(mobileTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(mobileTxt).child("password").setValue(passwordTxt);

                                Toast.makeText(Register.this, "Registered successfully.", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}