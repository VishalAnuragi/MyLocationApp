package com.example.mylocationapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var TAG : String = "TAG_MAIN_ACTIVITY"

    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var myJob : Job

    private lateinit var latTV : TextView
    private lateinit var lonTV : TextView

    private val TIME_INTERVAL = 5*60*1000L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        latTV = findViewById(R.id.latitudeTV)
        lonTV = findViewById(R.id.longitudeTV)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



    }

    override fun onStart() {
        super.onStart()

        myJob = startRepeatingJob(TIME_INTERVAL)
    }

    override fun onStop() {
        super.onStop()

        myJob.cancel()
    }

    @OptIn(InternalCoroutinesApi::class)
    private fun startRepeatingJob(timeInterval: Long): Job {
        return CoroutineScope(Dispatchers.Default).launch {

            while (NonCancellable.isActive) {
                Log.d(TAG, "Starting coroutine in thread = ${Thread.currentThread().name}")
                getCurrentLocation()
                delay(timeInterval)
            }
        }
    }

    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||  ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED) {
            // request permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE), LOCATION_PERMISSION_REQ_CODE);

        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location
                try {
                    latitude = location.latitude
                    longitude = location.longitude
                    latTV.text = latitude.toString()
                    lonTV.text = longitude.toString()

                    crateAndWriteOnFile(latitude , longitude)
                }catch (e: Exception){
                    e.stackTrace
                }
            }
            .addOnFailureListener {
            }
    }

    private fun crateAndWriteOnFile(latitude: Double, longitude: Double) {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDateTime: String = dateFormat.format(Date())

        val text = "$currentDateTime ::: Latitude : $latitude , Longitude : $longitude"

        val FILE_NAME = "example.txt";


        val fOut = openFileOutput(
            FILE_NAME,
            MODE_APPEND
        )
        val osw = OutputStreamWriter(fOut)
        osw.write(text)
        osw.appendLine()
        osw.flush()
        osw.close()

    }



}