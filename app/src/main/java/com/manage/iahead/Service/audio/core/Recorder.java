package com.manage.iahead.Service.audio.core;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Process;
import android.util.Log;

public class Recorder {

    private final static int SAMPLE_RATE = 44100;

    private int audioSource;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private Thread thread;
    private Callback callback;

    public Recorder() {
    }

    public Recorder(int source, Callback callback) {
        this.callback = callback;
        audioSource = source;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void start() {
        if (thread != null) return;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                int minBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, channelConfig, audioEncoding);
                AudioRecord recorder = new AudioRecord(audioSource, SAMPLE_RATE, channelConfig, audioEncoding, minBufferSize);

                if (recorder.getState() == AudioRecord.STATE_UNINITIALIZED) {
                    Thread.currentThread().interrupt();
                    return;
                } else {
                    Log.i(Recorder.class.getSimpleName(), "Started.");
                    //callback.onStart();
                }

                byte[] buffer = new byte[minBufferSize];
                recorder.startRecording();

                while (thread != null && !thread.isInterrupted() && recorder.read(buffer, 0, minBufferSize) > 0) {
                    callback.onBufferAvailable(buffer);
                }
                recorder.stop();
                recorder.release();
            }
        }, Recorder.class.getName());
        thread.start();
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }
}
