package com.br.brqinvestimentos

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.br.brqinvestimentos.model.Moeda
import com.br.brqinvestimentos.service.FinancasServices
import com.br.brqinvestimentos.service.RetrofitHelper
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class TelaHome : AppCompatActivity() {

    private lateinit var linear: LinearLayout
    private var toolbar: Toolbar? = null
    private val toolbarTitle: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_home)
        getMoedas()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        configActionBar()

        setIsHeading(findViewById(R.id.toolbar_title))


    }

    fun getMoedas(){
        val retrofit = RetrofitHelper.getRetrofitInstance("https://api.hgbrasil.com")
        val endpoint = retrofit.create(FinancasServices::class.java)
        endpoint.buscaMoedas().enqueue(object: Callback<List<Moeda?>>{
            override fun onResponse(call: Call<List<Moeda?>>, response: Response<List<Moeda?>>) {
                var data = mutableListOf<List<Moeda?>>()
                response.body()?.let{
                    data.add(it)
                }

                println(data.count())
            }

            override fun onFailure(call: Call<List<Moeda>>, t: Throwable) {
                println("Não foi")
            }


        })
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

}