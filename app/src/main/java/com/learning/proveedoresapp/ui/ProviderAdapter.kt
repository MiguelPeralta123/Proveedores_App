package com.learning.proveedoresapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learning.proveedoresapp.R
import com.learning.proveedoresapp.model.Provider

class ProviderAdapter(private val providers: ArrayList<Provider>) :
    RecyclerView.Adapter<ProviderAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvEmpresa = itemView.findViewById<TextView>(R.id.tv_empresa)
        val tvNombre = itemView.findViewById<TextView>(R.id.tv_nombre)
        val tvRfc = itemView.findViewById<TextView>(R.id.tv_rfc)
        val tvCurp = itemView.findViewById<TextView>(R.id.tv_curp)
        /*val tvRegimenCapital = itemView.findViewById<TextView>(R.id.tv_regimen_capital)
        val tvBanco = itemView.findViewById<TextView>(R.id.tv_banco)
        val tvCuenta = itemView.findViewById<TextView>(R.id.tv_cuenta)
        val tvClabe = itemView.findViewById<TextView>(R.id.tv_clabe)
        val tvMoneda = itemView.findViewById<TextView>(R.id.tv_moneda)*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_provider, parent, false)
        )
    }

    override fun getItemCount() = providers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val provider = providers[position]

        holder.tvEmpresa.text = "Subir en: ${provider.empresa}"
        holder.tvNombre.text = provider.nombre
        holder.tvRfc.text = provider.rfc
        holder.tvCurp.text = provider.curp
        /*holder.tvRegimenCapital.text = provider.regimenCapital
        holder.tvBanco.text = provider.banco
        holder.tvCuenta.text = provider.cuenta
        holder.tvClabe.text = provider.clabe
        holder.tvMoneda.text = provider.moneda*/
    }
}