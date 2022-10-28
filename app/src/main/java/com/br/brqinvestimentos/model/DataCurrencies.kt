package com.br.brqinvestimentos.model

import com.google.gson.annotations.SerializedName

data class DataCurrencies(

    @SerializedName("currencies")
    var currencies: ResultDataCurrencies = ResultDataCurrencies()
)

data class ResultDataCurrencies(
    @SerializedName("source")
    var source: String = "",
    @SerializedName("USD")
    var USD: MoedaModel? = null,
    @SerializedName("EUR")
    var EUR: MoedaModel? = null,
    @SerializedName("GBP")
    var GBP: MoedaModel? = null,
    @SerializedName("ARS")
    var ARS: MoedaModel? = null,
    @SerializedName("CAD")
    var CAD: MoedaModel? = null
)