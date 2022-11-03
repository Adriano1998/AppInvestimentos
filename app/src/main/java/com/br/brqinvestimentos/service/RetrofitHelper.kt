package com.br.brqinvestimentos.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    private val URL_BASE = "https://api.hgbrasil.com/"


    fun initApiFinancas(): FinancasApi {

        val interceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(FinancasApi::class.java)
    }
}


