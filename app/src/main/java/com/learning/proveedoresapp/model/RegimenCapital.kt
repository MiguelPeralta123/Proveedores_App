package com.learning.proveedoresapp.model

data class RegimenCapital(
    val nombre: String,
    val clave: String
) {
    override fun toString(): String {
        return "$nombre $clave"
    }
}
