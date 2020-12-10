package com.ix.ibrahim7.lightsensor

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import kotlin.properties.Delegates


class MyService : Service(),SensorEventListener {


    lateinit var mSensorManager: SensorManager
    var isFlashAvailable =  false
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null
    var play by Delegates.notNull<Boolean>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {

            isFlashAvailable = applicationContext.packageManager
                    .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val mAccelerometer: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST)
            mCameraManager =  getSystemService(Context.CAMERA_SERVICE) as CameraManager
            mCameraId = mCameraManager!!.cameraIdList[0]
            play=true
            Log.e("eee try", "Try")
        } catch (e: Exception) {
            Log.e("eee ERROR", e.message.toString())
        }


        return super.onStartCommand(intent, flags, startId)
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_LIGHT) {
            val x= event.values[0]

            if (x > 20){
                if (!play) {
                    switchFlashLight(false)
                    play = true
                }
            }else{
                if (play) {
                    switchFlashLight(true)
                    play = false
                }
            }
            Log.e("eeee change", "value $x")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun switchFlashLight(status: Boolean) {
        try {
            mCameraManager!!.setTorchMode(mCameraId!!, status)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    override fun onDestroy() {
        val intent = Intent(this,MyPhoneReciver::class.java)
        sendBroadcast(intent)
        super.onDestroy()
    }

}