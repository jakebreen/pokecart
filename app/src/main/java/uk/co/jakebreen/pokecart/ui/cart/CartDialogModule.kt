package uk.co.jakebreen.pokecart.ui.cart

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val cartDialogModule = module {
    scope(named<CartDialogFragment>()) {
        viewModel { CartDialogViewModel(get(), get()) }
    }
}