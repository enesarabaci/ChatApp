package com.example.whatsappclone.Util

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.loadImage(url : String) {
    if (url != "") {
        Picasso.get().load(url).fit().centerCrop().into(this)
    }
}