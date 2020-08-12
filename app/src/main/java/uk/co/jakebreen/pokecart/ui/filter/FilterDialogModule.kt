package uk.co.jakebreen.pokecart.ui.filter

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val filterDialogModule = module {
    viewModel { FilterDialogViewModel(get()) }
}