package com.learning.proveedoresapp.model

data class Familia(
    val tipo: String,
    val familia: String
) {
    override fun toString(): String {
        return familia
    }
}
