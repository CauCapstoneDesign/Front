package com.example.front

import android.content.ContentValues
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.RequestHttpURLConnection
import com.example.adapter.BoarderTitleAdapter
import com.example.adapter.contact.BoarderTitleAdapterContact
import com.example.data.list_item
import kotlinx.android.synthetic.main.activity_boarder_title.*
import org.json.JSONArray
import org.json.JSONException

class BoarderTitleActivity : AppCompatActivity() {
    val upLoadServerUri ="http://18.222.119.238:3000/board/getpost"
    lateinit var boarderTitleAdapter : BoarderTitleAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var boarderTitleView : BoarderTitleAdapterContact.View
    lateinit var boarderTitleModel : BoarderTitleAdapterContact.Model
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boarder_title)
        onaction(0,5)

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = recycler1
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
//        titleAdapter=BoarderTitleAdapter()
        boarderTitleAdapter=BoarderTitleAdapter(this)
        boarderTitleView=boarderTitleAdapter
        boarderTitleModel=boarderTitleAdapter
        boarderTitleView.onClickFunc= {position -> onClickListener(position) }
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
            Log.d("offset,onPostExecute",s)
            //동기화
        }
        private fun jsonParsing(json: String) {
            try {
                //parsing
                val jsonArray= JSONArray(json)
                for (i:Int in 0 until jsonArray.length()) {
                    var title=jsonArray.getJSONObject(i).get("title")
                    var id =jsonArray.getJSONObject(i).get("id")
                    var creation_time =jsonArray.getJSONObject(i).get("creation_time")
                    var author_id = jsonArray.getJSONObject(i).get("author_id")
                    boarderTitleModel.addItem(list_item(0,author_id.toString(),title.toString(),creation_time.toString(),id.toString()))
                    recyclerView.adapter=boarderTitleAdapter
                }

//            var a = String(bytes)
            } catch (e: JSONException) {
                e.printStackTrace()
            }


        }

    }
    fun onaction(offset: Int,limit: Int){

        val url = upLoadServerUri
        val values= ContentValues()
        values.put("offset", offset)
        values.put("limit", limit)
        Log.d("offset,limit",offset.toString()+limit.toString())
        val networkTask = NetworkTask(url,values)
        networkTask.execute()
    }
        private fun onClickListener(position: Int) {
            Log.d("Test3",position.toString())
        val nextIntent = Intent(this, BoarderActivity::class.java)
            nextIntent.putExtra("position",position)
//            nextIntent.putExtra("")
        startActivity(nextIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

    }
}
