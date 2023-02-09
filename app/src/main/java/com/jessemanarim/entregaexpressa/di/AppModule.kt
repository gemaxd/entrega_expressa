package com.jessemanarim.entregaexpressa.di

import com.jessemanarim.entregaexpressa.data.repository.DeliveryRepositoryImpl
import com.jessemanarim.entregaexpressa.domain.repository.DeliveryRepository
import org.koin.dsl.module

val appModule = module{
    single<DeliveryRepository>{
        DeliveryRepositoryImpl()
    }
}