package com.br.brqinvestimentos

import com.br.brqinvestimentos.model.DataCurrencies
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.viewModel.MoedaViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class MoedaViewModelTest : BaseTest() {

    val api = mockk<MoedaRepository>(relaxUnitFun = true)
    var viewModel: MoedaViewModel = MoedaViewModel(api)


    @MockK
    lateinit var moedaModel: MoedaModel


    @Test
    fun quandoChamarApi_deveRetornarMoedasDaApi() {
        val resultado = DataCurrencies().apply {
            this.currencies.USD = MoedaModel(
                nome = "Dollar",
                variacao = BigDecimal.ZERO,
                valorCompra = 1.0,
                valorVenda = 2.50,
                isoMoeda = "USD"
            )
        }
        val listaEsperada = listOfNotNull(
            resultado.currencies.USD,
            resultado.currencies.EUR,
            resultado.currencies.CAD,
            resultado.currencies.GBP,
            resultado.currencies.ARS,
            resultado.currencies.AUD,
            resultado.currencies.JPY,
            resultado.currencies.CNY,
            resultado.currencies.BTC
        )

        coEvery { api.carregaMoedas() } returns resultado

        viewModel.atualizaMoedas()

        Assert.assertEquals(listaEsperada, viewModel.listaDeMoedas.value)


    }

    @Test
    fun quandoChamarApi_deveRetornarErro() {

        coEvery { api.carregaMoedas() } throws Exception("Algo inesperado aconteceu com a nossa requisição.")


        viewModel.atualizaMoedas()
        assertEquals(
            "Algo inesperado aconteceu com a nossa requisição.",
            viewModel.toastMessageObserver.value
        )


    }


}


