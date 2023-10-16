package com.example.demorest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demorest.model.User;
import com.example.demorest.net.BackendService;
import com.example.demorest.net.Result;
import com.example.demorest.net.ResultCallBack;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    private Button btnRegister;
    private Button btnBack;
    private EditText etPhone;
    private EditText etEmail;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bind();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackendService service = new BackendService(executorService);
                User us = new User(etPhone.getText().toString(), etName.getText().toString(), etEmail.getText().toString());
                service.RegisterUser(us, new ResultCallBack<Boolean>() {
                    @Override
                    public void onComplete(Result<Boolean> result) {
                        if (result instanceof Result.Success) {
                            runOnUiThread(() -> {
                                Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(RegisterActivity.this, "Registered failed", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }

    protected void bind() {
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
    }
}