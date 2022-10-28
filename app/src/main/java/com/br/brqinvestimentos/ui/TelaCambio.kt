package com.br.brqinvestimentos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.databinding.ActivityTelaCambioBinding
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.utils.FuncoesUtils

class TelaCambio : AppCompatActivity() {

    private val binding by lazy {
        ActivityTelaCambioBinding.inflate(layoutInflater)
    }

    private var moeda: MoedaModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        moeda = intent.getSerializableExtra("moeda") as? MoedaModel

        moeda?.let {
            vinculaCampos(it)
        }


    }

    private fun vinculaCampos(it: MoedaModel) {
        binding.txtNomeMoedaCambio.text = it.nome
        binding.txtVariacaoMoedaCambio.text = it.variacao.toString()
        FuncoesUtils.trocaCorVariacaoMoeda(binding.txtVariacaoMoedaCambio, it)
        FuncoesUtils.acertaCasasDecimaisVariacao(it, binding.txtVariacaoMoedaCambio)
        binding.txtCompraMoedaCambio.text = it.valorCompra.toString()
        binding.txtVendaMoedaCambio.text = it.valorVenda.toString()
    }

}