package com.example.demorest.net;

import com.example.demorest.model.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;


public class BackendService {
    private final Executor executor;

    public BackendService(Executor executor) {
        this.executor = executor;
    }

    public void RegisterUser(User user, final ResultCallBack<Boolean> callBack) {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(String.format("%s/register", Config.URL));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("POST");  // POST method
                    conn.setRequestProperty("Content-Type", "application/json; charset=utf-8"); // tell API server that we'll send a JSON body request
                    conn.setRequestProperty("Accept", "application/json"); // desired to receive a JSON response
                    conn.setDoOutput(true);

                    String requestBody = new Gson().toJson(user);
                    OutputStream os = conn.getOutputStream();
                    os.write(requestBody.getBytes("utf-8"));

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        callBack.onComplete(new Result.Error<>(new Exception("failed")));
                        return;
                    }

                    callBack.onComplete(new Result.Success<>(new Boolean(true)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void GetUserInfo(String phone, final ResultCallBack<User> callBack) {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(String.format("%s/user_info?phone=%s", Config.URL, phone));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");  // GET method
                    conn.setRequestProperty("Accept", "application/json"); // desired to receive a JSON response
                    conn.setDoOutput(false);

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        callBack.onComplete(new Result.Error<>(new Exception("failed")));
                        return;
                    }

                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }

                        User us = new Gson().fromJson(response.toString(), User.class);
                        callBack.onComplete(new Result.Success<>(us));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
