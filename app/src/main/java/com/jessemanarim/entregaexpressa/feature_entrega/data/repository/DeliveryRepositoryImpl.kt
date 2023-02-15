package com.jessemanarim.entregaexpressa.feature_entrega.data.repository

import com.jessemanarim.entregaexpressa.EntregaExpressaDatabase
import com.jessemanarim.entregaexpressa.feature_entrega.data.api.ApiService
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.City
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.SimpleResponse
import com.jessemanarim.entregaexpressa.feature_entrega.domain.repository.DeliveryRepository
import retrofit2.Response

class DeliveryRepositoryImpl(
    private val apiService: ApiService,
    private val database: EntregaExpressaDatabase
): DeliveryRepository {
    override fun fetchDeliveries(): List<Delivery> {
        return database.deliveryDao().fetchAllDeliveries()
    }

    override fun createDelivery(delivery: Delivery) {
        database.deliveryDao().insertDelivery(delivery)
    }

    override fun updateDelivery(delivery: Delivery) {
        database.deliveryDao().updateDelivery(delivery)
    }

    override fun deleteDelivery(delivery: Delivery) {
        database.deliveryDao().deleteDelivery(delivery)
    }

    override suspend fun findDelivery(deliveryId: Int): Delivery {
        return database.deliveryDao().findDelivery(deliveryId)
    }

    override suspend fun fetchCities(countryState: String): SimpleResponse<List<City>> {
        return safeApiCall { apiService.fetchCities(countryState = countryState) }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): SimpleResponse<T>{
        return try {
            SimpleResponse.success(apiCall.invoke())
        } catch (e: java.lang.Exception){
            SimpleResponse.failure(e)
        }
    }
}