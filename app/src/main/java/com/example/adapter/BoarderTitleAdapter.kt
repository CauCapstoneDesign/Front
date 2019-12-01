package com.example.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.contact.BoarderTitleAdapterContact
import com.example.adapter.viewholder.BoarderTitleViewHolder
//import com.example.adapter.contact.BoarderTitleAdapterContact
import com.example.data.list_item

class BoarderTitleAdapter(private val context: Context) :RecyclerView.Adapter<BoarderTitleViewHolder>(),BoarderTitleAdapterContact.View,BoarderTitleAdapterContact.Model{

    override var onClickFunc: ((Int) -> Unit)? = null
    private var itemList: MutableList<list_item> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoarderTitleViewHolder
        = BoarderTitleViewHolder(context,parent,onClickFunc)

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holderTitle: BoarderTitleViewHolder, position: Int) {
        holderTitle.onBind(itemList[position], position)
    }
    override fun addItem(item: list_item) {
        itemList.add(item)
    }

    override fun addItems(recyclerList: MutableList<list_item>) {
        this.itemList = recyclerList
    }
    override fun clearItem() {
        itemList = arrayListOf()
    }
     override fun getItem(position: Int): list_item = itemList[position]
     override fun getSize(): Int = itemList.size
}