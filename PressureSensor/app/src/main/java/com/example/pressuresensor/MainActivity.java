package com.example.pressuresensor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{

    private TextView textView;
    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private boolean isPressure;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView);
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)!=null){
            isPressure=true;
            pressureSensor=sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        }
        else {
            textView.setText("Pressure sensor not found");
            isPressure=false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPressure){
            sensorManager.registerListener(this,pressureSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
            textView.setText(sensorEvent.values[0]+"hPa");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}