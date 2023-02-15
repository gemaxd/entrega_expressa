package com.jessemanarim.entregaexpressa

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jessemanarim.entregaexpressa.feature_entrega.data.local.dao.DeliveryDao
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery

@Database(entities = [Delivery::class], version = 1)
abstract class EntregaExpressaDatabase : RoomDatabase() {
    abstract fun deliveryDao(): DeliveryDao
}