package com.br.brqinvestimentos.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.databinding.ActivityTelaCambioBinding
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.utils.FuncoesUtils
import com.br.brqinvestimentos.utils.moedas.MoedasUtils

class TelaCambio : AppCompatActivity() {

    private val binding by lazy {
        ActivityTelaCambioBinding.inflate(layoutInflater)
    }


    private var moeda: MoedaModel? = null

    private val sbSaldo = StringBuilder()
    private val sbCaixa = StringBuilder()


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        moeda = intent.getSerializableExtra("moeda") as? MoedaModel

        moeda?.let {
            vinculaCamposMoeda(it)
        }

        binding.btnComprar.isEnabled = true

        desabilitaOuHabilitaBotao()

        binding.btnVoltarTelaHome.setOnClickListener {
            finish()
        }

        sbSaldo?.let {
            it.append(binding.txtSaldoDisponivel.text)
                .append(FuncoesUtils.quantidadeSaldo)
                .toString()
            binding.txtSaldoDisponivel.text = it
        }

        sbCaixa?.let {
            it.append(FuncoesUtils.quantidadeEmCaixa)
                .append(" ")
                .append(moeda?.nome)
                .append(" ")
            it.append(binding.txtEmCaixa.text)
                .toString()
            binding.txtEmCaixa.text = it
        }

        binding.txtinpQuantidade.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                binding.txtinpQuantidade.text = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if(moeda?.valorCompra)
            }

            override fun afterTextChanged(s: Editable?) {


            }
        }
        )
        MoedasUtils.hashMoedas
    }


    private fun desabilitaOuHabilitaBotao() {
        if (!binding.btnComprar.isEnabled) {
            binding.btnComprar.setBackgroundResource(R.drawable.retangulobotao)
        } else {
            binding.btnComprar.setBackgroundResource(R.drawable.retangulobotaoativado)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun vinculaCamposMoeda(it: MoedaModel) {
        binding.txtNomeMoedaCambio.text = "${it.isoMoeda} - ${it.nome}"
        binding.txtVariacaoMoedaCambio.text = it.variacao.toString()
        FuncoesUtils.trocaCorVariacaoMoeda(binding.txtVariacaoMoedaCambio, it)
        FuncoesUtils.acertaCasasDecimaisVariacao(it, binding.txtVariacaoMoedaCambio)

        validaCamposCompraeVenda(it)


    }

    @SuppressLint("SetTextI18n")
    private fun validaCamposCompraeVenda(it: MoedaModel) {
        if (it.valorCompra == null) {
            binding.txtCompraMoedaCambio.text = "Compra: R$ 0.0"
        } else {
            binding.txtCompraMoedaCambio.text = "Compra: R$ ${it.valorCompra} "
        }

        if (it.valorVenda == null) {

            binding.txtVendaMoedaCambio.text = "Venda: R$ 0.0"
        } else {
            binding.txtVendaMoedaCambio.text = "Venda: R$ ${it.valorVenda} "
        }
    }


}