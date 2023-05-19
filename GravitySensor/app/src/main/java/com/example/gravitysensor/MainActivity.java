package com.example.gravitysensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView xtextView,ytextView,ztextView;
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private boolean isGravity;

    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        xtextView=findViewById(R.id.xtextView);
        ytextView=findViewById(R.id.ytextView);
        ztextView=findViewById(R.id.ztextView);


        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)!=null){
            gravitySensor=sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            isGravity=true;
        }
        else{
            xtextView.setText("Gravity Sensor Not Found");
            ytextView.setText("");
            ztextView.setText("");
            isGravity=false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isGravity){
            sensorManager.unregisterListener(this,gravitySensor);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isGravity){
            sensorManager.registerListener(this,gravitySensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        xtextView.setText(sensorEvent.values[0]+" m/s²");
        ytextView.setText(sensorEvent.values[1]+" m/s²");
        ztextView.setText(sensorEvent.values[2]+" m/s²");

        if(sensorEvent.values[2]<=9.7){
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }
        else{
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}