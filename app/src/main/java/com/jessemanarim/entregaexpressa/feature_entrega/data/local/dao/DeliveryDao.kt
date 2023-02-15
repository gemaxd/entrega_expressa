package com.jessemanarim.entregaexpressa.feature_entrega.data.local.dao

import androidx.room.*
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery

@Dao
interface DeliveryDao {

    @Insert
    fun insertDelivery(delivery: Delivery)

    @Delete
    fun deleteDelivery(delivery: Delivery)

    @Update
    fun updateDelivery(delivery: Delivery)

    @Query("SELECT * FROM Delivery WHERE DeliveryId = :deliveryId Limit 1")
    fun findDelivery(deliveryId: Int): Delivery

    @Query("SELECT * FROM Delivery")
    fun fetchAllDeliveries(): List<Delivery>

}