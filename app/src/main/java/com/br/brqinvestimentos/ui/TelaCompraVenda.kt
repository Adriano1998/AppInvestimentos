package com.br.brqinvestimentos.ui

import android.content.Intent
import android.os.Bundle
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.databinding.ActivityTelaCompraVendaBinding
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.utils.Constantes.Companion.EHCOMPRA
import com.br.brqinvestimentos.utils.Constantes.Companion.MOEDA
import com.br.brqinvestimentos.utils.Constantes.Companion.QUANTIDADE
import com.br.brqinvestimentos.utils.Constantes.Companion.QUANTIDADE_TOTAL
import com.br.brqinvestimentos.utils.FuncoesUtils.formatadorMoedaBrasileira

class TelaCompraVenda : BaseActivity() {

    private val binding by lazy {
        ActivityTelaCompraVendaBinding.inflate(layoutInflater)
    }
    private var moeda: MoedaModel? = null

    private val sbTexto = StringBuilder()

    var operacaoTexto = ""
    var operacaoToolbar = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        moeda = intent.getSerializableExtra(MOEDA) as? MoedaModel

        binding.btnVaiParaHome.setOnClickListener {
            vaiParaTelaHome()
        }
        val ehCompra = intent.getBooleanExtra(EHCOMPRA, false)

        configuraSubTituloToolbar(true, getString(R.string.cambio), binding.toolbarcompravenda.toolbarSubTitle)

        decideCompraOuVenda(ehCompra)

        configuraTelaCompra()
        configuraToolbar(
            true,
            operacaoToolbar,
            binding.toolbarcompravenda.toolbarTitle,
            binding.toolbarcompravenda.btnVoltarTelaMoedas
        )

    }

    private fun decideCompraOuVenda(ehCompra: Boolean) {
        if (!ehCompra) {
            operacaoToolbar = getString(R.string.Vender)
            operacaoTexto = getString(R.string.vender)

        } else {
            operacaoToolbar = getString(R.string.Comprar)
            operacaoTexto = getString(R.string.comprar)
        }
    }

    private fun configuraTelaCompra() {
        val quantidade = intent.getIntExtra(QUANTIDADE, 0)
        val calculoTotal = intent.getDoubleExtra(QUANTIDADE_TOTAL,0.0)
        sbTexto.let { sb ->
            sb.append(
                getString(R.string.parabens),
                getString(R.string.pula_linha),
                getString(R.string.espaco),
                getString(R.string.voce_acabou_de),
                operacaoTexto, getString(R.string.pula_linha),
                quantidade,
                getString(R.string.espaco),
                moeda?.isoMoeda,
                getString(R.string.espaco),
                getString(R.string.traco),
                getString(R.string.espaco),
                moeda?.nome,
                getString(R.string.virgula),
                getString(R.string.pula_linha),
                getString(R.string.totalizando),
                getString(R.string.pula_linha),
                formatadorMoedaBrasileira(calculoTotal)
            )
            binding.textoCompraVenda.text = sb
        }

    }

    private fun vaiParaTelaHome() {
        val intent = Intent(this@TelaCompraVenda, TelaHome::class.java)
        moeda?.let {
            viewModel.pegaValorHashmap(it.isoMoeda)
        }
        intent.putExtra(MOEDA, moeda)
        startActivity(intent)
    }
}