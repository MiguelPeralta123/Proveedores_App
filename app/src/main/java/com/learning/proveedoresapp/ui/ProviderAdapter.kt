package com.learning.proveedoresapp.ui

import android.os.Build
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.learning.proveedoresapp.R
import com.learning.proveedoresapp.model.Proveedor
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class ProviderAdapter:
    RecyclerView.Adapter<ProviderAdapter.ViewHolder>() {

    var providers = ArrayList<Proveedor>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvNombreFiscal = itemView.findViewById<TextView>(R.id.tv_nombre_fiscal)
        val tvEmpresa = itemView.findViewById<TextView>(R.id.tv_empresa)
        val tvRfc = itemView.findViewById<TextView>(R.id.tv_rfc)
        val tvFechaCreacion = itemView.findViewById<TextView>(R.id.tv_fecha_creacion)

        // Show details
        val tvCurp = itemView.findViewById<TextView>(R.id.tv_curp)
        val tvNombreComercial = itemView.findViewById<TextView>(R.id.tv_nombre_comercial)

        // Expand button
        val linearLayoutDetails = itemView.findViewById<LinearLayout>(R.id.linear_layout_provider_details)
        val ibExpand = itemView.findViewById<ImageButton>(R.id.ib_expand)
        val tvSeeMore = itemView.findViewById<TextView>(R.id.tv_see_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_provider, parent, false)
        )
    }

    override fun getItemCount() = providers.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val provider = providers[position]

        holder.tvNombreFiscal.text = provider.nombreFiscal
        holder.tvEmpresa.text = provider.empresa
        holder.tvRfc.text = provider.rfc
        holder.tvFechaCreacion.text = parseDate(provider.fechaCreacion)

        // Show details
        holder.tvCurp.text = provider.curp
        holder.tvNombreComercial.text = provider.nombreComercial

        // Expand button
        holder.ibExpand.setOnClickListener {
            // Animation to expand/collapse request details
            TransitionManager.beginDelayedTransition(holder.linearLayoutDetails as ViewGroup, AutoTransition())

            if (holder.linearLayoutDetails.visibility == View.VISIBLE) {
                holder.linearLayoutDetails.visibility = View.GONE
                holder.ibExpand.setImageResource(R.drawable.ic_expand_more)
                holder.tvSeeMore.text = "Ver mas"
            } else {
                holder.linearLayoutDetails.visibility = View.VISIBLE
                holder.ibExpand.setImageResource(R.drawable.ic_expand_less)
                holder.tvSeeMore.text = "Ver menos"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDate(apiDateString: String): String {
        // Parse the API date string into a LocalDateTime object
        val apiDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val localDateTime = LocalDateTime.parse(apiDateString, apiDateFormatter)

        // Format the LocalDateTime object into the desired string format
        val outputDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        return localDateTime.format(outputDateFormatter)
    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    fun parseDate(dateString: String): String {
        // Parse the API date string into a LocalDateTime object
        val apiDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val localDateTime = LocalDateTime.parse(dateString, apiDateFormatter)

        // Format the LocalDateTime object into the desired string format
        val outputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
        return localDateTime.format(outputDateFormatter)
    }*/
}