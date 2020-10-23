package com.example.whatsappclone.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.whatsappclone.Model.ChatsModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat

class ChatsViewModel : ViewModel() {

    var list = MutableLiveData<ArrayList<ChatsModel>>()
    var loading = MutableLiveData<Boolean>()
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    val hm = HashMap<String, String>()
    val data = ArrayList<ChatsModel>()


    fun getSohbet() {
        hm.clear()
        data.clear()

        db.collection("users").document(auth.currentUser?.email.toString()).collection("chats").addSnapshotListener { value, error ->
            if (error == null && value != null) {
                for (ds in value.documents) {
                    db.collection("users").document(ds.get("chat").toString()).addSnapshotListener { value, error ->
                        if (error == null && value != null) {
                            val username = value.get("username").toString()
                            val image = value.get("image").toString()
                            val mail = value.get("mail").toString()

                            db.collection("chats").document(ds.get("uuid").toString()).collection("messages").orderBy("date", Query.Direction.DESCENDING).limit(1).addSnapshotListener { value, error ->
                                if (error == null && value != null && value.size() > 0) {
                                    val lastMessage = value.documents.get(0).get("message").toString()
                                    val date = value.documents.get(0).get("date") as Timestamp
                                    val longTime = date.toDate().time
                                    val dateFormat = SimpleDateFormat("d MMM yyyy, HH:mm")
                                    val time = dateFormat.format(longTime)

                                    val model = ChatsModel(username, image, lastMessage, time, longTime, mail, "0")
                                    addData(model)
                                    getNews()

                                }
                            }
                        }
                    }
                }
            }
            if (value?.size() == 0) {
                println("empty value")
                loading.value = false
            }
        }

    }

    private fun getNews() {
        db.collection("users").document(auth.currentUser?.email.toString()).collection("chats").addSnapshotListener { value, error ->
            if (error == null && value != null && value.size() > 0) {
                for (ds in value) {
                    ds.getLong("new")?.let {
                        if (it.toInt() > 0) {
                            ds.get("chat").toString()
                            var i = 0
                            for (d in data) {
                                if (d.mail == ds.get("chat").toString()) {
                                    data.get(i).new = it.toInt().toString()
                                    list.value = data
                                    break
                                }
                                i++
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addData(model: ChatsModel) {

        var i = 0
        var ok = false
        for (d in data) {
            if (d.mail == model.mail) {
                data[i] = model
                ok = true
                break
            }
            i++
        }
        if (!ok) {
            data.add(model)
        }

        sortData()
        list.value = data
        loading.value = false
    }

    private fun sortData() {
        val sortedList = data.sortedByDescending { it.longTime }
        data.clear()
        data.addAll(sortedList)
    }

}


