package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_create

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.jessemanarim.entregaexpressa.R
import com.jessemanarim.entregaexpressa.common.util.getCountryCode
import com.jessemanarim.entregaexpressa.databinding.FragmentDeliveryCreationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DeliveryCreationFragment : Fragment() {

    private var _binding: FragmentDeliveryCreationBinding? = null
    private val binding get() = _binding!!
    private val _viewModel: DeliveryCreationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        setupObservers()
        setupListeners()
    }

    private fun setupComponents(){
        with(binding){
            val items = getCountryCode(requireContext()).map {it.code}
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            (ddClientUf.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }

    private fun setupObservers(){
        _viewModel.citiesList.observe(viewLifecycleOwner) { citiesList ->
            with(binding){
                ddClientCity.isEnabled = citiesList.isNotEmpty()
                val cityAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.list_item,
                    citiesList.map { cityResponse -> cityResponse.name }
                )
                (ddClientCity.editText as? AutoCompleteTextView)?.setAdapter(cityAdapter)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupListeners(){
        with(binding){
            buttonSecond.setOnClickListener {
                validateErrors()
//                CoroutineScope(Dispatchers.IO).launch {
//                    _viewModel.createDelivery(
//                        Delivery(
//                            packageQuantity = 10,
//                            clientName = "Jessé",
//                            clientCPF = "000000000"
//                        )
//                    )
//                }
//                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }

            ddClientUfType.setOnItemClickListener { adapterView, _, i, _ ->
                ddClientCityType.setText("")
                CoroutineScope(Dispatchers.IO).launch {
                    _viewModel.fetchCities(adapterView.getItemAtPosition(i).toString())
                }
            }

            dpLimitDateType.setOnClickListener {
                val c = Calendar.getInstance()
                val currentYear = c.get(Calendar.YEAR)
                val currentMonth = c.get(Calendar.MONTH)
                val currentDay = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                    dpLimitDateType.setText("$dayOfMonth/${month+1}/$year")
                }, currentYear, currentMonth, currentDay)

                datePickerDialog.show()
            }
        }
    }

    private fun validateErrors(){
        with(binding){
            if (tiClientNameType.text.isNullOrBlank() || tiClientNameType.text.isNullOrEmpty())
                tiClientName.error = getString(R.string.error_empty_client_name)
            else
                tiClientName.error = null

            if (tiClientCpfType.text.isNullOrBlank() || tiClientCpfType.text.isNullOrEmpty())
                tiClientCpf.error = getString(R.string.error_empty_cpf)
            else
                tiClientCpf.error = null

            if (tiClientCepType.text.isNullOrBlank() || tiClientCepType.text.isNullOrEmpty())
                tiClientCep.error = getString(R.string.error_empty_cep)
            else
                tiClientCep.error = null

            if (ddClientUfType.text.isNullOrBlank() || ddClientUfType.text.isNullOrEmpty())
                ddClientUf.error = getString(R.string.error_empty_uf)
            else
                ddClientUf.error = null

            if (ddClientCityType.text.isNullOrBlank() || ddClientCityType.text.isNullOrEmpty())
                ddClientCity.error = getString(R.string.error_empty_city)
            else
                ddClientCity.error = null

            if (tiClientDistrictType.text.isNullOrBlank() || tiClientDistrictType.text.isNullOrEmpty())
                tiClientDistrict.error = getString(R.string.error_empty_district)
            else
                tiClientDistrict.error = null

            if (tiClientStreetType.text.isNullOrBlank() || tiClientStreetType.text.isNullOrEmpty())
                tiClientStreet.error = getString(R.string.error_empty_street)
            else
                tiClientStreet.error = null

            if (tiClientAddressNumberType.text.isNullOrBlank() || tiClientAddressNumberType.text.isNullOrEmpty())
                tiClientAddressNumber.error = getString(R.string.error_empty_number)
            else
                tiClientAddressNumber.error = null

            if (tiPackageQuantityType.text.isNullOrBlank() || tiPackageQuantityType.text.isNullOrEmpty())
                tiPackageQuantity.error = getString(R.string.error_empty_packages)
            else
                tiPackageQuantity.error = null

            if (dpLimitDateType.text.isNullOrBlank() || dpLimitDateType.text.isNullOrEmpty())
                dpLimitDate.error = getString(R.string.error_empty_limit_date)
            else
                dpLimitDate.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}