package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jessemanarim.entregaexpressa.R
import com.jessemanarim.entregaexpressa.databinding.FragmentDeliveryDetailBinding
import com.jessemanarim.entregaexpressa.databinding.FragmentDeliveryListBinding

private const val DELIVERY_ID = "deliveryId"
class DeliveryDetailFragment : Fragment() {
    private var deliveryId: Int? = null

    private var _binding: FragmentDeliveryDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeliveryDetailBinding.inflate(inflater, container, false)

        arguments?.let {
            deliveryId = it.getInt(DELIVERY_ID)
            Toast.makeText(context, "ID 'e $deliveryId", Toast.LENGTH_SHORT).show()
        }

        with(binding){
            groupClientInputs.visibility = View.GONE
        }

        return binding.root
    }

}