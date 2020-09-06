package com.androidv.globalcoronastatus.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androidv.globalcoronastatus.R
import com.androidv.globalcoronastatus.activity.CountryData
import com.androidv.globalcoronastatus.model.Counrtries
import com.squareup.picasso.Picasso

class CountryListAdapter(val context: Context, var countryList: ArrayList<Counrtries>) :
    RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>() {

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt_CountryName: TextView = view.findViewById(R.id.txt_CountryName)
        val txt_CountryCases: TextView = view.findViewById(R.id.txt_CountryCases)
        val img_CountryFlag: ImageView = view.findViewById(R.id.img_CountryFlag)
        val llContent_home: LinearLayout = view.findViewById(R.id.llContent_home)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_country_single_row, parent, false)
        return CountryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countryList[position]
        holder.txt_CountryName.text = country.countryName
        holder.txt_CountryCases.text = java.text.NumberFormat.getIntegerInstance().format(country.countryCases.toLong())

        Picasso.get().load(country.countryFlag).error(R.drawable.img_asset_global)
            .into(holder.img_CountryFlag)

        holder.llContent_home.setOnClickListener {
            //Toast.makeText(context,"Clicked on ${country.countryName}",Toast.LENGTH_SHORT).show()
            val intent = Intent(context,CountryData::class.java)
            intent.putExtra("country_Name",country.countryName)
            intent.putExtra("country_Flag",country.countryFlag)
            context.startActivity(intent)
        }
    }

    fun filterList(filteredList: ArrayList<Counrtries>){
        countryList = filteredList
        notifyDataSetChanged()
    }
}