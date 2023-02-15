package com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_detail

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.jessemanarim.entregaexpressa.R
import com.jessemanarim.entregaexpressa.common.presentation.components.cepFieldFilters
import com.jessemanarim.entregaexpressa.common.presentation.components.cepMaskTextWatcher
import com.jessemanarim.entregaexpressa.common.presentation.components.cpfFieldFilters
import com.jessemanarim.entregaexpressa.common.presentation.components.cpfMaskTextWatcher
import com.jessemanarim.entregaexpressa.common.presentation.getErrorOrNull
import com.jessemanarim.entregaexpressa.common.util.getCountryCode
import com.jessemanarim.entregaexpressa.databinding.FragmentDeliveryDetailBinding
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import com.jessemanarim.entregaexpressa.feature_entrega.domain.validation.getCEPErrorOrNull
import com.jessemanarim.entregaexpressa.feature_entrega.domain.validation.getCPFErrorOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

private const val DELIVERY_ID = "deliveryId"
class DeliveryDetailFragment : Fragment() {

    private val _viewModel: DeliveryDetailViewModel by viewModel()

    private var _binding: FragmentDeliveryDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var _localDelivery: Delivery
    private lateinit var _referenceDelivery: Delivery

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryDetailBinding.inflate(inflater, container, false)

        subscribeObservers()
        fetchArguments()
        setupStateFlow()
        setupListeners()

