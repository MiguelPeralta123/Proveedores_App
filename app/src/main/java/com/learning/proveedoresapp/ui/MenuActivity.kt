package com.learning.proveedoresapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.learning.proveedoresapp.R
import com.learning.proveedoresapp.util.PreferenceHelper
import com.learning.proveedoresapp.util.PreferenceHelper.set

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Handling new request button
        val btnGoToNewRequest = findViewById<Button>(R.id.btn_new_request)
        btnGoToNewRequest.setOnClickListener {
            goToNewRequest()
        }

        // Handling my requests button
        val btnGoToMyRequests = findViewById<Button>(R.id.btn_my_requests)
        btnGoToMyRequests.setOnClickListener {
            goToMyRequests()
        }

        // Handling logout button
        val btnLogout = findViewById<Button>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            clearSessionPreference()
            goToLogin()
        }
    }

    private fun goToNewRequest() {
        val i = Intent(this, NewRequestActivity::class.java)
        startActivity(i)
    }

    private fun goToMyRequests() {
        val i = Intent(this, MyRequestsActivity::class.java)
        startActivity(i)
    }

    private fun goToLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun clearSessionPreference() {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["jwt"] = ""
    }
}