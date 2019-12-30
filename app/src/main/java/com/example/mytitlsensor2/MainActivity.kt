package com.example.mytitlsensor2

import android.hardware.SensorEvent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.subjects.BehaviorSubject


class MainActivity : SensorBaseActivity() {

    private val proxy = BehaviorSubject.create<SensorEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("###","OnCreate MAIN")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPitchVal = object : MyInterface {
            override fun mPichVal(mP: Float) {
                Log.d("### ", "mY pITCH vAL = $mP")
            }
        }
    }

    override fun onResume() {
        Log.d("###","OnResume MAIN")
        super.onResume()
        //initSensorManager()
    }

    override fun onStop() {
        //unRegisterListener()
        Log.d("###","OnStop MAIN")
        super.onStop()
    }
}
