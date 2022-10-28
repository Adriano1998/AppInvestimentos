package com.br.brqinvestimentos.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.adapter.ListaMoedasAdapter
import com.br.brqinvestimentos.databinding.ActivityTelaHomeBinding
import com.br.brqinvestimentos.viewModel.MoedaViewModel

class TelaHome : AppCompatActivity() {

    lateinit var viewModel: MoedaViewModel

    //    lateinit var rvCurrencies: RecyclerView
    private var toolbar: Toolbar? = null


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
        viewModel = ViewModelProvider(this)[MoedaViewModel::class.java]

        configuraToolbar()


        viewModel.listaDeMoedas.observe(this) {
            adapter.atualiza(it)
        }

        viewModel.atualizaMoedas()



        configActionBar()

        setIsHeading(findViewById(R.id.toolbar_title))


    }

    private fun configuraToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }


    private fun setIsHeading(textView: TextView) {

        ViewCompat.setAccessibilityDelegate(textView, object : AccessibilityDelegateCompat() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View,
                info: AccessibilityNodeInfoCompat
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    host.isAccessibilityHeading = true
                } else {
                    info.isHeading = true
                }
            }
        })
    }

    private fun configActionBar() {
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun configuraRecyclerView() {
        binding.rvMoedasTelaHome.adapter = adapter
        binding.rvMoedasTelaHome.layoutManager = LinearLayoutManager(this)
        adapter.quandoClicaNoItem = { moeda ->
            Intent(this, TelaCambio::class.java).apply {
                putExtra("moeda", moeda)
                startActivity(this)
            }
        }
    }

}