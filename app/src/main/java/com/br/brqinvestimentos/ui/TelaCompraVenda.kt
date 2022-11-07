package com.br.brqinvestimentos.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.br.brqinvestimentos.databinding.ActivityTelaCompraVendaBinding
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.viewModel.MainViewModelFactory
import com.br.brqinvestimentos.viewModel.MoedaViewModel

class TelaCompraVenda : AppCompatActivity() {

    private val binding by lazy {
        ActivityTelaCompraVendaBinding.inflate(layoutInflater)
    }
    private var moeda: MoedaModel? = null
    lateinit var viewModel: MoedaViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        moeda = intent.getSerializableExtra("moeda") as? MoedaModel

        viewModel = ViewModelProvider(this, MainViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )

        moeda?.let {
            binding.textView.text = it.nome.toString()
            binding.textView2.text = it.isoValor.toString()
            binding.textView3.text = it.isoMoeda
//            viewModel.simulaValorParaSingleton(it)
        }
        binding.button.setOnClickListener {
            val intent = Intent(applicationContext, TelaHome::class.java)
            viewModel.simulaValorParaSingleton(moeda!!)
            intent.putExtra("moeda", moeda)
            startActivity(intent)
        }
    }
}