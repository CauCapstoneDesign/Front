package com.example.adapter.contact

import com.example.data.list_item

interface BoarderTitleAdapterContact {

        interface View{
            var onClickFunc: ((Int)->Unit)?

        }
        interface Model {
            fun addItem(item: list_item)
            fun addItems(recyclerList: MutableList<list_item>)
            fun getItem(position: Int): list_item
            fun clearItem()
            fun getSize(): Int
        }
}