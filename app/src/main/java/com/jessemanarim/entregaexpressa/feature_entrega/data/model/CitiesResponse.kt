package com.jessemanarim.entregaexpressa.feature_entrega.data.model

import com.google.gson.annotations.SerializedName
import retrofit2.Response

data class City(
    @SerializedName("nome")
    val name: String
)

data class CitiesResponse(
    val cities: List<City> = emptyList()
)

data class SimpleResponse<T> (
    val status: Status,
    val data: Response<T>?,
    val exception: Exception?
){
    companion object {
        fun <T> success(data: Response<T>): SimpleResponse<T> {
            return SimpleResponse(
                status = Status.Success,
                data = data,
                exception = null
            )
        }

        fun <T> failure(exception: Exception): SimpleResponse<T>{
            return SimpleResponse(
                status = Status.Failure,
                data = null,
                exception = exception
            )
        }
    }

    sealed class Status {
        object Success: Status()
        object Failure: Status()
    }

    val isSuccessful: Boolean
        get() = this.data?.isSuccessful == true

    val body: T
        get() = this.data!!.body()!!

}
