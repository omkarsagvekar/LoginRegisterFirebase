package com.example.loginregisterfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdateDialog extends DialogFragment {

    private EditText etFullName, etEmail, etPassword;
    private Button btnUpdate;
    private ImageButton btnClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_update_dialog, container, false);
//        view.layout(300, 0, 0, 0);
        etFullName = view.findViewById(R.id.et_updateFullname);
        etEmail = view.findViewById(R.id.et_updateEmail);
        etPassword = view.findViewById(R.id.et_updatePassword);
        btnUpdate = view.findViewById(R.id.btn_changeUpdate);
        btnClose = view.findViewById(R.id.btn_close);

        Bundle bundle = getArguments();
        String fullName = bundle.getString("FullName", "Full name");
        String email = bundle.getString("Email", "Email");
        String password = bundle.getString("Password", "password");
        String uniqueMobNum = bundle.getString("MobileNum");

        etFullName.setText(fullName);
        etEmail.setText(email);
        etPassword.setText(password);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hashMap = new HashMap();
                hashMap.put("fullname", etFullName.getText().toString());
                hashMap.put("email", etEmail.getText().toString());
                hashMap.put("password", etPassword.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("users").child(uniqueMobNum).updateChildren(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(view.getContext(), "Data updated successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(), "Error while uploading", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
//        return super.onCreateView(inflater, container, savedInstanceState);
    }

}