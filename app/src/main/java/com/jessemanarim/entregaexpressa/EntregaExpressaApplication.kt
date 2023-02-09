package com.jessemanarim.entregaexpressa

import android.app.Application
import com.jessemanarim.entregaexpressa.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EntregaExpressaApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@EntregaExpressaApplication)
            modules(appModule)
        }
    }
}