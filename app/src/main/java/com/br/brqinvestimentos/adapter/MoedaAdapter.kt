package com.br.brqinvestimentos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.model.MoedaModel

class MoedaAdapter(private val onClick: (MoedaModel) -> Unit) :
    RecyclerView.Adapter<MoedaViewHolder>() {

    private val listaMoedas = mutableListOf<MoedaModel?>()
//    private var onItemClickListener : OnItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoedaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_moedas, parent, false)
        return MoedaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoedaViewHolder, position: Int) {
        listaMoedas[position]?.let { moeda ->
            holder.vincula(moeda)
            holder.itemView.setOnClickListener { onClick.invoke(moeda) }
        }

    }

    override fun getItemCount(): Int = listaMoedas.size

    fun atualiza(newList: List<MoedaModel?>) {
        listaMoedas.clear()
        listaMoedas.addAll(newList)
        notifyDataSetChanged()
    }

}
