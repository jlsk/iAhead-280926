package com.manage.iahead;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manage.iahead.Jsyn.PlayTone;
import com.manage.iahead.Jsyn.PlayToneListener;
import com.manage.iahead.Obj.calibrationObj;
import com.manage.iahead.Utility.SharedPref;

import org.jtransforms.fft.DoubleFFT_1D;

import java.lang.reflect.Type;

public class CalibrationActivity extends AppCompatActivity {
    private final static String TAG = CalibrationActivity.class.getSimpleName();
    private TextView tv_min, tv_max, tv_db;
    private LinearLayout ll_Save, ll_play, ll_stop, ll_back;

    private AudioRecord mRecorder;
    private boolean isRecording;

    private Thread recordingThread = null;

    private DoubleFFT_1D fft = null;

    private int mAmplitude;
    private double mDecibel;
    private double mFrequency;

    private final static int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private final static int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private final static int RECORDER_SAMPLERATE = 44100;
    private final static int BYTES_PER_ELEMENT = 2;
    private final static int BLOCK_SIZE = AudioRecord.getMinBufferSize(
            RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING)
            / BYTES_PER_ELEMENT;
    private final static int BLOCK_SIZE_FFT = 1764;
    private final static int NUMBER_OF_FFT_PER_SECOND = RECORDER_SAMPLERATE
            / BLOCK_SIZE_FFT;
    private final static double FREQRESOLUTION = ((double) RECORDER_SAMPLERATE)
            / BLOCK_SIZE_FFT;

    private float[] THIRD_OCTAVE = {16, 20, 25, 31.5f, 40, 50, 63, 80, 100, 125, 160, 200, 250, 315, 400, 500,
            630, 800, 1000, 1250, 1600, 2000, 2500, 3150, 4000, 5000, 6300, 8000, 10000, 12500, 16000, 20000};
    String[] THIRD_OCTAVE_LABEL = {"16", "20", "25", "31.5", "40", "50", "63", "80", "100", "125", "160", "200", "250", "315", "400", "500",
            "630", "800", "1000", "1250", "1600", "2000", "2500", "3150", "4000", "5000", "6300", "8000", "10000", "12500", "16000", "20000"};

    private double filter = 0;

    private double[] weightedA = new double[BLOCK_SIZE_FFT];
    private double actualFreq;

    double linearFftAGlobalRunning = 0;
    private long fftCount = 0;
    private double dbFftAGlobalRunning;
    private float[] dbBandRunning = new float[THIRD_OCTAVE.length];
    private float[] linearBandRunning = new float[THIRD_OCTAVE.length];

    double dbFftAGlobalMinTemp = 0;
    double dbFftAGlobalMaxTemp = 0;
    int dbFftAGlobalMinFirst = 0;
    int dbFftAGlobalMaxFirst = 0;

    float[] dbBandMax = new float[THIRD_OCTAVE.length];
    float[] dbBandMin = new float[THIRD_OCTAVE.length];
    int kkk = 0;

    private double dbFftAGlobalMax;
    private double dbFftAGlobalMin;
    private EditText et_gain;
    private EditText et_500, et_1000, et_2000, et_4000;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        SharedPref.init(this);
        tv_db = findViewById(R.id.tv_db);
        tv_max = findViewById(R.id.tv_max);
        tv_min = findViewById(R.id.tv_min);
        et_gain = findViewById(R.id.et_gain);
        String gainStr = SharedPref.read(SharedPref.GAIN, "");
        et_gain.setText(gainStr);

        et_500 = findViewById(R.id.et_500);
        et_1000 = findViewById(R.id.et_1000);
        et_2000 = findViewById(R.id.et_2000);
        et_4000 = findViewById(R.id.et_4000);

