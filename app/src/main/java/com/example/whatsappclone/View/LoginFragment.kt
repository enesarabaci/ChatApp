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
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment(val main : MainActivity) : Fragment() {

    val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_to_signUp.setOnClickListener{
            main.changeItem(1)
        }
        login.setOnClickListener {
            if (login_mail.text.toString() == "" || login_password.text.toString() == "") {
                Toast.makeText(main, "Please fill in the blank fields", Toast.LENGTH_SHORT).show()
            }else {
                auth.signInWithEmailAndPassword(login_mail.text.toString(), login_password.text.toString()).addOnSuccessListener {
                    val intent = Intent(main, BaseActivity::class.java)
                    startActivity(intent)
                    main.finish()
                }.addOnFailureListener { exception ->
                    Toast.makeText(main, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}