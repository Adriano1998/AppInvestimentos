package com.br.brqinvestimentos.utils.moedas

object MoedasUtils {

    val hashMoedas: HashMap<String, Int> = HashMap<String, Int>()

    init {
        hashMoedas.put("USD", 7)
        hashMoedas.put("EUR", 7)
        hashMoedas.put("CAD", 7)
        hashMoedas.put("GBP", 7)
        hashMoedas.put("ARS", 7)
        hashMoedas.put("AUD", 7)
        hashMoedas.put("JPY", 7)
        hashMoedas.put("CNY", 7)
        hashMoedas.put("BTC", 7)
    }

    fun atualizaValoresMoedas(hash: HashMap<String, Int>) {
        hash.get(hash.keys.toString())?.let { hash.put(key = hash.keys.toString(), value = it) }
    }

    fun pegaValorMoeda(hash: HashMap<String, Int>) : HashMap<String, Int>{

    }
}