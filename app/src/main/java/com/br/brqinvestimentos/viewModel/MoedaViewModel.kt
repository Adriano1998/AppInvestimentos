package com.br.brqinvestimentos.viewModel

import androidx.lifecycle.MutableLiveData
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.utils.FuncoesUtils
import com.br.brqinvestimentos.utils.FuncoesUtils.mapeiaNome
import com.br.brqinvestimentos.utils.FuncoesUtils.quantidadeSaldo
import com.br.brqinvestimentos.utils.FuncoesUtils.valoresMoedas
import kotlinx.coroutines.launch

class MoedaViewModel(private val repository: MoedaRepository) : BaseViewModel() {

    val listaDeMoedas = MutableLiveData<List<MoedaModel?>>()
    val toastMessageObserver = MutableLiveData<String>()

    fun atualizaMoedas() {
        launch {
            try {
                val call = repository.carregaMoedas()
                val listaMoedas = mapeiaNome(
                    listOfNotNull(
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
                toastMessageObserver.postValue("Algo inesperado aconteceu com a nossa requisição.")
            }
        }
    }

    fun validaQuantidadeComVenda(quantidade: Int, moedaModel: MoedaModel): Boolean {
        if (quantidade <= pegaValorHashmap(moedaModel.isoMoeda) && quantidade > 0) {
            return true
        }
        return false
    }

    fun validaQuantidadeComCompra(quantidade: Int, moedaModel: MoedaModel): Boolean {
        if (moedaModel.valorCompra != null) {
            if (quantidade * moedaModel.valorCompra <= FuncoesUtils.quantidadeSaldo && quantidade > 0) {
                return true
            }
            return false
        }
        return false
    }

    fun pegaValorHashmap(isoMoeda: String): Int {
        var quantidadeSimulada = 0
        if (valoresMoedas.containsKey(isoMoeda)) {
            valoresMoedas.map {
                if (it.key == isoMoeda) {
                    quantidadeSimulada = it.value
                }
            }
        }
        return quantidadeSimulada
    }


    fun calculaCompra(quantidade: Int, moedaModel: MoedaModel): Double {
        var quantidadeTotal = 0.0
        if (validaQuantidadeComCompra(quantidade, moedaModel) && moedaModel.valorCompra != null) {
            valoresMoedas.forEach {
                if (it.key == moedaModel.isoMoeda) {
                    var quantidadeSimulada = it.value
                    quantidadeSimulada += quantidade
                    valoresMoedas[it.key] = quantidadeSimulada
                }
            }
            quantidadeTotal = quantidade * moedaModel.valorCompra
            quantidadeSaldo -= quantidadeTotal
        }
        return quantidadeTotal
    }

    fun calculaVenda(quantidade: Int, moedaModel: MoedaModel) : Double {
        var quantidadeTotal = 0.0
        if (validaQuantidadeComVenda(quantidade, moedaModel) && moedaModel.valorVenda != null) {

            valoresMoedas.forEach {
                if (it.key == moedaModel.isoMoeda) {
                    var quantidadeSimulada = it.value
                    quantidadeSimulada -= quantidade
                    valoresMoedas[it.key] = quantidadeSimulada
                }
            }
            quantidadeTotal = quantidade * moedaModel.valorVenda
            quantidadeSaldo += quantidadeTotal
        }
        return quantidadeTotal
    }

}