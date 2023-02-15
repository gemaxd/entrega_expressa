package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_list

import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery

sealed class DeliveryListUiState {
    object Empty: DeliveryListUiState()
    data class Success(val list: List<Delivery>): DeliveryListUiState()
}
