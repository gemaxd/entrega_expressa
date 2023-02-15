package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_create

sealed class DeliveryCreationUiState {
    object Loading: DeliveryCreationUiState()
    object Ready: DeliveryCreationUiState()
}
