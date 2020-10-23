package com.example.whatsappclone.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whatsappclone.Adapter.MainViewPagerAdapter
import com.example.whatsappclone.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPagerAdapter = MainViewPagerAdapter(supportFragmentManager, this)
        main_viewPager.adapter = viewPagerAdapter

        if (auth.currentUser != null) {
            val intent = Intent(this, BaseActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun changeItem(index : Int) {
        main_viewPager.setCurrentItem(index)
    }

}