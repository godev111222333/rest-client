package com.example.demorest.net;

public interface ResultCallBack<T> {
    void onComplete(Result<T> result);
}

