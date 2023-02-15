package com.jessemanarim.entregaexpressa.feature_entrega.domain.validation

import com.jessemanarim.entregaexpressa.R
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery

fun getClientNameErrorOrNull(clientName: String?): Int? =
    if(clientName.isNullOrEmpty() || clientName.isBlank())
        R.string.error_empty_client_name
    else
        null

fun getCPFErrorOrNull(clientCPF: String?): Int? {
    val numbers = clientCPF?.replace(".", "")?.replace("-", "")

    if(clientCPF.isNullOrEmpty() || clientCPF.isBlank())
        return R.string.error_empty_cpf

    val rawCpf = clientCPF.toString()
        .replace(".", "")
        .replace("-", "")
    if (rawCpf.length < 11)
        return R.string.error_invalid_cpf

    val firstVerifierDigit = calculateVerifierDigit(numbers!!.substring(0, 9))
    val secondVerifierDigit = calculateVerifierDigit(numbers.substring(0, 9) + firstVerifierDigit)

    if(isEveryCharacterEqual(numbers))
        R.string.error_invalid_cpf

    if(!clientCPF.endsWith("$firstVerifierDigit$secondVerifierDigit"))
        return R.string.error_invalid_cpf

    return null
}
fun getCEPErrorOrNull(deliveryCEP: String?): Int?  {
    if((deliveryCEP.isNullOrEmpty() || deliveryCEP.isBlank()))
        return R.string.error_empty_cep

    val rawCpf = deliveryCEP.toString().replace("-", "")
    if (rawCpf.length < 8)
        return R.string.error_invalid_cep

    return null
}

fun getDeliveryUFErrorOrNull(deliveryUF: String?): Int? =
    if(deliveryUF.isNullOrEmpty() || deliveryUF.isBlank()) R.string.error_empty_uf else null

fun getDeliveryCityErrorOrNull(deliveryCity: String?): Int? =
    if(deliveryCity.isNullOrEmpty() || deliveryCity.isBlank()) R.string.error_empty_city else null

fun getDeliveryDistrictErrorOrNull(district: String?): Int? =
    if(district.isNullOrEmpty() || district.isBlank()) R.string.error_empty_district else null

fun getDeliveryStreetErrorOrNull(street: String?): Int? =
    if(street.isNullOrEmpty() || street.isBlank()) R.string.error_empty_street else null

fun getDeliveryNumberErrorOrNull(number: String?): Int? =
    if (number.isNullOrEmpty() || number.isBlank()) R.string.error_empty_number else null

fun getDeliveryPackagesErrorOrNull(packages: String?): Int? =
    if(packages.isNullOrEmpty() || packages.isBlank()) R.string.error_empty_packages else null

fun getDeliveryLimitDateErrorOrNull(limitDate: String?): Int? =
    if(limitDate.isNullOrEmpty() || limitDate.isBlank()) R.string.error_empty_limit_date else null

fun Delivery.isDeliveryValid(): Boolean =
    getClientNameErrorOrNull(this.clientName) == null &&
    getCPFErrorOrNull(this.clientCPF) == null &&
    getCEPErrorOrNull(this.deliveryCEP) == null &&
    getDeliveryUFErrorOrNull(this.deliveryUF) == null &&
    getDeliveryCityErrorOrNull(this.deliveryCity) == null &&
    getDeliveryDistrictErrorOrNull(this.deliveryDistrict) == null &&
    getDeliveryStreetErrorOrNull(this.deliveryStreet) == null &&
    getDeliveryNumberErrorOrNull(this.deliveryNumber) == null &&
    getDeliveryPackagesErrorOrNull(this.deliveryPackages) == null  &&
    getDeliveryLimitDateErrorOrNull(this.deliveryLimitDate) == null

private fun calculateVerifierDigit(numbers: String): Int {
    var sum = 0
    for (i in numbers.indices) {
        sum += numbers[i].toString().toInt() * ((numbers.length+1) - i)
    }
    val remainder = sum % 11
    return if (remainder < 2) 0 else 11 - remainder
}

fun isEveryCharacterEqual(input: String): Boolean {
    return input.all { it == input[0] }
}

