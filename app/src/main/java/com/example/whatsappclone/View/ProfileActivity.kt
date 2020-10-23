package com.example.whatsappclone.View

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone.R
import com.example.whatsappclone.Util.loadImage
import com.example.whatsappclone.ViewModel.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    lateinit var viewModel : ProfileViewModel
    var name = ""
    var about = ""
    var image = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ProfileViewModel::class.java)
        viewModel.getDatas()
        observeViewModel()

        profile_image.setOnClickListener {
            imagePermission()
        }

        setSupportActionBar(profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        profile_editName.setOnClickListener {
            bottomSheet("username")
        }
        profile_editAbout.setOnClickListener {
            bottomSheet("about")
        }

    }

    private fun observeViewModel() {
        viewModel.name.observe(this, Observer {
            it?.let {
                name = it
                profile_name.text = name
            }
        })
        viewModel.about.observe(this, Observer {
            it?.let {
                about = it
                profile_about.text = about
            }
        })
        viewModel.image.observe(this, Observer {
            it?.let {
                image = it
                profile_image.loadImage(image)
            }
        })
    }

    private fun bottomSheet(key : String) {
        val bottomSheet = BottomSheetDialog(this)
        bottomSheet.setContentView(R.layout.profile_bottom_sheet)
        val title = bottomSheet.findViewById<TextView>(R.id.profile_bs_title)
        val nameText = bottomSheet.findViewById<EditText>(R.id.profile_bs_name)
        val cancel = bottomSheet.findViewById<Button>(R.id.profile_bs_cancel)
        val save = bottomSheet.findViewById<Button>(R.id.profile_bs_save)
        bottomSheet.show()

        if(key == "username") {
            title?.text = "Enter your name"
            nameText?.setText(name)

            save?.setOnClickListener {
                viewModel.saveData(nameText?.text.toString(), "username", this)
                bottomSheet.dismiss()
            }
        }
        else if(key == "about") {
            title?.text = "Enter your tag"
            nameText?.setText(about)

            save?.setOnClickListener {
                viewModel.saveData(nameText?.text.toString(), "about", this)
                bottomSheet.dismiss()
            }
        }

        cancel?.setOnClickListener {
            bottomSheet.dismiss()
        }
    }

    private fun imagePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1 && grantResults.size > 0 && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && data != null && resultCode == Activity.RESULT_OK) {
            val uri = data.data
            uri?.let {
                viewModel.loadImage(uri, this)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

}