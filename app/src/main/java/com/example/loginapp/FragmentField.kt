package com.example.loginapp


import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_field.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*


class FragmentField : Fragment() {

    private lateinit var userDao: AppDatabase
    private val job = Job()
    private val coroutineContext = CoroutineScope(job + Dispatchers.IO)
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_field, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDao = AppDatabase(context ?: return)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context ?: return)
        surveyEdt.isEnabled = false
        if (!checkStoragePermission()) {
            requestStoragePermission()
        } else {
            coroutineContext.launch {
                surveyEdt.setText(userDao.userDao().getId().toString())
            }
        }
        dateEdt.setOnClickListener {
            datePicker()
        }
        nextBtn.setOnClickListener {
            insertDb()
            //activity?.onBackPressed()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(callback: ((Location?) -> Unit)) {
        if (checkLocationPermissions()) {

            mFusedLocationClient.lastLocation.addOnCompleteListener(this.requireActivity()) { task ->
                val location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                }
                callback.invoke(location)
            }
        } else {
            Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
            requestLocationPermissions()
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity as MainActivity)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
        }
    }

//    private fun isLocationEnabled(): Boolean {
//        val locationManager: LocationManager = getSystemService(this as MainActivity,AppCompatActivity.LOCATION_SERVICE) as LocationManager
//        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//            LocationManager.NETWORK_PROVIDER
//        )
//    }
//
//    fun isExternalStorageAvailable(): Boolean {
//        val extStorageState = Environment.getExternalStorageState()
//        return if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
//            true
//        } else {
//            false
//        }
//    }

    private fun checkLocationPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity as MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity as MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun checkStoragePermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity as MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            activity as MainActivity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            PERMISSION_ID
        )
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            activity as MainActivity,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                coroutineContext.launch {

                    surveyEdt.setText(userDao.userDao().getId().toString())
                }
                getLastLocation(callback = {

                })


            }
        }
    }

    private fun datePicker() {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
                val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                dateEdt.setText(dat)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun insertDb() {
        getLastLocation(callback = {
            if (it != null) {
                val location = "${it.latitude},${it.longitude}"
                val logIn = User()
                coroutineContext.launch {
                    logIn.surveyId = surveyEdt.text.toString().toInt()
                    logIn.surveyorName = surveyorNameEdt.text.toString()
                    logIn.zone = zoneEdt.text.toString()
                    logIn.district = districtEdt.text.toString()
                    logIn.structureType = structureTypeEdt.text.toString()
                    logIn.constructionType = constructionTypeEdt.text.toString()
                    logIn.roadClassification = roadClassificationEdt.text.toString()
                    logIn.roadNumber = roadNumberEdt.text.toString()
                    logIn.startEasting = startEastingEdt.text.toString()
                    logIn.startNorthing = startNorthingEdt.text.toString()
                    logIn.endNorthing = endNorthingEdt.text.toString()
                    logIn.endEasting = endEastingEdt.text.toString()
                    logIn.noOfSpan = noOfSpanEdt.text.toString()
                    logIn.widthOfStructure = widthOfStructureEdt.text.toString()
                    logIn.spanLength = spanLenghtEdt.text.toString()
                    logIn.overAllCollection = overAllCollectionEdt.text.toString()
                    logIn.date = dateEdt.text.toString()
                    logIn.latLong = location
                    userDao.userDao().insertAll(logIn)
                }
                loadFragmentCamera()
            } else {
                Toast.makeText(context, "location is null", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadFragmentCamera() {
       val intent = Intent(context, CameraActivity::class.java)
        startActivity(intent)
    }

}