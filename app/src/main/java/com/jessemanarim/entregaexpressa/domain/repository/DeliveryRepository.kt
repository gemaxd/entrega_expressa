package com.jessemanarim.entregaexpressa.domain.repository

interface DeliveryRepository {
    fun fetchDeliveries()
    fun createDelivery()
    fun updateDelivery()
    fun deleteDelivery()
    fun findDelivery()
}