package com.learning.proveedoresapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.learning.proveedoresapp.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val textViewGoToLogin = findViewById<TextView>(R.id.tv_go_to_login)
        textViewGoToLogin.setOnClickListener {
            goToLogin()
        }
    }

    private fun goToLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}