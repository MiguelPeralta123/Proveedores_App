package com.learning.proveedoresapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.learning.proveedoresapp.R
import com.learning.proveedoresapp.io.ApiService
import com.learning.proveedoresapp.io.response.LoginResponse
import com.learning.proveedoresapp.util.PreferenceHelper
import com.learning.proveedoresapp.util.PreferenceHelper.get
import com.learning.proveedoresapp.util.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if (preferences["jwt", ""].contains("."))
            goToMenu()

        // Navigating to menu view
        val btnLogin = findViewById<Button>(R.id.btn_login)
        btnLogin.setOnClickListener {
            performLogin()
        }

        // Navigating to register view
        val textViewGoToRegister = findViewById<TextView>(R.id.tv_go_to_register)
        textViewGoToRegister.setOnClickListener {
            goToRegister()
        }
    }

    private fun goToMenu() {
        val i = Intent(this, MenuActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    private fun createSessionPreference(jwt: String) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["jwt"] = jwt
    }

    private fun performLogin() {
        val username = findViewById<EditText>(R.id.et_username).text.toString()
        val password = findViewById<EditText>(R.id.et_password).text.toString()

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(applicationContext, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val call = apiService.postLogin(username, password)
        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse == null) {
                        Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (loginResponse.success) {
                        createSessionPreference(loginResponse.jwt)
                        goToMenu()
                    }
                    else {
                        Toast.makeText(applicationContext, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}