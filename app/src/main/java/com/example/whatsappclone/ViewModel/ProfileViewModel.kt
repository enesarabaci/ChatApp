package com.example.whatsappclone.ViewModel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModel : ViewModel() {

    var name = MutableLiveData<String>()
    var about = MutableLiveData<String>()
    var image = MutableLiveData<String>()
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()

    fun getDatas() {

        db.collection("users").document(auth.currentUser?.email.toString()).get().addOnSuccessListener {
            name.value = it.get("username").toString()
            about.value = it.get("about").toString()
            image.value = it.get("image").toString()
        }

    }

    fun saveData(data : String, info : String, context : Context) {
        if (info == "username") {
            db.collection("users").document(auth.currentUser?.email.toString()).update("username", data).addOnSuccessListener {
                Toast.makeText(context, "Username changed..", Toast.LENGTH_SHORT).show()
                name.value = data
            }
        }
        else if(info == "about") {
            db.collection("users").document(auth.currentUser?.email.toString()).update("about", data).addOnSuccessListener {
                Toast.makeText(context, "About changed..", Toast.LENGTH_SHORT).show()
                about.value = data
            }
        }
    }

    fun loadImage(uri : Uri, context : Context) {
        val mail = auth.currentUser?.email.toString()
        val ref = storage.reference.child("images").child(mail)
        ref.putFile(uri).addOnSuccessListener {
            val uploadRef = storage.reference.child("images").child(mail)
            uploadRef.downloadUrl.addOnSuccessListener {
                it?.let { imageUri ->
                    db.collection("users").document(mail).update("image", it.toString()).addOnSuccessListener {
                        Toast.makeText(context, "Image saved..", Toast.LENGTH_SHORT).show()
                        image.value = imageUri.toString()
                    }
                }
            }
        }
    }

}