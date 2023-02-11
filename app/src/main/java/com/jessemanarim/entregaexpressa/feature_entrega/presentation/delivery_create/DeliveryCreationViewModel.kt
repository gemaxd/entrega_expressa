package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.CitiesListResponse
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import com.jessemanarim.entregaexpressa.feature_entrega.domain.repository.DeliveryRepository

class DeliveryCreationViewModel(
    private val deliveryRepository: DeliveryRepository
): ViewModel() {

    private var _citiesList = MutableLiveData<List<CitiesListResponse>>()
    val citiesList: LiveData<List<CitiesListResponse>> = _citiesList

    suspend fun fetchCities(stateCode: String){
        _citiesList.postValue(deliveryRepository.fetchCities(stateCode))
    }

    suspend fun createDelivery(delivery: Delivery){
        deliveryRepository.createDelivery(delivery)
    }

}