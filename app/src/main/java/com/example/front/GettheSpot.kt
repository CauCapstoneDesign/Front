package com.example.front

import android.os.AsyncTask
import com.example.RequestHttpURLConnection
import android.content.ContentValues
import android.util.Log
import com.example.data.Spot
import com.google.gson.Gson
import org.json.JSONException

class GettheSpot {
    lateinit var spot  :Array<Spot>
//    private  val mapsActivity = MapsActivity()
    private lateinit var mapActivity: MapsActivity
    var gson = Gson()
    val url ="http://18.222.119.238:3000/Spot/getspot"
    fun onaction(){

        var networkTask = NetworkTask(url, null)
        networkTask.execute()

    }
     fun takemap(mapActivity: MapsActivity) {
         this.mapActivity = mapActivity
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
            jsonParsing(s!!)
            //동기화

            mapActivity.getmapAsnc()


        }
    }

    private fun jsonParsing(json: String) {
        try {
             spot = gson.fromJson(json, Array<Spot>::class.java)

        } catch (e: JSONException) {
            e.printStackTrace()
        }


    }
}