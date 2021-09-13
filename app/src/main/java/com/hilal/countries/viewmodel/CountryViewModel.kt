package com.hilal.countries.viewmodel

import Country
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountryViewModel : ViewModel() {

    val countryLiveData = MutableLiveData<Country>()

    fun getDataFromRoom() {
        val country = Country("Turkey","Asia","Ankara","TRY","Turkish","www.ss.com")
        countryLiveData.value = country
    }
}