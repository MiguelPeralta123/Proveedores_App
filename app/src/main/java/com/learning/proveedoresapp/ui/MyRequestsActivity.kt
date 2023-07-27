package com.learning.proveedoresapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.learning.proveedoresapp.R

class MyRequestsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_requests)

        val btnProviders = findViewById<Button>(R.id.btn_providers)
        btnProviders.setOnClickListener {
            goToMyProviders()
        }

        val btnGoBack = findViewById<Button>(R.id.btn_menu)
        btnGoBack.setOnClickListener {
            goToMenu()
        }
    }

    private fun goToMyProviders() {
        val i = Intent(this, MyProvidersActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun goToMenu() {
        val i = Intent(this, MenuActivity::class.java)
        startActivity(i)
        finish()
    }
}