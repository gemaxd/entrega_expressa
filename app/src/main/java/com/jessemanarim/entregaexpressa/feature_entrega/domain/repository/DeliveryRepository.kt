package com.jessemanarim.entregaexpressa.feature_entrega.domain.repository

import com.jessemanarim.entregaexpressa.feature_entrega.data.model.CitiesListResponse
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery

interface DeliveryRepository {
    fun fetchDeliveries(): List<Delivery>
    fun createDelivery(delivery: Delivery)
    fun updateDelivery(delivery: Delivery)
    fun deleteDelivery(delivery: Delivery)
    fun findDelivery(deliveryId: Int): Delivery
    suspend fun fetchCities(countryState: String): List<CitiesListResponse>
}