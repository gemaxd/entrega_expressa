package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jessemanarim.entregaexpressa.R
import com.jessemanarim.entregaexpressa.databinding.FragmentDeliveryListBinding
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeliveriesListFragment : Fragment() {

    private var _binding: FragmentDeliveryListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DeliveriesAdapter
    private var _deliveries = emptyList<Delivery>()

    private val _viewModel: DeliveryListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryListBinding.inflate(inflater, container, false)

        subscribeStateFlow()

        return binding.root
    }

    private fun prepareAdapter(){
        adapter = DeliveriesAdapter(
            _deliveries,
            deleteClick = { currentDelivery -> confirmRegisterExclusion(currentDelivery) },
            openDetailClick = { currentDelivery -> navigateToDetails(currentDelivery) }
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun subscribeStateFlow(){
        lifecycleScope.launchWhenStarted {
            _viewModel.uiState.collectLatest { uiState ->
                when(uiState){
                    is DeliveryListUiState.Success -> {
                        binding.containerFeedback.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        loadDeliveryList(uiState.list)
                    }
                    is DeliveryListUiState.Empty -> {
                        binding.containerFeedback.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_DeliveryListFragment_to_DeliveryCreationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadDeliveryList(list: List<Delivery>){
        _deliveries = list
        prepareAdapter()
    }

    fun navigateToDetails(currentDelivery: Delivery){
        val bundle = Bundle()
        bundle.putInt("deliveryId", currentDelivery.deliveryId)
        findNavController().navigate(R.id.action_DeliveryListFragment_to_DeliveryDetailFragment, bundle)
    }

    private fun confirmRegisterExclusion(currentDelivery: Delivery){
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.confirm_exclusion_message))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    _viewModel.deleteDelivery(currentDelivery)
                }
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
}