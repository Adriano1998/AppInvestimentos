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
import com.br.brqinvestimentos.utils.FuncoesUtils.formataPorcentagem
import com.br.brqinvestimentos.utils.FuncoesUtils.formatadorMoedaBrasileira
import com.br.brqinvestimentos.utils.FuncoesUtils.increaseTouch
import com.br.brqinvestimentos.utils.FuncoesUtils.quantidadeSaldo
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

    private val sbCompra = StringBuilder()

    private val sbVenda = StringBuilder()


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
        binding.txtSaldoDisponivelVariavel.text = formatadorMoedaBrasileira(quantidadeSaldo)
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

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}


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
                    val caracteresDigitados = s.toString().toInt()
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


        sbSaldo.let {
            it.append(binding.txtSaldoDisponivel.text)
                .append(" ")

            binding.txtSaldoDisponivel.text = it
        }

        binding.toolbarcambio.toolbarTitle.let {
            it.contentDescription = "${it.text}, Titulo"
        }

        binding.txtSaldoDisponivel.let {
            it.contentDescription = "${it.text} $quantidadeSaldo"
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
        binding.txtVariacaoMoedaCambio.text =
            it.variacao?.let { variacao -> formataPorcentagem(variacao) }
        FuncoesUtils.trocaCorVariacaoMoeda(binding.txtVariacaoMoedaCambio, it)
        validaCamposCompraeVenda(it)
    }

    @SuppressLint("SetTextI18n")
    private fun validaCamposCompraeVenda(it: MoedaModel) {
        if (it.valorCompra == null) {
            binding.txtCompraMoedaCambio.text = "Compra: ${formatadorMoedaBrasileira(0.00)}"
        } else {
            sbCompra.let {
                //codigo duplicado.
                it.append(binding.txtCompraMoedaCambio.text)
                    .append(" ")
                    .append(moeda?.valorCompra?.let { compra -> formatadorMoedaBrasileira(compra) })
                binding.txtCompraMoedaCambio.text = it
            }
        }

        if (it.valorVenda == null) {
            binding.txtVendaMoedaCambio.text = " ${formatadorMoedaBrasileira(0.00)}"
        } else {
            sbVenda.let {
                //codigo duplicado.
                it.append(binding.txtVendaMoedaCambio.text)
                    .append(" ")
                    .append(moeda?.valorVenda?.let { venda -> formatadorMoedaBrasileira(venda) })
                binding.txtVendaMoedaCambio.text = it
            }
        }
    }

    private fun iniciaToolbarCambio() {
        setSupportActionBar(binding.toolbarcambio.toolbarCambio)
        supportActionBar?.setDisplayShowTitleEnabled(false)


    }


}