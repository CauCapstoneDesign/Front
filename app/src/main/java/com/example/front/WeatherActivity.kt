package com.example.front

import android.content.ContentValues
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adapter.WeatherAdapter
import com.example.data.WeatherData

import kotlinx.android.synthetic.main.activity_weather.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList
import javax.net.ssl.HttpsURLConnection

class WeatherActivity: AppCompatActivity() {

    internal var urlString = "https://api.openweathermap.org/data/2.5/forecast?lat=37.476200&lon=126.973154&appid=1fb33d62bd23230319e28ca3a894c212"
    private var str: String? = null
    private var receiveMsg: String? = null
    private val temp = ArrayList<String>()
    private val w_description = ArrayList<String>()
    private val humidity = ArrayList<String>()
    private val dt_text = ArrayList<String>()
    private var sunrise: Int = 0
    private var sunset: Int = 0
    private var city: String? = null
    private var timezone: Int = 0
    private var wdata = ArrayList<WeatherData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val networkTask = NetworkTask()
        networkTask.execute(Unit)
    }
    inner class NetworkTask : AsyncTask<Unit, Unit, String>() {
        //        val loadData = object : AsyncTask<Unit, Unit, String>() {
        override fun doInBackground(vararg params: Unit?): String {
            var weatherUrl: URL? = null
            try {
                weatherUrl = URL("https://api.openweathermap.org/data/2.5/forecast?lat=37.476200&lon=126.973154&appid=1fb33d62bd23230319e28ca3a894c212")
                val myConnection = weatherUrl.openConnection() as HttpsURLConnection
                if (myConnection.responseCode == 200) {
                    Log.d("fuck", "3")
                    val responseBody = myConnection.inputStream
                    Log.d("fuck", "4")
                    val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
                    val reader = BufferedReader(responseBodyReader)
                    val buffer = StringBuffer()
                    str = reader.readLine()
                    while (str != null) {
                        buffer.append(str)
                        str = reader.readLine()
                    }
                    receiveMsg = buffer.toString()
                    val jsonObject = JSONObject(receiveMsg!!)
                    val weatherArray = jsonObject.getJSONArray("list")
                    val cityInfo = jsonObject.getJSONObject("city")

                    for (i in 0..weatherArray.length()) {
                        val weatherObject = weatherArray.getJSONObject(i)
                        var main = weatherObject.getJSONObject("main")
                        temp.add((main.getString("temp").toInt()-273).toString())

                        humidity.add(main.getString("humidity"))
                        Log.d("asdf", humidity[i])
                        dt_text.add(weatherObject.getString("dt_txt"))
                        Log.d("asdf", dt_text[i])
                        val w = weatherObject.getJSONArray("weather")
                        main = w.getJSONObject(0)
                        w_description.add(main.getString("description"))
                        Log.d("asdf", w_description[i])
                        wdata.add(WeatherData(temp[i], humidity[i],dt_text[i], w_description[i]))
                    }
                    city = cityInfo.getString("name")
                    sunrise = cityInfo.getInt("sunrise")
                    sunset = cityInfo.getInt("sunset")
                    timezone = cityInfo.getInt("timezone")

                } else {
                    // Error handling code goes here
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return "success"
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // Show progress from UI thread
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            display()
        }
    }
    fun display(){
        rv_main_list.adapter = WeatherAdapter(wdata)
        rv_main_list.layoutManager = LinearLayoutManager(this)
    }
}
