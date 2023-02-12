package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_create

data class AuthenticationState (
    var isValidName : Boolean = true,
    var isValidCPF : Boolean = true,
    var isValidCEP : Boolean = true,
    var isValidUF : Boolean = true,
    var isValidCity : Boolean = true,
    var isValidDistrict : Boolean = true,
    var isValidStreet : Boolean = true,
    var isValidNumber : Boolean = true,
    var isValidPackage : Boolean = true,
    var isValidLimitDate : Boolean = true
)
