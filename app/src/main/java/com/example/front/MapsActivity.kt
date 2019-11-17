package com.example.front


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
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
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.marker_list.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_maps.*
import android.location.Geocoder
import android.location.Address
import android.os.SystemClock
import android.widget.Button
import android.widget.EditText
import java.io.IOException
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.data.Spot
import com.google.android.gms.maps.model.BitmapDescriptorFactory


class MapsActivity : AppCompatActivity(), OnMapReadyCallback ,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var view: View
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var geocoder: Geocoder
    private lateinit var button: Button
    private lateinit var editText :EditText
    private var isFabOpen = false
    private lateinit var fab_open :Animation
    private lateinit var fab_close :Animation
    private lateinit var  onemarker :Marker
    private var isSelectedMarker = false

    private lateinit var gettheSpot :GettheSpot

 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
     // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment


        //맵 데이터 가져오기

        gettheSpot=GettheSpot()
        gettheSpot.takemap(this)
        gettheSpot.onaction()


        //search button
        editText =search_editText
        button=search_button

        //floating button
        initFloatingButtonAction()


        view = layoutInflater.inflate(R.layout.marker_list, null) //마커 클릭시


    }

    fun getmapAsnc(){

        //동기화
        mapFragment.getMapAsync(this)
    }



    private fun initFloatingButtonAction() {
         fab_open = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
         fab_close = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)

        val fab: FloatingActionButton = fab_main as FloatingActionButton
        val fab1: FloatingActionButton = fab_sub1 as FloatingActionButton
        val fab2: FloatingActionButton = fab_sub2 as FloatingActionButton
        fab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
            toggleFab()
            }
        })
        fab1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if(isSelectedMarker){
                    val nextIntent = Intent(this@MapsActivity, EnrollMarkerActivity::class.java)

                    nextIntent.putExtra("marker.lat",onemarker.position.latitude.toString())
                    nextIntent.putExtra("marker.long",onemarker.position.longitude.toString())
                    nextIntent.putExtra("marker.addr",latToadd(onemarker.position))
                    startActivity(nextIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                }
                toggleFab()
            }
        })
        fab2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val nextIntent=Intent(this@MapsActivity, GetHttpDataActivity::class.java)
                startActivity(nextIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                toggleFab()
            }
        })

    }
    fun toggleFab() {
        if (isFabOpen) {
            fab_main.setImageResource(R.drawable.ic_plus_24)
            fab_sub1.startAnimation(fab_close)
            fab_sub2.startAnimation(fab_close)
            fab_sub1.isClickable = false
            fab_sub2.isClickable = false
            isFabOpen = false
        }
        else {
            fab_main.setImageResource(R.drawable.ic_plus_24)
            fab_sub1.startAnimation(fab_open)
            fab_sub2.startAnimation(fab_open)
            fab_sub1.isClickable = true
            fab_sub2.isClickable = true
            isFabOpen = true
        }
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
        setFloatingButtonAction()
        geocoder = Geocoder(this)

        // for loop를 통한 n개의 마커 생성
        for (idx in 0..9) {
            val makerOptions = MarkerOptions()
            makerOptions
                    .position(LatLng(37.503444 + (idx*0.001), 126.957041 + (idx*0.001)))
                    .title("마커$idx") // 타이틀.
                    .snippet("박상오교수님사랑해요 - 최창환 - ")
            mMap.addMarker(makerOptions)
        }
//                 TODO: "modify spot uninitialize//
        for (spot :Spot in gettheSpot.spot) {
            Log.d("id", spot.id)
            Log.d("latitude", spot.latitude)
            Log.d("longitude", spot.longitude)
        }
        for (spot :Spot in gettheSpot.spot) {
            val makerOptions = MarkerOptions()
            makerOptions
                    .position(LatLng(spot.latitude.toDouble(), spot.longitude.toDouble()))
                    .title(spot.name) // 타이틀.
                    .snippet(spot.address)
            mMap.addMarker(makerOptions)
        }

        val zoomLevel = 16.0f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.503444, 126.957041), zoomLevel))

        //검색 버튼 클릭
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val str = editText.text.toString()
                lateinit var addressList: List<Address>
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10) // 최대 검색 결과 개수
                } catch (e: IOException) {
//                    e.printStackTrace()
                }

                // 콤마를 기준으로 split
                val splitStr = addressList[0].toString().split(",")
                val address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length - 2) // 주소
                val latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1) // 위도
                val longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1) // 경도


                // 좌표(위도, 경도) 생성
                val point = LatLng(java.lang.Double.parseDouble(latitude), java.lang.Double.parseDouble(longitude))
                // 마커 생성
                val mOptions2 = MarkerOptions()
                mOptions2
                    .title("search result")
                    .snippet(address)
                    .position(point)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                // 전 마커 삭제
                    deleteBeforeMaker()
                // 마커 추가
                onemarker=mMap.addMarker(mOptions2)


                // 해당 좌표로 화면 줌
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15f))
            }
        })
    }


    @SuppressLint("MissingPermission")
    private fun setFloatingButtonAction(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_NETWORK_STATE), 1)

        }
        //현재 위치 버튼
        mMap.isMyLocationEnabled = true

    }
    private fun latToadd(latLng: LatLng):String{
        var list: List<Address>? =null
        try {
            var d1 = latLng.latitude
            var d2  = latLng.longitude
            list = geocoder.getFromLocation(
                    d1, // 위도
                    d2, // 경도
                    10) // 얻어올 값의 개수

        } catch (e:IOException) {
            e.printStackTrace()
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
            return list!!.get(0).getAddressLine(0).toString()
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
        deleteBeforeMaker()
        isSelectedMarker=true
        Log.d("맵좌표","좌표: 위도(" + p0.latitude.toString()+ "),경도(" + (p0.longitude.toString()) + ")")
        val makerOptions = MarkerOptions()
        makerOptions
                .position(LatLng(p0.latitude, p0.longitude))
                .title("미정")
                .snippet("박상오교수님사랑해요 - 최창환 - ")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mapFragment.getMapAsync {
            onemarker=mMap.addMarker(makerOptions)

        }

    }
    private fun deleteBeforeMaker() {
        if(isSelectedMarker){
        onemarker.remove()
        }

    }

}