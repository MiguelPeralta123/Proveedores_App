package com.learning.proveedoresapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import com.learning.proveedoresapp.R
import com.learning.proveedoresapp.io.ApiService
import com.learning.proveedoresapp.model.Empresa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateProviderActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_provider)

        val btnSave = findViewById<Button>(R.id.btn_save)
        btnSave.setOnClickListener {
            // Field validation
            val linearLayoutCreateProvider = findViewById<LinearLayout>(R.id.linear_layout_create_provider)
            val etNombre = findViewById<EditText>(R.id.et_nombre)
            val etRfc = findViewById<EditText>(R.id.et_rfc)
            val etCuenta = findViewById<EditText>(R.id.et_cuenta)
            val etClabe = findViewById<EditText>(R.id.et_clabe)

            etNombre.error = if (etNombre.text.toString().isEmpty()) "Campo obligatorio" else null
            etRfc.error = if (etRfc.text.toString().isEmpty()) "Campo obligatorio" else null
            etCuenta.error = if (etCuenta.text.toString().isEmpty()) "Campo obligatorio" else null
            etClabe.error = if (etClabe.text.toString().isEmpty()) "Campo obligatorio" else null

            if (etNombre.error != null || etRfc.error != null || etCuenta.error != null || etClabe.error != null) {
                Snackbar.make(linearLayoutCreateProvider, "La solicitud contiene campos incorrectos", Snackbar.LENGTH_LONG).show()
            }
            else {
                // If all fields are correct, go to confirm view
                val cvCreateProvider = findViewById<CardView>(R.id.cv_create_provider)
                val cvConfirmData = findViewById<CardView>(R.id.cv_confirm_data)
                loadConfirmData()
                cvCreateProvider.visibility = View.GONE
                cvConfirmData.visibility = View.VISIBLE
            }
        }

        val btnModify = findViewById<Button>(R.id.btn_modify)
        btnModify.setOnClickListener {
            val cvCreateProvider = findViewById<CardView>(R.id.cv_create_provider)
            val cvConfirmData = findViewById<CardView>(R.id.cv_confirm_data)
            cvCreateProvider.visibility = View.VISIBLE
            cvConfirmData.visibility = View.GONE
        }

        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        btnConfirm.setOnClickListener {
            Toast.makeText(applicationContext, "Solicitud guardada con exito", Toast.LENGTH_LONG).show()
            finish()
        }

        // Adding data to spinners
        loadEmpresas()
        val spinnerRegimenCapital = findViewById<Spinner>(R.id.spinner_regimen_capital)
        val spinnerBanco = findViewById<Spinner>(R.id.spinner_banco)
        val spinnerMoneda = findViewById<Spinner>(R.id.spinner_moneda)

        val regimenCapitalOptions = arrayOf("S.A. de C.V.", "Otro regimen", "Otro mas")
        val bancoOptions = arrayOf("Banorte", "BBVA", "Santander")
        val monedaOptions = arrayOf("MXP", "USD")

        spinnerRegimenCapital.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, regimenCapitalOptions)
        spinnerBanco.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bancoOptions)
        spinnerMoneda.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, monedaOptions)
    }

    // Adding data to Empresa spinner
    private fun loadEmpresas() {
        val spinnerEmpresa = findViewById<Spinner>(R.id.spinner_empresa)
        val call = apiService.getEmpresas()
        call.enqueue(object: Callback<ArrayList<Empresa>> {
            override fun onResponse(
                call: Call<ArrayList<Empresa>>,
                response: Response<ArrayList<Empresa>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val empresas = it.toMutableList()
                        spinnerEmpresa.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, empresas)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Empresa>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar las empresas",
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadConfirmData() {
        val spinnerEmpresa = findViewById<Spinner>(R.id.spinner_empresa)
        val spinnerRegimenCapital = findViewById<Spinner>(R.id.spinner_regimen_capital)
        val spinnerBanco = findViewById<Spinner>(R.id.spinner_banco)
        val spinnerMoneda = findViewById<Spinner>(R.id.spinner_moneda)
        val etNombre = findViewById<EditText>(R.id.et_nombre)
        val etRfc = findViewById<EditText>(R.id.et_rfc)
        val etCurp = findViewById<EditText>(R.id.et_curp)
        val etCuenta = findViewById<EditText>(R.id.et_cuenta)
        val etClabe = findViewById<EditText>(R.id.et_clabe)

        // TextViews
        val tvEmpresa = findViewById<TextView>(R.id.tv_empresa)
        val tvNombre = findViewById<TextView>(R.id.tv_nombre)
        val tvRfc = findViewById<TextView>(R.id.tv_rfc)
        val tvCurp = findViewById<TextView>(R.id.tv_curp)
        val tvRegimenCapital = findViewById<TextView>(R.id.tv_regimen_capital)
        val tvBanco = findViewById<TextView>(R.id.tv_banco)
        val tvCuenta = findViewById<TextView>(R.id.tv_cuenta)
        val tvClabe = findViewById<TextView>(R.id.tv_clabe)
        val tvMoneda = findViewById<TextView>(R.id.tv_moneda)

        tvEmpresa.text = spinnerEmpresa.selectedItem.toString()
        tvNombre.text = etNombre.text.toString()
        tvRfc.text = etRfc.text.toString()
        tvCurp.text = etCurp.text.toString()
        tvRegimenCapital.text = spinnerRegimenCapital.selectedItem.toString()
        tvBanco.text = spinnerBanco.selectedItem.toString()
        tvCuenta.text = etCuenta.text.toString()
        tvClabe.text = etClabe.text.toString()
        tvMoneda.text = spinnerMoneda.selectedItem.toString()
    }

    override fun onBackPressed() {
        // If confirmData is visible, return to createProvider
        val cvCreateProvider = findViewById<CardView>(R.id.cv_create_provider)
        val cvConfirmData = findViewById<CardView>(R.id.cv_confirm_data)
        if (cvConfirmData.visibility == View.VISIBLE) {
            cvConfirmData.visibility = View.GONE
            cvCreateProvider.visibility = View.VISIBLE
            return
        }
        // Else, display AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Seguro que desea salir?")
        builder.setMessage("Los cambios realizados se perderán")
        builder.setPositiveButton("Salir") {dialog, which ->
            finish()
        }
        builder.setNegativeButton("Cancelar") {dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}