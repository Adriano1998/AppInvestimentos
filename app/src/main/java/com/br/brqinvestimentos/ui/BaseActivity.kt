package com.br.brqinvestimentos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.br.brqinvestimentos.R
import com.br.brqinvestimentos.databinding.ActivityBaseBinding
import com.br.brqinvestimentos.databinding.ToolbarGenericaBinding
import com.br.brqinvestimentos.repository.MoedaRepository
import com.br.brqinvestimentos.viewModel.MainViewModelFactory
import com.br.brqinvestimentos.viewModel.MoedaViewModel

open class BaseActivity : AppCompatActivity() {

    protected lateinit var viewModel: MoedaViewModel

    private val binding by lazy {
        ToolbarGenericaBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        viewModel = ViewModelProvider(this, MainViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )
    }

    protected open fun configuraToolbar(boolean: Boolean, titulo: String, txtTitulo: TextView, imgbtn: ImageButton) {
        setSupportActionBar(binding.toolbarGenerica)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        txtTitulo.text = titulo
        if (boolean) {
            imgbtn.visibility = View.VISIBLE
            imgbtn.setOnClickListener {
                finish()
            }
        }
    }

    protected open fun configuraSubTituloToolbar(boolean: Boolean, subTitulo: String, txtSubTitulo: TextView){
        if(boolean){
            txtSubTitulo.text = subTitulo
            txtSubTitulo.visibility = View.VISIBLE
        }
    }
}