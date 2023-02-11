package com.jessemanarim.entregaexpressa.feature_entrega.data.model

import com.google.gson.annotations.SerializedName

data class CountryState(
    @SerializedName("nome")
    var name: String? = null,
    @SerializedName("sigla")
    var code: String? = null
)
