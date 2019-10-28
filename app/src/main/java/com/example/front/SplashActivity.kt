package com.example.front

import android.app.Activity

import android.os.Bundle
import android.content.Intent



class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Thread.sleep(2500)//2.5초
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        startActivity(Intent(this, LoginActivity::class.java))
        finish()


    }
}
