package com.jessemanarim.entregaexpressa.feature_entrega.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Delivery")
data class Delivery(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("DeliveryId")
    var deliveryId: Int = 0,

    @ColumnInfo("PackageQuantity")
    var packageQuantity: Int,

    @ColumnInfo("ClientName")
    var clientName: String,

    @ColumnInfo("ClientCPF")
    var clientCPF: String
)
