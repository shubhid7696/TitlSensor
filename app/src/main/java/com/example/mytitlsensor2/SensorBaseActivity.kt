package com.example.mytitlsensor2

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


@SuppressLint("Registered")
open class SensorBaseActivity : AppCompatActivity() {


    private var mSensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    // Gravity rotational data
    private var gravity: FloatArray? = null
    // Magnetic rotational data
    private var magnetic: FloatArray? = null //for magnetic rotational data
    private var accels: FloatArray? = FloatArray(3)
    private var mags: FloatArray? = FloatArray(3)
    private var values = FloatArray(3)

    // azimuth, pitch and roll
    private var azimuth: Float = 0.toFloat()
    private var pitch: Float = 0.toFloat()
    private var roll: Float = 0.toFloat()
    var prePitch = 0.0f
    var defValue = 0
    lateinit var mPitchVal: MyInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe()
    }

    override fun onStop() {
        unRegisterListener()
        super.onStop()
    }

/*    val observer by lazy {
        object : Observer<SensorEvent> {
        override fun onNext(t: SensorEvent) {
            println("onNext: $t")
            Log.d("### observer Thread:", Thread.currentThread().name)
        }

        override fun onSubscribe(d: Disposable) {
            println("onSubscribe")
        }

        override fun onError(e: Throwable) {
            println("onError: " + e.message)
        }

        override fun onComplete() {
            Log.d("###","onComplete")
        }
    }
    }*/

    var observable = Observable.create(ObservableOnSubscribe<SensorEvent> {

        initSensorManager()
    })

    fun initSensorManager() {

        val str = Thread.currentThread().name

        Log.d("### Current Thread:", str)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer  = mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        // initialize listener
        mSensorManager?.registerListener( sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL )
        mSensorManager?.registerListener( sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL )
    }

    var sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        override fun onSensorChanged(event: SensorEvent) {

            when (event.sensor.getType()) {
                Sensor.TYPE_ACCELEROMETER  -> { accels = event.values.clone() }
                Sensor.TYPE_MAGNETIC_FIELD -> { mags   = event.values.clone() }
            }

            if (mags != null && accels != null) {
                gravity = FloatArray(9)
                magnetic = FloatArray(9)
                SensorManager.getRotationMatrix(gravity, magnetic, accels, mags)
                val outGravity = FloatArray(9)
                SensorManager.remapCoordinateSystem( gravity, SensorManager.AXIS_X, SensorManager.AXIS_Z, outGravity )
                SensorManager.getOrientation(outGravity, values)
                pitch = values[1] * 57.29f
                //roll = values[2] * 57.2957795f
                mags      =  null
                accels    =  null
            }
            Log.d("### pitch", ""+pitch)

            val str = Thread.currentThread().name

            Log.d("### Current Thread:", str)
            mPitchVal.mPichVal(pitch)
        }
    }

    fun unRegisterListener() {
        if (mSensorManager != null) {
            mSensorManager?.unregisterListener(sensorEventListener)
        }
    }
}