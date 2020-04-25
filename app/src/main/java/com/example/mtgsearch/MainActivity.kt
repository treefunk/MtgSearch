package com.example.mtgsearch

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit:Retrofit = Retrofit
            .Builder()
            .baseUrl("https://api.scryfall.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val scryService = retrofit.create(ScryService::class.java)

        setupSearch(scryService,this)
    }

    private fun setupSearch(scryService: ScryService, context: Context) {

        val etSearch = findViewById<MaterialAutoCompleteTextView>(R.id.et_search)
        val ivCard   = findViewById<ImageView>(R.id.iv_card)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                val call = scryService.autoCompCards(s.toString())

                if(!s.toString().isEmpty()){
                    call.enqueue(object : Callback<Response>{
                        override fun onFailure(call: Call<Response>, t: Throwable) {
                        }

                        override fun onResponse(
                            call: Call<Response>,
                            response: retrofit2.Response<Response>
                        ) {
                            var choices = response.body()?.data!!.toMutableList()
                            val searchAdapter = ArrayAdapter<String>(context,
                                android.R.layout.simple_list_item_1,
                                choices)
                            etSearch.setAdapter(searchAdapter)
                            searchAdapter.notifyDataSetChanged()
                        }
                    })
                }

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.v(TAG, "before text changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        etSearch.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.v(TAG,"search ${etSearch.text}")
                val call = scryService.getCardByName(etSearch.text.toString())

                if(!etSearch.text.toString().isEmpty()){
                    call.enqueue(object: Callback<CardSearchResponse>{
                        override fun onFailure(call: Call<CardSearchResponse>, t: Throwable) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onResponse(
                            call: Call<CardSearchResponse>,
                            response: retrofit2.Response<CardSearchResponse>
                        ) {
                            Log.v(TAG,"success")
                            Glide.with(context)
                                .load(Uri.parse(response.body()?.data?.get(0)?.imageUris?.large))
                                .into(ivCard)
                        }
                    })
                }
            }
        })

    }
}
