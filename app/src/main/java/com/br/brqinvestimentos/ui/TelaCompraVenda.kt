package com.br.brqinvestimentos.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.br.brqinvestimentos.databinding.ActivityTelaCompraVendaBinding
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.utils.FuncoesUtils
import com.br.brqinvestimentos.viewModel.MainViewModelFactory
import com.br.brqinvestimentos.viewModel.MoedaViewModel
import java.math.RoundingMode

class TelaCompraVenda : AppCompatActivity() {

    private val binding by lazy {
        ActivityTelaCompraVendaBinding.inflate(layoutInflater)
    }
    private var moeda: MoedaModel? = null
    lateinit var viewModel: MoedaViewModel
    private val sbTexto = StringBuilder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        moeda = intent.getSerializableExtra("moeda") as? MoedaModel

        viewModel = ViewModelProvider(this, MainViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )
        binding.toolbarcompravenda.btnVoltarTelaCambio.setOnClickListener {
            finish()
        }


        binding.btnVaiParaHome.setOnClickListener {
            val intent = Intent(applicationContext, TelaHome::class.java)
            viewModel.simulaValorParaSingleton(moeda!!)
            intent.putExtra("moeda", moeda)
            startActivity(intent)
        }
        val quantidade = intent.getIntExtra("quantidade", 0)

        if (!FuncoesUtils.ehCompra) {
            iniciaToolbarVenda()
            val contaVenda = quantidade * moeda?.valorVenda!!
            sbTexto.let {
                it.append("Parabéns!\n")
                    .append("Você acabou de vender\n")
                    .append(quantidade, " ")
                    .append(moeda?.isoMoeda, " ")
                    .append("-", " ")
                    .append(moeda?.nome)
                    .append(", \n")
                    .append("totalizando\n")
                    .append("R$ ")
                    .append(contaVenda.toBigDecimal().setScale(2, RoundingMode.UP))

                binding.textoCompraVenda.text = it
            }
        } else {
            iniciaToolbarCompra()
            val contaCompra = quantidade * moeda?.valorCompra!!
            sbTexto.let {
                it.append("Parabéns!\n")
                    .append("Você acabou de \n")
                    .append("comprar ")
                    .append(quantidade)
                    .append(" ", moeda?.isoMoeda, " ")
                    .append("-\n")
                    .append(moeda?.nome)
                    .append(", totalizando\n")
                    .append("R$ ")
                    .append(contaCompra.toBigDecimal().setScale(2, RoundingMode.UP))
                binding.textoCompraVenda.text = it

            }
        }

    }

    private fun iniciaToolbarVenda() {
        setSupportActionBar(binding.toolbarcompravenda.toolbarCompraVenda)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            binding.toolbarcompravenda.toolbarcompraevendaTitle.text = "Vender"
        }

    }

    private fun iniciaToolbarCompra() {
        setSupportActionBar(binding.toolbarcompravenda.toolbarCompraVenda)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            binding.toolbarcompravenda.toolbarcompraevendaTitle.text = "Comprar"
        }

    }
}