package com.br.brqinvestimentos.ui

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
import com.br.brqinvestimentos.utils.Constantes.Companion.QUANTIDADE_TOTAL
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


    override fun onResume() {
        super.onResume()
        configuraVariaveisOnResume()
        configuraSubTituloToolbar(
            true,
            getString(R.string.Moedas),
            binding.toolbarCambio.toolbarSubTitle
        )
    }

    private fun configuraVariaveisOnResume() {
        moeda?.let {

            buildString {
                this.append(
                    viewModel.pegaValorHashmap(it.isoMoeda).toString(),
                    getString(R.string.espaco),
                    moeda?.nome,
                    getString(R.string.espaco),
                    getString(R.string.em_caixa)
                )
                binding.txtEmCaixa.text = this
            }
        }
        binding.txtSaldoDisponivelVariavel.text = formatadorMoedaBrasileira(quantidadeSaldo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        moeda = intent.getSerializableExtra(MOEDA) as? MoedaModel

        configuraToolbar(
            true,
            getString(R.string.cambio),
            binding.toolbarCambio.toolbarTitle,
            binding.toolbarCambio.btnVoltarTelaMoedas
        )

        moeda?.let {
            vinculaCamposMoeda(it)
            viewModel.pegaValorHashmap(it.isoMoeda)
        }
        desabilitaBotoesCompraeVenda()

        configuraCampoQuantidade()

        configuraMostrarSaldoDisponivel()

        configuraAcessibilidade()
    }

    private fun configuraMostrarSaldoDisponivel() {
        sbSaldo.let {
            it.append(binding.txtSaldoDisponivel.text, getString(R.string.espaco))
            binding.txtSaldoDisponivel.text = it
        }
    }

    private fun configuraAcessibilidade() {
        binding.txtSaldoDisponivel.contentDescription = quantidadeSaldo.toString()
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
        intent.putExtra(QUANTIDADE_TOTAL, total)
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
        intent.putExtra(QUANTIDADE_TOTAL, total)
        startActivity(intent)
    }

    private fun desabilitaBotoesCompraeVenda() {
        desabilitaBotao(binding.btnVender, R.drawable.retangulobotao)
        desabilitaBotao(binding.btnComprar, R.drawable.retangulobotao)
    }

    private fun vinculaCamposMoeda(it: MoedaModel) {
        buildString {
            this.append(it.isoMoeda, getString(R.string.espacoComTraco), it.nome)
            binding.txtNomeMoedaCambio.text = this
        }

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
                    getString(R.string.espaco),
                    moeda?.valorCompra?.let { compra -> formatadorMoedaBrasileira(compra) })
                binding.txtCompraMoedaCambio.text = it
            }
        }

        if (it.valorVenda == null) {
            binding.txtVendaMoedaCambio.text =
                sbVenda.append(
                    binding.txtVendaMoedaCambio.text,
                    getString(R.string.espaco),
                    formatadorMoedaBrasileira(0.00)
                )
        } else {
            sbVenda.let {
                it.append(
                    binding.txtVendaMoedaCambio.text,
                    getString(R.string.espaco),
                    moeda?.valorVenda?.let { venda -> formatadorMoedaBrasileira(venda) })
                binding.txtVendaMoedaCambio.text = it
            }
        }
    }
}