package com.learning.proveedoresapp.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
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
import com.learning.proveedoresapp.model.Banco
import com.learning.proveedoresapp.model.Empresa
import com.learning.proveedoresapp.model.Estado
import com.learning.proveedoresapp.model.Grupo
import com.learning.proveedoresapp.model.Moneda
import com.learning.proveedoresapp.model.Pais
import com.learning.proveedoresapp.model.Persona
import com.learning.proveedoresapp.model.RegimenCapital
import com.learning.proveedoresapp.model.RegimenFiscal
import com.learning.proveedoresapp.model.RetencionISR
import com.learning.proveedoresapp.model.RetencionIVA
import com.learning.proveedoresapp.model.TipoTercero
import com.learning.proveedoresapp.util.PreferenceHelper
import com.learning.proveedoresapp.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateProviderActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    // Handling file uploads
    companion object {
        private const val REQUEST_CODE_PICK_FILE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_provider)

        // Handling file uploads
        val btnUploadConstancia = findViewById<Button>(R.id.btn_upload_constancia)
        btnUploadConstancia.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "application/pdf,image/*" // Allow PDFs and images
            startActivityForResult(i, REQUEST_CODE_PICK_FILE)
        }

        // Handling file uploads
        val btnUploadEstadoCuenta = findViewById<Button>(R.id.btn_upload_estado_cuenta)
        btnUploadEstadoCuenta.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "application/pdf,image/*" // Allow PDFs and images
            startActivityForResult(i, REQUEST_CODE_PICK_FILE)
        }

        val btnSave = findViewById<Button>(R.id.btn_save)
        btnSave.setOnClickListener {
            // Field validation
            val linearLayoutCreateProvider = findViewById<LinearLayout>(R.id.linear_layout_create_provider)
            val etNombre = findViewById<EditText>(R.id.et_nombre_fiscal)
            val etRfc = findViewById<EditText>(R.id.et_rfc)
            val etCuenta = findViewById<EditText>(R.id.et_cuenta)
            val etClabe = findViewById<EditText>(R.id.et_clabe)

            etNombre.error = if (etNombre.text.toString().isEmpty()) "Campo obligatorio" else null
            etRfc.error = if (etRfc.text.toString().isEmpty()) "Campo obligatorio" else null
            etCuenta.error = if (etCuenta.text.toString().isEmpty()) "Campo obligatorio" else null
            etClabe.error = if (etClabe.text.toString().isEmpty()) "Campo obligatorio" else null

            if (etNombre.error != null || etRfc.error != null || etCuenta.error != null || etClabe.error != null) {
                Snackbar.make(linearLayoutCreateProvider, "La solicitud contiene campos incorrectos",
                    Snackbar.LENGTH_SHORT).show()
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
            Toast.makeText(applicationContext, "Solicitud guardada con exito", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Adding data to spinners
        loadEmpresas()
        loadPaises()
        listenPaisChange()
        loadGrupos()
        loadPersonas()
        loadTiposTercero()
        loadRegimenFiscal()
        loadRetencionISR()
        loadRetencionIVA()
        loadRegimenCapital()
        loadBancos()
        loadMonedas()
    }

    // Handling file uploads
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK) {
            val selectedFileUri = data?.data
            // Handle the selected file URI here
            // For example, you might want to upload the file to a server or process it in some way.
            Toast.makeText(this@CreateProviderActivity, "Archivo añadido", Toast.LENGTH_SHORT).show()
        }
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
                        val empresas = it.toMutableList()
                        spinnerEmpresa.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, empresas)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Empresa>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar las empresas",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to País spinner
    private fun loadPaises() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerPais = findViewById<Spinner>(R.id.spinner_pais)
        val call = apiService.getPaises("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Pais>> {
            override fun onResponse(
                call: Call<ArrayList<Pais>>,
                response: Response<ArrayList<Pais>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val paises = it.toMutableList()
                        spinnerPais.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, paises)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Pais>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar los países",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Listening to changes on Pais spinner to load estados spinner
    private fun listenPaisChange() {
        val spinnerPais = findViewById<Spinner>(R.id.spinner_pais)
        spinnerPais.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val pais = adapter?.getItemAtPosition(position) as Pais
                loadEstados(pais.pais)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    // Adding data to Estado spinner
    private fun loadEstados(paisName: String) {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerEstado = findViewById<Spinner>(R.id.spinner_estado)
        val call = apiService.getEstados(paisName, "Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Estado>> {
            override fun onResponse(
                call: Call<ArrayList<Estado>>,
                response: Response<ArrayList<Estado>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val estados = it.toMutableList()
                        spinnerEstado.adapter = ArrayAdapter(this@CreateProviderActivity, android.R.layout.simple_list_item_1, estados)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Estado>>, t: Throwable) {
                Toast.makeText(applicationContext, "Se produjo un error al cargar los estados",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to Grupo spinner
    private fun loadGrupos() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerGrupo = findViewById<Spinner>(R.id.spinner_grupo)
        val call = apiService.getGrupos("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Grupo>> {
            override fun onResponse(
                call: Call<ArrayList<Grupo>>,
                response: Response<ArrayList<Grupo>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val grupos = it.toMutableList()
                        spinnerGrupo.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, grupos)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Grupo>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar los grupos",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to Persona spinner
    private fun loadPersonas() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerPersona = findViewById<Spinner>(R.id.spinner_persona)
        val call = apiService.getPersonas("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Persona>> {
            override fun onResponse(
                call: Call<ArrayList<Persona>>,
                response: Response<ArrayList<Persona>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val personas = it.toMutableList()
                        spinnerPersona.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, personas)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Persona>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar los tipos de persona",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to TipoTercero spinner
    private fun loadTiposTercero() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerTipoTercero = findViewById<Spinner>(R.id.spinner_tipo_tercero)
        val call = apiService.getTiposTercero("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<TipoTercero>> {
            override fun onResponse(
                call: Call<ArrayList<TipoTercero>>,
                response: Response<ArrayList<TipoTercero>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val tipos_tercero = it.toMutableList()
                        spinnerTipoTercero.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, tipos_tercero)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<TipoTercero>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar los tipos de tercero",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to RegimenFiscal spinner
    private fun loadRegimenFiscal() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerRegimenFiscal = findViewById<Spinner>(R.id.spinner_regimen_fiscal)
        val call = apiService.getRegimenFiscal("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<RegimenFiscal>> {
            override fun onResponse(
                call: Call<ArrayList<RegimenFiscal>>,
                response: Response<ArrayList<RegimenFiscal>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val regimenes = it.toMutableList()
                        spinnerRegimenFiscal.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, regimenes)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<RegimenFiscal>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar los regímenes fiscales",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to RetencionISR spinner
    private fun loadRetencionISR() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerRetencionISR = findViewById<Spinner>(R.id.spinner_isr_ret)
        val call = apiService.getRetencionISR("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<RetencionISR>> {
            override fun onResponse(
                call: Call<ArrayList<RetencionISR>>,
                response: Response<ArrayList<RetencionISR>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val opciones = it.toMutableList()
                        spinnerRetencionISR.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, opciones)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<RetencionISR>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar las opciones de retención ISR",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to RetencionIVA spinner
    private fun loadRetencionIVA() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerRetencionIVA = findViewById<Spinner>(R.id.spinner_iva_ret)
        val call = apiService.getRetencionIVA("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<RetencionIVA>> {
            override fun onResponse(
                call: Call<ArrayList<RetencionIVA>>,
                response: Response<ArrayList<RetencionIVA>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val opciones = it.toMutableList()
                        spinnerRetencionIVA.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, opciones)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<RetencionIVA>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar las opciones de retención IVA",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to RegimenCapital spinner
    private fun loadRegimenCapital() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerRegimenCapital = findViewById<Spinner>(R.id.spinner_regimen_capital)
        val call = apiService.getRegimenCapital("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<RegimenCapital>> {
            override fun onResponse(
                call: Call<ArrayList<RegimenCapital>>,
                response: Response<ArrayList<RegimenCapital>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val opciones = it.toMutableList()
                        spinnerRegimenCapital.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, opciones)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<RegimenCapital>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar los regímenes de capital",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to Banco spinner
    private fun loadBancos() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerBanco = findViewById<Spinner>(R.id.spinner_banco)
        val spinnerBanco2 = findViewById<Spinner>(R.id.spinner_banco_2)
        val call = apiService.getBancos("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Banco>> {
            override fun onResponse(
                call: Call<ArrayList<Banco>>,
                response: Response<ArrayList<Banco>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val bancos = it.toMutableList()
                        spinnerBanco.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, bancos)
                        spinnerBanco2.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, bancos)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Banco>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar los bancos",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adding data to Moneda spinner
    private fun loadMonedas() {
        // Getting jwt from preferencies
        val jwt = preferences["jwt", ""]
        val spinnerMoneda = findViewById<Spinner>(R.id.spinner_moneda)
        val spinnerMoneda2 = findViewById<Spinner>(R.id.spinner_moneda_2)
        val call = apiService.getMonedas("Bearer $jwt")
        call.enqueue(object: Callback<ArrayList<Moneda>> {
            override fun onResponse(
                call: Call<ArrayList<Moneda>>,
                response: Response<ArrayList<Moneda>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val monedas = it.toMutableList()
                        spinnerMoneda.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, monedas)
                        spinnerMoneda2.adapter = ArrayAdapter(this@CreateProviderActivity,
                            android.R.layout.simple_list_item_1, monedas)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<Moneda>>, t: Throwable) {
                Toast.makeText(this@CreateProviderActivity, "Se produjo un error al cargar las monedas",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadConfirmData() {
        val spinnerEmpresa = findViewById<Spinner>(R.id.spinner_empresa)
        val etRfc = findViewById<EditText>(R.id.et_rfc)
        val etCurp = findViewById<EditText>(R.id.et_curp)
        val etNombreFiscal = findViewById<EditText>(R.id.et_nombre_fiscal)
        val etNombreComercial = findViewById<EditText>(R.id.et_nombre_comercial)
        val etCalle = findViewById<EditText>(R.id.et_calle)
        val etNumeroExterior = findViewById<EditText>(R.id.et_numero_exterior)
        val etNumeroInterior = findViewById<EditText>(R.id.et_numero_interior)
        val etCodigoPostal = findViewById<EditText>(R.id.et_codigo_postal)
        val spinnerPais = findViewById<Spinner>(R.id.spinner_pais)
        val spinnerEstado = findViewById<Spinner>(R.id.spinner_estado)
        val etMunicipio = findViewById<EditText>(R.id.et_municipio)
        val etLocalidad = findViewById<EditText>(R.id.et_localidad)
        val etDiasCredito = findViewById<EditText>(R.id.et_dias_credito)
        val etLimiteCreditoMN = findViewById<EditText>(R.id.et_limite_credito_mn)
        val etLimiteCreditoME = findViewById<EditText>(R.id.et_limite_credito_me)
        val etTelefono1 = findViewById<EditText>(R.id.et_telefono_1)
        val etTelefono2 = findViewById<EditText>(R.id.et_telefono_2)
        val etContacto = findViewById<EditText>(R.id.et_contacto)
        val spinnerGrupo = findViewById<Spinner>(R.id.spinner_grupo)
        val etCorreoContacto = findViewById<EditText>(R.id.et_correo_contacto)
        val etCorreoPagos = findViewById<EditText>(R.id.et_correo_pagos)
        val etPaginaWeb = findViewById<EditText>(R.id.et_pagina_web)
        val spinnerPersona = findViewById<Spinner>(R.id.spinner_persona)
        val spinnerTipoTercero = findViewById<Spinner>(R.id.spinner_tipo_tercero)
        val etIdFiscal = findViewById<EditText>(R.id.et_id_fiscal)
        val spinnerRegimenFiscal = findViewById<Spinner>(R.id.spinner_regimen_fiscal)
        val spinnerRetencionISR = findViewById<Spinner>(R.id.spinner_isr_ret)
        val spinnerRetencionIVA = findViewById<Spinner>(R.id.spinner_iva_ret)
        val spinnerRegimenCapital = findViewById<Spinner>(R.id.spinner_regimen_capital)
        val spinnerBanco = findViewById<Spinner>(R.id.spinner_banco)
        val etCuenta = findViewById<EditText>(R.id.et_cuenta)
        val etClabe = findViewById<EditText>(R.id.et_clabe)
        val spinnerMoneda = findViewById<Spinner>(R.id.spinner_moneda)
        val spinnerBanco2 = findViewById<Spinner>(R.id.spinner_banco_2)
        val etCuenta2 = findViewById<EditText>(R.id.et_cuenta_2)
        val etClabe2 = findViewById<EditText>(R.id.et_clabe_2)
        val spinnerMoneda2 = findViewById<Spinner>(R.id.spinner_moneda_2)

        // TextViews
        val tvEmpresa = findViewById<TextView>(R.id.tv_empresa)
        val tvRfc = findViewById<TextView>(R.id.tv_rfc)
        val tvCurp = findViewById<TextView>(R.id.tv_curp)
        val tvNombreFiscal = findViewById<TextView>(R.id.tv_nombre_fiscal)
        val tvNombreComercial = findViewById<TextView>(R.id.tv_nombre_comercial)
        val tvCalle = findViewById<TextView>(R.id.tv_calle)
        val tvNumeroExterior = findViewById<TextView>(R.id.tv_numero_exterior)
        val tvNumeroInterior = findViewById<TextView>(R.id.tv_numero_interior)
        val tvCodigoPostal = findViewById<TextView>(R.id.tv_codigo_postal)
        val tvPais = findViewById<TextView>(R.id.tv_pais)
        val tvEstado = findViewById<TextView>(R.id.tv_estado)
        val tvMunicipio = findViewById<TextView>(R.id.tv_municipio)
        val tvLocalidad = findViewById<TextView>(R.id.tv_localidad)
        val tvDiasCredito = findViewById<TextView>(R.id.tv_dias_credito)
        val tvLimiteCreditoMN = findViewById<TextView>(R.id.tv_limite_credito_mn)
        val tvLimiteCreditoME = findViewById<TextView>(R.id.tv_limite_credito_me)
        val tvTelefono1 = findViewById<TextView>(R.id.tv_telefono_1)
        val tvTelefono2 = findViewById<TextView>(R.id.tv_telefono_2)
        val tvContacto = findViewById<TextView>(R.id.tv_contacto)
        val tvGrupo = findViewById<TextView>(R.id.tv_grupo)
        val tvCorreoContacto = findViewById<TextView>(R.id.tv_correo_contacto)
        val tvCorreoPagos = findViewById<TextView>(R.id.tv_correo_pagos)
        val tvPaginaWeb = findViewById<TextView>(R.id.tv_pagina_web)
        val tvPersona = findViewById<TextView>(R.id.tv_persona)
        val tvTipoTercero = findViewById<TextView>(R.id.tv_tipo_tercero)
        val tvIdFiscal = findViewById<TextView>(R.id.tv_id_fiscal)
        val tvRegimenFiscal = findViewById<TextView>(R.id.tv_regimen_fiscal)
        val tvRetencionISR = findViewById<TextView>(R.id.tv_retencion_isr)
        val tvRetencionIVA = findViewById<TextView>(R.id.tv_retencion_iva)
        val tvRegimenCapital = findViewById<TextView>(R.id.tv_regimen_capital)
        val tvBanco = findViewById<TextView>(R.id.tv_banco)
        val tvCuenta = findViewById<TextView>(R.id.tv_cuenta)
        val tvClabe = findViewById<TextView>(R.id.tv_clabe)
        val tvMoneda = findViewById<TextView>(R.id.tv_moneda)
        val tvBanco2 = findViewById<TextView>(R.id.tv_banco_2)
        val tvCuenta2 = findViewById<TextView>(R.id.tv_cuenta_2)
        val tvClabe2 = findViewById<TextView>(R.id.tv_clabe_2)
        val tvMoneda2 = findViewById<TextView>(R.id.tv_moneda_2)

        tvEmpresa.text = spinnerEmpresa.selectedItem.toString()
        tvRfc.text = etRfc.text.toString()
        tvCurp.text = etCurp.text.toString()
        tvNombreFiscal.text = etNombreFiscal.text.toString()
        tvNombreComercial.text = etNombreComercial.text.toString()
        tvCalle.text = etCalle.text.toString()
        tvNumeroExterior.text = etNumeroExterior.text.toString()
        tvNumeroInterior.text = etNumeroInterior.text.toString()
        tvCodigoPostal.text = etCodigoPostal.text.toString()
        tvPais.text = spinnerPais.selectedItem.toString()
        tvEstado.text = spinnerEstado.selectedItem.toString()
        tvMunicipio.text = etMunicipio.text.toString()
        tvLocalidad.text = etLocalidad.text.toString()
        tvDiasCredito.text = etDiasCredito.text.toString()
        tvLimiteCreditoMN.text = etLimiteCreditoMN.text.toString()
        tvLimiteCreditoME.text = etLimiteCreditoME.text.toString()
        tvTelefono1.text = etTelefono1.text.toString()
        tvTelefono2.text = etTelefono2.text.toString()
        tvContacto.text = etContacto.text.toString()
        tvGrupo.text = spinnerGrupo.selectedItem.toString()
        tvCorreoContacto.text = etCorreoContacto.text.toString()
        tvCorreoPagos.text = etCorreoPagos.text.toString()
        tvPaginaWeb.text = etPaginaWeb.text.toString()
        tvPersona.text = spinnerPersona.selectedItem.toString()
        tvTipoTercero.text = spinnerTipoTercero.selectedItem.toString()
        tvIdFiscal.text = etIdFiscal.text.toString()
        tvRegimenFiscal.text = spinnerRegimenFiscal.selectedItem.toString()
        tvRetencionISR.text = spinnerRetencionISR.selectedItem.toString()
        tvRetencionIVA.text = spinnerRetencionIVA.selectedItem.toString()
        tvRegimenCapital.text = spinnerRegimenCapital.selectedItem.toString()
        tvBanco.text = spinnerBanco.selectedItem.toString()
        tvCuenta.text = etCuenta.text.toString()
        tvClabe.text = etClabe.text.toString()
        tvMoneda.text = spinnerMoneda.selectedItem.toString()
        tvBanco2.text = spinnerBanco2.selectedItem.toString()
        tvCuenta2.text = etCuenta2.text.toString()
        tvClabe2.text = etClabe2.text.toString()
        tvMoneda2.text = spinnerMoneda2.selectedItem.toString()
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