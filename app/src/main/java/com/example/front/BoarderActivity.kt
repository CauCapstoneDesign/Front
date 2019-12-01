package com.example.front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_boarder.*

class BoarderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boarder)
//        var textView=asd

        var boarder_id =intent.getIntExtra("position",0)
//        asd.text=boarder_id.toString()

    }
}
