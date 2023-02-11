package com.jessemanarim.entregaexpressa.di

import android.app.Application
import androidx.room.Room
import com.jessemanarim.entregaexpressa.EntregaExpressaDatabase
import com.jessemanarim.entregaexpressa.feature_entrega.data.local.dao.DeliveryDao
import com.jessemanarim.entregaexpressa.feature_entrega.data.repository.DeliveryRepositoryImpl
import com.jessemanarim.entregaexpressa.feature_entrega.domain.repository.DeliveryRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module{
    single<DeliveryRepository>{
        DeliveryRepositoryImpl(get(), get())
    }

    fun provideDataBase(application: Application): EntregaExpressaDatabase {
        return Room.databaseBuilder(application, EntregaExpressaDatabase::class.java, "EntregaExpressaDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(dataBase: EntregaExpressaDatabase): DeliveryDao {
        return dataBase.deliveryDao()
    }
    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }
}
