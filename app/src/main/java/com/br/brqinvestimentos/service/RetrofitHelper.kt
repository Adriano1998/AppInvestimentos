package com.br.brqinvestimentos.service

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

//    fun initApiFinancas(): FinancasServices{
//        val client = OkHttpClient.Builder()
//            .addInterceptor(HeaderInterceptor())
//            .build()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.hgbrasil.com")
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//
//        return retrofit.create(FinancasServices::class.java)
//    }

    companion object {
        fun getRetrofitInstance(path: String): Retrofit {
            val client = OkHttpClient.Builder()
                .addInterceptor(HeaderInterceptor())
                .build()
            return Retrofit.Builder()
                .baseUrl(path)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
    }

}
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("apiMoedas", "268db62e")
                .build()
        )
    }
}

//class HeaderInterceptor : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
//        proceed(
//            request()
//                .newBuilder()
//                .build()
//        )
//    }
//}