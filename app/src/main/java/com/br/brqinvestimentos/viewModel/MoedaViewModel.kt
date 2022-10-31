package com.br.brqinvestimentos.viewModel

import androidx.lifecycle.MutableLiveData
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.service.RetrofitHelper
import com.br.brqinvestimentos.utils.FuncoesUtils.mapeiaNome
import kotlinx.coroutines.launch

class MoedaViewModel : BaseViewModel() {

    val listaDeMoedas = MutableLiveData<List<MoedaModel?>>()


    fun atualizaMoedas() {
        launch {
            try {
                val services = RetrofitHelper().initApiFinancas()
                val call = services.buscaMoedas()
                val listaMoedas = mapeiaNome(
                    listOf(
                        call.currencies.USD,
                        call.currencies.EUR,
                        call.currencies.CAD,
                        call.currencies.GBP,
                        call.currencies.ARS,
                        call.currencies.AUD,
                        call.currencies.JPY,
                        call.currencies.CNY,
                        call.currencies.BTC


                    )
                )
                listaDeMoedas.postValue(listaMoedas)
            } catch (e: Exception) {

            }
        }
    }



}