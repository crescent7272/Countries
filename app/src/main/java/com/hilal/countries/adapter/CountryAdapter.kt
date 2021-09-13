package com.hilal.countries.adapter

import Country
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.hilal.countries.R
import com.hilal.countries.util.downloadFromUrl
import com.hilal.countries.util.placeHolderProgressBar
import com.hilal.countries.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.item_country.view.*

class CountryAdapter(val countryList: ArrayList<Country>): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    class CountryViewHolder(var view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_country,parent, false)

        return CountryViewHolder(view);
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        holder.view.name.text = countryList[position].countryName
        holder.view.region.text = countryList[position].countryRegion

        holder.view.setOnClickListener { it->

            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(5)
            Navigation.findNavController(it).navigate(action)
        }

        holder.view.imageView.downloadFromUrl(countryList[position].imageUrl.toString(), placeHolderProgressBar(holder.view.context))

    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun updateCountryList(newCountrylist: List<Country>) {
        countryList.clear()
        countryList.addAll(newCountrylist)
        notifyDataSetChanged()
    }

}