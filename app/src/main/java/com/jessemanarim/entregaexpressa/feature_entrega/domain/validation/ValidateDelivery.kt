package com.jessemanarim.entregaexpressa.feature_entrega.domain.validation

import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery

fun isClientNameValid(delivery: Delivery): Boolean =
    (!delivery.clientName.isNullOrEmpty() && !delivery.clientName.isNullOrBlank())

fun isClientCPFValid(delivery: Delivery): Boolean =
    (!delivery.clientCPF.isNullOrEmpty() && !delivery.clientCPF.isNullOrBlank())

fun isDeliveryCEPValid(delivery: Delivery): Boolean =
    (!delivery.deliveryCEP.isNullOrEmpty() && !delivery.deliveryCEP.isNullOrBlank())

fun isDeliveryUFValid(delivery: Delivery): Boolean =
    (!delivery.deliveryUF.isNullOrEmpty() && !delivery.deliveryUF.isNullOrBlank())

fun isDeliveryCityValid(delivery: Delivery): Boolean =
    (!delivery.deliveryCity.isNullOrEmpty() && !delivery.deliveryCity.isNullOrBlank())

fun isDeliveryDistrictValid(delivery: Delivery): Boolean =
    (!delivery.deliveryDistrict.isNullOrEmpty() && !delivery.deliveryDistrict.isNullOrBlank())

fun isDeliveryStreetValid(delivery: Delivery): Boolean =
    (!delivery.deliveryStreet.isNullOrEmpty() && !delivery.deliveryStreet.isNullOrBlank())

fun isDeliveryNumberValid(delivery: Delivery): Boolean =
    (!delivery.deliveryNumber.isNullOrEmpty() && !delivery.deliveryNumber.isNullOrBlank())

fun isDeliveryPackagesValid(delivery: Delivery): Boolean =
    (!delivery.deliveryPackages.isNullOrEmpty() && !delivery.deliveryPackages.isNullOrBlank())

fun isDeliveryLimitDateValid(delivery: Delivery): Boolean =
    (!delivery.deliveryLimitDate.isNullOrEmpty() && !delivery.deliveryLimitDate.isNullOrBlank())

fun Delivery.isDeliveryValid(): Boolean =
    isClientNameValid(this) &&
    isClientCPFValid(this) &&
    isDeliveryCEPValid(this) &&
    isDeliveryUFValid(this) &&
    isDeliveryCityValid(this) &&
    isDeliveryDistrictValid(this) &&
    isDeliveryStreetValid(this) &&
    isDeliveryNumberValid(this) &&
    isDeliveryPackagesValid(this) &&
    isDeliveryLimitDateValid(this)
