package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jessemanarim.entregaexpressa.R
import com.jessemanarim.entregaexpressa.databinding.FragmentFirstBinding
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeliveriesListFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    var _deliveries = emptyList<Delivery>()

    val _viewModel: DeliveryListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        setupObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            _viewModel.fetchAllDeliveries()
        }        
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers(){
        _viewModel.deliveries.observe(viewLifecycleOwner, Observer {
            val adapter = DeliveriesAdapter(it){ currentDelivery ->
                CoroutineScope(Dispatchers.IO).launch {
                    _viewModel.deleteDelivery(currentDelivery)
                }
            }
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
        })
    }

}