package com.learning.proveedoresapp.model

data class RegimenFiscal(
    val descripcion: String
) {
    override fun toString(): String {
        return descripcion
    }
}
