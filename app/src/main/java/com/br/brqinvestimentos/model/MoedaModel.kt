package com.br.brqinvestimentos.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal

data class MoedaModel(
    @SerializedName("name")
    val nome: String? = null,
    @SerializedName("variation")
    var variacao: BigDecimal? = null,
    @SerializedName("buy")
    val valorCompra: Double? = null,
    @SerializedName("sell")
    val valorVenda: Double? = null,
    var isoMoeda: String = "",
    var isoValor: Int = 0

) : Serializable

