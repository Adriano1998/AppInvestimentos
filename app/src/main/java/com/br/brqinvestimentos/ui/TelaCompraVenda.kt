package com.br.brqinvestimentos.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.br.brqinvestimentos.databinding.ActivityTelaCompraVendaBinding
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.utils.Constantes.Companion.EHCOMPRA
import com.br.brqinvestimentos.utils.Constantes.Companion.MOEDA
import com.br.brqinvestimentos.utils.Constantes.Companion.QUANTIDADE
import com.br.brqinvestimentos.utils.FuncoesUtils.formatadorMoedaBrasileira

class TelaCompraVenda : BaseActivity() {

    private val binding by lazy {
        ActivityTelaCompraVendaBinding.inflate(layoutInflater)
    }
    private var moeda: MoedaModel? = null

    private val sbTexto = StringBuilder()

    var operacaoTexto = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        moeda = intent.getSerializableExtra(MOEDA) as? MoedaModel

        binding.btnVaiParaHome.setOnClickListener {
            vaiParaTelaHome()
        }
        val ehCompra = intent.getBooleanExtra(EHCOMPRA, false)

        var operacaoToolbar = ""

        configuraSubTituloToolbar(true, "Câmbio", binding.toolbarcompravenda.toolbarSubTitle)

        if (!ehCompra) {
            operacaoToolbar = "Vender"
            operacaoTexto = "vender"

        } else {
            operacaoToolbar = "Comprar"
            operacaoTexto = "comprar"
        }
        configuraTelaCompra()
        configuraToolbar(
            true,
            operacaoToolbar,
            binding.toolbarcompravenda.toolbarTitle,
            binding.toolbarcompravenda.btnVoltarTelaMoedas
        )

    }

    private fun configuraTelaCompra() {
        val quantidade = intent.getIntExtra(QUANTIDADE, 0)
        val calculoTotal = intent.getDoubleExtra("qtTotal",0.0)
        sbTexto.let { sb ->
            sb.append(
                "Parabéns!\n",
                "Você acabou de \n",
                operacaoTexto, "\n",
                quantidade, "\t",
                "\t",
                moeda?.isoMoeda,
                "\t",
                "-\n",
                moeda?.nome,
                ", totalizando\n",
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