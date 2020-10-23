package com.example.whatsappclone.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsappclone.Adapter.ChatRecyclerViewAdapter
import com.example.whatsappclone.Model.ChatModel
import com.example.whatsappclone.R
import com.example.whatsappclone.ViewModel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    lateinit var viewModel : ChatViewModel
    var list = ArrayList<ChatModel>()
    val auth = FirebaseAuth.getInstance()
    val adapter = ChatRecyclerViewAdapter(list, auth.currentUser?.email.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setSupportActionBar(toolbar_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val name = getIntent().getStringExtra("name").toString()
        val mail = getIntent().getStringExtra("mail").toString()
        supportActionBar?.title = name

        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        chat_rv.layoutManager = linearLayoutManager
        chat_rv.adapter = adapter

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ChatViewModel::class.java)
        viewModel.getChats(mail)
        viewModel.deleteNews(mail)

        chat_send_button.setOnClickListener{
            val message = messageBox.text.toString()
            viewModel.sendMessage(message, mail, this)
            messageBox.text.clear()
        }

        observeViewModel()

        toolbar_chat.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra("mail", mail)
            intent.putExtra("parent", "ChatActivity")
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.list.observe(this, Observer {
            it?.let {
                list = it
                adapter.updateList(list)
                chat_rv.smoothScrollToPosition(list.size -1)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val intent = Intent(this, BaseActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val intent = Intent(this, BaseActivity::class.java)
        startActivity(intent)
        this.finish()
    }

}