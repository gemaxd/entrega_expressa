package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.CitiesResponse
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import com.jessemanarim.entregaexpressa.feature_entrega.domain.repository.DeliveryRepository
import com.jessemanarim.entregaexpressa.feature_entrega.domain.validation.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeliveryCreationViewModel(
    private val deliveryRepository: DeliveryRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<DeliveryCreationUiState>(DeliveryCreationUiState.Ready)
    val uiState: StateFlow<DeliveryCreationUiState> = _uiState

    private val _isClientNameValid = MutableLiveData<Int?>()
    val isClientNameValid: LiveData<Int?> = _isClientNameValid

    private val _isClientCPFValid = MutableLiveData<Int?>()
    val isClientCPFValid: LiveData<Int?> = _isClientCPFValid

    private val _isDeliveryCEPValid = MutableLiveData<Int?>()
    val isDeliveryCEPValid: LiveData<Int?> = _isDeliveryCEPValid

    private val _isDeliveryUFValid = MutableLiveData<Int?>()
    val isDeliveryUFValid: LiveData<Int?> =_isDeliveryUFValid

    private val _isDeliveryCityValid = MutableLiveData<Int?>()
    val isDeliveryCityValid: LiveData<Int?> =_isDeliveryCityValid

    private val _isDeliveryDistrictValid = MutableLiveData<Int?>()
    val isDeliveryDistrictValid: LiveData<Int?> =_isDeliveryDistrictValid

    private val _isDeliveryStreetValid = MutableLiveData<Int?>()
    val isDeliveryStreetValid: LiveData<Int?> =_isDeliveryStreetValid

    private val _isDeliveryNumberValid = MutableLiveData<Int?>()
    val isDeliveryNumberValid: LiveData<Int?> =_isDeliveryNumberValid

    private val _isDeliveryPackagesValid = MutableLiveData<Int?>()
    val isDeliveryPackagesValid: LiveData<Int?> =_isDeliveryPackagesValid

    private val _isDeliveryLimitDateValid = MutableLiveData<Int?>()
    val isDeliveryLimitDateValid: LiveData<Int?> =_isDeliveryLimitDateValid

    private var _citiesResponse = MutableLiveData<CitiesResponse?>()
    val citiesResponse: LiveData<CitiesResponse?> = _citiesResponse

    suspend fun fetchCities(stateCode: String){
        _uiState.value = DeliveryCreationUiState.Loading
        val response = deliveryRepository.fetchCities(stateCode)

        _uiState.value = DeliveryCreationUiState.Ready
        _citiesResponse.postValue(
            if(response.isSuccessful)
                CitiesResponse( cities = response.body )
            else
                null
        )
    }

    fun createDelivery(delivery: Delivery){
        deliveryRepository.createDelivery(delivery)
    }

    fun validateClientName(delivery: Delivery){
        _isClientNameValid.value = getClientNameErrorOrNull(delivery.clientName)
    }

    fun validateDeliveryUF(delivery: Delivery){
        _isDeliveryUFValid.value = getDeliveryUFErrorOrNull(delivery.deliveryUF)
    }

    fun validateDeliveryCity(delivery: Delivery){
        _isDeliveryCityValid.value = getDeliveryCityErrorOrNull(delivery.deliveryCity)
    }

    fun validateDeliveryDistrict(delivery: Delivery){
        _isDeliveryDistrictValid.value = getDeliveryDistrictErrorOrNull(delivery.deliveryDistrict)
    }

    fun validateDeliveryStreet(delivery: Delivery){
        _isDeliveryStreetValid.value = getDeliveryStreetErrorOrNull(delivery.deliveryStreet)
    }

    fun validateDeliveryNumber(delivery: Delivery){
        _isDeliveryNumberValid.value = getDeliveryNumberErrorOrNull(delivery.deliveryNumber)
    }

    fun validateDeliveryPackages(delivery: Delivery){
        _isDeliveryPackagesValid.value = getDeliveryPackagesErrorOrNull(delivery.deliveryPackages)
    }

    fun validateDeliveryLimitDate(delivery: Delivery){
        _isDeliveryLimitDateValid.value = getDeliveryLimitDateErrorOrNull(delivery.deliveryLimitDate)
    }

    private fun validateClientCPF(delivery: Delivery){
        _isClientCPFValid.value = getCPFErrorOrNull(delivery.clientCPF)
    }

    private fun validateDeliveryCEP(delivery: Delivery){
        _isDeliveryCEPValid.value = getCEPErrorOrNull(delivery.deliveryCEP)
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