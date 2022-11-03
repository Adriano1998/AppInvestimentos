package com.br.brqinvestimentos.service

import com.br.brqinvestimentos.model.DataCurrencies
import com.br.brqinvestimentos.model.MoedaModel
import retrofit2.http.GET
import java.util.*

interface FinancasApi {

    @GET("finance?fields=only_results,currencies&key=268db62e")
    suspend fun buscaMoedas() : DataCurrencies



}