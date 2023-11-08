package com.manage.iahead.Jsyn;

import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.LinearRamp;
import com.jsyn.unitgen.SineOscillator;
import com.manage.iahead.Jsyn.AndroidAudio;
import com.manage.iahead.Jsyn.PlayToneListener;

/**
 * Created by closeloop on 07/07/2018.
 */

public class PlayTone {

    private final static String TAG = PlayTone.class.getSimpleName();
    private static Integer COUNTDOWN_INTERVAL_1S = 1000; //2 second

    private Synthesizer mSynth;
//    private SineOscillator mOsc;
    private SineOscillator mOsc1;
//    private SineOscillator mOsc2;
    private LineOut mLineOut;
    private LinearRamp mAmpJack;

    private PlayToneListener playToneListener;

    public PlayTone(PlayToneListener playToneListener, Activity activity) {
        mSynth = JSyn.createSynthesizer( new AndroidAudio(activity));
        mOsc1 = new SineOscillator();
//        mOsc2 = new SineOscillator();
        mSynth.add(mAmpJack = new LinearRamp());
        mSynth.add(mOsc1);
//        mSynth.add(mOsc2);
        mSynth.add(mLineOut = new LineOut());
        this.playToneListener = playToneListener;
    }

    public void play(boolean isLeft, double frequency, double amplitude, long sleep) {
        mOsc1.frequency.set(frequency);
        mOsc1.amplitude.set(amplitude);
        mOsc1.output.connect(0, mLineOut.input, isLeft ? 0:1);

        Log.d(TAG, "Amplitude: " + mOsc1.amplitude.get());
        Log.d(TAG, "Frequency: " + mOsc1.frequency.get());

        // disable another channel
//        mOsc2.frequency.set(0);
//        mOsc2.amplitude.set(0);
//        mOsc2.output.connect(0, mLineOut.input, isLeft ? 1:0);

//        Log.d(TAG, "Amplitude: " + mOsc2.amplitude.get());
//        Log.d(TAG, "Frequency: " + mOsc2.frequency.get());

        mSynth.start();
        mLineOut.start();
        playToneListener.onPlay();
        new CountDownTimer(sleep, COUNTDOWN_INTERVAL_1S){
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                mSynth.stop();
                mLineOut.stop();
                playToneListener.onSleep();
            }
        }.start();

    }

    public void playStereo(double frequency, double amplitude, long sleep) {

        mOsc1.frequency.set(frequency);
        mOsc1.amplitude.set(amplitude);
        mOsc1.output.connect(0, mLineOut.input, 1);
        // disable another channel
//        mOsc2.frequency.set(0);
//        mOsc2.amplitude.set(0);
//        mOsc2.output.connect(0, mLineOut.input, 0);

//        Log.d(TAG, "Amplitude: " + mOsc2.amplitude.get());
//        Log.d(TAG, "Frequency: " + mOsc2.frequency.get());

        mSynth.start();
        mLineOut.start();
        playToneListener.onPlay();
        new CountDownTimer(sleep, COUNTDOWN_INTERVAL_1S){
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                mSynth.stop();
                mLineOut.stop();
                playToneListener.onSleep();
            }
        }.start();

    }


    private void set(SineOscillator osc, double frequency, double amplitude) {
        osc.frequency.set(frequency);
        mAmpJack.getInput().set(amplitude);

        Log.d(TAG, "Input Frequency: " + osc.frequency.get());
        Log.d(TAG, "Input Amplitude: " + osc.amplitude.get());

        mSynth.start();
        mLineOut.start();
    }

    public void stop() {
        playToneListener.onSleep();
        mLineOut.stop();
        mSynth.stop();
    }

}
