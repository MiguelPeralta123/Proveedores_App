package com.learning.proveedoresapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.learning.proveedoresapp.R

class CreateProviderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_provider)

        val btnSave = findViewById<Button>(R.id.btn_save)
        btnSave.setOnClickListener {
            saveProvider()
        }
    }

    private fun saveProvider() {
        Toast.makeText(applicationContext, "Solicitud guardada con exito", Toast.LENGTH_SHORT).show()
        finish()
    }
}