package uk.co.jakebreen.pokecart.ui.shop

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemResources

val shopModule = module {
    scope(named<ShopActivity>()) {
        factory { resources() }
        viewModel { ShopViewModel(get(), get(), get()) }
    }
}

fun resources() = ShopItemResources()