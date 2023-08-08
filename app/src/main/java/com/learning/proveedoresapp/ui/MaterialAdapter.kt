package com.learning.proveedoresapp.ui

import android.content.Intent
import android.os.Build
import android.speech.tts.TextToSpeech
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.learning.proveedoresapp.R
import com.learning.proveedoresapp.model.Material
import com.learning.proveedoresapp.model.Proveedor
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MaterialAdapter: RecyclerView.Adapter<MaterialAdapter.ViewHolder>() {

    var materials = ArrayList<Material>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvId = itemView.findViewById<TextView>(R.id.tv_id)
        val tvNombre = itemView.findViewById<TextView>(R.id.tv_nombre)
        val tvEmpresa = itemView.findViewById<TextView>(R.id.tv_empresa)
        val tvFecha = itemView.findViewById<TextView>(R.id.tv_fecha_creacion)
        val tvTipoAlta = itemView.findViewById<TextView>(R.id.tv_tipo_alta)

        // Show details
        val tvFamilia = itemView.findViewById<TextView>(R.id.tv_familia)
        val tvSubfamilia = itemView.findViewById<TextView>(R.id.tv_subfamilia)
        val tvMarca = itemView.findViewById<TextView>(R.id.tv_marca)
        val tvParteModelo = itemView.findViewById<TextView>(R.id.tv_parte_modelo)
        val tvNombreComun = itemView.findViewById<TextView>(R.id.tv_nombre_comun)
        val tvMedida = itemView.findViewById<TextView>(R.id.tv_medida)
        val tvIngActivo = itemView.findViewById<TextView>(R.id.tv_ing_activo)
        val tvTipoProducto = itemView.findViewById<TextView>(R.id.tv_tipo_producto)
        val tvAlias = itemView.findViewById<TextView>(R.id.tv_alias)
        val tvUnidad = itemView.findViewById<TextView>(R.id.tv_unidad)
        val tvIva = itemView.findViewById<TextView>(R.id.tv_iva)
        val tvIeps = itemView.findViewById<TextView>(R.id.tv_ieps)
        val tvProposito = itemView.findViewById<TextView>(R.id.tv_proposito)
        val tvEsImportado = itemView.findViewById<TextView>(R.id.tv_es_importado)
        val tvEsMaterialEmpaque = itemView.findViewById<TextView>(R.id.tv_es_material_empaque)
        val tvEsProdTerminado = itemView.findViewById<TextView>(R.id.tv_es_prod_terminado)
        val tvComentarios = itemView.findViewById<TextView>(R.id.tv_comentarios)

        // Expand button
        val linearLayoutDetails = itemView.findViewById<LinearLayout>(R.id.linear_layout_material_details)
        val ibExpand = itemView.findViewById<ImageButton>(R.id.ib_expand)
        val tvSeeMore = itemView.findViewById<TextView>(R.id.tv_see_more)

        // Modify button
        val btnModify = itemView.findViewById<TextView>(R.id.btn_modify_material)
        val context = itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_material, parent, false)
        )
    }

    override fun getItemCount() = materials.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val material = materials[position]

        holder.tvId.text = material.id.toString()
        holder.tvNombre.text = material.nombre
        holder.tvEmpresa.text = material.empresa
        //holder.tvFecha.text = parseDate(material.fechaCreacion)
        holder.tvFecha.text = material.fechaCreacion
        holder.tvTipoAlta.text = material.tipoAlta

        // Show details
        holder.tvFamilia.text = material.familia
        holder.tvSubfamilia.text = material.subfamilia
        holder.tvMarca.text = material.marca
        holder.tvParteModelo.text = material.parte_modelo
        holder.tvNombreComun.text = material.nombreComun
        holder.tvMedida.text = material.medida
        holder.tvIngActivo.text = material.ingActivo
        holder.tvTipoProducto.text = material.tipoProducto
        holder.tvAlias.text = material.alias
        holder.tvUnidad.text = material.unidad
        holder.tvIva.text = material.iva
        holder.tvIeps.text = material.ieps
        holder.tvProposito.text = material.proposito
        holder.tvEsImportado.text = if (material.esImportado) "Si" else "No"
        holder.tvEsMaterialEmpaque.text = if (material.esMaterialEmpaque) "Si" else "No"
        holder.tvEsProdTerminado.text = if (material.esProdTerminado) "Si" else "No"
        holder.tvComentarios.text = material.comentarios

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

        holder.btnModify.setOnClickListener() {
            val i = Intent(holder.context, ModifyMaterialActivity::class.java)
            i.putExtra("materialId", holder.tvId.text.toString().toInt())
            holder.context.startActivity(i)
        }
    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    fun parseDate(apiDateString: String): String {
        // Parse the API date string into a LocalDateTime object
        val apiDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val localDateTime = LocalDateTime.parse(apiDateString, apiDateFormatter)

        // Format the LocalDateTime object into the desired string format
        val outputDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        return localDateTime.format(outputDateFormatter)
    }*/
}