package com.learning.proveedoresapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.learning.proveedoresapp.R

class CreateProviderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_provider)

        val btnSave = findViewById<Button>(R.id.btn_save)
        btnSave.setOnClickListener {
            val cvCreateProvider = findViewById<CardView>(R.id.cv_create_provider)
            val cvConfirmData = findViewById<CardView>(R.id.cv_confirm_data)
            loadConfirmData()
            cvCreateProvider.visibility = View.GONE
            cvConfirmData.visibility = View.VISIBLE
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
            Toast.makeText(applicationContext, "Solicitud guardada con exito", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Adding data to spinners
        val spinnerEmpresa = findViewById<Spinner>(R.id.spinner_empresa)
        val spinnerRegimenCapital = findViewById<Spinner>(R.id.spinner_regimen_capital)
        val spinnerBanco = findViewById<Spinner>(R.id.spinner_banco)
        val spinnerMoneda = findViewById<Spinner>(R.id.spinner_moneda)

        val empresaOptions = arrayOf("Ricofarms", "Moonrise", "Wellin", "Ricofarms - Moonrise - Wellin")
        val regimenCapitalOptions = arrayOf("S.A. de C.V.", "Otro regimen", "Otro mas")
        val bancoOptions = arrayOf("Banorte", "BBVA", "Santander")
        val monedaOptions = arrayOf("MXP", "USD")

        spinnerEmpresa.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, empresaOptions)
        spinnerRegimenCapital.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, regimenCapitalOptions)
        spinnerBanco.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bancoOptions)
        spinnerMoneda.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, monedaOptions)
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