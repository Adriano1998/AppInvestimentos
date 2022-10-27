package com.br.brqinvestimentos.adapter

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.model.DataCurrencies
import com.br.brqinvestimentos.model.MoedaModel
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

class MoedaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cardMoeda: LinearLayout = itemView.findViewById(R.id.ll_cardMoeda)
    private val nomeMoeda = itemView.findViewById<TextView>(R.id.txtNomeMoeda)
    var variacaoMoeda = itemView.findViewById<TextView>(R.id.txtVariacaoMoeda)

    fun vincula(moeda: MoedaModel) {
        nomeMoeda.text = moeda.nome
        var variacao = "0.0"
        indicaCorDaVariacao(moeda, variacao)
        variacaoMoeda.text = moeda.variacao?.setScale(2, RoundingMode.UP).toString() + "%"

    }

    private fun indicaCorDaVariacao(
        moeda: MoedaModel,
        variacao: String
    ) {
        if (moeda.variacao!! < variacao.toBigDecimal() && moeda.variacao != null) {
            variacaoMoeda.setTextColor(Color.RED)
        } else if (moeda.variacao!! == variacao.toBigDecimal() && moeda.variacao != null) {
            variacaoMoeda.setTextColor(Color.WHITE)
        } else {
            variacaoMoeda.setTextColor(Color.GREEN)
        }
    }


}