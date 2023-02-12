package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jessemanarim.entregaexpressa.R
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery

class DeliveriesAdapter (
    private val mDeliveries: List<Delivery>,
    private val deleteClick: (delivery: Delivery) -> Unit,
    private val openDetailClick: (delivery: Delivery) -> Unit
    ) : RecyclerView.Adapter<DeliveriesAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deliveryIdValue: TextView = itemView.findViewById(R.id.tv_delivery_id)
        val clientName: TextView = itemView.findViewById(R.id.tv_client_name)
        val deliveryQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val limitDate: TextView = itemView.findViewById(R.id.tv_limit_date)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btn_delete)
        val container: ConstraintLayout = itemView.findViewById(R.id.container_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveriesAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.delivery_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: DeliveriesAdapter.ViewHolder, position: Int) {
        val delivery: Delivery = mDeliveries[position]

        val deliveryId = viewHolder.deliveryIdValue
        val clientName = viewHolder.clientName
        val quantity = viewHolder.deliveryQuantity
        val limitDate = viewHolder.limitDate
        val btnDelete = viewHolder.deleteButton
        val container = viewHolder.container

        deliveryId.text = delivery.deliveryId.toString()
        clientName.text = delivery.clientName
        quantity.text = delivery.deliveryPackages
        limitDate.text = delivery.deliveryLimitDate
        btnDelete.setOnClickListener {
            deleteClick(delivery)
            removeItem(position)
        }
        container.setOnClickListener {
            openDetailClick(delivery)
        }
    }

    fun removeItem(position: Int) {
        mDeliveries.drop(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return mDeliveries.size
    }
}