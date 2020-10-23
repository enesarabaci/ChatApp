package com.example.whatsappclone.ViewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.whatsappclone.Model.ChatModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatViewModel : ViewModel() {

    var list = MutableLiveData<ArrayList<ChatModel>>()
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    var open = true

    fun getChats(mail : String) {
        db.collection("users").document(auth.currentUser?.email.toString()).collection("chats").whereEqualTo("chat", mail).addSnapshotListener { value, error ->
            if (error != null) {
                println(error.localizedMessage)
            }else {
                if (value?.documents?.size != 0) {
                    val uuid = value?.documents?.get(0)?.get("uuid").toString()
                    getChatsFromDB(uuid)
                }
            }
        }

        db.collection("users").document(auth.currentUser?.email.toString()).collection("chats").whereEqualTo("chat", mail).get().addOnSuccessListener {
            if (it.documents.size != 0) {
                val uuid = it.documents.get(0).get("uuid").toString()
                getChatsFromDB(uuid)
            }
        }
    }

    fun deleteNews(mail : String) {
        db.collection("users").document(auth.currentUser?.email.toString()).collection("chats").whereEqualTo("chat", mail).addSnapshotListener { value, error ->
            if (error == null && value != null && value.size() > 0 && open) {
                val uuid = value.documents.get(0).get("uuid").toString()
                db.collection("users").document(auth.currentUser?.email.toString()).collection("chats").document(uuid).update("new", 0).addOnSuccessListener {

                }.addOnFailureListener {
                    println("deleteNews error.")
                }
            }
        }
    }

    private fun getChatsFromDB(uuid : String) {
        val data = ArrayList<ChatModel>()

        db.collection("chats").document(uuid).collection("messages").addSnapshotListener { value, error ->
            if (error != null) {
                println(error.localizedMessage)
            }else {
                if (value != null) {
                    data.clear()
                    for (ds in value.documents) {
                        val message = ds.get("message") as String
                        val date = ds.get("date") as Timestamp
                        val longTime = date.toDate().time
                        val user = ds.get("user").toString()

                        val dateFormat = SimpleDateFormat("d MMM yyyy, HH:mm")
                        val time = dateFormat.format(date.toDate().time)

                        val chat = ChatModel(message, user, time, longTime)
                        data.add(chat)
                    }
                    if (data.size > 0) {
                        sortArray(data)
                    }
                }
            }
        }
    }

    fun sendMessage(message : String, mail : String, context: Context) {
        checkUuid(mail, message, context)
    }

    private fun checkUuid(mail : String, message : String, context : Context) {
        var uuid = ""
        db.collection("users").document(auth.currentUser?.email.toString()).collection("chats").whereEqualTo("chat", mail).get().addOnSuccessListener {
            if (it.documents.size != 0) {
                uuid = it.documents.get(0).get("uuid").toString()
                sendMessageToID(uuid, message, context, mail)
            }else {
                createUuid(mail, message, context)
            }
        }
    }

    private fun createUuid(mail : String, message : String, context : Context) {
        val uuid = UUID.randomUUID().toString()
        val hm1 = HashMap<String, Any>()
        hm1.put("uuid", uuid)
        hm1.put("chat", mail)
        hm1.put("new", 0)
        val hm2 = HashMap<String, Any>()
        hm2.put("uuid", uuid)
        hm2.put("chat", auth.currentUser?.email.toString())
        hm2.put("new", 0)

        db.collection("users").whereEqualTo("mail", auth.currentUser?.email.toString()).get().addOnSuccessListener {
            if (it.documents.size > 0) {
                val username : String = it.documents.get(0).get("username").toString()
                //hm2.put("username", username)

                db.collection("users").document(auth.currentUser?.email.toString()).collection("chats").document(uuid).set(hm1).addOnSuccessListener {
                    db.collection("users").document(mail).collection("chats").document(uuid).set(hm2).addOnSuccessListener {
                        sendMessageToID(uuid, message, context, mail)
                    }
                }
            }
        }
    }

    private fun sendMessageToID(uuid : String, message : String, context : Context, mail : String) {
        val msg = HashMap<kotlin.String, kotlin.Any>()
        msg.put("message", message)
        msg.put("user", auth.currentUser?.email.toString())
        msg.put("date", com.google.firebase.Timestamp.now().toDate())
        db.collection("chats").document(uuid).collection("messages").add(msg).addOnSuccessListener {

            db.collection("users").document(mail).collection("chats").document(uuid).get().addOnSuccessListener {
                var new = 1
                it.getLong("new")?.let {
                    new = it.toInt()
                    new++
                }
                db.collection("users").document(mail).collection("chats").document(uuid).update("new", new).addOnSuccessListener {
                    Toast.makeText(context, "gönderildi", android.widget.Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { exception ->
                    println("update error ${exception.localizedMessage}")
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(context, exception.localizedMessage, android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun sortArray(data : ArrayList<ChatModel>) {
        val sortedList = data.sortedBy { it.longTime }
        data.clear()
        data.addAll(sortedList)
        list.value = data
    }

    override fun onCleared() {
        open = false
        super.onCleared()
    }



}