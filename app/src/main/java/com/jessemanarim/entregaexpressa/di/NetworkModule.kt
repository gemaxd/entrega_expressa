package com.jessemanarim.entregaexpressa.di

import com.google.gson.GsonBuilder
import com.jessemanarim.entregaexpressa.BuildConfig
import com.jessemanarim.entregaexpressa.feature_entrega.data.api.ApiService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    factory {
        val builder = OkHttpClient
            .Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)

        builder.build()
    }

    factory {
        GsonBuilder()
            .setLenient()
            .create()
    }

    factory{ GsonConverterFactory.create(get()) }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
            .create(ApiService::class.java)
    }



}