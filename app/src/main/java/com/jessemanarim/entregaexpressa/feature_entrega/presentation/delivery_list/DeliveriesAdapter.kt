package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jessemanarim.entregaexpressa.R
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery

class DeliveriesAdapter (
    private val mDeliveries: List<Delivery>,
    private val deleteClick: (delivery: Delivery) -> Unit
    ) : RecyclerView.Adapter<DeliveriesAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deliveryIdValue = itemView.findViewById<TextView>(R.id.tv_delivery_id)
        val clientName = itemView.findViewById<TextView>(R.id.tv_client_name)
        val deliveryQuantity = itemView.findViewById<TextView>(R.id.tv_quantity)
        val limitDate = itemView.findViewById<TextView>(R.id.tv_limit_date)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveriesAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.delivery_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: DeliveriesAdapter.ViewHolder, position: Int) {
        val delivery: Delivery = mDeliveries.get(position)

        val deliveryId = viewHolder.deliveryIdValue
        val clientName = viewHolder.clientName
        val quantity = viewHolder.deliveryQuantity
        val limitDate = viewHolder.limitDate
        val btnDelete = viewHolder.deleteButton

        deliveryId.setText(delivery.deliveryId.toString())
        clientName.setText(delivery.clientName)
        quantity.setText(delivery.packageQuantity.toString())
        limitDate.setText("10/10/2023")
        btnDelete.setOnClickListener {
           deleteClick(delivery)
        }
    }

    override fun getItemCount(): Int {
        return mDeliveries.size
    }
}