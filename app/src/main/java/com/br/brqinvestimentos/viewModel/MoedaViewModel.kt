package com.br.brqinvestimentos.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.brqinvestimentos.model.DataCurrencies
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.service.RetrofitHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MoedaViewModel : BaseViewModel() {

    val listaDeMoedas = MutableLiveData<List<MoedaModel?>>()


    fun atualizaMoedas() {
        launch {
            try {
                val services = RetrofitHelper().initApiFinancas()
                val call = services.buscaMoedas()
                val listaMoedas = listOf<MoedaModel?>(
                    call.currencies.USD,
                    call.currencies.EUR,
                    call.currencies.CAD,
                    call.currencies.GBP,
                    call.currencies.ARS
                )
                listaDeMoedas.postValue(listaMoedas)
            } catch (e: Exception) {

            }
        }
    }


}