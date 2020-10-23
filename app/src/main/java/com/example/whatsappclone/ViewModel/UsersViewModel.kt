package com.example.whatsappclone.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.whatsappclone.Model.ChatsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UsersViewModel : ViewModel() {

    var list = MutableLiveData<ArrayList<ChatsModel>>()
    var loading = MutableLiveData<Boolean>()
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    fun getSohbet() {
        db.collection("users").get().addOnSuccessListener {
            val data = ArrayList<ChatsModel>()
            for (ds in it.documents) {
                if (auth.currentUser?.email.toString() != ds.get("mail").toString()) {
                    val model = ChatsModel(ds.get("username").toString(), ds.get("image").toString(), "", "", 0, ds.get("mail").toString(), "0")
                    
                    data.add(model)
                    loading.value = false
                }
            }
            list.value = data
        }.addOnFailureListener { exception ->
            loading.value = true
        }
    }

}