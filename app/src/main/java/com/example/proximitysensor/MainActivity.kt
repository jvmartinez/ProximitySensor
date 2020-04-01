package com.example.proximitysensor

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.proximityssensor.R


class MainActivity : AppCompatActivity(), SensorEventListener{
    private lateinit var icoSensor: ImageView
    private lateinit var lblTitleSensor:  TextView
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        icoSensor = findViewById(R.id.ico_sensor)
        lblTitleSensor = findViewById(R.id.lbl_title_sensor)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if (sensor == null) {
            icoSensor.setColorFilter(Color.YELLOW)
            lblTitleSensor.setText("Sensor not available")
        }
        executeSensor()
    }

    override fun onAccuracyChanged(sensorEvent: Sensor?, accuracy: Int) {
        when (accuracy) {
            SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                Log.i("Main Activity", "Sensor status unreliable")
            }
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                Log.i("Main Activity", "Sensor status accuracy low")
            }
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> {
                Log.i("Main Activity", "Sensor status accuracy high")
            }
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
                Log.i("Main Activity", "Sensor status accuracy medium")
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.values[0]  <  sensor!!.maximumRange ) {
                icoSensor.setColorFilter(Color.GREEN)
                lblTitleSensor.setText("Sensor Active")
            } else {
                icoSensor.setColorFilter(Color.RED)
                lblTitleSensor.setText("Sensor Inactive")
            }
        }
    }

    override fun onPause() {
        stopSensor()
        super.onPause()
    }

    override fun onResume() {
        executeSensor()
        super.onResume()
    }

    fun executeSensor() {
        sensorManager?.registerListener(this, sensor, 3000 * 2000)
    }

    fun stopSensor() {
        sensorManager?.unregisterListener(this)
    }
}
