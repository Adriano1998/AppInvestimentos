package com.br.brqinvestimentos.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.databinding.ActivityTelaCambioBinding
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.utils.FuncoesUtils
import com.br.brqinvestimentos.viewModel.MainViewModelFactory
import com.br.brqinvestimentos.viewModel.MoedaViewModel

class TelaCambio : AppCompatActivity() {

    private val binding by lazy {
        ActivityTelaCambioBinding.inflate(layoutInflater)
    }

    lateinit var viewModel: MoedaViewModel


    private var moeda: MoedaModel? = null

    private val sbSaldo = StringBuilder()
    private val sbCaixa = StringBuilder()


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        moeda = intent.getSerializableExtra("moeda") as? MoedaModel

        viewModel = ViewModelProvider(this, MainViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )

        moeda?.let {
            vinculaCamposMoeda(it)
        }

        binding.btnVoltarTelaHome.setOnClickListener {
            finish()
        }

        sbSaldo?.let {
            it.append(binding.txtSaldoDisponivel.text)
                .append(" ")
                .append(FuncoesUtils.quantidadeSaldo)
                .toString()
            binding.txtSaldoDisponivel.text = it
        }

        sbCaixa?.let {
            it.append(moeda?.isoValor)
                .append(" ")
                .append(moeda?.nome)
                .append(" ")
            it.append(binding.txtEmCaixa.text)
                .toString()
            binding.txtEmCaixa.text = it
        }

        viewModel.desabilitaBotao(binding.btnVender, R.drawable.retangulobotao)

        viewModel.desabilitaBotao(binding.btnComprar, R.drawable.retangulobotao)

        binding.txtinpQuantidade.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank() && moeda?.valorVenda != null) {
                    var textoDigitado = s.toString().toInt()
                    if (viewModel.validaQuantidadeComVenda(textoDigitado, moeda!!)) {
                        viewModel.habilitaBotao(binding.btnVender, R.drawable.retangulobotaoativado)
                        binding.btnVender.setOnClickListener{
                            viewModel.calculaVenda(textoDigitado, moeda!!, FuncoesUtils).let {
                                val intent = Intent(applicationContext, TelaHome::class.java)
                                intent.putExtra("isoValor", it)
                                startActivity(intent)
                            }

                        }
                    } else {
                        viewModel.desabilitaBotao(binding.btnVender, R.drawable.retangulobotao)
                    }
                } else {
                    viewModel.desabilitaBotao(binding.btnVender, R.drawable.retangulobotao)
                }

                if (s.toString().isNotBlank() && moeda?.valorCompra!=null) {
                    var caracteresDigitados = s.toString().toInt()
                    if(viewModel.validaQuantidadeComCompra(caracteresDigitados, moeda!!  )){
                        viewModel.habilitaBotao(binding.btnComprar, R.drawable.retangulobotaoativado)
                        binding.btnComprar.setOnClickListener{
                            viewModel.calculaCompra(caracteresDigitados, moeda!!, FuncoesUtils)
                            finish()
                        }
                    }
                    else{
                        viewModel.desabilitaBotao(binding.btnComprar, R.drawable.retangulobotao)
                    }
                }
                else{
                    viewModel.desabilitaBotao(binding.btnComprar, R.drawable.retangulobotao)
                }


            }
        }
        )
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
            binding.txtCompraMoedaCambio.text = "Compra: R$ 0.00"
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