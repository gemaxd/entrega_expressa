package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.CitiesListResponse
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import com.jessemanarim.entregaexpressa.feature_entrega.domain.repository.DeliveryRepository
import com.jessemanarim.entregaexpressa.feature_entrega.domain.validation.*

class DeliveryCreationViewModel(
    private val deliveryRepository: DeliveryRepository
): ViewModel() {
    private val _isClientNameValid = MutableLiveData(true)
    val isClientNameValid: LiveData<Boolean> = _isClientNameValid

    private val _isClientCPFValid = MutableLiveData(true)
    val isClientCPFValid: LiveData<Boolean> = _isClientCPFValid

    private val _isDeliveryCEPValid = MutableLiveData(true)
    val isDeliveryCEPValid: LiveData<Boolean> = _isDeliveryCEPValid

    private val _isDeliveryUFValid = MutableLiveData(true)
    val isDeliveryUFValid: LiveData<Boolean> =_isDeliveryUFValid

    private val _isDeliveryCityValid = MutableLiveData(true)
    val isDeliveryCityValid: LiveData<Boolean> =_isDeliveryCityValid

    private val _isDeliveryDistrictValid = MutableLiveData(true)
    val isDeliveryDistrictValid: LiveData<Boolean> =_isDeliveryDistrictValid

    private val _isDeliveryStreetValid = MutableLiveData(true)
    val isDeliveryStreetValid: LiveData<Boolean> =_isDeliveryStreetValid

    private val _isDeliveryNumberValid = MutableLiveData(true)
    val isDeliveryNumberValid: LiveData<Boolean> =_isDeliveryNumberValid

    private val _isDeliveryPackagesValid = MutableLiveData(true)
    val isDeliveryPackagesValid: LiveData<Boolean> =_isDeliveryPackagesValid

    private val _isDeliveryLimitDateValid = MutableLiveData(true)
    val isDeliveryLimitDateValid: LiveData<Boolean> =_isDeliveryLimitDateValid

    private var _citiesList = MutableLiveData<List<CitiesListResponse>>()
    val citiesList: LiveData<List<CitiesListResponse>> = _citiesList

    suspend fun fetchCities(stateCode: String){
        _citiesList.postValue(deliveryRepository.fetchCities(stateCode))
    }

    fun createDelivery(delivery: Delivery){
        deliveryRepository.createDelivery(delivery)
    }

    fun validateClientName(delivery: Delivery){
        _isClientNameValid.value = isClientNameValid(delivery)
    }

    fun validateClientCPF(delivery: Delivery){
        _isClientCPFValid.value = isClientCPFValid(delivery)
    }

    fun validateDeliveryCEP(delivery: Delivery){
        _isDeliveryCEPValid.value = isDeliveryCEPValid(delivery)
    }

    fun validateDeliveryUF(delivery: Delivery){
        _isDeliveryUFValid.value = isDeliveryUFValid(delivery)
    }

    fun validateDeliveryCity(delivery: Delivery){
        _isDeliveryCityValid.value = isDeliveryCityValid(delivery)
    }

    fun validateDeliveryDistrict(delivery: Delivery){
        _isDeliveryDistrictValid.value = isDeliveryDistrictValid(delivery)
    }

    fun validateDeliveryStreet(delivery: Delivery){
        _isDeliveryStreetValid.value = isDeliveryStreetValid(delivery)
    }

    fun validateDeliveryNumber(delivery: Delivery){
        _isDeliveryNumberValid.value = isDeliveryNumberValid(delivery)
    }

    fun validateDeliveryPackages(delivery: Delivery){
        _isDeliveryPackagesValid.value = isDeliveryPackagesValid(delivery)
    }

    fun validateDeliveryLimitDate(delivery: Delivery){
        _isDeliveryLimitDateValid.value = isDeliveryLimitDateValid(delivery)
    }

    fun validateFields(delivery: Delivery): Boolean {
        validateClientName(delivery)
        validateClientCPF(delivery)
        validateDeliveryCEP(delivery)
        validateDeliveryUF(delivery)
        validateDeliveryCity(delivery)
        validateDeliveryDistrict(delivery)
        validateDeliveryStreet(delivery)
        validateDeliveryNumber(delivery)
        validateDeliveryPackages(delivery)
        validateDeliveryLimitDate(delivery)

        return delivery.isDeliveryValid()
    }
}