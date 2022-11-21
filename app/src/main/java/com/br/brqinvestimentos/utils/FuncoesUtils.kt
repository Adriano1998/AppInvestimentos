package com.br.brqinvestimentos.utils

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.model.MoedaModel
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

object FuncoesUtils {

    var quantidadeSaldo = 1000.0
    var usd = 5
    var eur = 3
    var gbp = 4
    var ars = 3
    var cad = 2
    var aud = 0
    var jpy = 4
    var cny = 2
    var btc = 2


//    val map: HashMap<String, Int> = hashMapOf(
//        "USD" to 15, "EUR" to 10, "GBP" to 0, "ARS" to 3, "CAD" to 5, "AUD" to 3,
//        "JPY" to 2, "CNY" to 4, "BTC" to 1
//    )


    fun trocaCorVariacaoMoeda(txtVariacao: TextView, moeda: MoedaModel) {
        val variacao = "0.0"
        moeda.variacao?.let { variacaoDinamica ->
            if (variacaoDinamica < variacao.toBigDecimal()) {
                configuraCor(txtVariacao, R.color.variation_red)
            } else if (variacaoDinamica == variacao.toBigDecimal()) {
                configuraCor(txtVariacao, R.color.variation_white)
            } else {
                configuraCor(txtVariacao, R.color.variation_green)
            }
        }

    }

    fun configuraCor(text: TextView, cor: Int) {
        text.setTextColor(ContextCompat.getColor(text.context, cor))
    }

    fun formatadorMoedaBrasileira(valor: Double): String {
        val locale = Locale("pt", "BR")
        val formatBrasil = NumberFormat.getCurrencyInstance(locale).format(valor)
        return formatBrasil
    }

    fun formataPorcentagem(valor: BigDecimal): String {
        val decimalFormat = DecimalFormatSymbols.getInstance()
        decimalFormat.decimalSeparator = ','
        val df = DecimalFormat("#,##0.00 '%'", decimalFormat).format(valor)
        return df
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

