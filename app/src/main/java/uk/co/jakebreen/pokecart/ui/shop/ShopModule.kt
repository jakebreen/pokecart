package uk.co.jakebreen.pokecart.ui.shop

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val shopModule = module {
    viewModel { ShopViewModel(get(), get()) }
}