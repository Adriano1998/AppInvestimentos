package com.br.brqinvestimentos.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.databinding.ActivityTelaCambioBinding
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.utils.Constantes.Companion.EHCOMPRA
import com.br.brqinvestimentos.utils.Constantes.Companion.MOEDA
import com.br.brqinvestimentos.utils.Constantes.Companion.QUANTIDADE
import com.br.brqinvestimentos.utils.FuncoesUtils
import com.br.brqinvestimentos.utils.FuncoesUtils.formataPorcentagem
import com.br.brqinvestimentos.utils.FuncoesUtils.formatadorMoedaBrasileira
import com.br.brqinvestimentos.utils.FuncoesUtils.quantidadeSaldo
import com.br.brqinvestimentos.utils.FuncoesUtils.trocaCorVariacaoMoeda

class TelaCambio : BaseActivity() {

    private val binding by lazy {
        ActivityTelaCambioBinding.inflate(layoutInflater)
    }

    private var moeda: MoedaModel? = null

    private val sbSaldo = StringBuilder()

    private val sbCompra = StringBuilder()

    private val sbVenda = StringBuilder()

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        configuraVariaveisOnResume()
        configuraSubTituloToolbar(true, "Moedas", binding.toolbarCambio.toolbarSubTitle)
    }

    @SuppressLint("SetTextI18n")
    private fun configuraVariaveisOnResume() {
        moeda?.let {
            binding.txtEmCaixa.text = viewModel.pegaValorHashmap(it.isoMoeda)
                .toString() + "\u0020 " + moeda?.nome + " " + "em caixa"
        }
        binding.txtSaldoDisponivelVariavel.text = formatadorMoedaBrasileira(quantidadeSaldo)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        moeda = intent.getSerializableExtra(MOEDA) as? MoedaModel

        configuraToolbar(
            true,
            "Câmbio",
            binding.toolbarCambio.toolbarTitle,
            binding.toolbarCambio.btnVoltarTelaMoedas
        )
        moeda?.let {
            vinculaCamposMoeda(it)
            viewModel.pegaValorHashmap(it.isoMoeda)
        }

        desabilitaBotoesCompraeVenda()

        configuraCampoQuantidade()

        sbSaldo.let {
            it.append(binding.txtSaldoDisponivel.text, " ")
            binding.txtSaldoDisponivel.text = it
        }

        binding.txtSaldoDisponivel.let {
            it.contentDescription = "${it.text} $quantidadeSaldo"
        }
    }

    private fun configuraCampoQuantidade() {
        binding.txtinpQuantidade.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank() && moeda?.valorVenda != null) {
                    val textoDigitado = s.toString().toInt()
                    moeda?.let { moedaModel ->
                        if (viewModel.validaQuantidadeComVenda(textoDigitado, moedaModel)) {
                            habilitaBotao(binding.btnVender, R.drawable.retangulobotaoativado)
                            binding.btnVender.setOnClickListener {
                                binding.txtinpQuantidade.text?.clear()
                                val total =
                                    viewModel.calculaVenda(textoDigitado, moedaModel)
                                vaiParaTelaCompraVendaVendendo(textoDigitado, total)

                            }
                        } else {
                            desabilitaBotao(binding.btnVender, R.drawable.retangulobotao)
                        }
                    }

                } else {
                    desabilitaBotao(binding.btnVender, R.drawable.retangulobotao)
                }

                if (s.toString().isNotBlank() && moeda?.valorCompra != null) {
                    val caracteresDigitados = s.toString().toInt()
                    moeda?.let { moeda ->
                        if (viewModel.validaQuantidadeComCompra(caracteresDigitados, moeda)) {
                            habilitaBotao(
                                binding.btnComprar,
                                R.drawable.retangulobotaoativado
                            )
                            binding.btnComprar.setOnClickListener {
                                binding.txtinpQuantidade.text?.clear()
                                val total = viewModel.calculaCompra(caracteresDigitados, moeda)
                                vaiParaTelaCompraVendaComprando(caracteresDigitados, total)
                            }
                        } else {
                            desabilitaBotao(binding.btnComprar, R.drawable.retangulobotao)
                        }
                    }
                } else {
                    desabilitaBotao(binding.btnComprar, R.drawable.retangulobotao)
                }
            }
        })
    }

    private fun vaiParaTelaCompraVendaComprando(caracteresDigitados: Int, total: Double) {
        val intent = Intent(this@TelaCambio, TelaCompraVenda::class.java)
        intent.putExtra(MOEDA, moeda)
        intent.putExtra(QUANTIDADE, caracteresDigitados)
        intent.putExtra(EHCOMPRA, true)
        intent.putExtra("qtTotal", total)
        startActivity(intent)
    }

    fun habilitaBotao(botao: Button, caminhoDrawable: Int) {
        botao.isEnabled = true
        botao.setBackgroundResource(caminhoDrawable)
    }

    fun desabilitaBotao(botao: Button, caminhoDrawable: Int) {
        botao.isEnabled = false
        botao.setBackgroundResource(caminhoDrawable)
    }

    private fun vaiParaTelaCompraVendaVendendo(textoDigitado: Int, total: Double) {
        val intent = Intent(this@TelaCambio, TelaCompraVenda::class.java)
        intent.putExtra(MOEDA, moeda)
        intent.putExtra(EHCOMPRA, false)
        intent.putExtra(QUANTIDADE, textoDigitado)
        intent.putExtra("qtTotal", total)
        startActivity(intent)
    }

    private fun desabilitaBotoesCompraeVenda() {
        desabilitaBotao(binding.btnVender, R.drawable.retangulobotao)
        desabilitaBotao(binding.btnComprar, R.drawable.retangulobotao)
    }

    @SuppressLint("SetTextI18n")
    private fun vinculaCamposMoeda(it: MoedaModel) {
        binding.txtNomeMoedaCambio.text = "${it.isoMoeda} - ${it.nome}"
        binding.txtVariacaoMoedaCambio.text =
            it.variacao?.let { variacao -> formataPorcentagem(variacao) }
        trocaCorVariacaoMoeda(binding.txtVariacaoMoedaCambio, it)
        validaCamposCompraeVenda(it)
    }

    private fun validaCamposCompraeVenda(it: MoedaModel) {
        if (it.valorCompra == null) {
            binding.txtCompraMoedaCambio.text =
                sbCompra.append(binding.txtCompraMoedaCambio.text, formatadorMoedaBrasileira(0.00))
        } else {
            sbCompra.let {
                it.append(
                    binding.txtCompraMoedaCambio.text,
                    "\t",
                    moeda?.valorCompra?.let { compra -> formatadorMoedaBrasileira(compra) })
                binding.txtCompraMoedaCambio.text = it
            }
        }

        if (it.valorVenda == null) {
            binding.txtVendaMoedaCambio.text =
                sbVenda.append(
                    binding.txtVendaMoedaCambio.text,
                    "\t${formatadorMoedaBrasileira(0.00)}"
                )
        } else {
            sbVenda.let {
                it.append(
                    binding.txtVendaMoedaCambio.text,
                    "\t",
                    moeda?.valorVenda?.let { venda -> formatadorMoedaBrasileira(venda) })
                binding.txtVendaMoedaCambio.text = it
            }
        }
    }
}