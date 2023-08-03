package com.learning.proveedoresapp.io

import com.learning.proveedoresapp.io.response.LoginResponse
import com.learning.proveedoresapp.io.response.SimpleResponse
import com.learning.proveedoresapp.model.Banco
import com.learning.proveedoresapp.model.Empresa
import com.learning.proveedoresapp.model.Estado
import com.learning.proveedoresapp.model.Familia
import com.learning.proveedoresapp.model.Grupo
import com.learning.proveedoresapp.model.Material
import com.learning.proveedoresapp.model.MaterialTipoAlta
import com.learning.proveedoresapp.model.Moneda
import com.learning.proveedoresapp.model.Pais
import com.learning.proveedoresapp.model.Persona
import com.learning.proveedoresapp.model.Proveedor
import com.learning.proveedoresapp.model.RegimenCapital
import com.learning.proveedoresapp.model.RegimenFiscal
import com.learning.proveedoresapp.model.RetencionISR
import com.learning.proveedoresapp.model.RetencionIVA
import com.learning.proveedoresapp.model.Subfamilia
import com.learning.proveedoresapp.model.TipoAlta
import com.learning.proveedoresapp.model.TipoTercero
import com.learning.proveedoresapp.model.Unidad
import com.learning.proveedoresapp.model.UsoCFDI
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    // Login to the app
    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    // Retrieving empresas from api
    @GET(value="empresas")
    fun getEmpresas(@Header(value="Authorization") authHeader: String): Call<ArrayList<Empresa>>

    // Retrieving tipos de alta from api
    @GET(value="tipo_alta")
    fun getTiposAlta(@Header(value="Authorization") authHeader: String): Call<ArrayList<TipoAlta>>

    // Retrieving paises from api
    @GET(value="paises")
    fun getPaises(@Header(value="Authorization") authHeader: String): Call<ArrayList<Pais>>

    // Retrieving estados from api
    @GET(value="estados/{pais}")
    fun getEstados(@Path(value="pais") paisName: String, @Header(value="Authorization") authHeader: String):
            Call<ArrayList<Estado>>

    // Retrieving grupos from api
    @GET(value="grupos")
    fun getGrupos(@Header(value="Authorization") authHeader: String): Call<ArrayList<Grupo>>

    // Retrieving tipos persona from api
    @GET(value="personas")
    fun getPersonas(@Header(value="Authorization") authHeader: String): Call<ArrayList<Persona>>

    // Retrieving tipos tercero from api
    @GET(value="tipo_proveedor")
    fun getTiposTercero(@Header(value="Authorization") authHeader: String): Call<ArrayList<TipoTercero>>

    // Retrieving regimenes fiscales from api
    @GET(value="regimen_fiscal")
    fun getRegimenFiscal(@Header(value="Authorization") authHeader: String): Call<ArrayList<RegimenFiscal>>

    // Retrieving retencion isr from api
    @GET(value="retencion_isr")
    fun getRetencionISR(@Header(value="Authorization") authHeader: String): Call<ArrayList<RetencionISR>>

    // Retrieving retencion iva from api
    @GET(value="retencion_iva")
    fun getRetencionIVA(@Header(value="Authorization") authHeader: String): Call<ArrayList<RetencionIVA>>

    // Retrieving regimenes de capital from api
    @GET(value="regimen_capital")
    fun getRegimenCapital(@Header(value="Authorization") authHeader: String): Call<ArrayList<RegimenCapital>>

    // Retrieving usos CFDI from api
    @GET(value="uso_cfdi")
    fun getUsoCFDI(@Header(value="Authorization") authHeader: String): Call<ArrayList<UsoCFDI>>

    // Retrieving bancos from api
    @GET(value="bancos")
    fun getBancos(@Header(value="Authorization") authHeader: String): Call<ArrayList<Banco>>

    // Retrieving monedas from api
    @GET(value="monedas")
    fun getMonedas(@Header(value="Authorization") authHeader: String): Call<ArrayList<Moneda>>

    // Retrieving proveedores from api
    @GET(value="proveedores")
    fun getProveedores(@Header(value="Authorization") authHeader: String): Call<ArrayList<Proveedor>>

    // Creating a new provider request
    @Multipart
    @POST("proveedores")
    @Headers("Accept: application/json")
    fun postProveedor(
        @Header("Authorization") authHeader: String,
        @Part("empresa") empresa: String,
        @Part("tipo_alta") tipoAlta: String,
        @Part("persona") persona: String,
        @Part("rfc") rfc: String,
        @Part("curp") curp: String,
        @Part("regimen_capital") regimenCapital: String,
        @Part("nombre_fiscal") nombreFiscal: String,
        @Part("nombre_comercial") nombreComercial: String,
        @Part("uso_cfdi") usoCFDI: String,
        @Part("telefono_1") telefono1: String,
        @Part("telefono_2") telefono2: String,
        @Part("contacto") contacto: String,
        @Part("grupo") grupo: String,
        @Part("correo_contacto") correoContacto: String,
        @Part("correo_pagos") correoPagos: String,
        @Part("sitio_web") sitioWeb: String,
        @Part("tipo_tercero") tipoTercero: String,
        @Part("id_fiscal") idFiscal: String,
        @Part("regimen_fiscal") regimenFiscal: String,
        @Part("dias_credito") diasCredito: Int,
        @Part("limite_credito_MN") limiteCreditoMN: Double,
        @Part("limite_credito_ME") limiteCreditoME: Double,
        @Part("retencion_iva") retencionIVA: String,
        @Part("retencion_isr") retencionISR: String,
        @Part("calle") calle: String,
        @Part("numero_exterior") numeroExterior: String,
        @Part("numero_interior") numeroInterior: String,
        @Part("codigo_postal") codigoPostal: String,
        @Part("localidad") localidad: String,
        @Part("municipio") municipio: String,
        @Part("estado") estado: String,
        @Part("pais") pais: String,
        @Part("banco") banco: String,
        @Part("cuenta") cuenta: String,
        @Part("moneda") moneda: String,
        @Part("clabe") clabe: String,
        @Part("banco_2") banco2: String,
        @Part("cuenta_2") cuenta2: String,
        @Part("moneda_2") moneda2: String,
        @Part("clabe_2") clabe2: String,
        @Part constancia: MultipartBody.Part,
        @Part("estado_cuenta") estadoCuenta: MultipartBody.Part
    ): Call<SimpleResponse>

    // MATERIAL APIS

    // Retrieving tipos de alta from api
    @GET(value="material_tipo_alta")
    fun getMaterialTipoAlta(@Header(value="Authorization") authHeader: String): Call<ArrayList<MaterialTipoAlta>>

    // Retrieving familias from api
    @GET(value="material_familias/{tipo_alta}")
    fun getFamilias(@Path(value="tipo_alta") tipoAlta: String, @Header(value="Authorization") authHeader: String):
            Call<ArrayList<Familia>>

    // Retrieving subfamilias from api
    @GET(value="material_subfamilias/{familia}")
    fun getSubfamilias(@Path(value="familia") familia: String, @Header(value="Authorization") authHeader: String):
            Call<ArrayList<Subfamilia>>

    // Retrieving unidades from api
    @GET(value="material_unidades/{tipo_alta}")
    fun getUnidades(@Path(value="tipo_alta") tipoAlta: String, @Header(value="Authorization") authHeader: String):
            Call<ArrayList<Unidad>>

    // Retrieving materiales from api
    @GET(value="materiales")
    fun getMateriales(@Header(value="Authorization") authHeader: String): Call<ArrayList<Material>>

    @FormUrlEncoded
    @POST("materiales")
    @Headers("Accept: application/json")
    fun postMaterial(
        @Header("Authorization") authHeader: String,
        @Field("empresa") empresa: String,
        @Field("nombre") nombre: String,
        @Field("tipo_alta") tipoAlta: String,
        @Field("familia") familia: String,
        @Field("subfamilia") subfamilia: String,
        @Field("marca") marca: String,
        @Field("parte_modelo") parteModelo: String,
        @Field("nombre_comun") nombreComun: String,
        @Field("medida") medida: String,
        @Field("ing_activo") ingActivo: String,
        @Field("tipo_producto") tipoProducto: String,
        @Field("alias") alias: String,
        @Field("unidad") unidad: String,
        @Field("iva") iva: String,
        @Field("ieps") ieps: String,
        @Field("proposito") proposito: String,
        @Field("es_importado") esImportado: Boolean,
        @Field("es_material_empaque") esMaterialEmpaque: Boolean,
        @Field("es_prod_terminado") esProdTerminado: Boolean,
    ): Call<SimpleResponse>

    companion object Factory {
        private const val BASE_URL = "http://192.168.1.5:89/api/"
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}