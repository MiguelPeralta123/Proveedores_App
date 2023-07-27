package com.learning.proveedoresapp.model

data class Empresa(
    val nombre: String
) {
    override fun toString(): String {
        return nombre
    }
}