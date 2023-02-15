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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jessemanarim.entregaexpressa.R
import com.jessemanarim.entregaexpressa.common.presentation.components.cepFieldFilters
import com.jessemanarim.entregaexpressa.common.presentation.components.cepMaskTextWatcher
import com.jessemanarim.entregaexpressa.common.presentation.components.cpfFieldFilters
import com.jessemanarim.entregaexpressa.common.presentation.components.cpfMaskTextWatcher
import com.jessemanarim.entregaexpressa.common.presentation.getErrorOrNull
import com.jessemanarim.entregaexpressa.common.util.getCountryCode
import com.jessemanarim.entregaexpressa.databinding.FragmentDeliveryCreationBinding
import com.jessemanarim.entregaexpressa.feature_entrega.data.model.Delivery
import com.jessemanarim.entregaexpressa.feature_entrega.domain.validation.getCEPErrorOrNull
import com.jessemanarim.entregaexpressa.feature_entrega.domain.validation.getCPFErrorOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DeliveryCreationFragment : Fragment() {

    private val _viewModel: DeliveryCreationViewModel by viewModel()

    private var _binding: FragmentDeliveryCreationBinding? = null
    private val binding get() = _binding!!
    private var _currentDelivery = Delivery()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryCreationBinding.inflate(inflater, container, false)
        subscribeStateFlow()
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

    private fun subscribeStateFlow(){
        lifecycleScope.launchWhenStarted {
            _viewModel.uiState.collectLatest { uiState ->
                with(binding){
                    when(uiState){
                        is DeliveryCreationUiState.Ready -> {
                            loadingView.visibility = View.GONE
                        }
                        is DeliveryCreationUiState.Loading -> {
                            loadingView.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setupObservers(){
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

    @SuppressLint("SetTextI18n")
    private fun setupListeners(){
        with(binding){
            buttonSecond.setOnClickListener {
                if(_viewModel.validateFields(_currentDelivery)){
                    CoroutineScope(Dispatchers.IO).launch {
                        _viewModel.createDelivery(delivery = _currentDelivery)
                    }
                    findNavController().navigate(R.id.action_DeliveryCreationFragment_to_DeliveryListFragment)
                }
            }

            tiClientNameType.doAfterTextChanged {
                _currentDelivery.clientName = it.toString()
                _viewModel.validateClientName(_currentDelivery)
            }

            setupCPFField()

            setupCEPField()

            ddClientUfType.setOnItemClickListener { adapterView, _, i, _ ->
                _currentDelivery.deliveryUF = adapterView.getItemAtPosition(i).toString()
                _viewModel.validateDeliveryUF(_currentDelivery)
                ddClientCityType.setText("")
                CoroutineScope(Dispatchers.IO).launch {
                    _viewModel.fetchCities(adapterView.getItemAtPosition(i).toString())
                }
            }

            ddClientCityType.setOnItemClickListener { adapterView, _, i, _ ->
                _currentDelivery.deliveryCity = adapterView.getItemAtPosition(i).toString()
                _viewModel.validateDeliveryCity(_currentDelivery)
            }

            tiClientDistrictType.doAfterTextChanged {
                _currentDelivery.deliveryDistrict = it.toString()
                _viewModel.validateDeliveryDistrict(_currentDelivery)
            }

            tiClientStreetType.doAfterTextChanged {
                _currentDelivery.deliveryStreet = it.toString()
                _viewModel.validateDeliveryStreet(_currentDelivery)
            }

            tiClientAddressNumberType.doAfterTextChanged {
                _currentDelivery.deliveryNumber = it.toString()
                _viewModel.validateDeliveryNumber(_currentDelivery)
            }

            tiPackageQuantityType.doAfterTextChanged {
                _currentDelivery.deliveryPackages = it.toString()
                _viewModel.validateDeliveryPackages(_currentDelivery)
            }

            tiClientComplementType.doAfterTextChanged {
                _currentDelivery.deliveryComplement = it.toString()
            }

            dpLimitDateType.setOnClickListener {
                openCalendar()
            }
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
                _currentDelivery.deliveryLimitDate = "$dayOfMonth/${month+1}/$year"
                _viewModel.validateDeliveryLimitDate(_currentDelivery)
            }, currentYear, currentMonth, currentDay)

            datePickerDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCEPField(){
        with(binding){
            tiClientCepType.filters = cepFieldFilters()
            tiClientCepType.addTextChangedListener(cepMaskTextWatcher { cep ->
                _currentDelivery.deliveryCEP = cep
                tiClientCep.error = requireContext().getErrorOrNull(getCEPErrorOrNull(cep))
            })
        }
    }

    private fun setupCPFField(){
        with(binding){
            tiClientCpfType.filters = cpfFieldFilters()
            tiClientCpfType.addTextChangedListener(cpfMaskTextWatcher { cpf ->
                _currentDelivery.clientCPF = cpf
                tiClientCpf.error = requireContext().getErrorOrNull(getCPFErrorOrNull(cpf))
            })
        }
    }
}