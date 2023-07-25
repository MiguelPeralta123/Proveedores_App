package com.learning.proveedoresapp.model

data class User(
    val id: Int,
    val username: String,
    val nombre: String,
    val puesto: String,
    val correo: String,
    val comprador: Boolean,
    val aprob_compras: Boolean,
    val aprob_finanzas: Boolean,
    val aprob_sistemas: Boolean,
    val admin: Boolean,
)