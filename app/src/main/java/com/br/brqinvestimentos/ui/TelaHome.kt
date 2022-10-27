package com.br.brqinvestimentos.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.adapter.MoedaAdapter
import com.br.brqinvestimentos.viewModel.MoedaViewModel

class TelaHome : AppCompatActivity() {

    lateinit var viewModel: MoedaViewModel
    lateinit var rvCurrencies: RecyclerView
    private lateinit var linear: LinearLayout
    private var toolbar: Toolbar? = null
    private val toolbarTitle: TextView? = null

    val moedaAdapter = MoedaAdapter {
        Toast.makeText(this, "${it.toString()}", Toast.LENGTH_LONG).show()
    }

//    private val repositorioAdapter = RepositorioAdapter { repo ->
//        Intent(this, IssuesActivity::class.java).apply {
//            putExtra("repo_p", repo)
//            putExtra("owner_p", "")
//            startActivity(this)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_home)
        viewModel = ViewModelProvider(this)[MoedaViewModel::class.java]
        configuraRecyclerView()
        configuraToolbar()

        viewModel.listaDeMoedas.observe(this) {
            moedaAdapter.atualiza(it)
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
        //alterar eventos de acessibilidade
        //uso para texto como titulo:
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

    private fun configuraRecyclerView(){
        rvCurrencies = findViewById(R.id.rvMoedasTelaHome)
        rvCurrencies.layoutManager = LinearLayoutManager(this)
        rvCurrencies.adapter = moedaAdapter
    }

}