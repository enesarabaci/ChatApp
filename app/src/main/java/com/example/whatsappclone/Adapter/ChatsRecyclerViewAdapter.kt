package com.example.whatsappclone.Adapter

import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.Model.ChatsModel
import com.example.whatsappclone.R
import com.example.whatsappclone.Util.loadImage
import com.example.whatsappclone.View.ChatActivity
import com.example.whatsappclone.View.InfoActivity
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.pp_dialog.*
import kotlinx.android.synthetic.main.sohbetler_row.view.*

class ChatsRecyclerViewAdapter(var list : ArrayList<ChatsModel>, val context : Fragment) : RecyclerView.Adapter<ChatsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.sohbetler_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.sohbetler_row_user.text = list.get(position).user
        holder.itemView.sohbetler_row_lastMessage.text = list.get(position).lastMessage
        holder.itemView.sohbetler_row_date.text = list.get(position).time
        holder.itemView.sohbetler_row_image.loadImage(list.get(position).image)
        holder.itemView.sohbetler_new.visibility = View.GONE
        if (list.get(position).new != "0") {
            println("neww")
            holder.itemView.sohbetler_new.visibility = View.VISIBLE
            holder.itemView.sohbetler_new.text = list.get(position).new
        }

        holder.itemView.sohbetler_row_sohbet.setOnClickListener {
            val intent = Intent(context.context, ChatActivity::class.java)
            intent.putExtra("name", list.get(position).user)
            intent.putExtra("mail", list.get(position).mail)
            context.startActivity(intent)
        }
        holder.itemView.sohbetler_row_image.setOnClickListener {
            val dialog = Dialog(context.requireContext())
            dialog.setContentView(R.layout.pp_dialog)
            dialog.show()
            dialog.pp_image.loadImage(list.get(position).image)
            dialog.pp_chat.setOnClickListener {
                val intent = Intent(context.context, ChatActivity::class.java)
                intent.putExtra("name", list.get(position).user)
                intent.putExtra("mail", list.get(position).mail)
                context.startActivity(intent)
                dialog.dismiss()
            }
            dialog.pp_profile.setOnClickListener {
                val intent = Intent(context.context, InfoActivity::class.java)
                intent.putExtra("mail", list.get(position).mail)
                context.startActivity(intent)
                dialog.dismiss()
            }
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }

    fun updateList(newList : ArrayList<ChatsModel>) {
        list = newList
        notifyDataSetChanged()
    }

}