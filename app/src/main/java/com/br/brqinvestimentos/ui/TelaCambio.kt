package com.br.brqinvestimentos.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.TouchDelegate
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModelProvider
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.databinding.ActivityTelaCambioBinding
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.utils.FuncoesUtils
import com.br.brqinvestimentos.utils.FuncoesUtils.increaseTouch
import com.br.brqinvestimentos.viewModel.MainViewModelFactory
import com.br.brqinvestimentos.viewModel.MoedaViewModel
import java.math.RoundingMode

class TelaCambio : AppCompatActivity() {

    private val binding by lazy {
        ActivityTelaCambioBinding.inflate(layoutInflater)
    }

    lateinit var viewModel: MoedaViewModel

    private var moeda: MoedaModel? = null

    private val sbSaldo = StringBuilder()

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        moeda?.let {
            viewModel.simulaValorParaSingleton(it)

        }

        configuraVariaveisOnResume()

    }

    @SuppressLint("SetTextI18n")
    private fun configuraVariaveisOnResume() {
        binding.txtEmCaixa.text = moeda?.isoValor.toString() + " " + moeda?.nome + " " + "em caixa"
        binding.txtSaldoDisponivelVariavel.text = FuncoesUtils.quantidadeSaldo.toBigDecimal()
            .setScale(2, RoundingMode.UP).toString()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniciaToolbarCambio()
        setContentView(binding.root)


        moeda = intent.getSerializableExtra("moeda") as? MoedaModel

        viewModel = ViewModelProvider(this, MainViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )

        moeda?.let {
            vinculaCamposMoeda(it)
            viewModel.simulaValorParaSingleton(it)
        }

        binding.toolbarcambio.btnVoltarTelaMoedas.setOnClickListener {
            finish()
        }

        desabilitaBotoesCompraeVenda()

        binding.txtinpQuantidade.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }


            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank() && moeda?.valorVenda != null) {
                    var textoDigitado = s.toString().toInt()
                    if (viewModel.validaQuantidadeComVenda(textoDigitado, moeda!!)) {
                        viewModel.habilitaBotao(binding.btnVender, R.drawable.retangulobotaoativado)
                        binding.btnVender.setOnClickListener {
                            binding.txtinpQuantidade.text?.clear()
                            viewModel.calculaVenda(textoDigitado, moeda!!, FuncoesUtils).let {
                                vaiParaTelaCompraVendaVendendo(textoDigitado)
                            }
                        }
                    } else {
                        viewModel.desabilitaBotao(binding.btnVender, R.drawable.retangulobotao)
                    }
                } else {
                    viewModel.desabilitaBotao(binding.btnVender, R.drawable.retangulobotao)
                }

                if (s.toString().isNotBlank() && moeda?.valorCompra != null) {
                    var caracteresDigitados = s.toString().toInt()
                    if (viewModel.validaQuantidadeComCompra(caracteresDigitados, moeda!!)) {
                        viewModel.habilitaBotao(
                            binding.btnComprar,
                            R.drawable.retangulobotaoativado
                        )
                        binding.btnComprar.setOnClickListener {
                            binding.txtinpQuantidade.text?.clear()
                            viewModel.calculaCompra(caracteresDigitados, moeda!!, FuncoesUtils)
                            vaiParaTelaCompraVendaComprando(caracteresDigitados)
                        }
                    } else {
                        viewModel.desabilitaBotao(binding.btnComprar, R.drawable.retangulobotao)
                    }
                } else {
                    viewModel.desabilitaBotao(binding.btnComprar, R.drawable.retangulobotao)
                }
            }
        })


        sbSaldo?.let {
            it.append(binding.txtSaldoDisponivel.text)
                .append(" ")

            binding.txtSaldoDisponivel.text = it
        }

        binding.toolbarcambio.toolbarTitle.let {
            it.contentDescription = "${it.text}, Titulo"
        }

        binding.txtSaldoDisponivel.let {
            it.contentDescription = "${it.text} ${FuncoesUtils.quantidadeSaldo}"
        }

        increaseTouch(binding.toolbarcambio.btnVoltarTelaMoedas, 150F)

    }

    private fun vaiParaTelaCompraVendaComprando(caracteresDigitados: Int) {
        val intent = Intent(applicationContext, TelaCompraVenda::class.java)
        viewModel.simulaValorParaSingleton(moeda!!)
        intent.putExtra("moeda", moeda)
        intent.putExtra("quantidade", caracteresDigitados)
        FuncoesUtils.ehCompra = true
        startActivity(intent)
    }

    private fun vaiParaTelaCompraVendaVendendo(textoDigitado: Int) {
        val intent = Intent(applicationContext, TelaCompraVenda::class.java)
        viewModel.simulaValorParaSingleton(moeda!!)
        intent.putExtra("moeda", moeda)
        intent.putExtra("quantidade", textoDigitado)
        FuncoesUtils.ehCompra = false
        startActivity(intent)
    }

    private fun desabilitaBotoesCompraeVenda() {
        viewModel.desabilitaBotao(binding.btnVender, R.drawable.retangulobotao)

        viewModel.desabilitaBotao(binding.btnComprar, R.drawable.retangulobotao)
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
            binding.txtCompraMoedaCambio.text = "Compra: R$ ${
                it.valorCompra.toString().toBigDecimal().setScale(2, RoundingMode.UP)
            } "
        }

        if (it.valorVenda == null) {

            binding.txtVendaMoedaCambio.text = "Venda: R$ 0.00"
        } else {
            binding.txtVendaMoedaCambio.text =
                "Venda: R$ ${
                    it.valorVenda.toString().toBigDecimal().setScale(2, RoundingMode.UP)
                } "
        }
    }

    private fun iniciaToolbarCambio() {
        setSupportActionBar(binding.toolbarcambio.toolbarCambio)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
        }


    }


}