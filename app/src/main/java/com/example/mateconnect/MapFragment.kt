package com.example.mateconnect

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

//
class MapFragment : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    private var database = FirebaseFirestore.getInstance()
    private var Name = database.collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_map)
        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }
    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location

                Toast.makeText(applicationContext, currentLocation.latitude.toString() + "" +
                        currentLocation.longitude, Toast.LENGTH_SHORT).show()
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        var latLng = LatLng(44.6652059,-63.5677427)
        //LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("ABCD!")
        googleMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.getUiSettings()?.setZoomControlsEnabled(true)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap?.addMarker(markerOptions)



        latLng = LatLng(44.651070,-63.582687)
        val markerOptions1 = MarkerOptions().position(latLng).title("GUT here")
        //googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.getUiSettings()?.setZoomControlsEnabled(true)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap?.addMarker(markerOptions1)

        latLng = LatLng(44.6388974444, -63.5737310384)
        val markerOptions2 = MarkerOptions().position(latLng).title("John Ham")
        //googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.getUiSettings()?.setZoomControlsEnabled(true)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap?.addMarker(markerOptions2)

        latLng = LatLng(44.630719, -63.50816)
        val markerOptions3 = MarkerOptions().position(latLng).title("OPA")
        //googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.getUiSettings()?.setZoomControlsEnabled(true)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap?.addMarker(markerOptions3)

        latLng = LatLng(44.659102,-63.588315)
        val markerOptions4 = MarkerOptions().position(latLng).title("XYZ HERE!")
        //googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.getUiSettings()?.setZoomControlsEnabled(true)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap?.addMarker(markerOptions4)

        latLng = LatLng(44.7258, -63.6540)
        val markerOptions5 = MarkerOptions().position(latLng).title("JOHN")
        //googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.getUiSettings()?.setZoomControlsEnabled(true)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap?.addMarker(markerOptions5)

        latLng = LatLng(44.6603, -63.6462)
        val markerOptions6 = MarkerOptions().position(latLng).title("KIJ")
        //googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.getUiSettings()?.setZoomControlsEnabled(true)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap?.addMarker(markerOptions6)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

}
