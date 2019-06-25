package com.perminov.network;

public interface ResponseCallback<R> {
    void onEnd(R apiResponse);
}
