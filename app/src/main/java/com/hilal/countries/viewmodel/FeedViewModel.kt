package com.hilal.countries.viewmodel

import Country
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.hilal.countries.service.CountryAPIService
import com.hilal.countries.service.CountryDatabase
import com.hilal.countries.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {

    private val countryApiService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private val TAG = "FeedViewModel"
    private var refreshTime = 10*60*1000*1000*1000L

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData() {

        val update_time = customPreferences.getTime()
        if (update_time != null && update_time != 0L && System.nanoTime() - update_time < refreshTime) {
            getDataFromSQLite()
        } else {
            getDataFromAPI()
        }

    /*
        val country = Country("Turkey","Asia", "Ankara", "TRY","Turkish","www.ss.com")
        val country2 = Country("France","Europe", "Paris", "EUR","French","www.ss.com")
        val country3 = Country("Germany","Europe", "Berlin", "EUR","German","www.ss.com")

        val countryList = arrayListOf<Country>(country,country2,country3)

        countries.value = countryList
        countryError.value = false
        countryLoading.value = false*/
    }

    private fun getDataFromSQLite() {

        launch {

            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()

            showCountries(countries)
            Toast.makeText(getApplication(),"Countries from SQLite", Toast.LENGTH_LONG).show()

        }

    }

    private fun getDataFromAPI() {
        countryLoading.value = true

        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(value: List<Country>) {
                        Log.d(TAG,"onsuccess value = "+value.size)
                        storeSQLite(value)
                        Toast.makeText(getApplication(),"Countries from API",Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable?) {

                        countryLoading.value = false
                        countryError.value = true
                        if (e != null) {
                            e.printStackTrace()
                        }
                    }

                })

        )
    }

    private fun showCountries(countryList: List<Country>){
        countries.value = countryList
        countryError.value = false
        countryLoading.value = false

    }

    private fun storeSQLite(list: List<Country>){

        launch {

            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val listlong = dao.insertAll(*list.toTypedArray()) // list -> individual
            var i = 0

            while(i<list.size) {
                list[i].uuid = listlong[i].toInt()
                i = i +1
            }

            showCountries(list)
        }

        customPreferences.saveTime(System.nanoTime())

    }

}