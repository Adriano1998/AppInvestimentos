package com.br.brqinvestimentos.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.brqinvestimentos.adapter.ListaMoedasAdapter
import com.br.brqinvestimentos.databinding.ActivityTelaHomeBinding
import com.br.brqinvestimentos.model.MoedaModel
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.viewModel.MainViewModelFactory
import com.br.brqinvestimentos.viewModel.MoedaViewModel

class TelaHome : AppCompatActivity() {

    private val repository by lazy {
        MoedaRepository()
    }

//    private val viewModel: MoedaViewModel by viewModels()


    lateinit var viewModel: MoedaViewModel
//    private var viewModel by lazy{
//        MoedaViewModel(repository)
//        ViewModelProvider(this)[MoedaViewModel::class.java]
//    }

//    lateinit var viewModel: MoedaViewModel(repository)

    private val binding by lazy {
        ActivityTelaHomeBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        ListaMoedasAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        viewModel = ViewModelProvider(this, MainViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )
//        viewModel = ViewModelProvider(this)[MoedaViewModel::class.java]
        viewModel.listaDeMoedas.observe(this) {
            adapter.atualiza(it)
        }
        viewModel.atualizaMoedas()



    }

    private fun configuraRecyclerView() {
        binding.rvMoedasTelaHome.adapter = adapter
        binding.rvMoedasTelaHome.layoutManager = LinearLayoutManager(this)
        adapter.quandoClicaNoItem = { moeda ->
            vaiParaTelaCambio(moeda)
        }
    }

    private fun vaiParaTelaCambio(moeda: MoedaModel) {
        Intent(this, TelaCambio::class.java).apply {
            putExtra("moeda", moeda)
            startActivity(this)
        }
    }

}