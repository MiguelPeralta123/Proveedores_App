package com.learning.proveedoresapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learning.proveedoresapp.R
import com.learning.proveedoresapp.io.ApiService
import com.learning.proveedoresapp.model.Proveedor
import com.learning.proveedoresapp.util.PreferenceHelper
import com.learning.proveedoresapp.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProvidersActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private val providerAdapter = ProviderAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_providers)

        val rvProviders = findViewById<RecyclerView>(R.id.rv_providers)

        rvProviders.layoutManager = LinearLayoutManager(this)
        rvProviders.adapter = providerAdapter

        loadProveedores()
    }

    private fun loadProveedores() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getProveedores("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Proveedor>> {
            override fun onResponse(
                call: Call<ArrayList<Proveedor>>,
                response: Response<ArrayList<Proveedor>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        providerAdapter.providers = it
                        providerAdapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Proveedor>>, t: Throwable) {
                Toast.makeText(this@MyProvidersActivity, "Error al cargar las solicitudes de proveedor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}