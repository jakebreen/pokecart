package uk.co.jakebreen.pokecart.ui.filter

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uk.co.jakebreen.pokecart.model.filter.Filter

val filterModule = module {
    single { getFilter() }
    viewModel { FilterDialogViewModel(get()) }
}

fun getFilter() = Filter()