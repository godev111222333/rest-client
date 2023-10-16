package com.example.demorest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demorest.model.User;
import com.example.demorest.net.BackendService;
import com.example.demorest.net.Result;
import com.example.demorest.net.ResultCallBack;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    private Button btnFind;
    private Button btnReg;
    private EditText etPhone;
    private TextView tvPhone;
    private TextView tvEmail;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackendService service = new BackendService(executorService);
                String phone = etPhone.getText().toString();
                service.GetUserInfo(phone, new ResultCallBack<User>() {
                    @Override
                    public void onComplete(Result<User> result) {
                        if (result instanceof Result.Success) {
                            User us = ((Result.Success<User>) result).data;
                            tvName.setText("Name: " + us.getName());
                            tvPhone.setText("Phone: " + us.getPhone());
                            tvEmail.setText("Email: " + us.getEmail());
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Get user info failed", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    protected void bind() {
        btnFind = findViewById(R.id.btnFind);
        btnReg = findViewById(R.id.btnReg);
        etPhone = findViewById(R.id.etFindPhone);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
    }
}