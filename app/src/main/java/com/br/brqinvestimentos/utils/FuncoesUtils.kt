package com.br.brqinvestimentos.utils

import android.graphics.Color
import android.widget.TextView
import com.br.brqinvestimentos.model.MoedaModel
import java.math.RoundingMode

class FuncoesUtils {

    companion object Factory {

        fun trocaCorVariacaoMoeda(txtVariacao: TextView, moeda: MoedaModel) {
            val variacao = "0.0"
            if (moeda.variacao!! < variacao.toBigDecimal() && moeda.variacao != null) {
                txtVariacao.setTextColor(Color.RED)
            } else if (moeda.variacao!! == variacao.toBigDecimal() && moeda.variacao != null) {
                txtVariacao.setTextColor(Color.WHITE)
            } else {
                txtVariacao.setTextColor(Color.GREEN)
            }
        }

        fun acertaCasasDecimaisVariacao(moeda: MoedaModel, txtVariacao: TextView){
              txtVariacao.text = moeda.variacao?.setScale(2, RoundingMode.UP).toString() + "%"

        }
    }
}
