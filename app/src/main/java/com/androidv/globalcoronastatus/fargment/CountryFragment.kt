package com.androidv.globalcoronastatus.fargment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.androidv.globalcoronastatus.R
import com.androidv.globalcoronastatus.adapter.CountryListAdapter
import com.androidv.globalcoronastatus.model.Counrtries
import com.androidv.globalcoronastatus.util.ConnectionManager
import org.json.JSONException

class CountryFragment(val contextParam: Context) : Fragment() {
    //lateinit var fab_btn: FloatingActionButton // Fab

    lateinit var recyclerCountry: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: CountryListAdapter

    lateinit var et_Search: EditText

    lateinit var ll_NF: LinearLayout

    val countryInfoList = arrayListOf<Counrtries>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_country, container, false)

        recyclerCountry = view.findViewById(R.id.recyclerCountries)
        layoutManager = LinearLayoutManager(contextParam)
        et_Search = view.findViewById(R.id.et_Search)

        ll_NF=view.findViewById(R.id.ll_NF)

        val queue = Volley.newRequestQueue(contextParam)
        val url = "https://disease.sh/v3/covid-19/countries"
        println("COUNTRY LIST ACTIVITY: Checking Connection")

        if (ConnectionManager().checkConnectivity(contextParam)) {
            println("COUNTRY LIST ACTIVITY: Connection found proceeding to get data")

            val jsonArrayRequest = object : JsonArrayRequest(Request.Method.GET,
                url, null, Response.Listener {
                    try {
                        println("COUNTRY LIST ACTIVITY: Inside Try Block")
                        // println("COUNTRY LIST ACTIVITY: The response is: $it")
                        val data = it
                        //println("COUNTRY LIST ACTIVITY: length of data is ${data.length()}" )

                        for (i in 0 until data.length()) {
                            val countryJsonObject = data.getJSONObject(i)
                            val flagobj = countryJsonObject.getJSONObject("countryInfo")
                            val flag = flagobj.getString("flag")
                            val countryObject = Counrtries(
                                countryJsonObject.getString("country"),
                                countryJsonObject.getString("cases"),
                                flag.toString()
                            )

                            countryInfoList.add(countryObject)
                            recyclerAdapter = CountryListAdapter(contextParam, countryInfoList)
                            recyclerCountry.adapter = recyclerAdapter
                            recyclerCountry.layoutManager = layoutManager
                        }

                    } catch (e: JSONException) {
                        println("COUNTRY LIST ACTIVITY: Error in catch is: $e")

                    }
                }, Response.ErrorListener {
                    println("COUNTRY LIST ACTIVITY: The Error in Volly is: $it")

                }) {}

            queue.add(jsonArrayRequest)
        }
        et_Search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterSearch(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        et_Search.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterSearch(et_Search.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
        return view
    }

    fun filterSearch(styTyped: String) {
        val filteredList = arrayListOf<Counrtries>()

        for (country in countryInfoList) {
            if (country.countryName.toLowerCase().contains(styTyped.toLowerCase())) {
                filteredList.add(country)
            }
        }
        if (filteredList.size == 0) {
            ll_NF.visibility = View.VISIBLE
        }else{
            ll_NF.visibility = View.GONE
        }
        recyclerAdapter.filterList(filteredList)
    }
}