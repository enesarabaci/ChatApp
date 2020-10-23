package com.example.whatsappclone.Adapter

import android.graphics.drawable.Drawable
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.Model.ChatModel
import com.example.whatsappclone.R
import kotlinx.android.synthetic.main.chat_row1.view.*
import kotlinx.android.synthetic.main.chat_row2.view.*

class ChatRecyclerViewAdapter(var list : ArrayList<ChatModel>, val mail : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        if (list.get(position).user == mail) {
            return 0
        }else {
            return 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_row1, parent, false)
            return ViewHolder1(view)
        }else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_row2, parent, false)
            return ViewHolder2(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list.get(position).user == mail) {
            val viewHolder = holder as ViewHolder1
            viewHolder.chatMessage.text = list.get(position).chat
            viewHolder.chatTime.text = list.get(position).time
            viewHolder.chatMessageBox.setBackgroundResource(R.drawable.message_shape_second)
            if (list.get(position).first == true) {
                viewHolder.chatMessageBox.setBackgroundResource(R.drawable.message_shape_first)
            }

        }else {
            val viewHolder = holder as ViewHolder2
            viewHolder.chatMessage.text = list.get(position).chat
            viewHolder.chatTime.text = list.get(position).time
            viewHolder.chatMessageBox.setBackgroundResource(R.drawable.message_shape_fourth)
            if (list.get(position).first == true) {
                viewHolder.chatMessageBox.setBackgroundResource(R.drawable.message_shape_third)
            }
        }
    }

    class ViewHolder1(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var chatMessage : TextView
        lateinit var chatTime : TextView
        lateinit var chatMessageBox : LinearLayout
        init {
            chatMessage = view.chat_row_message1
            chatTime = view.chat_row_time1
            chatMessageBox = view.chat_row_messageBox1
        }
    }
    class ViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var chatMessage : TextView
        lateinit var chatTime : TextView
        lateinit var chatMessageBox : LinearLayout
        init {
            chatMessage = view.chat_row_message2
            chatTime = view.chat_row_time2
            chatMessageBox = view.chat_row_messageBox2
        }
    }

    public fun updateList(newList : ArrayList<ChatModel>) {
        list = newList
        update()
        notifyDataSetChanged()
    }

    private fun update() {
        var last = 0
        for (cm in list) {
            if (cm.user == mail && last != 1) {
                cm.first = true
                last = 1
            }
            if (cm.user != mail && last != 2) {
                cm.first = true
                last = 2
            }
        }
    }

}