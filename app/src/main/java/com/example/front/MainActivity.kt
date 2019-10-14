package com.example.front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adultbutton = adultversion
        adultbutton.setOnClickListener {
            val nextIntent = Intent(this, MapsActivity::class.java)
            startActivity(nextIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

        }




    }
}
