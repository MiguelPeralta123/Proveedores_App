package com.learning.proveedoresapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learning.proveedoresapp.R
import com.learning.proveedoresapp.io.ApiService
import com.learning.proveedoresapp.model.Material
import com.learning.proveedoresapp.model.Proveedor
import com.learning.proveedoresapp.util.PreferenceHelper
import com.learning.proveedoresapp.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyMaterialsActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private val materialAdapter = MaterialAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_materials)

        val rvMaterials = findViewById<RecyclerView>(R.id.rv_materials)

        rvMaterials.layoutManager = LinearLayoutManager(this)
        rvMaterials.adapter = materialAdapter

        loadMateriales()
    }

    private fun loadMateriales() {
        val jwt = preferences["jwt", ""]
        val call = apiService.getMateriales("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Material>> {
            override fun onResponse(
                call: Call<ArrayList<Material>>,
                response: Response<ArrayList<Material>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        materialAdapter.materials = it
                        materialAdapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Material>>, t: Throwable) {
                Toast.makeText(this@MyMaterialsActivity, "Error al cargar las solicitudes de material", Toast.LENGTH_SHORT).show()
            }
        })
    }
}