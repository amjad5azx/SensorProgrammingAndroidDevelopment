package com.example.compass;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView textView;
    private ImageView imageView;
    private SensorManager sensorManager;
    private Sensor acceleormeter,magnetometer;

    private float[] lastAccelerometer=new float[3];
    private float[] lastMagnetometer=new float[3];
    private float[] rotationMatrix=new float[9];
    private float[] orientation=new float[3];

    private boolean isLastAccelerometerCopied=false;
    private boolean isLastMagnetometerCopied=false;

    long lastUpdatedTime=0;
    float currentDegree=0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView);
        imageView=findViewById(R.id.imageView);

        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleormeter=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this,acceleormeter);
        sensorManager.unregisterListener(this,magnetometer);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this,acceleormeter,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,magnetometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor==acceleormeter){
            System.arraycopy(sensorEvent.values,0,lastAccelerometer,0,sensorEvent.values.length);
            isLastAccelerometerCopied=true;
        } if(sensorEvent.sensor==magnetometer){
            System.arraycopy(sensorEvent.values,0,lastMagnetometer,0,sensorEvent.values.length);
            isLastMagnetometerCopied=true;
        }

        if(isLastAccelerometerCopied&&isLastAccelerometerCopied&&System.currentTimeMillis()-lastUpdatedTime>250){
            SensorManager.getRotationMatrix(rotationMatrix,null,lastAccelerometer,lastMagnetometer);
            SensorManager.getOrientation(rotationMatrix,orientation);

            float azimuthInRadian=orientation[0];
            float azimuthInDegree= (float) Math.toDegrees(azimuthInRadian);

            RotateAnimation rotateAnimation=new RotateAnimation(currentDegree,-azimuthInDegree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setDuration(250);
            rotateAnimation.setFillAfter(true);
            imageView.startAnimation(rotateAnimation);

            currentDegree=-azimuthInDegree;
            lastUpdatedTime=System.currentTimeMillis();

            int x=(int) azimuthInDegree;
            textView.setText(x+"°");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}