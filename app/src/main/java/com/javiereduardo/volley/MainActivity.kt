package com.example.volley

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request

import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.javiereduardo.volley.Pais
import org.json.JSONException
import java.text.DecimalFormat
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    var adaptadorPaises:PaisesAdapter?=null
    var listaPaises:ArrayList<Pais>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        listaPaises = ArrayList<Pais>()


        adaptadorPaises = PaisesAdapter(listaPaises!!,this)
        val MiRecyclerView:RecyclerView
        MiRecyclerView = findViewById(R.id.miRecyclerCovid)
        MiRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        MiRecyclerView.adapter = adaptadorPaises

        val queue = Volley.newRequestQueue(this)
        val url = "https://covid-api.mmediagroup.fr/v1/cases"

        val peticionDatosCovid = JsonArrayRequest(Request.Method.GET,url,null,Response.Listener { response ->
            for (index in 0..response.length()-1){
                try {
                    val paisJson = response.getJSONObject(index)
                    val nombrePais = paisJson.getString("countryregion")
                    var numerosConfirmados = paisJson.getInt("confirmed")
                    val numeroMuertos = paisJson.getInt("deaths")
                    val numeroRecuperados = paisJson.getInt("recovered")
                    val countryCodeJson = paisJson.getJSONObject("countrycode")
                    val codigoPais =countryCodeJson.getString("iso2")
                    //numerosConfirmados = DecimalFormat(numerosConfirmados)

                    //Objeto de kotlin
                    val paisIndividual = Pais(nombrePais,numerosConfirmados,numeroMuertos,numeroRecuperados,codigoPais)
                    listaPaises!!.add(paisIndividual)
                }catch (e:JSONException){
                    Log.wtf("JsonError",e.localizedMessage)

                }
            }
            listaPaises!!.sortByDescending { it.confirmados }
            adaptadorPaises!!.notifyDataSetChanged()
        },
            Response.ErrorListener { error ->
                Log.e("error_volley",error.localizedMessage)
            })

        queue.add(peticionDatosCovid)

    }
}