package com.br.brqinvestimentos.utils

import android.content.SharedPreferences
import android.graphics.Color
import android.widget.TextView
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.viewModel.MoedaViewModel
import java.math.RoundingMode

object FuncoesUtils {

    var quantidadeSaldo = 1000.0
    var usd = 6
    var eur = 1
    var gbp = 0
    var ars = 3
    var cad = 2
    var aud = 0
    var jpy = 4
    var cny = 5
    var btc = 2



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

//    fun mapeiaValoresMoedas(moedas: List<MoedaModel?>): List<MoedaModel?> {
//        return moedas.map {
//            it?.apply {
//                    it.isoValor =
//                        when (it.nome) {
//                            "Dollar" -> 4
//                            "Euro" -> 7
//                            "Pound Sterling" -> 3
//                            "Argentine Peso" -> 2
//                            "Canadian Dollar" -> 8
//                            "Australian Dollar" -> 5
//                            "Japanese Yen" -> 1
//                            "Renminbi" -> 0
//                            "Bitcoin" -> 9
//                            else -> 0
//                        }
//
//
//            }
//        }
//    }

//    fun inicializaShared(shared: SharedPreferences, moeda: MoedaModel){
//        val shared =
//    }


}

