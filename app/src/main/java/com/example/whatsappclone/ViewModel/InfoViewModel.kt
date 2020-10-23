package com.example.whatsappclone.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.whatsappclone.Model.InfoModel
import com.google.firebase.firestore.FirebaseFirestore

class InfoViewModel : ViewModel() {

    val info = MutableLiveData<InfoModel>()
    val db = FirebaseFirestore.getInstance()

    fun getData(mail : String) {
        db.collection("users").document(mail).get().addOnSuccessListener {
            info.value = InfoModel(it.get("username").toString(), it.get("about").toString(), it.get("image").toString())
        }
    }
}