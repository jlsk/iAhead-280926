package com.manage.iahead.Utility;

import android.app.Activity;
import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;

public class Uti {
    public static boolean isHeadphonesPlugged(Activity activity){
        AudioManager audioManager = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
        AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
        for(AudioDeviceInfo deviceInfo : audioDevices){
            if(deviceInfo.getType()==AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                    || deviceInfo.getType()==AudioDeviceInfo.TYPE_WIRED_HEADSET){
                return true;
            }
        }
        return false;
    }
}
