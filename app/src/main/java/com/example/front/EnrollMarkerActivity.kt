package com.example.front

import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_enroll_marker.*
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.io.IOException


class EnrollMarkerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enroll_marker)

        val addressname = addressname
        val addImageButton = addImageButton
        val intent = intent



        val d1 = intent.getStringExtra("marker.lat")
        val d2 = intent.getStringExtra("marker.long")
        val addr =intent.getStringExtra("marker.addr")
        Log.d("인텐트 맵좌표","좌표: 위도(" + d1.toString()+ "),경도(" + (d2.toString()) + ")"+addr.toString())
        addressname.text=addr




    }

}