        radioGroup = findViewById(R.id.radioGroup);
        Gson gson = new Gson();
        String leftstr = SharedPref.read(SharedPref.LEFT, "");
        String rightstr = SharedPref.read(SharedPref.RIGHT, "");
        calibrationObj leftObj = gson.fromJson(leftstr, calibrationObj.class);
        if(leftObj!=null){
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedOption = selectedRadioButton.getText().toString();
                if(selectedOption.equalsIgnoreCase("Left")){
                    et_500.setText(leftObj.getAmp500());
                    et_1000.setText(leftObj.getAmp1000());
                    et_2000.setText(leftObj.getAmp2000());
                    et_4000.setText(leftObj.getAmp4000());
                }
            }
        }
        calibrationObj rightObj = gson.fromJson(rightstr, calibrationObj.class);
        if(rightObj!=null){
            int selectedId = radioGroup.getCheckedRadioButtonId();

            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedOption = selectedRadioButton.getText().toString();
                if(selectedOption.equalsIgnoreCase("Right")){
                    et_500.setText(leftObj.getAmp500());
                    et_1000.setText(leftObj.getAmp1000());
                    et_2000.setText(leftObj.getAmp2000());
                    et_4000.setText(leftObj.getAmp4000());
                }
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedOption = selectedRadioButton.getText().toString();
                if(selectedOption.equalsIgnoreCase("Left")){
                    String leftstr = SharedPref.read(SharedPref.LEFT, "");
                    calibrationObj leftObj = gson.fromJson(leftstr, calibrationObj.class);
                    if(leftObj!=null) {
                        et_500.setText(leftObj.getAmp500());
                        et_1000.setText(leftObj.getAmp1000());
                        et_2000.setText(leftObj.getAmp2000());
                        et_4000.setText(leftObj.getAmp4000());
                    }else{
                        et_500.setText("");
                        et_1000.setText("");
                        et_2000.setText("");
                        et_4000.setText("");
                    }
                }else{
                    String rightstr = SharedPref.read(SharedPref.RIGHT, "");
                    calibrationObj rightObj = gson.fromJson(rightstr, calibrationObj.class);
                    if(rightObj!=null) {
                        et_500.setText(rightObj.getAmp500());
                        et_1000.setText(rightObj.getAmp1000());
                        et_2000.setText(rightObj.getAmp2000());
                        et_4000.setText(rightObj.getAmp4000());
                    }else{
                        et_500.setText("");
                        et_1000.setText("");
                        et_2000.setText("");
                        et_4000.setText("");
                    }
                }
            }
        });

        ll_Save = findViewById(R.id.ll_Save);
        ll_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_500.getText().toString().length()==0){
                    Toast.makeText(CalibrationActivity.this, "Please insert amplitude 500", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_1000.getText().toString().length()==0){
                    Toast.makeText(CalibrationActivity.this, "Please insert amplitude 1000", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_2000.getText().toString().length()==0){
                    Toast.makeText(CalibrationActivity.this, "Please insert amplitude 2000", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_4000.getText().toString().length()==0){
                    Toast.makeText(CalibrationActivity.this, "Please insert amplitude 4000", Toast.LENGTH_SHORT).show();
                    return;
                }
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String selectedOption = selectedRadioButton.getText().toString();
                    Gson gson = new Gson();
                    calibrationObj obj = new calibrationObj();
                    obj.setAmp500(et_500.getText().toString());
                    obj.setAmp1000(et_1000.getText().toString());
                    obj.setAmp2000(et_2000.getText().toString());
                    obj.setAmp4000(et_4000.getText().toString());
                    String json = gson.toJson(obj);
                    if(selectedOption.equalsIgnoreCase("Left")){
                        SharedPref.write(SharedPref.LEFT, json);
                    }else{
                        SharedPref.write(SharedPref.RIGHT, json);
                    }
                }
                Toast.makeText(CalibrationActivity.this, "Save successfully", Toast.LENGTH_SHORT).show();

            }
        });

        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
                finish();
            }
        });
        ll_stop = findViewById(R.id.ll_stop);
        ll_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });
        ll_play = findViewById(R.id.ll_play);
        ll_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_gain.getText().toString().length()>0) {
                    SharedPref.write(SharedPref.GAIN, et_gain.getText().toString());
                }
                precalculateWeightedA();
                listeninggain();
            }
        });

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        2000);
            }
        }
        else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
        }
    }

    private void listeninggain() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BLOCK_SIZE * BYTES_PER_ELEMENT);

        if (mRecorder.getState() == 1)
            Log.d("nostro log", "Il recorder è pronto");
        else
            Log.d("nostro log", "Il recorder non è pronto");

        mRecorder.startRecording();
        isRecording = true;

        fft = new DoubleFFT_1D(BLOCK_SIZE_FFT);

        recordingThread = new Thread(new Runnable() {
            public void run() {
                // Array di raw data (tot : BLOCK_SIZE_FFT * 2 bytes)
                short rawData[] = new short[BLOCK_SIZE_FFT];

                // Array di mag non pesati (BLOCK_SIZE_FFT / 2 perchè è il numero di
                // bande utili)
                final float dbFft[] = new float[BLOCK_SIZE_FFT / 2];

                // Array di mag pesati
                final float dbFftA[] = new float[BLOCK_SIZE_FFT / 2];

                float normalizedRawData;

                // La fft lavora con double e con numeri complessi (re + im in
                // sequenza)
                double[] audioDataForFFT = new double[BLOCK_SIZE_FFT * 2];

                // Soglia di udibilita (20*10^(-6))
                float amplitudeRef = 0.00002f;



                // terzi ottave
                final float[] dbBand = new float[THIRD_OCTAVE.length];

                final float[] linearBand = new float[THIRD_OCTAVE.length];
                final float[] linearBandCount = new float[THIRD_OCTAVE.length];
                int n = 3;
//                float summingLinearBand = 0f;
//                int controllo_frequenze = 0;
//                int controllo_frequenze_1 = 0;

                // Variabili per calcolo medie Time Display
                int indexTimeDisplay = 1;
                double linearATimeDisplay = 0;


                // Variabili per calcolo medie Time Log
                int indexTimeLog = 0;
                double linearTimeLog = 0;
                double linearATimeLog = 0;
                final float[] linearBandTimeLog = new float[THIRD_OCTAVE.length];

                final float linearFftTimeDisplay[] = new float[BLOCK_SIZE_FFT / 2];
                final float linearFftATimeDisplay[] = new float[BLOCK_SIZE_FFT / 2];

                int initial_delay = 0;


                while (isRecording) {

                    // Leggo i dati
                    mRecorder.read(rawData, 0, BLOCK_SIZE_FFT);

                    // inserito un delay iniziale perché all'attivazione si avevano livelli molto alti di running leq (>100 dB) e minimi bassi (10 dB) dovuti forse all'attivazione inizale della periferica

                    initial_delay++;

                    if (initial_delay > 20) {

                        for (int i = 0, j = 0; i < BLOCK_SIZE_FFT; i++, j += 2) {

                            // Range [-1,1]
                            normalizedRawData = (float) rawData[i]
                                    / (float) Short.MAX_VALUE;

                            // filter = ((double) (fastA * normalizedRawData))
                            // + (fastB * filter);
                            filter = normalizedRawData;

                            // Finestra di Hannings
                            double x = (2 * Math.PI * i) / (BLOCK_SIZE_FFT - 1);
                            double winValue = (1 - Math.cos(x)) * 0.5d;

                            // Parte reale
                            audioDataForFFT[j] = filter * winValue;

                            // Parte immaginaria
                            audioDataForFFT[j + 1] = 0.0;
                        }

                        // FFT
                        fft.complexForward(audioDataForFFT);

                        // Magsum non pesati
                        double linearFftGlobal = 0;

                        // Magsum pesati
                        double linearFftAGlobal = 0;

                        // indice per terzi ottava
                        int k = 0;

                        for (int ki = 0; ki < THIRD_OCTAVE.length; ki++) {
                            linearBandCount[ki] = 0;
                            linearBand[ki] = 0;
                            dbBand[ki] = 0;
                        }

                        // Leggo fino a BLOCK_SIZE_FFT/2 perchè in tot ho BLOCK_SIZE_FFT/2
                        // bande utili
                        for (int i = 0, j = 0; i < BLOCK_SIZE_FFT / 2; i++, j += 2) {

                            double re = audioDataForFFT[j];
                            double im = audioDataForFFT[j + 1];

                            // Magnitudo
                            double mag = Math.sqrt((re * re) + (im * im));

                            // Ponderata A
                            // da capire: per i = 0 viene un valore non valido (forse meno infinito), ma ha senso?
                            // questo si ritrova poi nel grafico:
                            // per i=0 la non pesata ha un valore, mentre la pesata non ce l'ha...
                            double weightFormula = weightedA[i];

                            String gainStr =  et_gain.getText().toString();
                            if(gainStr.equalsIgnoreCase("")){
                                gainStr="0";
                            }
                            dbFft[i] = (float) (10 * Math.log10(mag * mag
                                    / amplitudeRef))
                                    + (float) Float.valueOf(gainStr);
                            dbFftA[i] = (float) (10 * Math.log10(mag * mag
                                    * weightFormula
                                    / amplitudeRef))
                                    + (float) Float.valueOf(gainStr);

                            linearFftGlobal += Math.pow(10, (float) dbFft[i] / 10f);
                            linearFftAGlobal += Math.pow(10, (float) dbFftA[i] / 10f);

                            float linearFft = (float) Math.pow(10, (float) dbFft[i] / 10f);


                            if ((0 <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 17.8f)) {
                                linearBandCount[0] += 1;
                                linearBand[0] += linearFft;
                                dbBand[0] = (float) (10 * Math.log10(linearBand[0]));
                            }
                            if ((17.8f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 22.4f)) {
                                linearBandCount[1] += 1;
                                linearBand[1] += linearFft;
                                dbBand[1] = (float) (10 * Math.log10(linearBand[1]));
                            }
                            if ((22.4f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 28.2f)) {
                                linearBandCount[2] += 1;
                                linearBand[2] += linearFft;
                                dbBand[2] = (float) (10 * Math.log10(linearBand[2]));
                            }
                            if ((28.2f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 35.5f)) {
                                linearBandCount[3] += 1;
                                linearBand[3] += linearFft;
                                dbBand[3] = (float) (10 * Math.log10(linearBand[3]));
                            }
                            if ((35.5f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 44.7f)) {
                                linearBandCount[4] += 1;
                                linearBand[4] += linearFft;
                                dbBand[4] = (float) (10 * Math.log10(linearBand[4]));
                            }
                            if ((44.7f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 56.2f)) {
                                linearBandCount[5] += 1;
                                linearBand[5] += linearFft;
                                dbBand[5] = (float) (10 * Math.log10(linearBand[5]));
                            }
                            if ((56.2f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 70.8f)) {
                                linearBandCount[6] += 1;
                                linearBand[6] += linearFft;
                                dbBand[6] = (float) (10 * Math.log10(linearBand[6]));
                            }
                            if ((70.8f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 89.1f)) {
                                linearBandCount[7] += 1;
                                linearBand[7] += linearFft;
                                dbBand[7] = (float) (10 * Math.log10(linearBand[7]));
                            }
                            if ((89.1f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 112f)) {
                                linearBandCount[8] += 1;
                                linearBand[8] += linearFft;
                                dbBand[8] = (float) (10 * Math.log10(linearBand[8]));
                            }
                            if ((112f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 141f)) {
                                linearBandCount[9] += 1;
                                linearBand[9] += linearFft;
                                dbBand[9] = (float) (10 * Math.log10(linearBand[9]));
                            }
                            if ((141f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 178f)) {
                                linearBandCount[10] += 1;
                                linearBand[10] += linearFft;
                                dbBand[10] = (float) (10 * Math.log10(linearBand[10]));
                            }
                            if ((178f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 224f)) {
                                linearBandCount[11] += 1;
                                linearBand[11] += linearFft;
                                dbBand[11] = (float) (10 * Math.log10(linearBand[11]));
                            }
                            if ((224f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 282f)) {
                                linearBandCount[12] += 1;
                                linearBand[12] += linearFft;
                                dbBand[12] = (float) (10 * Math.log10(linearBand[12]));
                            }
                            if ((282f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 355f)) {
                                linearBandCount[13] += 1;
                                linearBand[13] += linearFft;
                                dbBand[13] = (float) (10 * Math.log10(linearBand[13]));
                            }
                            if ((355f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 447f)) {
                                linearBandCount[14] += 1;
                                linearBand[14] += linearFft;
                                dbBand[14] = (float) (10 * Math.log10(linearBand[14]));
                            }
                            if ((447f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 562f)) {
                                linearBandCount[15] += 1;
                                linearBand[15] += linearFft;
                                dbBand[15] = (float) (10 * Math.log10(linearBand[15]));
                            }
                            if ((562f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 708f)) {
                                linearBandCount[16] += 1;
                                linearBand[16] += linearFft;
                                dbBand[16] = (float) (10 * Math.log10(linearBand[16]));
                            }
                            if ((708f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 891f)) {
                                linearBandCount[17] += 1;
                                linearBand[17] += linearFft;
                                dbBand[17] = (float) (10 * Math.log10(linearBand[17]));
                            }
                            if ((891f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 1122f)) {
                                linearBandCount[18] += 1;
                                linearBand[18] += linearFft;
                                dbBand[18] = (float) (10 * Math.log10(linearBand[18]));
                            }
                            if ((1122f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 1413f)) {
                                linearBandCount[19] += 1;
                                linearBand[19] += linearFft;
                                dbBand[19] = (float) (10 * Math.log10(linearBand[19]));
                            }
                            if ((1413f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 1778f)) {
                                linearBandCount[20] += 1;
                                linearBand[20] += linearFft;
                                dbBand[20] = (float) (10 * Math.log10(linearBand[20]));
                            }
                            if ((1778f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 2239f)) {
                                linearBandCount[21] += 1;
                                linearBand[21] += linearFft;
                                dbBand[21] = (float) (10 * Math.log10(linearBand[21]));
                            }
                            if ((2239f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 2818f)) {
                                linearBandCount[22] += 1;
                                linearBand[22] += linearFft;
                                dbBand[22] = (float) (10 * Math.log10(linearBand[22]));
                            }
                            if ((2818f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 3548f)) {
                                linearBandCount[23] += 1;
                                linearBand[23] += linearFft;
                                dbBand[23] = (float) (10 * Math.log10(linearBand[23]));
                            }
                            if ((3548f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 4467f)) {
                                linearBandCount[24] += 1;
                                linearBand[24] += linearFft;
                                dbBand[24] = (float) (10 * Math.log10(linearBand[24]));
                            }
                            if ((4467f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 5623f)) {
                                linearBandCount[25] += 1;
                                linearBand[25] += linearFft;
                                dbBand[25] = (float) (10 * Math.log10(linearBand[25]));
                            }
                            if ((5623f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 7079f)) {
                                linearBandCount[26] += 1;
                                linearBand[26] += linearFft;
                                dbBand[26] = (float) (10 * Math.log10(linearBand[26]));
                            }
                            if ((7079f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 8913f)) {
                                linearBandCount[27] += 1;
                                linearBand[27] += linearFft;
                                dbBand[27] = (float) (10 * Math.log10(linearBand[27]));
                            }
                            if ((8913f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 11220f)) {
                                linearBandCount[28] += 1;
                                linearBand[28] += linearFft;
                                dbBand[28] = (float) (10 * Math.log10(linearBand[28]));
                            }
                            if ((11220f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 14130f)) {
                                linearBandCount[29] += 1;
                                linearBand[29] += linearFft;
                                dbBand[29] = (float) (10 * Math.log10(linearBand[29]));
                            }
                            if ((14130f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 17780f)) {
                                linearBandCount[30] += 1;
                                linearBand[30] += linearFft;
                                dbBand[30] = (float) (10 * Math.log10(linearBand[30]));
                            }
                            if ((17780f <= i * FREQRESOLUTION) && (i * FREQRESOLUTION < 22390f)) {
                                linearBandCount[31] += 1;
                                linearBand[31] += linearFft;
                                dbBand[31] = (float) (10 * Math.log10(linearBand[31]));
                            }

                        }


                        final double dbFftAGlobal = 10 * Math.log10(linearFftAGlobal);

                        // calcolo min e max valore globale FFT pesato A
                        if (dbFftAGlobal > 0) {
                            if (dbFftAGlobalMinFirst == 0) {
                                dbFftAGlobalMinTemp = dbFftAGlobal;
                                dbFftAGlobalMinFirst = 1;
                            } else {
                                if (dbFftAGlobalMinTemp > dbFftAGlobal) {
                                    dbFftAGlobalMinTemp = dbFftAGlobal;
                                }
                            }
                            if (dbFftAGlobalMaxFirst == 0) {
                                dbFftAGlobalMaxTemp = dbFftAGlobal;
                                dbFftAGlobalMaxFirst = 1;
                            } else {
                                if (dbFftAGlobalMaxTemp < dbFftAGlobal) {
                                    dbFftAGlobalMaxTemp = dbFftAGlobal;
                                }
                            }
                        }
                        dbFftAGlobalMin = dbFftAGlobalMinTemp;
                        dbFftAGlobalMax = dbFftAGlobalMaxTemp;

                        Log.d(TAG, "Min: " + dbFftAGlobalMin);
                        Log.d(TAG, "Max: " + dbFftAGlobalMax);

                        tv_db.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_db.setText(String.valueOf(dbFftAGlobal));
                            }
                        });
                        tv_max.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_max.setText(String.valueOf(dbFftAGlobalMax));
                            }
                        });
                        tv_min.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_min.setText(String.valueOf(dbFftAGlobalMin));
                            }
                        });

                        // Running Leq
                        fftCount++;
                        linearFftAGlobalRunning += linearFftAGlobal;
                        dbFftAGlobalRunning = 10 * Math.log10(linearFftAGlobalRunning / fftCount);

                        for (int ki = 0; ki < THIRD_OCTAVE.length; ki++) {
                            linearBandRunning[ki] += linearBand[ki];
                            dbBandRunning[ki] = 10 * (float) Math.log10(linearBandRunning[ki] / fftCount);
                        }

                        // calcolo min e max per dbBand non pesato
                        // definisco minimi e massimi per le bande
                        for (int kk = 0; kk < dbBand.length; kk++) {
                            if (dbBandMax[kk] < dbBand[kk]) {
                                dbBandMax[kk] = dbBand[kk];
                            }
                            if (kkk >= 10) { // controllo per bontà leq bande: solo se kkk > 10 misurano bene
                                if (dbBandMin[kk] == 0f) {
                                    if (dbBand[kk] > 0) {
                                        dbBandMin[kk] = dbBand[kk];
                                    }
                                } else if (dbBandMin[kk] > dbBand[kk]) {
                                    dbBandMin[kk] = dbBand[kk];
                                }
                            }
                        }
                        kkk++;
                    }
                }
            }
        });

        recordingThread.start();
    }

    private void precalculateWeightedA() {
        for (int i = 0; i < BLOCK_SIZE_FFT; i++) {
            double actualFreq = FREQRESOLUTION * i;
            double actualFreqSQ = actualFreq * actualFreq;
            double actualFreqFour = actualFreqSQ * actualFreqSQ;
            double actualFreqEight = actualFreqFour * actualFreqFour;

            double t1 = 20.598997 * 20.598997 + actualFreqSQ;
            t1 = t1 * t1;
            double t2 = 107.65265 * 107.65265 + actualFreqSQ;
            double t3 = 737.86223 * 737.86223 + actualFreqSQ;
            double t4 = 12194.217 * 12194.217 + actualFreqSQ;
            t4 = t4 * t4;

            double weightFormula = (3.5041384e16 * actualFreqEight)
                    / (t1 * t2 * t3 * t4);

            weightedA[i] = weightFormula;
        }
    }

    private void stopRecording() {
        // stops the recording activity
        if (mRecorder != null) {
            isRecording = false;
            try {
                recordingThread.join();
                //fos.close();
            } catch (Exception e) {
                Log.d("nostro log",
                        "Il Thread principale non può attendere la chiusura del thread secondario dell'audio");
            }
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            recordingThread = null;
        }
    }
}
