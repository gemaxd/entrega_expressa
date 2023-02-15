package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_list

import androidx.lifecycle.ViewModel
import com.jessemanarim.entregaexpressa.EntregaExpressaDatabase
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeliveryListViewModel(
    private val database: EntregaExpressaDatabase
): ViewModel() {

    private val _uiState = MutableStateFlow<DeliveryListUiState>(DeliveryListUiState.Empty)
    val uiState: StateFlow<DeliveryListUiState> = _uiState

    init {
        showList()
    }

    private fun showList(){
        CoroutineScope(IO).launch {
            val list = database.deliveryDao().fetchAllDeliveries()
            if(list.isEmpty()){
                _uiState.value = DeliveryListUiState.Empty
            } else {
                _uiState.value = DeliveryListUiState.Success(list)
            }
        }
    }

    fun deleteDelivery(delivery: Delivery)  {
        database.deliveryDao().deleteDelivery(delivery)
        showList()
    }

}