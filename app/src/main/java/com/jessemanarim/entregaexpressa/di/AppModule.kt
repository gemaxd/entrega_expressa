package com.jessemanarim.entregaexpressa.di

import com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_create.DeliveryCreationViewModel
import com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_detail.DeliveryDetailViewModel
import com.jessemanarim.entregaexpressa.feature_entrega.presentation.delivery_list.DeliveryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { DeliveryListViewModel(get()) }
    viewModel { DeliveryDetailViewModel(get()) }
    viewModel { DeliveryCreationViewModel(get()) }
}