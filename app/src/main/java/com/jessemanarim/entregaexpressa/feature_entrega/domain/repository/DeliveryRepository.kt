package com.jessemanarim.entregaexpressa.feature_entrega.domain.repository

import com.jessemanarim.entregaexpressa.feature_entrega.data.model.City
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.SimpleResponse

interface DeliveryRepository {
    fun fetchDeliveries(): List<Delivery>
    fun createDelivery(delivery: Delivery)
    fun updateDelivery(delivery: Delivery)
    fun deleteDelivery(delivery: Delivery)
    suspend fun findDelivery(deliveryId: Int): Delivery
    suspend fun fetchCities(countryState: String): SimpleResponse<List<City>>
}