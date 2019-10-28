package com.example.front


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.marker_list.view.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback ,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var view: View
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        view = layoutInflater.inflate(R.layout.marker_list, null)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("onMapReady()", "ready")
        mMap = googleMap
        mapFragment.getMapAsync {
            mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {

                // Use default InfoWindow frame
                override fun getInfoWindow(p0: Marker): View? {
                    return null
                }

                override fun getInfoContents(p0: Marker): View? {
                    Log.d("getInfoContents()","marker click")
                    val addressTxt = view.addressTxt as TextView
                    addressTxt.text = p0.title
                    val mobileTxt =view.mobileTxt as TextView
                    mobileTxt.text=p0.snippet
                    return view
                }
            })
        }
        mMap.setOnMapClickListener(this)
        mMap.setOnMarkerClickListener(this)
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

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        val center = CameraUpdateFactory.newLatLng(p0!!.position)
        mMap.animateCamera(center)
        Log.d("onMarkerClick", "Click here")
        p0.showInfoWindow()
        return true
    }

    override fun onMapClick(p0: LatLng?) {
        drawMarker(p0!!)
    }
    private fun drawMarker(p0: LatLng) {
        Log.d("맵좌표","좌표: 위도(" + p0!!.latitude.toString()+ "),경도(" + (p0.longitude.toString()) + ")")
        val makerOptions = MarkerOptions()
        makerOptions
                .position(LatLng(p0.latitude, p0.longitude))
                .title("미정")
                .snippet("박상오교수님사랑해요 - 최창환 - ")
        mapFragment.getMapAsync {
            mMap.addMarker(makerOptions)
        }
    }

}