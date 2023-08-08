package com.learning.proveedoresapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
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
import com.learning.proveedoresapp.io.response.SimpleResponse
import com.learning.proveedoresapp.model.Empresa
import com.learning.proveedoresapp.model.Familia
import com.learning.proveedoresapp.model.Material
import com.learning.proveedoresapp.model.MaterialTipoAlta
import com.learning.proveedoresapp.model.Subfamilia
import com.learning.proveedoresapp.model.Unidad
import com.learning.proveedoresapp.util.PreferenceHelper
import com.learning.proveedoresapp.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifyMaterialActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private var empresas = listOf<Empresa>()
    private var tipos = listOf<MaterialTipoAlta>()
    private var familias = listOf<Familia>()
    private var subfamilias = listOf<Subfamilia>()
    private var unidades = listOf<Unidad>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_material)

        // Getting id from MaterialAdapter
        val materialId = intent.getIntExtra("materialId", 0)

        val btnApprove = findViewById<Button>(R.id.btn_approve)
        btnApprove.setOnClickListener {
            // Field validation
            val linearLayoutModifyMaterial = findViewById<LinearLayout>(R.id.linear_layout_modify_material)
            val etNombre = findViewById<EditText>(R.id.et_nombre)
            val etProposito = findViewById<EditText>(R.id.et_proposito)

            etNombre.error = if (etNombre.text.toString().isEmpty()) "Campo obligatorio" else null
            etProposito.error = if (etProposito.text.toString().isEmpty()) "Campo obligatorio" else null

            if (etNombre.error != null || etProposito.error != null) {
                Snackbar.make(linearLayoutModifyMaterial, "La solicitud contiene campos incorrectos",
                    Snackbar.LENGTH_SHORT).show()
            }
            else {
                // If all fields are correct, go to confirm view
                val cvModifyMaterial = findViewById<CardView>(R.id.cv_modify_material)
                val cvConfirmData = findViewById<CardView>(R.id.cv_confirm_data)
                loadConfirmData()
                cvModifyMaterial.visibility = View.GONE
                cvConfirmData.visibility = View.VISIBLE
            }
        }

        val btnModify = findViewById<Button>(R.id.btn_modify)
        btnModify.setOnClickListener {
            val cvCreateMaterial = findViewById<CardView>(R.id.cv_create_material)
            val cvConfirmData = findViewById<CardView>(R.id.cv_confirm_data)
            cvCreateMaterial.visibility = View.VISIBLE
            cvConfirmData.visibility = View.GONE
        }

        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        btnConfirm.setOnClickListener {
            performPutMaterial()
        }

        // Adding data to spinners
        loadEmpresas()
        loadMaterialTipoAlta()
        listenTipoAltaChange()
        listenFamiliaChange()

        // Loading the record
        loadMaterial(materialId)
    }

    private fun performPutMaterial() {
        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        btnConfirm.isClickable = false

        val jwt = preferences["jwt", ""]
        val authorization = "Bearer $jwt"

        val empresa = findViewById<TextView>(R.id.tv_empresa).text.toString()
        val nombre = findViewById<TextView>(R.id.tv_nombre).text.toString()
        val tipoAlta = findViewById<TextView>(R.id.tv_tipo_alta).text.toString()
        val familia = findViewById<TextView>(R.id.tv_familia).text.toString()
        val subfamilia = findViewById<TextView>(R.id.tv_subfamilia).text.toString()
        val marca = findViewById<TextView>(R.id.tv_marca).text.toString()
        val parteModelo = findViewById<TextView>(R.id.tv_parte_modelo).text.toString()
        val nombreComun = findViewById<TextView>(R.id.tv_nombre_comun).text.toString()
        val medida = findViewById<TextView>(R.id.tv_medida).text.toString()
        val ingActivo = findViewById<TextView>(R.id.tv_ing_activo).text.toString()
        val tipoProducto = findViewById<TextView>(R.id.tv_tipo_producto).text.toString()
        val alias = findViewById<TextView>(R.id.tv_alias).text.toString()
        val unidad = findViewById<TextView>(R.id.tv_unidad).text.toString()
        val iva = findViewById<TextView>(R.id.tv_iva).text.toString()
        val ieps = findViewById<TextView>(R.id.tv_ieps).text.toString()
        val proposito = findViewById<TextView>(R.id.tv_proposito).text.toString()
        val esImportado = findViewById<CheckBox>(R.id.cb_es_importado).isChecked
        val esMaterialEmpaque = findViewById<CheckBox>(R.id.cb_es_material_empaque).isChecked
        val esProdTerminado = findViewById<CheckBox>(R.id.cb_es_prod_terminado).isChecked

        val call = apiService.postMaterial(authorization, empresa, nombre,tipoAlta, familia, subfamilia, marca, parteModelo,
            nombreComun, medida, ingActivo, tipoProducto, alias, unidad, iva, ieps, proposito, esImportado, esMaterialEmpaque,
            esProdTerminado)
        call.enqueue(object: Callback<SimpleResponse> {
            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ModifyMaterialActivity, "Solicitud de material registrada con exito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@ModifyMaterialActivity, "Error al registrar la solicitud de material", Toast.LENGTH_SHORT).show()
                    btnConfirm.isClickable = true
                }
            }

            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                Toast.makeText(this@ModifyMaterialActivity, "Error al registrar la solicitud de material", Toast.LENGTH_SHORT).show()
                btnConfirm.isClickable = true
            }

        })
    }

    // Adding data to Empresa spinner
    private fun loadEmpresas() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerEmpresa = findViewById<Spinner>(R.id.spinner_empresa)
        val call = apiService.getEmpresas("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Empresa>> {
            override fun onResponse(
                call: Call<ArrayList<Empresa>>,
                response: Response<ArrayList<Empresa>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        empresas = it.toMutableList()
                        spinnerEmpresa.adapter = ArrayAdapter(this@ModifyMaterialActivity,
                            android.R.layout.simple_list_item_1, empresas)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Empresa>>, t: Throwable) {
                Toast.makeText(this@ModifyMaterialActivity, "Se produjo un error al cargar las empresas",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to TipoAlta spinner
    private fun loadMaterialTipoAlta() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerTipoAlta = findViewById<Spinner>(R.id.spinner_material_tipo_alta)
        val call = apiService.getMaterialTipoAlta("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<MaterialTipoAlta>> {
            override fun onResponse(
                call: Call<ArrayList<MaterialTipoAlta>>,
                response: Response<ArrayList<MaterialTipoAlta>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        tipos = it.toMutableList()
                        spinnerTipoAlta.adapter = ArrayAdapter(this@ModifyMaterialActivity,
                            android.R.layout.simple_list_item_1, tipos)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<MaterialTipoAlta>>, t: Throwable) {
                Toast.makeText(this@ModifyMaterialActivity, "Se produjo un error al cargar los tipos de alta",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Listening to changes on TipoAlta spinner to load Familia spinner
    private fun listenTipoAltaChange() {
        val spinnerTipoAlta = findViewById<Spinner>(R.id.spinner_material_tipo_alta)
        spinnerTipoAlta.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val tipoAlta = adapter?.getItemAtPosition(position) as MaterialTipoAlta
                loadFamilias(tipoAlta.tipo)
                loadUnidades(tipoAlta.tipo)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    // Adding data to Familia spinner
    private fun loadFamilias(tipoAlta: String) {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerFamilia = findViewById<Spinner>(R.id.spinner_familia)
        val call = apiService.getFamilias(tipoAlta, "Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Familia>> {
            override fun onResponse(
                call: Call<ArrayList<Familia>>,
                response: Response<ArrayList<Familia>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        familias = it.toMutableList()
                        spinnerFamilia.adapter = ArrayAdapter(this@ModifyMaterialActivity, android.R.layout.simple_list_item_1, familias)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Familia>>, t: Throwable) {
                Toast.makeText(applicationContext, "Se produjo un error al cargar las familias",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Listening to changes on Familia spinner to load Subfamilia spinner
    private fun listenFamiliaChange() {
        val spinnerFamilia = findViewById<Spinner>(R.id.spinner_familia)
        spinnerFamilia.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val familia = adapter?.getItemAtPosition(position) as Familia
                loadSubfamilias(familia.familia)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    // Adding data to Subfamilia spinner
    private fun loadSubfamilias(familia: String) {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerSubfamilia = findViewById<Spinner>(R.id.spinner_subfamilia)
        val call = apiService.getSubfamilias(familia, "Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Subfamilia>> {
            override fun onResponse(
                call: Call<ArrayList<Subfamilia>>,
                response: Response<ArrayList<Subfamilia>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        subfamilias = it.toMutableList()
                        spinnerSubfamilia.adapter = ArrayAdapter(this@ModifyMaterialActivity, android.R.layout.simple_list_item_1,
                            subfamilias)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Subfamilia>>, t: Throwable) {
                Toast.makeText(applicationContext, "Se produjo un error al cargar las subfamilias",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to Unidad spinner
    private fun loadUnidades(tipoAlta: String) {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerUnidad = findViewById<Spinner>(R.id.spinner_unidad)
        val call = apiService.getUnidades(tipoAlta, "Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Unidad>> {
            override fun onResponse(
                call: Call<ArrayList<Unidad>>,
                response: Response<ArrayList<Unidad>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        unidades = it.toMutableList()
                        spinnerUnidad.adapter = ArrayAdapter(this@ModifyMaterialActivity,
                            android.R.layout.simple_list_item_1, unidades)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Unidad>>, t: Throwable) {
                Toast.makeText(applicationContext, "Se produjo un error al cargar las unidades",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to all fields
    private fun loadMaterial(idMaterial: Int) {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val tvSolicitante = findViewById<TextView>(R.id.tv_solicitante)
        val spinnerEmpresa = findViewById<Spinner>(R.id.spinner_empresa)
        val etNombre = findViewById<EditText>(R.id.et_nombre)
        val spinnerTipoAlta = findViewById<Spinner>(R.id.spinner_material_tipo_alta)
        val spinnerFamilia = findViewById<Spinner>(R.id.spinner_familia)
        val spinnerSubfamilia = findViewById<Spinner>(R.id.spinner_subfamilia)
        val etMarca = findViewById<EditText>(R.id.et_marca)
        val etParteModelo = findViewById<EditText>(R.id.et_parte_modelo)
        val etNombreComun = findViewById<EditText>(R.id.et_nombre_comun)
        val etMedida = findViewById<EditText>(R.id.et_medida)
        val etIngActivo = findViewById<EditText>(R.id.et_ing_activo)
        val etTipoProducto = findViewById<EditText>(R.id.et_tipo_producto)
        val etAlias = findViewById<EditText>(R.id.et_alias)
        val spinnerUnidad = findViewById<Spinner>(R.id.spinner_unidad)
        val etIva = findViewById<EditText>(R.id.et_iva)
        val etIeps = findViewById<EditText>(R.id.et_ieps)
        val etProposito = findViewById<EditText>(R.id.et_proposito)
        val cbEsImportado = findViewById<CheckBox>(R.id.cb_es_importado)
        val cbEsMaterialEmpaque = findViewById<CheckBox>(R.id.cb_es_material_empaque)
        val cbEsProdTerminado = findViewById<CheckBox>(R.id.cb_es_prod_terminado)
        val etComentarios = findViewById<EditText>(R.id.et_comentarios)

        val call = apiService.getMaterialById(idMaterial, "Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Material>> {
            override fun onResponse(
                call: Call<ArrayList<Material>>,
                response: Response<ArrayList<Material>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val material = it

                        val empresaPosition = empresas.indexOfFirst { it.toString() == material[0].empresa }
                        val tipoAltaPosition = tipos.indexOfFirst { it.toString() == material[0].tipoAlta }
                        val familiaPosition = familias.indexOfFirst { it.toString() == material[0].familia }
                        val subfamiliaPosition = subfamilias.indexOfFirst { it.toString() == material[0].subfamilia }
                        val unidadPosition = unidades.indexOfFirst { it.toString() == material[0].unidad }

                        tvSolicitante.text = material[0].nombreSolicitante
                        spinnerEmpresa.setSelection(empresaPosition)
                        etNombre.setText(material[0].nombre)
                        spinnerTipoAlta.setSelection(tipoAltaPosition)
                        spinnerFamilia.setSelection(familiaPosition)
                        spinnerSubfamilia.setSelection(subfamiliaPosition)
                        etMarca.setText(material[0].marca)
                        etParteModelo.setText(material[0].parte_modelo)
                        etNombreComun.setText(material[0].nombreComun)
                        etMedida.setText(material[0].medida)
                        etIngActivo.setText(material[0].ingActivo)
                        etTipoProducto.setText(material[0].tipoProducto)
                        etAlias.setText(material[0].alias)
                        spinnerUnidad.setSelection(unidadPosition)
                        etIva.setText(material[0].iva)
                        etIeps.setText(material[0].ieps)
                        etProposito.setText(material[0].proposito)
                        cbEsImportado.isChecked = material[0].esImportado
                        cbEsMaterialEmpaque.isChecked = material[0].esMaterialEmpaque
                        cbEsProdTerminado.isChecked = material[0].esProdTerminado
                        etComentarios.setText(material[0].comentarios)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Material>>, t: Throwable) {
                Toast.makeText(this@ModifyMaterialActivity, "Se produjo un error al cargar la solicitud",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadConfirmData() {
        val tvSolicitante = findViewById<TextView>(R.id.tv_solicitante)
        val spinnerEmpresa = findViewById<Spinner>(R.id.spinner_empresa)
        val etNombre = findViewById<EditText>(R.id.et_nombre)
        val spinnerTipoAlta = findViewById<Spinner>(R.id.spinner_material_tipo_alta)
        val spinnerFamilia = findViewById<Spinner>(R.id.spinner_familia)
        val spinnerSubfamilia = findViewById<Spinner>(R.id.spinner_subfamilia)
        val etMarca = findViewById<EditText>(R.id.et_marca)
        val etParteModelo = findViewById<EditText>(R.id.et_parte_modelo)
        val etNombreComun = findViewById<EditText>(R.id.et_nombre_comun)
        val etMedida = findViewById<EditText>(R.id.et_medida)
        val etIngActivo = findViewById<EditText>(R.id.et_ing_activo)
        val etTipoProducto = findViewById<EditText>(R.id.et_tipo_producto)
        val etAlias = findViewById<EditText>(R.id.et_alias)
        val spinnerUnidad = findViewById<Spinner>(R.id.spinner_unidad)
        val etIva = findViewById<EditText>(R.id.et_iva)
        val etIeps = findViewById<EditText>(R.id.et_ieps)
        val etProposito = findViewById<EditText>(R.id.et_proposito)
        val cbEsImportado = findViewById<CheckBox>(R.id.cb_es_importado)
        val cbEsMaterialEmpaque = findViewById<CheckBox>(R.id.cb_es_material_empaque)
        val cbEsProdTerminado = findViewById<CheckBox>(R.id.cb_es_prod_terminado)

        // TextViews
        val tvSolicitanteConfirm = findViewById<TextView>(R.id.tv_solicitante_confirm)
        val tvEmpresa = findViewById<TextView>(R.id.tv_empresa)
        val tvNombre = findViewById<TextView>(R.id.tv_nombre)
        val tvTipoAlta = findViewById<TextView>(R.id.tv_tipo_alta)
        val tvFamilia = findViewById<TextView>(R.id.tv_familia)
        val tvSubfamilia = findViewById<TextView>(R.id.tv_subfamilia)
        val tvMarca = findViewById<TextView>(R.id.tv_marca)
        val tvParteModelo = findViewById<TextView>(R.id.tv_parte_modelo)
        val tvNombreComun = findViewById<TextView>(R.id.tv_nombre_comun)
        val tvMedida = findViewById<TextView>(R.id.tv_medida)
        val tvIngActivo = findViewById<TextView>(R.id.tv_ing_activo)
        val tvTipoProducto = findViewById<TextView>(R.id.tv_tipo_producto)
        val tvAlias = findViewById<TextView>(R.id.tv_alias)
        val tvUnidad = findViewById<TextView>(R.id.tv_unidad)
        val tvIva = findViewById<TextView>(R.id.tv_iva)
        val tvIeps = findViewById<TextView>(R.id.tv_ieps)
        val tvProposito = findViewById<TextView>(R.id.tv_proposito)
        val tvEsImportado = findViewById<TextView>(R.id.tv_es_importado)
        val tvEsMaterialEmpaque = findViewById<TextView>(R.id.tv_es_material_empaque)
        val tvEsProdTerminado = findViewById<TextView>(R.id.tv_es_prod_terminado)

        tvSolicitanteConfirm.text = tvSolicitante.text.toString()
        tvEmpresa.text = spinnerEmpresa.selectedItem.toString()
        tvNombre.text = etNombre.text.toString()
        tvTipoAlta.text = spinnerTipoAlta.selectedItem.toString()
        tvFamilia.text = spinnerFamilia.selectedItem.toString()
        tvSubfamilia.text = spinnerSubfamilia.selectedItem.toString()
        tvMarca.text = etMarca.text.toString()
        tvParteModelo.text = etParteModelo.text.toString()
        tvNombreComun.text = etNombreComun.text.toString()
        tvMedida.text = etMedida.text.toString()
        tvIngActivo.text = etIngActivo.text.toString()
        tvTipoProducto.text = etTipoProducto.text.toString()
        tvAlias.text = etAlias.text.toString()
        tvUnidad.text = spinnerUnidad.selectedItem.toString()
        tvIva.text = etIva.text.toString()
        tvIeps.text = etIeps.text.toString()
        tvProposito.text = etProposito.text.toString()
        tvEsImportado.text = if (cbEsImportado.isChecked) "Si" else "No"
        tvEsMaterialEmpaque.text = if (cbEsMaterialEmpaque.isChecked) "Si" else "No"
        tvEsProdTerminado.text = if (cbEsProdTerminado.isChecked) "Si" else "No"
    }

    override fun onBackPressed() {
        // If confirmData is visible, return to createProvider
        val cvCreateMaterial = findViewById<CardView>(R.id.cv_create_material)
        val cvConfirmData = findViewById<CardView>(R.id.cv_confirm_data)
        if (cvConfirmData.visibility == View.VISIBLE) {
            cvConfirmData.visibility = View.GONE
            cvCreateMaterial.visibility = View.VISIBLE
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