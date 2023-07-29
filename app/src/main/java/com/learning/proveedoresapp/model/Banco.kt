package com.learning.proveedoresapp.model

data class Banco(
    val nombre: String,
    val clave: String
) {
    override fun toString(): String {
        return "$nombre $clave"
    }
}