        return binding.root
    }

    private fun setupComponents(){
        with(binding){
            val items = getCountryCode(requireContext()).map {it.code}
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            (ddClientUf.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }

    private fun setupStateFlow(){
        lifecycleScope.launchWhenStarted {
            _viewModel.uiState.collect {
                when(it){
                    is DeliveryDetailUiState.Loading -> {
                        isLoading()
                    }
                    is DeliveryDetailUiState.NotEditing -> {
                        isLoading(isLoading = false)
                        isEditing(isEnable = false)
                    }
                    is DeliveryDetailUiState.Editing -> {
                        isEditing(isEnable = true)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupListeners(){
        with(binding){
            btnDeliveryEdit.setOnClickListener {
                _viewModel.toggleFieldsInteraction(true)
                setupComponents()
            }
            btnDeliveryCancel.setOnClickListener {
                _viewModel.toggleFieldsInteraction(false)
                populateFields(_referenceDelivery)
            }
            btnDeliveryUpdate.setOnClickListener {
                if(_viewModel.validateFields(_localDelivery)){
                    _viewModel.updateDelivery(_localDelivery)
                    findNavController().navigate(R.id.action_DeliveryDetailFragment_to_DeliveryListFragment)
                }
            }

            tiClientCpfType.doAfterTextChanged {
                _localDelivery.clientCPF = it.toString()
            }

            tiClientNameType.doAfterTextChanged {
                _localDelivery.clientName = it.toString()
                _viewModel.validateClientName(_localDelivery)
            }

            setupCEPField()
            setupCPFField()

            ddClientUfType.setOnItemClickListener { adapterView, _, i, _ ->
                _localDelivery.deliveryUF = adapterView.getItemAtPosition(i).toString()
                _viewModel.validateDeliveryUF(_localDelivery)
                ddClientCityType.setText("")
                CoroutineScope(Dispatchers.IO).launch {
                    _viewModel.fetchCities(adapterView.getItemAtPosition(i).toString())
                }
            }

            ddClientCityType.setOnItemClickListener { adapterView, _, i, _ ->
                _localDelivery.deliveryCity = adapterView.getItemAtPosition(i).toString()
                _viewModel.validateDeliveryCity(_localDelivery)
            }

            tiClientDistrictType.doAfterTextChanged {
                _localDelivery.deliveryDistrict = it.toString()
                _viewModel.validateDeliveryDistrict(_localDelivery)
            }

            tiClientStreetType.doAfterTextChanged {
                _localDelivery.deliveryStreet = it.toString()
                _viewModel.validateDeliveryStreet(_localDelivery)
            }

            tiClientAddressNumberType.doAfterTextChanged {
                _localDelivery.deliveryNumber = it.toString()
                _viewModel.validateDeliveryNumber(_localDelivery)
            }

            tiPackageQuantityType.doAfterTextChanged {
                _localDelivery.deliveryPackages = it.toString()
                _viewModel.validateDeliveryPackages(_localDelivery)
            }

            tiClientComplementType.doAfterTextChanged {
                _localDelivery.deliveryComplement = it.toString()
            }

            dpLimitDateType.setOnClickListener {
                openCalendar()
            }
        }
    }

    private fun fetchDelivery(deliveryId: Int){
        _viewModel.fetchDeliveryInfo(deliveryId = deliveryId)
    }

    private fun subscribeObservers(){
        _viewModel.fetchedDelivery.observe(viewLifecycleOwner){
            _localDelivery = it
            _referenceDelivery = it.copy()
            populateFields(it)
        }

        _viewModel.citiesResponse.observe(viewLifecycleOwner) { cityResponse ->
            with(binding){
                if(cityResponse == null){
                    Snackbar.make(clGeneralData, "Houve um problema ao buscar as cidades", Snackbar.LENGTH_SHORT).show()
                } else {
                    ddClientCity.isEnabled = cityResponse.cities.isNotEmpty()
                    val cityAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.list_item,
                        cityResponse.cities.map { cityResponse -> cityResponse.name }
                    )
                    (ddClientCity.editText as? AutoCompleteTextView)?.setAdapter(cityAdapter)
                }
            }
        }
        validateErrors()
    }

    private fun fetchArguments(){
        arguments?.let {
            val deliveryId = it.getInt(DELIVERY_ID)
            fetchDelivery(deliveryId = deliveryId)
        }
    }

    private fun populateFields(delivery: Delivery){
        with(binding){
            tiClientNameType.setText(delivery.clientName)
            tiClientCpfType.setText(delivery.clientCPF)
            tiClientCepType.setText(delivery.deliveryCEP)
            ddClientUfType.setText(delivery.deliveryUF)
            ddClientCityType.setText(delivery.deliveryCity)
            tiClientDistrictType.setText(delivery.deliveryDistrict)
            tiClientStreetType.setText(delivery.deliveryStreet)
            tiClientAddressNumberType.setText(delivery.deliveryNumber)
            tiPackageQuantityType.setText(delivery.deliveryPackages)
            dpLimitDateType.setText(delivery.deliveryLimitDate)
            tiClientComplementType.setText(delivery.deliveryComplement)
        }
    }

    private fun isLoading(isLoading: Boolean = true){
        with(binding){
            if(isLoading){
                loadingView.isVisible = true
                clGeneralContent.isInvisible = true
            } else {
                loadingView.isVisible = false
                clGeneralContent.isInvisible = false
            }
        }
    }

    private fun isEditing(isEnable: Boolean){
        with(binding){
            for (i in 0 until containerBasicClientData.childCount){
                val view = containerBasicClientData.getChildAt(i)
                if (view is TextInputLayout)  view.isEnabled = isEnable
            }

            for (i in 0 until containerAddressData.childCount){
                val view = containerAddressData.getChildAt(i)
                if (view is TextInputLayout)  view.isEnabled = isEnable
            }

            for (i in 0 until containerDeliveryData.childCount){
                val view = containerDeliveryData.getChildAt(i)
                if (view is TextInputLayout)  view.isEnabled = isEnable
            }

            btnDeliveryEdit.isVisible = !isEnable
            btnDeliveryUpdate.isVisible = isEnable
            btnDeliveryCancel.isVisible = isEnable
        }
    }

    private fun validateErrors(){
        _viewModel.isClientNameValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                tiClientName.error = requireContext().getErrorOrNull(isValid)
            }
        }
        _viewModel.isClientCPFValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                tiClientCpf.error = requireContext().getErrorOrNull(isValid)
            }
        }
        _viewModel.isDeliveryCEPValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                tiClientCep.error = requireContext().getErrorOrNull(isValid)
            }
        }
        _viewModel.isDeliveryUFValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                ddClientUf.error = requireContext().getErrorOrNull(isValid)
            }
        }
        _viewModel.isDeliveryCityValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                ddClientCity.error = requireContext().getErrorOrNull(isValid)
            }
        }
        _viewModel.isDeliveryDistrictValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                tiClientDistrict.error = requireContext().getErrorOrNull(isValid)
            }
        }
        _viewModel.isDeliveryStreetValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                tiClientStreet.error = requireContext().getErrorOrNull(isValid)
            }
        }
        _viewModel.isDeliveryNumberValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                tiClientAddressNumber.error = requireContext().getErrorOrNull(isValid)
            }
        }
        _viewModel.isDeliveryPackagesValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                tiPackageQuantity.error = requireContext().getErrorOrNull(isValid)
            }
        }
        _viewModel.isDeliveryLimitDateValid.observe(viewLifecycleOwner) { isValid ->
            with(binding){
                dpLimitDate.error = requireContext().getErrorOrNull(isValid)
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
                _localDelivery.deliveryLimitDate = "$dayOfMonth/${month+1}/$year"
                _viewModel.validateDeliveryLimitDate(_localDelivery)
            }, currentYear, currentMonth, currentDay)

            datePickerDialog.show()
        }
    }

    private fun setupCEPField(){
        with(binding){
            tiClientCepType.filters = cepFieldFilters()
            tiClientCepType.addTextChangedListener(cepMaskTextWatcher { cep ->
                _localDelivery.deliveryCEP = cep
                tiClientCep.error = requireContext().getErrorOrNull(getCEPErrorOrNull(cep))
            })
        }
    }

    private fun setupCPFField(){
        with(binding){
            tiClientCpfType.filters = cpfFieldFilters()
            tiClientCpfType.addTextChangedListener(cpfMaskTextWatcher { cpf ->
                _localDelivery.clientCPF = cpf
                tiClientCpf.error = requireContext().getErrorOrNull(getCPFErrorOrNull(cpf))
            })
        }
    }
}