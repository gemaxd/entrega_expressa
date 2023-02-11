package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jessemanarim.entregaexpressa.EntregaExpressaDatabase
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery

class DeliveryListViewModel(
    private val database: EntregaExpressaDatabase
): ViewModel() {

    private val _deliveries = MutableLiveData<List<Delivery>>()
    val deliveries : LiveData<List<Delivery>> = _deliveries

    fun fetchAllDeliveries()  {
        _deliveries.postValue(database.deliveryDao().fetchAllDeliveries())
    }

    suspend fun deleteDelivery(delivery: Delivery)  {
        database.deliveryDao().deleteDelivery(delivery)
        fetchAllDeliveries()
    }

}