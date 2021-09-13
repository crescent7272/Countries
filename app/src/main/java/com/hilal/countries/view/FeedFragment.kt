package com.hilal.countries.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hilal.countries.R
import com.hilal.countries.adapter.CountryAdapter
import com.hilal.countries.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*
import java.util.EnumSet.of


class FeedFragment : Fragment() {

    private val TAG: String = "FeedFragment"
    private lateinit var viewModel : FeedViewModel
    private var countryAdapter = CountryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        viewModel.refreshData()

        countryList.layoutManager = LinearLayoutManager(context)
        countryList.adapter = countryAdapter



        /*

        val myString = "James"
        myString.myExtension("Hetfield")
        fragment_button.setOnClickListener {

            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment()


            Navigation.findNavController(it).navigate(action)

        }*/

        swipeRefreshLayout.setOnRefreshListener {

            countryList.visibility = View.GONE
            countryError.visibility = View.GONE
            countryLoading.visibility = View.VISIBLE
            viewModel.refreshData()
            swipeRefreshLayout.isRefreshing = false

        }
        observeLiveData()

    }

    fun observeLiveData() {
        viewModel.countries.observe(viewLifecycleOwner, Observer { countries ->

            countries?.let {
                countryList.visibility = View.VISIBLE
                countryAdapter.updateCountryList(countries)
            }
        })
        viewModel.countryError.observe(viewLifecycleOwner, Observer { error ->

            error?.let {

                if(it){
                    countryError.visibility = View.VISIBLE
                } else {
                    countryError.visibility = View.GONE
                }

            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading ->

            loading?.let {

                if(it) {
                    Log.d(TAG,"true")
                    countryLoading.visibility = View.VISIBLE
                    countryList.visibility = View.GONE
                    countryError.visibility = View.GONE
                }
                else {
                    Log.d(TAG,"false")
                    countryLoading.visibility = View.GONE
                }

            }
        })

    }
}