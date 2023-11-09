package com.manage.iahead;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseActivity extends AppCompatActivity {
    private AudioManager mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }
        mode = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        mode.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
}
