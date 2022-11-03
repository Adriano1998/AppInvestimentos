package com.br.brqinvestimentos.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.service.RetrofitHelper
import com.br.brqinvestimentos.utils.FuncoesUtils.mapeiaNome
import com.br.brqinvestimentos.utils.moedas.MoedasUtils
import kotlinx.coroutines.launch

class MoedaViewModel(private val repository: MoedaRepository) : BaseViewModel() {

    val listaDeMoedas = MutableLiveData<List<MoedaModel?>>()
//    val listaSingleton = MutableLiveData<MoedasUtils>()

    fun atualizaMoedas() {
        launch {
            try {
                val call = repository.carregaMoedas()
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

//    fun adicionaValoresMoedas(valorMoeda : MoedasUtils){
//        listaSingleton.value = valorMoeda
//    }


}