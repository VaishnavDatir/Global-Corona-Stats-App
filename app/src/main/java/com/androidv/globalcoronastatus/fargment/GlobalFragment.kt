package com.androidv.globalcoronastatus.fargment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.androidv.globalcoronastatus.R
import com.androidv.globalcoronastatus.util.ConnectionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import org.json.JSONException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GlobalFragment(val contextParam: Context) : Fragment() {

    //CARD 1
    lateinit var piechart: PieChart
    lateinit var ll_info:LinearLayout

    //CARD 2
    //lateinit var txt_updatedAt: TextView
    lateinit var txt_Cases: TextView
    lateinit var txt_TodayCases: TextView
    lateinit var txt_Death: TextView
    lateinit var txt_TodayDeath: TextView
    lateinit var txt_Recovered: TextView
    lateinit var txt_TodayRecovered: TextView
    lateinit var txt_Active: TextView
    lateinit var txt_Critical: TextView
    lateinit var txt_CountriesAffected: TextView
    lateinit var progress_circular:ProgressBar
    lateinit var ll_global:LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_global, container, false)

        //CARD 1
        piechart = view.findViewById(R.id.piechart)
        ll_info = view.findViewById(R.id.ll_info)

        //CARD 2
        //txt_updatedAt = view.findViewById(R.id.txt_updatedAt)
        txt_Cases = view.findViewById(R.id.txt_Cases)
        txt_TodayCases = view.findViewById(R.id.txt_TodayCases)
        txt_Death = view.findViewById(R.id.txt_Death)
        txt_TodayDeath = view.findViewById(R.id.txt_TodayDeath)
        txt_Recovered = view.findViewById(R.id.txt_Recovered)
        txt_TodayRecovered = view.findViewById(R.id.txt_TodayRecovered)
        txt_Active = view.findViewById(R.id.txt_Active)
        txt_Critical = view.findViewById(R.id.txt_Critical)
        txt_CountriesAffected = view.findViewById(R.id.txt_CountriesAffected)
        progress_circular = view.findViewById(R.id.progress_circular)
        ll_global =view.findViewById(R.id.ll_global)

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "https://disease.sh/v3/covid-19/all"
        println("MAIN ACTIVITY: Queue and Url assigned, Proceeding to Get JSON")

        if (ConnectionManager().checkConnectivity(contextParam)) {
            println("MAIN ACTIVITY: Internet Connection Found")
            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener {
                    try {
                        println("GLOBAL FRAGMENT: Inside Try Block")
                        //println("MAIN ACTIVITY: Response is: $it")
                        //val data = it.getString("cases")
                        //println("MAIN ACTIVITY: Data: $data")
                        //val updated = Integer.parseInt(it.getString("updated")).toFloat()
                     /*   val updated = it.getString("updated")
//                        println("MAIN ACTIVITY: Updated: $updated")

                        val date = Date(updated.toLong())
                        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa")
                        format.setTimeZone(TimeZone.getTimeZone("GMT+5:30"))
                        val formatted: String = format.format(date)*/
                       // txt_updatedAt.text = formatted.toString()
                        txt_Cases.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("cases").toLong())
                        txt_TodayCases.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("todayCases").toLong())
                        txt_Death.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("deaths").toLong())
                        txt_TodayDeath.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("todayDeaths").toLong())
                        txt_Recovered.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("recovered").toLong())
                        txt_TodayRecovered.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("todayRecovered").toLong())
                        txt_Active.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("active").toLong())
                        txt_Critical.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("critical").toLong())
                        txt_CountriesAffected.text = java.text.NumberFormat.getIntegerInstance().format(it.getString("affectedCountries").toLong())
                        progress_circular.visibility= View.GONE
                        ll_global.visibility = View.VISIBLE

                        piechart.addPieSlice(
                            PieModel(
                                "Cases",
                                Integer.parseInt(it.getString("cases")).toFloat(),
                                Color.parseColor("#FFA726")
                            )
                        )
                        piechart.addPieSlice(
                            PieModel(
                                "Deaths",
                                Integer.parseInt(it.getString("deaths")).toFloat(),
                                Color.parseColor("#EF5350")
                            )
                        )
                        piechart.addPieSlice(
                            PieModel(
                                "Recovered",
                                Integer.parseInt(it.getString("recovered")).toFloat(),
                                Color.parseColor("#66BB6A")
                            )
                        )
                        piechart.addPieSlice(
                            PieModel(
                                "Active",
                                Integer.parseInt(it.getString("active")).toFloat(),
                                Color.parseColor("#29B6F6")
                            )
                        )
                        piechart.startAnimation()
                        ll_info.visibility = View.VISIBLE

                    } catch (e: JSONException) {
                        println("MAIN ACTIVITY: Catch error is: $e")
                        Toast.makeText(
                            contextParam,
                            "Unexpected error occurred.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    println("MAIN ACTIVITY: ERROR: $it")
                    Toast.makeText(
                        contextParam,
                        "NO response from the server",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {}
            queue.add(jsonObjectRequest)

        } else {
            //Internet is not available
            print("GLOBAL FRAGMENT: Internet Connection Not Found")
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("ERROR")
            dialog.setCancelable(false)
            dialog.setMessage("Internet Connection is not available")
            dialog.setPositiveButton("Open Settings") { text, listner ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

}