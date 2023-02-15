package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.CitiesResponse
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import com.jessemanarim.entregaexpressa.feature_entrega.domain.repository.DeliveryRepository
import com.jessemanarim.entregaexpressa.feature_entrega.domain.validation.*
import com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_create.DeliveryCreationUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeliveryDetailViewModel(
    private val deliveryRepository: DeliveryRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<DeliveryDetailUiState>(DeliveryDetailUiState.Ready)
    val uiState: StateFlow<DeliveryDetailUiState> = _uiState

    private val _fetchedDelivery = MutableLiveData<Delivery>()
    val fetchedDelivery: LiveData<Delivery> = _fetchedDelivery

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
        _uiState.value = DeliveryDetailUiState.Loading
        val response = deliveryRepository.fetchCities(stateCode)
        _uiState.value = DeliveryDetailUiState.Ready
        _citiesResponse.postValue(
            if(response.failed)
                null
            else
                CitiesResponse(
                    cities = response.body
                )
        )
    }

    fun fetchDeliveryInfo(deliveryId: Int) {
        viewModelScope.launch {
            _uiState.value = DeliveryDetailUiState.Loading
            CoroutineScope(Dispatchers.IO).launch{
                _fetchedDelivery.postValue(deliveryRepository.findDelivery(deliveryId = deliveryId))
                _uiState.value = DeliveryDetailUiState.NotEditing
            }
        }
    }

    fun toggleFieldsInteraction(isEnabled: Boolean) {
        _uiState.value = if(isEnabled) DeliveryDetailUiState.Editing else DeliveryDetailUiState.NotEditing
    }

    fun updateDelivery(delivery: Delivery){
        CoroutineScope(Dispatchers.IO).launch {
            deliveryRepository.updateDelivery(delivery = delivery)
        }
    }

    fun validateClientName(delivery: Delivery){
        _isClientNameValid.value = getClientNameErrorOrNull(delivery.clientName)
    }

    fun validateClientCPF(delivery: Delivery){
        _isClientCPFValid.value = getCPFErrorOrNull(delivery.clientCPF)
    }

    fun validateDeliveryCEP(delivery: Delivery){
        _isDeliveryCEPValid.value = getCEPErrorOrNull(delivery.deliveryCEP)
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