package com.br.brqinvestimentos.repository

import com.br.brqinvestimentos.model.DataCurrencies
import com.br.brqinvestimentos.service.RetrofitHelper

class MoedaRepository {

    val services = RetrofitHelper().initApiFinancas()

    suspend fun carregaMoedas(): DataCurrencies {
        return services.buscaMoedas()
    }


}