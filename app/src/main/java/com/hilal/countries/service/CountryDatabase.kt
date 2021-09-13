package com.hilal.countries.service

import Country
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Country::class),version = 1)
abstract class CountryDatabase : RoomDatabase() {

    abstract fun countryDao() :CountryDao

    //Singleton icindne tek bir obje olusturulablir.heryerden ulasilabilircompanion object ile

    companion object {

        //Volatile => diger threadlere de gorunur hale getiriyor. coroutine kullandigimiz icin volatile kullanioz
        @Volatile private var instance: CountryDatabase? = null

        private val lock = Any()

        operator fun invoke(context:Context) = instance ?: synchronized(lock) {
           instance ?: makeDatabase(context).also {

               instance = it

           }
        }

        private fun makeDatabase(context : Context) = Room.databaseBuilder(
            //app context kullanirsak telefon dondugunde vs yokolmiuo
            context.applicationContext, CountryDatabase::class.java, "countrydatabase"
        ).build()


    }

}