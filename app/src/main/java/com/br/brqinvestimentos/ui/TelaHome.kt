package com.br.brqinvestimentos.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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


    private var moeda: MoedaModel? = null
    private var contador: Int = 0


    lateinit var viewModel: MoedaViewModel

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
        moeda = intent.getSerializableExtra("moeda") as? MoedaModel


        viewModel = ViewModelProvider(this, MainViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )

        viewModel.listaDeMoedas.observe(this) {
            adapter.atualiza(it)
        }
        viewModel.atualizaMoedas()

        viewModel.toastMessageObserver.observe(this) { message ->
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

        }
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