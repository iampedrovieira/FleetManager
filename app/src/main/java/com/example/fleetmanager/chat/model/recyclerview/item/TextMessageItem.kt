package com.example.fleetmanager.chat.model.recyclerview.item

import android.content.Context
import com.example.fleetmanager.R
import com.example.fleetmanager.chat.model.TextMessage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.ViewHolder

class TextMessageItem(val message: TextMessage, val context: Context):Item() {
    override fun bind(viewHolder: com.xwray.groupie.kotlinandroidextensions.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getLayout() = R.layout.item_text_message


}