package com.br.brqinvestimentos.viewModel

import android.widget.Button
import androidx.lifecycle.MutableLiveData
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.utils.FuncoesUtils
import com.br.brqinvestimentos.utils.FuncoesUtils.mapeiaNome
import com.br.brqinvestimentos.utils.FuncoesUtils.mapeiaValoresMoedas
import kotlinx.coroutines.launch

class MoedaViewModel(private val repository: MoedaRepository) : BaseViewModel() {

    val listaDeMoedas = MutableLiveData<List<MoedaModel?>>()


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
                ).let {
                    mapeiaValoresMoedas(it)
                }

                listaDeMoedas.postValue(listaMoedas)
            } catch (e: Exception) {

            }

        }

    }

    fun desabilitaBotao(botao: Button, caminhoDrawable: Int) {
        botao.isEnabled = false
        botao.setBackgroundResource(caminhoDrawable)
    }

    fun validaQuantidadeComVenda(quantidade: Int, moedaModel: MoedaModel): Boolean {
        if (quantidade <= moedaModel.isoValor && quantidade > 0) {
            return true
        }
        return false
    }

    fun validaQuantidadeComCompra(quantidade: Int, moedaModel: MoedaModel): Boolean {
        if (quantidade * moedaModel.valorCompra!! <= FuncoesUtils.quantidadeSaldo) {
            return true
        }
        return false
    }

    fun habilitaBotao(botao: Button, caminhoDrawable: Int) {
        botao.isEnabled = true
        botao.setBackgroundResource(caminhoDrawable)
    }

    fun calculaCompra(quantidade: Int, moedaModel: MoedaModel, funcoesUtils: FuncoesUtils) {
        if (validaQuantidadeComCompra(quantidade, moedaModel)) {
            funcoesUtils.quantidadeSaldo -= quantidade * moedaModel.valorCompra!!
        }
    }

    fun calculaVenda(quantidade: Int, moedaModel: MoedaModel, funcoesUtils: FuncoesUtils): Int {
        if (validaQuantidadeComVenda(quantidade, moedaModel)) {
            moedaModel.isoValor -= quantidade
            funcoesUtils.quantidadeSaldo += quantidade * moedaModel.valorVenda!!
        }
        return moedaModel.isoValor
    }


}