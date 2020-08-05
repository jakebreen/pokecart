package uk.co.jakebreen.pokecart.ui.filter

import org.koin.dsl.module
import uk.co.jakebreen.pokecart.model.filter.Filter

val filterModule = module {
    single { getFilter() }
    factory { getDialogPresenter(get()) }
}

fun getFilter() = Filter()

fun getDialogPresenter(filter: Filter) = FilterDialogPresenter(filter)