package com.example.whatsappclone.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.whatsappclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment(val main : MainActivity) : Fragment() {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUp_to_login.setOnClickListener {
            main.changeItem(0)
        }

        signup.setOnClickListener {
            if (register_mail.text.toString() == "" || register_password.text.toString() == " " || register_username.text.toString() == "") {
                Toast.makeText(main, "Please fill in the blank fields", Toast.LENGTH_SHORT).show()
            }else {
                auth.createUserWithEmailAndPassword(register_mail.text.toString(), register_password.text.toString()).addOnSuccessListener {
                    val hm = HashMap<String, Any>()
                    hm.put("username", register_username.text.toString())
                    hm.put("password", register_password.text.toString())
                    hm.put("mail", register_mail.text.toString())
                    hm.put("about", "")
                    hm.put("image", "")
                    db.collection("users").document(register_mail.text.toString()).set(hm).addOnSuccessListener {
                        val intent = Intent(main, BaseActivity::class.java)
                        startActivity(intent)
                        main.finish()
                    }.addOnFailureListener { exception ->
                        Toast.makeText(main, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(main, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

}