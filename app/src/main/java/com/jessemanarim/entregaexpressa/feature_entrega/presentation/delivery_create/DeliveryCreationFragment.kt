package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_create

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jessemanarim.entregaexpressa.R
import com.jessemanarim.entregaexpressa.common.util.getCountryCode
import com.jessemanarim.entregaexpressa.databinding.FragmentDeliveryCreationBinding
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DeliveryCreationFragment : Fragment() {

    private val _viewModel: DeliveryCreationViewModel by viewModel()

    private var _binding: FragmentDeliveryCreationBinding? = null
    private val binding get() = _binding!!
    private var currentDelivery = Delivery()

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

        validateErrors()
    }

    @SuppressLint("SetTextI18n")
    private fun setupListeners(){
        with(binding){
            buttonSecond.setOnClickListener {
                if(_viewModel.validateFields(currentDelivery)){
                    CoroutineScope(Dispatchers.IO).launch {
                        _viewModel.createDelivery(delivery = currentDelivery)
                    }
                    findNavController().navigate(R.id.action_DeliveryCreationFragment_to_DeliveryListFragment)
                }
            }

            tiClientNameType.doAfterTextChanged {
                currentDelivery.clientName = it.toString()
                _viewModel.validateClientName(currentDelivery)
            }

            tiClientCpfType.doAfterTextChanged {
                currentDelivery.clientCPF = it.toString()
                _viewModel.validateClientCPF(currentDelivery)
            }

            tiClientCepType.doAfterTextChanged {
                currentDelivery.deliveryCEP = it.toString()
                _viewModel.validateDeliveryCEP(currentDelivery)
            }

            ddClientUfType.setOnItemClickListener { adapterView, _, i, _ ->
                currentDelivery.deliveryUF = adapterView.getItemAtPosition(i).toString()
                _viewModel.validateDeliveryUF(currentDelivery)
                ddClientCityType.setText("")
                CoroutineScope(Dispatchers.IO).launch {
                    _viewModel.fetchCities(adapterView.getItemAtPosition(i).toString())
                }
            }

            ddClientCityType.setOnItemClickListener { adapterView, _, i, _ ->
                currentDelivery.deliveryCity = adapterView.getItemAtPosition(i).toString()
                _viewModel.validateDeliveryCity(currentDelivery)
            }

            tiClientDistrictType.doAfterTextChanged {
                currentDelivery.deliveryDistrict = it.toString()
                _viewModel.validateDeliveryDistrict(currentDelivery)
            }

            tiClientStreetType.doAfterTextChanged {
                currentDelivery.deliveryStreet = it.toString()
                _viewModel.validateDeliveryStreet(currentDelivery)
            }

            tiClientAddressNumberType.doAfterTextChanged {
                currentDelivery.deliveryNumber = it.toString()
                _viewModel.validateDeliveryNumber(currentDelivery)
            }

            tiPackageQuantityType.doAfterTextChanged {
                currentDelivery.deliveryPackages = it.toString()
                _viewModel.validateDeliveryPackages(currentDelivery)
            }

            dpLimitDateType.setOnClickListener {
                openCalendar()
            }
        }
    }

    private fun validateErrors(){
        _viewModel.isClientNameValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                if(isValid) tiClientName.error = null else tiClientName.error = getString(R.string.error_empty_client_name)
            }
        }
        _viewModel.isClientCPFValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                if(isValid) tiClientCpf.error = null else tiClientCpf.error = getString(R.string.error_empty_cpf)
            }
        }
        _viewModel.isDeliveryCEPValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                if(isValid) tiClientCep.error = null else tiClientCep.error = getString(R.string.error_empty_cep)
            }
        }
        _viewModel.isDeliveryUFValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                if(isValid) ddClientUf.error = null else ddClientUf.error = getString(R.string.error_empty_uf)
            }
        }
        _viewModel.isDeliveryCityValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                if(isValid) ddClientCity.error = null else ddClientCity.error = getString(R.string.error_empty_city)
            }
        }
        _viewModel.isDeliveryDistrictValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                if(isValid) tiClientDistrict.error = null else tiClientDistrict.error = getString(R.string.error_empty_district)
            }
        }
        _viewModel.isDeliveryStreetValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                if(isValid) tiClientStreet.error = null else tiClientStreet.error = getString(R.string.error_empty_street)
            }
        }
        _viewModel.isDeliveryNumberValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                if(isValid) tiClientAddressNumber.error = null else tiClientAddressNumber.error = getString(R.string.error_empty_number)
            }
        }
        _viewModel.isDeliveryPackagesValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                if(isValid) tiPackageQuantity.error = null else tiPackageQuantity.error = getString(R.string.error_empty_packages)
            }
        }
        _viewModel.isDeliveryLimitDateValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                if(isValid) dpLimitDate.error = null else dpLimitDate.error = getString(R.string.error_empty_limit_date)
            }
        }
    }

    private fun openCalendar(){
        with(binding){
            val c = Calendar.getInstance()
            val currentYear = c.get(Calendar.YEAR)
            val currentMonth = c.get(Calendar.MONTH)
            val currentDay = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                dpLimitDateType.setText(getString(R.string.date_string, dayOfMonth, month+1, year))
                currentDelivery.deliveryLimitDate = "$dayOfMonth/${month+1}/$year"
                _viewModel.validateDeliveryLimitDate(currentDelivery)
            }, currentYear, currentMonth, currentDay)

            datePickerDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}