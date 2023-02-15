package com.jessemanarim.entregaexpressa.feature_entrega.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Delivery")
data class Delivery(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("DeliveryId")
    var deliveryId: Int = 0,

    @ColumnInfo("ClientName")
    var clientName: String? = null,

    @ColumnInfo("ClientCPF")
    var clientCPF: String? = null,

    @ColumnInfo("DeliveryCEP")
    var deliveryCEP: String? = null,

    @ColumnInfo("DeliveryUF")
    var deliveryUF: String? = null,

    @ColumnInfo("DeliveryCity")
    var deliveryCity: String? = null,

    @ColumnInfo("DeliveryDistrict")
    var deliveryDistrict: String? = null,

    @ColumnInfo("DeliveryStreet")
    var deliveryStreet: String? = null,

    @ColumnInfo("DeliveryNumber")
    var deliveryNumber: String? = null,

    @ColumnInfo("DeliveryPackages")
    var deliveryPackages: String? = null,

    @ColumnInfo("DeliveryLimitDate")
    var deliveryLimitDate: String? = null,

    @ColumnInfo("DeliveryComplement")
    var deliveryComplement: String? = null,
)
