package com.br.brqinvestimentos.viewModel

import android.widget.Button
import androidx.lifecycle.MutableLiveData
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.utils.FuncoesUtils
import com.br.brqinvestimentos.utils.FuncoesUtils.ars
import com.br.brqinvestimentos.utils.FuncoesUtils.aud
import com.br.brqinvestimentos.utils.FuncoesUtils.btc
import com.br.brqinvestimentos.utils.FuncoesUtils.cad
import com.br.brqinvestimentos.utils.FuncoesUtils.cny
import com.br.brqinvestimentos.utils.FuncoesUtils.eur
import com.br.brqinvestimentos.utils.FuncoesUtils.gbp
import com.br.brqinvestimentos.utils.FuncoesUtils.jpy
import com.br.brqinvestimentos.utils.FuncoesUtils.mapeiaNome
import com.br.brqinvestimentos.utils.FuncoesUtils.usd
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
        if (quantidade <= moedaModel.isoValor && quantidade > 0) {
            return true
        }
        return false
    }

    fun simulaValorParaSingleton(moeda: MoedaModel) {
        when {
            moeda.isoMoeda.equals("USD") -> moeda.isoValor = usd
            moeda.isoMoeda.equals("EUR") -> moeda.isoValor = eur
            moeda.isoMoeda.equals("GBP") -> moeda.isoValor = gbp
            moeda.isoMoeda.equals("ARS") -> moeda.isoValor = ars
            moeda.isoMoeda.equals("CAD") -> moeda.isoValor = cad
            moeda.isoMoeda.equals("AUD") -> moeda.isoValor = aud
            moeda.isoMoeda.equals("JPY") -> moeda.isoValor = jpy
            moeda.isoMoeda.equals("CNY") -> moeda.isoValor = cny
            moeda.isoMoeda.equals("BTC") -> moeda.isoValor = btc
        }
    }

    fun subtraiValorSimulado(moeda: MoedaModel, quantidade: Int) {
        when {
            moeda.isoMoeda.equals("USD") -> usd -= quantidade
            moeda.isoMoeda.equals("EUR") -> eur -= quantidade
            moeda.isoMoeda.equals("GBP") -> gbp -= quantidade
            moeda.isoMoeda.equals("ARS") -> ars -= quantidade
            moeda.isoMoeda.equals("CAD") -> cad -= quantidade
            moeda.isoMoeda.equals("AUD") -> aud -= quantidade
            moeda.isoMoeda.equals("JPY") -> jpy -= quantidade
            moeda.isoMoeda.equals("CNY") -> cny -= quantidade
            moeda.isoMoeda.equals("BTC") -> btc -= quantidade
        }
    }

    fun somaValorSimulado(moeda: MoedaModel, quantidade: Int) {
        when {
            moeda.isoMoeda.equals("USD") -> usd += quantidade
            moeda.isoMoeda.equals("EUR") -> eur += quantidade
            moeda.isoMoeda.equals("GBP") -> gbp += quantidade
            moeda.isoMoeda.equals("ARS") -> ars += quantidade
            moeda.isoMoeda.equals("CAD") -> cad += quantidade
            moeda.isoMoeda.equals("AUD") -> aud += quantidade
            moeda.isoMoeda.equals("JPY") -> jpy += quantidade
            moeda.isoMoeda.equals("CNY") -> cny += quantidade
            moeda.isoMoeda.equals("BTC") -> btc += quantidade
        }
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

    fun calculaCompra(quantidade: Int, moedaModel: MoedaModel, funcoesUtils: FuncoesUtils) {
        if (validaQuantidadeComCompra(quantidade, moedaModel) && moedaModel.valorCompra != null) {
            funcoesUtils.quantidadeSaldo -= quantidade * moedaModel.valorCompra
            somaValorSimulado(moedaModel, quantidade)
        }
    }

    fun calculaVenda(quantidade: Int, moedaModel: MoedaModel, funcoesUtils: FuncoesUtils): Int {
        if (validaQuantidadeComVenda(quantidade, moedaModel) && moedaModel.valorVenda != null) {
            subtraiValorSimulado(moedaModel, quantidade)
            funcoesUtils.quantidadeSaldo += quantidade * moedaModel.valorVenda
        }
        return moedaModel.isoValor
    }

}