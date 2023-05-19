package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView textViewStepCounter,textViewStepDetector;
    private SensorManager sensorManager;
    private Sensor sensorCounter,sensorDetector;
    private boolean isStepCounter,isStepDetector;
    private int stepCount=0,stepDetect=0;

    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textViewStepCounter=findViewById(R.id.textViewStepCounter);
        textViewStepDetector=findViewById(R.id.textViewStepDetector);

        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isStepCounter=true;
        }
        else{
            textViewStepCounter.setText("Step Counter Sensor Not Found");
            isStepCounter=false;
        }

        if(sensorManager.getDefaultSensor((Sensor.TYPE_STEP_DETECTOR))!=null){
            sensorDetector=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            isStepDetector=true;
        }
        else{
            textViewStepDetector.setText("Step Detector Sensor Not Found");
            isStepDetector=false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.unregisterListener(this,sensorCounter);
        }

        if(sensorManager.getDefaultSensor((Sensor.TYPE_STEP_DETECTOR))!=null){
            sensorManager.unregisterListener(this,sensorDetector);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.registerListener(this,sensorCounter,SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(sensorManager.getDefaultSensor((Sensor.TYPE_STEP_DETECTOR))!=null){
            sensorManager.registerListener(this,sensorDetector,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor==sensorCounter){
            stepCount=(int) sensorEvent.values[0];
            textViewStepCounter.setText(String.valueOf(stepCount));
        }

        else if(sensorEvent.sensor==sensorDetector){
            stepDetect=(int) sensorEvent.values[0];
            textViewStepDetector.setText(String.valueOf(stepDetect));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}