package com.br.brqinvestimentos.utils

import android.graphics.Color
import android.widget.TextView
import com.br.brqinvestimentos.model.MoedaModel
import java.math.RoundingMode

object FuncoesUtils {


    fun trocaCorVariacaoMoeda(txtVariacao: TextView, moeda: MoedaModel) {
        val variacao = "0.0"
        if (moeda.variacao!! < variacao.toBigDecimal() && moeda.variacao != null) {
            txtVariacao.setTextColor(Color.parseColor("#D0021B"))
        } else if (moeda.variacao!! == variacao.toBigDecimal() && moeda.variacao != null) {
            txtVariacao.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            txtVariacao.setTextColor(Color.parseColor("#7ED321"))
        }
    }

    fun acertaCasasDecimaisVariacao(moeda: MoedaModel, txtVariacao: TextView) {
        txtVariacao.text = moeda.variacao?.setScale(2, RoundingMode.UP).toString() + "%"

    }

    fun mapeiaNome(moedas: List<MoedaModel?>): List<MoedaModel?> {
        return moedas.map {
            it?.apply {
                it.isoMoeda =
                    when (it.nome) {
                        "Dollar" -> "USD"
                        "Euro" -> "EUR"
                        "Pound Sterling" -> "GBP"
                        "Argentine Peso" -> "ARS"
                        "Canadian Dollar" -> "CAD"
                        "Australian Dollar" -> "AUD"
                        "Japanese Yen" -> "JPY"
                        "Renminbi" -> "CNY"
                        "Bitcoin" -> "BTC"
                        else -> ""
                    }
            }
        }
    }
}

