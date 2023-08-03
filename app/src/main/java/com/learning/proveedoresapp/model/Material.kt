package com.learning.proveedoresapp.model

import com.google.gson.annotations.SerializedName

data class Material(
    val id: Int,
    val empresa: String,
    @SerializedName("id_solicitante") val idSolicitante: Int,
    @SerializedName("nombre_solicitante") val nombreSolicitante: String,
    @SerializedName("fecha_creacion") val fechaCreacion: String,
    @SerializedName("id_modificador") val idModificador: Int,
    @SerializedName("nombre_modificador") val nombreModificador: String,
    @SerializedName("fecha_modificacion") val fechaModificacion: String,
    val nombre: String,
    @SerializedName("tipo_alta") val tipoAlta: String,
    val familia: String,
    val subfamilia: String,
    val marca: String,
    val parte_modelo: String,
    @SerializedName("nombre_comun") val nombreComun: String,
    val medida: String,
    @SerializedName("ing_activo") val ingActivo: String,
    @SerializedName("tipo_producto") val tipoProducto: String,
    val alias: String,
    val unidad: String,
    val iva: String,
    val ieps: String,
    val proposito: String,
    @SerializedName("es_importado") val esImportado: Boolean,
    @SerializedName("es_material_empaque") val esMaterialEmpaque: Boolean,
    @SerializedName("es_prod_terminado") val esProdTerminado: Boolean,
    val compras: Boolean,
    val finanzas: Boolean,
    val sistemas: Boolean,
    val aprobado: Boolean,
    @SerializedName("rechazado_compras") val rechazadoCompras: Boolean,
    @SerializedName("rechazado_finanzas") val rechazadoFinanzas: Boolean,
    @SerializedName("rechazado_sistemas") val rechazadoSistemas: Boolean,
    val comentarios: String
) {

}