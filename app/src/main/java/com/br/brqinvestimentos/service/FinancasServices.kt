package com.br.brqinvestimentos.service

import com.br.brqinvestimentos.model.Moeda
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface FinancasServices {

    @GET("/finance")
    fun buscaMoedas() : Call<List<Moeda?>>

}