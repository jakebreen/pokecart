package uk.co.jakebreen.pokecart.ui.filter

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val filterDialogModule = module {
    scope(named<FilterDialogFragment>()) {
        viewModel { FilterDialogViewModel(get()) }
    }
}