package com.learning.proveedoresapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learning.proveedoresapp.R
import com.learning.proveedoresapp.model.Proveedor

class MyProvidersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_providers)

        val rvProviders = findViewById<RecyclerView>(R.id.rv_providers)

        // Test providers
        val providers = ArrayList<Proveedor>()
        providers.add(
            Proveedor("Sonorg", "Computadoras del norte", "COMP1905189R1", "", "S.A. de C.V.",
                "Banorte", "0101010101", "010101010101010101", "MXP")
        )
        providers.add(
            Proveedor("Tago", "Juan Pérez", "PECJ1905189R1", "PECJ190518HSRRLS06",
                "S.A. de C.V.", "BBVA", "0101010101", "010101010101010101", "MXP")
        )
        providers.add(
            Proveedor("Sonorg - Tago - Wichita - Moonrise - Wellin", "Multiservicios de Sonora",
                "COMP1905189R1", "", "S.A. de C.V.", "Santander", "0101010101",
                "010101010101010101", "USD")
        )

        rvProviders.layoutManager = LinearLayoutManager(this)
        rvProviders.adapter = ProviderAdapter(providers)
    }
}