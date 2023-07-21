package com.learning.proveedoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.learning.proveedoresapp.util.PreferenceHelper
import com.learning.proveedoresapp.util.PreferenceHelper.get
import com.learning.proveedoresapp.util.PreferenceHelper.set

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if (preferences["session", false])
            goToMenu()

        // Navigating to menu view
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            goToMenu()
        }

        // Navigating to register view
        val textViewGoToRegister = findViewById<TextView>(R.id.textViewGoToRegister)
        textViewGoToRegister.setOnClickListener {
            goToRegister()
        }
    }

    private fun goToMenu() {
        val i = Intent(this, MenuActivity::class.java)
        createSessionPreference()
        startActivity(i)
        finish()
    }

    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    private fun createSessionPreference() {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["session"] = true
    }
}