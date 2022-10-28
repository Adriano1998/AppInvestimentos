package com.br.brqinvestimentos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.utils.FuncoesUtils

class TelaCambio : AppCompatActivity() {

    private var moeda: MoedaModel? = null
    private lateinit var txtnomeMoeda: TextView
    private lateinit var txtVariacaoMoeda: TextView
    private lateinit var txtCompraMoeda: TextView
    private lateinit var txtVendaMoeda: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_cambio)
        moeda = intent.getSerializableExtra("moeda") as? MoedaModel

        txtnomeMoeda = findViewById<TextView?>(R.id.txt1).apply {
            text = moeda?.nome
        }

        txtVariacaoMoeda = findViewById<TextView?>(R.id.txt2).apply {
            moeda?.let { FuncoesUtils.trocaCorVariacaoMoeda(this, it)
            FuncoesUtils.acertaCasasDecimaisVariacao(it, this)
            }

        }

        txtCompraMoeda = findViewById<TextView?>(R.id.txt3).apply {
            text = moeda?.valorCompra.toString()
        }

        txtVendaMoeda = findViewById<TextView?>(R.id.txt4).apply {
            text = moeda?.valorVenda.toString()
        }


    }

}