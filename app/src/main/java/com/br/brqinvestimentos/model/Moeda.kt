package com.br.brqinvestimentos.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal

data class Moeda(
    @SerializedName("results.currencies.USD.name")
    val nome: String?,
    @SerializedName("results.currencies.USD.variation")
    var variacao: BigDecimal?
): Serializable {
}