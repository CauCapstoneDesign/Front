package com.example.front

import android.os.AsyncTask
import com.example.RequestHttpURLConnection
import android.content.ContentValues
import android.util.Base64
import org.json.JSONException
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Log
import org.json.JSONArray
import java.io.ByteArrayInputStream

class GetthePhoto {
    private lateinit var mapActivity: MapsActivity
    lateinit var bitmap :Bitmap
    val upLoadServerUri ="http://18.222.119.238:3000/spot/getspotphoto"
    fun onaction(id: String){

        val url = upLoadServerUri
        val values= ContentValues()
        values.put("spot_id",id)
        Log.d("id",id)
        val networkTask = NetworkTask(url,values)
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
    private fun jsonParsing(json: String) {
        try {
            //parsing
            val jsonArray=JSONArray(json)
            val jsonObject=jsonArray.getJSONObject(0)
            val photo_url = jsonObject.getJSONObject("photo_url")
            val photoArray = photo_url.getJSONArray("data")
            val bytes = ByteArray(photoArray.length())
            for (i in 0 until photoArray.length()) {
                bytes[i] = (photoArray.get(i) as Int and 0xFF).toByte()
            }
            Log.d("bytes",String(bytes))
//            var a = String(bytes)

            val bImage = Base64.decode(String(bytes), 0)
            val bais = ByteArrayInputStream(bImage)
            bitmap = BitmapFactory.decodeStream(bais)



        } catch (e: JSONException) {
            e.printStackTrace()
        }


    }

    }
}