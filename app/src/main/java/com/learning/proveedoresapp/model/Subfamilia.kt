package com.learning.proveedoresapp.model

data class Subfamilia(
    val familia: String,
    val subfamilia: String
) {
    override fun toString(): String {
        return subfamilia
    }
}
