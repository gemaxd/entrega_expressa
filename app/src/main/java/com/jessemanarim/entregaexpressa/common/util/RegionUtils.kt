package com.jessemanarim.entregaexpressa.common.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.CountryState
import java.io.IOException

fun getCountryCode(context: Context): List<CountryState> {

    lateinit var jsonString: String
    try {
        jsonString = context.assets.open("country/country_states.json")
            .bufferedReader()
            .use { it.readText() }
    } catch (ioException: IOException) {
        Log.e("Exception type $ioException", ioException.message.toString())
    }

    val listCountryType = object : TypeToken<List<CountryState>>() {}.type
    return Gson().fromJson(jsonString, listCountryType)
}