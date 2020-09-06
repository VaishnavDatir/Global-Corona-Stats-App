package com.androidv.globalcoronastatus.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.androidv.globalcoronastatus.R
import com.androidv.globalcoronastatus.adapter.tabViewAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

   lateinit var fab_btn: FloatingActionButton // Fab

    /* //CARD 1
    lateinit var piechart: PieChart

    //CARD 2
    lateinit var txt_updatedAt:TextView
    lateinit var txt_Cases: TextView
    lateinit var txt_TodayCases: TextView
    lateinit var txt_Death: TextView
    lateinit var txt_TodayDeath: TextView
    lateinit var txt_Recovered: TextView
    lateinit var txt_TodayRecovered: TextView
    lateinit var txt_Active: TextView
    lateinit var txt_Critical: TextView
    lateinit var txt_CountriesAffected: TextView*/

    lateinit var viewPage: ViewPager
    lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPage = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.TabLayout)

        /*fab_btn = findViewById(R.id.btn_TrackCountries)
        fab_btn.setOnClickListener {
            val intent = Intent(this@MainActivity,CountryList::class.java)
            startActivity(intent)
            finish()
        }*/

        val fragmentAdapter = tabViewAdapter(supportFragmentManager,this)
        viewPage.adapter = fragmentAdapter

        tabLayout.setupWithViewPager(viewPage)
    }

}