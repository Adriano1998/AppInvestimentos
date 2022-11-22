package com.br.brqinvestimentos.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.br.brqinvestimentos.databinding.ActivityTelaCompraVendaBinding
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.utils.Constantes.Companion.EHCOMPRA
import com.br.brqinvestimentos.utils.Constantes.Companion.MOEDA
import com.br.brqinvestimentos.utils.Constantes.Companion.QUANTIDADE
import com.br.brqinvestimentos.utils.FuncoesUtils.formatadorMoedaBrasileira
import com.br.brqinvestimentos.viewModel.MainViewModelFactory
import com.br.brqinvestimentos.viewModel.MoedaViewModel

class TelaCompraVenda : BaseActivity() {

    private val binding by lazy {
        ActivityTelaCompraVendaBinding.inflate(layoutInflater)
    }
    private var moeda: MoedaModel? = null

    private val sbTexto = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        moeda = intent.getSerializableExtra(MOEDA) as? MoedaModel

        binding.toolbarcompravenda.btnVoltarTelaCambio.setOnClickListener {
            finish()
        }

        binding.btnVaiParaHome.setOnClickListener {
            vaiParaTelaHome()
        }

        val quantidade = intent.getIntExtra(QUANTIDADE, 0)
        val ehCompra = intent.getBooleanExtra(EHCOMPRA, false)

        if (!ehCompra) {
            configuraTelaVenda(quantidade)
        } else {
            configuraTelaCompra(quantidade)
        }


    }

    private fun configuraTelaCompra(quantidade: Int) {
        iniciaToolbarCompra()
        moeda?.valorCompra?.let { compra ->
            val contaCompra = quantidade * compra
            sbTexto.let { sb ->
                sb.append(
                    "Parabéns!\n",
                    "Você acabou de \n",
                    "comprar ",
                    quantidade, "\t",
                    "\t",
                    moeda?.isoMoeda,
                    "\t",
                    "-\n",
                    moeda?.nome,
                    ", totalizando\n",
                    formatadorMoedaBrasileira(contaCompra)
                )
                binding.textoCompraVenda.text = sb
            }
        }

    }

    private fun configuraTelaVenda(quantidade: Int) {
        iniciaToolbarVenda()
        moeda?.valorVenda?.let { venda ->
            val contaVenda = quantidade * venda
            sbTexto.let { sb ->
                sb.append(
                    "Parabéns!\n",
                    "Você acabou de vender\n",
                    quantidade,"\t",
                    "\t",
                    moeda?.isoMoeda, "\t",
                    "-", "\t",
                    moeda?.nome,
                    ", \n",
                    "totalizando\n",
                    formatadorMoedaBrasileira(contaVenda)
                )
                binding.textoCompraVenda.text = sb
            }
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

    @SuppressLint("SetTextI18n")
    private fun iniciaToolbarVenda() {
        iniciaToolbar(binding.toolbarcompravenda.toolbarCompraVenda)
        supportActionBar?.let {
            binding.toolbarcompravenda.toolbarcompraevendaTitle.text = "Vender"
            binding.toolbarcompravenda.toolbarcompraevendaTitle.let { text ->
                text.contentDescription = "Vender, Titulo"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun iniciaToolbarCompra() {
        iniciaToolbar(binding.toolbarcompravenda.toolbarCompraVenda)
        supportActionBar?.let {
            binding.toolbarcompravenda.toolbarcompraevendaTitle.text = "Comprar"
            binding.toolbarcompravenda.toolbarcompraevendaTitle.let { text ->
                text.contentDescription = "Comprar, Titulo"
            }
        }
    }
}