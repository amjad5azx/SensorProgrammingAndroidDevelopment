package com.example.shakedetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView xtextView,ytextView,ztextView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccel,start=false;
    private float currentX,currentY,currentZ,lastX,lastY,lastZ;
    private float xDifference,yDifference,zDifference;
    private float shakeThreshold=5f;
    private Vibrator vibrator;
    private Flashlight flashlight;

    private boolean flash=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xtextView=findViewById(R.id.xtextView);
        ytextView=findViewById(R.id.ytextView);
        ztextView=findViewById(R.id.ztextView);
        flashlight=new Flashlight(this);

        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccel=true;
        }
        else{
            xtextView.setText("Acceleormeter Sensor not available");
            ytextView.setText("");
            ztextView.setText("");
            isAccel=false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isAccel){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isAccel){
            sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        xtextView.setText(sensorEvent.values[0]+" m/s²");
        ytextView.setText(sensorEvent.values[1]+" m/s²");
        ztextView.setText(sensorEvent.values[2]+" m/s²");

        currentX=sensorEvent.values[0];
        currentY=sensorEvent.values[1];
        currentZ=sensorEvent.values[2];

        if(start){
            xDifference=Math.abs(lastX-currentX);
            yDifference=Math.abs(lastY-currentY);
            zDifference=Math.abs(lastZ-currentZ);

            if((xDifference>shakeThreshold&&yDifference>shakeThreshold)||
                    (yDifference>shakeThreshold&&zDifference>shakeThreshold)||
                    (xDifference>shakeThreshold&&zDifference>shakeThreshold)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if(!flash){
                        flashlight.turnOn();
                        flash=true;
                    }
                    else{
                        flashlight.turnOff();
                        flash=false;
                    }
                    vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                }
                else{
                    vibrator.vibrate(500);
                    if(!flash){
                        flashlight.turnOn();
                        flash=true;
                    }
                    else{
                        flashlight.turnOff();
                        flash=false;
                    }
                }
            }

        }

        lastX=currentX;
        lastY=currentY;
        lastZ=currentZ;
        start=true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}