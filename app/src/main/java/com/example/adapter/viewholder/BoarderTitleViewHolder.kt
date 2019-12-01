package com.example.adapter.viewholder

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.data.list_item
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class BoarderTitleViewHolder(private val context: Context, parent: ViewGroup?, val clickListenerFunc: ((Int) -> Unit)?)
    :RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(com.example.front.R.layout.recyclerview_item, parent, false)) {
    private val author_id by lazy {
        itemView.author_id_textview as TextView
    }
    private val titleTextView by lazy {
        itemView.title_textview as TextView
    }
    private val dataTextView by lazy {
        itemView.date_textview as TextView
    }

    fun onBind(data: list_item, position: Int) {
        author_id.text=data.author_id
        titleTextView.text = data.title
        dataTextView.text= data.write_date

        itemView.setOnClickListener {
//                 position 가져오는 함수인데 노필요
//                val pos = adapterPosition
            Log.d("Test1",position.toString())
                if (position != RecyclerView.NO_POSITION) {
                    Log.d("Test2",position.toString())
                    clickListenerFunc?.invoke(position)

            }

        }
    }
}