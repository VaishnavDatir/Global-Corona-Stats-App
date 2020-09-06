package com.androidv.globalcoronastatus.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.androidv.globalcoronastatus.fargment.CountryFragment
import com.androidv.globalcoronastatus.fargment.GlobalFragment

class tabViewAdapter(fm: FragmentManager,val contextParam: Context):FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {

        return if(position == 0){
            GlobalFragment(contextParam)
        } else{
            CountryFragment(contextParam)
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0){
            "GLOBAL"
        }
        else{
            "COUNTRY"
        }
    }
}