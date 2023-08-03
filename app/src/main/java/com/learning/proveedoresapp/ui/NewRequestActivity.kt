package com.learning.proveedoresapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.learning.proveedoresapp.R

class NewRequestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_request)

        val btnProvider = findViewById<Button>(R.id.btn_provider)
        btnProvider.setOnClickListener {
            goToCreateProvider()
        }

        val btnMaterial = findViewById<Button>(R.id.btn_material)
        btnMaterial.setOnClickListener {
            goToCreateMaterial()
        }

        val btnGoBack = findViewById<Button>(R.id.btn_menu)
        btnGoBack.setOnClickListener {
            goToMenu()
        }
    }

    private fun goToCreateProvider() {
        val i = Intent(this, CreateProviderActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun goToCreateMaterial() {
        val i = Intent(this, CreateMaterialActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun goToMenu() {
        val i = Intent(this, MenuActivity::class.java)
        startActivity(i)
        finish()
    }
}