package edu.wisc.ece.minilab4task2;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/* Shyamal H Anadkat */
public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepCounter, stepDetector, dirSensor, accSensor;
    private TextView cntr_txt, dir_txt, cntr_txt_acc;
    private int stepCount = 0;
    private final double up_threshold = 1.9;
    private final double low_threshold = 0.9;
    boolean went_above = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cntr_txt = findViewById(R.id.step_cntr);
        dir_txt = findViewById(R.id.step_dir);
        cntr_txt_acc = findViewById(R.id.step_cntr_acc);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        dirSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this,
                stepCounter,
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this,
                stepDetector,
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this,
                dirSensor,
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this,
                accSensor,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        float[] cntrVals;
        if (sensorEvent.values != null && sensorEvent.values.length > 0)

        {
            if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                cntrVals = sensorEvent.values;
                cntr_txt.setText(Integer.toString((int) cntrVals[0]));
            }

            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                // Movement values
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                double mag = Math.sqrt(Math.pow(x, 2)
                        + Math.pow(y, 2)
                        + Math.pow(z, 2));
                double magNoG = mag - sensorManager.STANDARD_GRAVITY;

                if (magNoG >= up_threshold) {
                    went_above = true;
                }
                if (magNoG <= low_threshold) {
                    if (went_above) {
                        stepCount++;
                        went_above = false;
                    }
                }
                cntr_txt_acc.setText(Integer.toString(stepCount));
            }

            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
                float degree = sensorEvent.values[0];
                String dir = "";

                if (degree >= 0 && degree < 45) {
                    dir = "N";
                } else if (degree >= 90 && degree < 135) {
                    dir = "E";
                } else if (degree >= 180 && degree < 225) {
                    dir = "S";
                } else if (degree >= 270 && degree < 315) {
                    dir = "W";
                } else if (degree >= 45 && degree < 90) {
                    dir = "NE";
                } else if (degree >= 135 && degree < 180) {
                    dir = "SE";
                } else if (degree >= 225 && degree < 270) {
                    dir = "SW";
                } else if (degree >= 315 && degree < 360) {
                    dir = "NW";
                }
                dir_txt.setText(dir);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and involved sensors
        sensorManager.registerListener(this, stepCounter,
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, stepDetector,
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, dirSensor,
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, accSensor,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        // unregister listener to save battery
        super.onPause();
        sensorManager.unregisterListener(this, stepCounter);
        sensorManager.unregisterListener(this, stepDetector);
        sensorManager.unregisterListener(this, dirSensor);
        sensorManager.unregisterListener(this, accSensor);
    }
}
