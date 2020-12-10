package com.ix.ibrahim7.lightsensor

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity()  {


    lateinit var sensorManager: SensorManager
    lateinit var sensor: Sensor

    var play by Delegates.notNull<Boolean>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        }else{
            Toast.makeText(this,"not found", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onStop() {
        ContextCompat.startForegroundService(this,Intent(this@MainActivity, MyService::class.java))
        super.onStop()
    }

}