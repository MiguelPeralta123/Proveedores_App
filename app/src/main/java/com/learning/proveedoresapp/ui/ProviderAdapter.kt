package com.learning.proveedoresapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learning.proveedoresapp.R
import com.learning.proveedoresapp.model.Proveedor

class ProviderAdapter:
    RecyclerView.Adapter<ProviderAdapter.ViewHolder>() {

    var providers = ArrayList<Proveedor>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvNombreFiscal = itemView.findViewById<TextView>(R.id.tv_nombre_fiscal)
        val tvEmpresa = itemView.findViewById<TextView>(R.id.tv_empresa)
        val tvRfc = itemView.findViewById<TextView>(R.id.tv_rfc)
        val tvFechaCreacion = itemView.findViewById<TextView>(R.id.tv_fecha_creacion)

        // Request details
        val
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_provider, parent, false)
        )
    }

    override fun getItemCount() = providers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val provider = providers[position]

        holder.tvNombreFiscal.text = provider.nombreFiscal
        holder.tvEmpresa.text = "Subir en: ${provider.empresa}"
        holder.tvRfc.text = provider.rfc
        holder.tvFechaCreacion.text = provider.curp
    }
}