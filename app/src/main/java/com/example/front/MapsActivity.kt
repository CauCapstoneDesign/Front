package com.example.front

import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions



@Suppress("UNREACHABLE_CODE")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback ,GoogleMap.OnMapClickListener{

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        // for loop를 통한 n개의 마커 생성
        for (idx in 0..9) {
            val makerOptions = MarkerOptions()
            makerOptions
                    .position(LatLng(37.503444 + (idx*0.001), 126.957041 + (idx*0.001)))
                    .title("마커$idx") // 타이틀.
                    .snippet("박상오교수님사랑해요 - 최창환 - ")
            mMap.addMarker(makerOptions)
        }

        val zoomLevel = 16.0f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.503444, 126.957041), zoomLevel))

        /*
         mMap = googleMap
         // 제2공에 마커 추가하고 줌인하기
         val cau = LatLng(37.503444, 126.957041)
         val markerOptions = MarkerOptions()
         markerOptions.position(cau)
         markerOptions.title("제2공학관")
         markerOptions.snippet("대학원생 많은 제2공학관")
         mMap.addMarker(markerOptions)
         mMap.moveCamera(CameraUpdateFactory.newLatLng(cau))
         val zoomLevel = 16.0f //This goes up to 21
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cau, zoomLevel))
         */
    }

    override fun onMapClick(p0: LatLng?) {

        val screenPt = mMap.projection.toScreenLocation(p0)
        val latLng: LatLng = mMap.projection.fromScreenLocation(screenPt)
        Log.d("맵좌표","좌표: 위도(" + p0!!.latitude.toString()+ "),경도(" + (p0!!.longitude.toString()) + ")")

        val makerOptions = MarkerOptions()
        makerOptions
                .position(LatLng(p0!!.latitude, p0!!.longitude))

                .title("미정")
                .snippet("박상오교수님사랑해요 - 최창환 - ")
        mMap.addMarker(makerOptions)


    }

}