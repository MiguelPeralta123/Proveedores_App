package com.learning.proveedoresapp.model

data class Unidad(
    val tipo: String,
    val unidad_medida: String,
    val abreviatura: String
) {
    override fun toString(): String {
        return unidad_medida
    }
}
