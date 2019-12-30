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
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Semaphore


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("###","OnCreate BASE")

        val observer = object : Observer<SensorEvent> {
            override fun onNext(t: SensorEvent) {
                println("onNext: $t")
            }

            override fun onSubscribe(d: Disposable) {
                println("onSubscribe")
            }


            override fun onError(e: Throwable) {
                println("onError: " + e.message)
            }

            override fun onComplete() {
                println("onComplete")
            }
        }

        observable.subscribeOn(Schedulers.io()).subscribe(observer)
    }

    override fun onResume() {
        super.onResume()
        //initSensorManager()
        Log.d("###","OnResume BASE")
    }

    override fun onStop() {
        //unRegisterListener()
        Log.d("###","OnStop BASE")
        super.onStop()
    }


    var observable = Observable.create(object: ObservableOnSubscribe<SensorEvent> {
        override fun subscribe(emitter: ObservableEmitter<SensorEvent>) {

            mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
            accelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            magnetometer  = mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

            val sensorEventListener = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
                override fun onSensorChanged(event: SensorEvent) {
                    //If type is accelerometer only assign values to global property mGravity

                    /*
                    val sensorType = event.sensor.getType()

                    when (sensorType) {
                        Sensor.TYPE_ACCELEROMETER -> {
                            accels = event.values.clone();
                        }
                        Sensor.TYPE_MAGNETIC_FIELD -> {
                            mags = event.values.clone();
                        }
                    }

                    if (mags != null && accels != null) {
                        gravity = FloatArray(9)
                        magnetic = FloatArray(9)
                        SensorManager.getRotationMatrix(gravity, magnetic, accels, mags)
                        val outGravity = FloatArray(9)
                        SensorManager.remapCoordinateSystem( gravity, SensorManager.AXIS_X, SensorManager.AXIS_Z, outGravity )
                        SensorManager.getOrientation(outGravity, values)

                        //azimuth = values[0] * 57.2957795f
                        pitch = values[1]// * 57.29f
                        //roll = values[2] * 57.2957795f
                        mags      =  null
                        accels    =  null
                    }*/
/*
            EventBus.getDefault().post(MPojo(pitch))*/

                    Log.d("### pitch", "sENSOR EVENT")
                    Log.d("### Current Thread:", Thread.currentThread().name)
                    //defValue++
                    /*if(Math.abs(prePitch - pitch) > 0.7 *//*&& defValue > 4*//* ){

                prePitch = pitch
                //defValue = 0
            }*/


                }
            }

            mSensorManager?.registerListener( sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL )
            mSensorManager?.registerListener( sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL )

        }
    })




    fun initSensorManager() {
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer  = mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        /*GlobalScope.launch {
            withContext(Dispatchers.IO) {
                initListeners()
            }
        }*/

        initListeners()
        //initListeners()
    }

    fun initListeners() {
        mSensorManager?.registerListener( sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL )
        mSensorManager?.registerListener( sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL )
    }

    var sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        override fun onSensorChanged(event: SensorEvent) {
            //If type is accelerometer only assign values to global property mGravity
            val sensorType = event.sensor.getType()

            when (sensorType) {
                Sensor.TYPE_ACCELEROMETER -> {
                    accels = event.values.clone();
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    mags = event.values.clone();
                }
            }

            if (mags != null && accels != null) {
                gravity = FloatArray(9)
                magnetic = FloatArray(9)
                SensorManager.getRotationMatrix(gravity, magnetic, accels, mags)
                val outGravity = FloatArray(9)
                SensorManager.remapCoordinateSystem( gravity, SensorManager.AXIS_X, SensorManager.AXIS_Z, outGravity )
                SensorManager.getOrientation(outGravity, values)

                //azimuth = values[0] * 57.2957795f
                pitch = values[1]// * 57.29f
                //roll = values[2] * 57.2957795f
                mags      =  null
                accels    =  null
            }
/*
            EventBus.getDefault().post(MPojo(pitch))*/

            Log.d("### pitch", ""+pitch)
            //defValue++
            /*if(Math.abs(prePitch - pitch) > 0.7 *//*&& defValue > 4*//* ){

                prePitch = pitch
                //defValue = 0
            }*/
        }
    }

    fun unRegisterListener() {
        if (mSensorManager != null) {
            mSensorManager?.unregisterListener(sensorEventListener)
        }
    }
}