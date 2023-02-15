package com.jessemanarim.entregaexpressa.feature_entrega.data.api

import com.jessemanarim.entregaexpressa.feature_entrega.data.model.CitiesResponse
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.City
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/v1/localidades/estados/{countryState}/municipios")
    suspend fun fetchCities(@Path("countryState") countryState: String): Response<List<City>>
}