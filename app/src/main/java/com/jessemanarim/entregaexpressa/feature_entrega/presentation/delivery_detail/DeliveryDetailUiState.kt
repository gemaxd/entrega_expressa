package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_detail

sealed class DeliveryDetailUiState {
    object Ready: DeliveryDetailUiState()
    object Loading: DeliveryDetailUiState()
    object Editing: DeliveryDetailUiState()
    object NotEditing: DeliveryDetailUiState()
}
