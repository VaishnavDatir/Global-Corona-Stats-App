package com.androidv.globalcoronastatus.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.androidv.globalcoronastatus.R
import com.androidv.globalcoronastatus.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import org.json.JSONException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CountryData : AppCompatActivity() {

    //Title Bar
    lateinit var ttl_CountryName: TextView
    lateinit var img_cFlag: ImageView
    lateinit var cd_btn_back:Button
    lateinit var img_fav: ImageView

    //CARD 1
    lateinit var cd_piechart: PieChart
    lateinit var cd_ll_info:LinearLayout

    //CARD 2
    //lateinit var cd_txt_updatedAt: TextView
    lateinit var cd_txt_Cases: TextView
    lateinit var cd_txt_TodayCases: TextView
    lateinit var cd_txt_Death: TextView
    lateinit var cd_txt_TodayDeath: TextView
    lateinit var cd_txt_Recovered: TextView
    lateinit var cd_txt_TodayRecovered: TextView
    lateinit var cd_txt_Active: TextView
    lateinit var cd_txt_Critical: TextView
    //lateinit var cd_txt_CountriesAffected: TextView
    lateinit var txt_CS:TextView
    lateinit var cd_progress_circular:ProgressBar
    lateinit var cd_ll_country:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_data)


        //Title Bar
        ttl_CountryName = findViewById(R.id.ttl_CountryName)
        img_cFlag = findViewById(R.id.img_cFlag)
        cd_btn_back =findViewById(R.id.cd_btn_back)
        cd_btn_back.setOnClickListener {
            onBackPressed()
        }
                val countryName = intent.getStringExtra("country_Name")
        val countryFlag = intent.getStringExtra("country_Flag")
        ttl_CountryName.text = countryName?.toString()
        Picasso.get().load(countryFlag.toString()).error(R.drawable.img_asset_global)
            .into(img_cFlag)

        //PAGE
        //CARD 1
        cd_piechart = findViewById(R.id.cd_piechart)
        cd_ll_info =findViewById(R.id.cd_ll_info)

        //CARD 2
        //cd_txt_updatedAt = findViewById(R.id.cd_txt_updatedAt)
        cd_txt_Cases = findViewById(R.id.cd_txt_Cases)
        cd_txt_TodayCases = findViewById(R.id.cd_txt_TodayCases)
        cd_txt_Death = findViewById(R.id.cd_txt_Death)
        cd_txt_TodayDeath = findViewById(R.id.cd_txt_TodayDeath)
        cd_txt_Recovered = findViewById(R.id.cd_txt_Recovered)
        cd_txt_TodayRecovered = findViewById(R.id.cd_txt_TodayRecovered)
        cd_txt_Active = findViewById(R.id.cd_txt_Active)
        cd_txt_Critical = findViewById(R.id.cd_txt_Critical)
        txt_CS=findViewById(R.id.txt_CS)
        txt_CS.text = "$countryName Stats"
        cd_progress_circular = findViewById(R.id.cd_progress_circular)
        cd_ll_country = findViewById(R.id.cd_ll_country)



        if (ConnectionManager().checkConnectivity(this)) {
           fetchCountryData(countryName.toString())
        }else{
            //Internet is not available
            print("GLOBAL FRAGMENT: Internet Connection Not Found")
           openInternetSettings()
        }

    }

    fun fetchCountryData(countryName:String){

        val queue = Volley.newRequestQueue(this)
        val url = "https://disease.sh/v3/covid-19/countries/$countryName"

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                println("COUNTRY DATA: Inside Response Listner")
                println("COUNTRY DATA:Response is $it")
                try {

                    cd_txt_Cases.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("cases").toLong())
                    cd_txt_TodayCases.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("todayCases").toLong())
                    cd_txt_Death.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("deaths").toLong())
                    cd_txt_TodayDeath.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("todayDeaths").toLong())
                    cd_txt_Recovered.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("recovered").toLong())
                    cd_txt_TodayRecovered.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("todayRecovered").toLong())
                    cd_txt_Active.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("active").toLong())
                    cd_txt_Critical.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("critical").toLong())

                    cd_progress_circular.visibility=View.GONE
                    cd_ll_country.visibility =View.VISIBLE

                    cd_piechart.addPieSlice(
                        PieModel(
                            "Cases",
                            Integer.parseInt(it.getString("cases")).toFloat(),
                            Color.parseColor("#FFA726")
                        )
                    )
                    cd_piechart.addPieSlice(
                        PieModel(
                            "Deaths",
                            Integer.parseInt(it.getString("deaths")).toFloat(),
                            Color.parseColor("#EF5350")
                        )
                    )
                    cd_piechart.addPieSlice(
                        PieModel(
                            "Recovered",
                            Integer.parseInt(it.getString("recovered")).toFloat(),
                            Color.parseColor("#66BB6A")
                        )
                    )
                    cd_piechart.addPieSlice(
                        PieModel(
                            "Active",
                            Integer.parseInt(it.getString("active")).toFloat(),
                            Color.parseColor("#29B6F6")
                        )
                    )
                    cd_piechart.startAnimation()
                    cd_ll_info.visibility= View.VISIBLE

                }catch (e: JSONException){
                    println("COUNTRY DATA ACTIVITY: Error in catch is: $e")
                    Toast.makeText(
                        this,
                        "Unexpected error occurred.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            Response.ErrorListener { println("COUNTRY DATA:Response error is $it") }) {}
        queue.add(jsonObjectRequest)
    }

    fun openInternetSettings(){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("ERROR")
        dialog.setCancelable(false)
        dialog.setMessage("Internet Connection is not available")
        dialog.setPositiveButton("Open Settings") { text, listner ->
            val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(settingIntent)
            this?.finish()
        }
        dialog.setNegativeButton("Exit") { text, listner ->
            ActivityCompat.finishAffinity(this)
        }
        dialog.create()
        dialog.show()
    }
}