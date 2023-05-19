package com.example.thirdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView textView;
    private SensorManager sensorManager;
    private float changedValue;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView);
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
            changedValue=sensorEvent.values[0];
            textView.setText(String.valueOf(changedValue));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //interface,type of sensor,time of sensor
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_NORMAL);
    }
}