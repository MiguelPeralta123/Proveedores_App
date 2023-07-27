package com.learning.proveedoresapp.model

data class Provider(
    val empresa: String,
    val nombre: String,
    val rfc: String,
    val curp: String,
    val regimenCapital: String,
    val banco: String,
    val cuenta: String,
    val clabe: String,
    val moneda: String
)
