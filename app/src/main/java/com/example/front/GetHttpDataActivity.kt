package com.example.front

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.os.AsyncTask
import com.example.RequestHttpURLConnection
import android.content.ContentValues
import android.util.Log
import com.example.data.Spot
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_get_http_data.*
import org.json.JSONException




class GetHttpDataActivity   : AppCompatActivity(){

    lateinit private var Httpview :TextView
    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_http_data)

        Httpview=httpview
        val url ="http://18.222.119.238:3000/Spot/getspot"

        var networkTask = NetworkTask(url, null)
        networkTask.execute()

    }
    inner class NetworkTask(private val url: String?, private val values: ContentValues?) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String? {

            val result: String? // 요청 결과를 저장할 변수.
            val requestHttpURLConnection = RequestHttpURLConnection()
            result = requestHttpURLConnection.request(url!!, values) // 해당 URL로 부터 결과물을 얻어온다.

            return result
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            Httpview.text=s
            jsonParsing(s!!)
        }
    }

    private fun jsonParsing(json: String) {
        try {
            val spot = gson.fromJson(json, Array<Spot>::class.java)
            Log.d("spot","spot의 id는"+ spot[1].id)
        } catch (e: JSONException) {
            e.printStackTrace()
        }


    }
}

