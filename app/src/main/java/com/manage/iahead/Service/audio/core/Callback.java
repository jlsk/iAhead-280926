package com.manage.iahead.Service.audio.core;

public interface Callback {
    void onBufferAvailable(byte[] buffer);
}