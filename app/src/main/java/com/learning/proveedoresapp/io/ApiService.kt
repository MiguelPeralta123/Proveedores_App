package com.learning.proveedoresapp.io

import com.learning.proveedoresapp.io.response.LoginResponse
import com.learning.proveedoresapp.model.Banco
import com.learning.proveedoresapp.model.Empresa
import com.learning.proveedoresapp.model.Estado
import com.learning.proveedoresapp.model.Grupo
import com.learning.proveedoresapp.model.Moneda
import com.learning.proveedoresapp.model.Pais
import com.learning.proveedoresapp.model.Persona
import com.learning.proveedoresapp.model.Proveedor
import com.learning.proveedoresapp.model.RegimenCapital
import com.learning.proveedoresapp.model.RegimenFiscal
import com.learning.proveedoresapp.model.RetencionISR
import com.learning.proveedoresapp.model.RetencionIVA
import com.learning.proveedoresapp.model.TipoAlta
import com.learning.proveedoresapp.model.TipoTercero
import com.learning.proveedoresapp.model.UsoCFDI
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
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
    @GET(value="proveedores/22")
    fun getProveedores(@Header(value="Authorization") authHeader: String): Call<ArrayList<Proveedor>>

    companion object Factory {
        private const val BASE_URL = "http://192.168.1.6:89/api/"
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}