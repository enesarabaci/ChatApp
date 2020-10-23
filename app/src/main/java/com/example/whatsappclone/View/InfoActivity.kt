package com.example.whatsappclone.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.R
import com.example.whatsappclone.Util.loadImage
import com.example.whatsappclone.ViewModel.InfoViewModel
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    lateinit var viewModel : InfoViewModel
    var parent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val intent = getIntent()
        val mail = intent.getStringExtra("mail").toString()
        parent = intent.getStringExtra("parent").toString()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(InfoViewModel::class.java)
        viewModel.getData(mail)
        observeViewModel()

        setSupportActionBar(info_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun observeViewModel() {
        viewModel.info.observe(this, Observer {
            it?.let {
                info_ct.title = it.username
                info_about.text = it.about
                info_image.loadImage(it.image)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}