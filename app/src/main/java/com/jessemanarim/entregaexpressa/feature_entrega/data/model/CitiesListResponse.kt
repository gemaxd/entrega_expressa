package com.jessemanarim.entregaexpressa.feature_entrega.data.model

import com.google.gson.annotations.SerializedName

data class CitiesListResponse (
    @SerializedName("nome")
    val name: String = ""
)
