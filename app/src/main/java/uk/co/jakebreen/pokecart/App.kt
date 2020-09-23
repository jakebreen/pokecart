package uk.co.jakebreen.pokecart

import android.app.Application
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import uk.co.jakebreen.pokecart.api.apiModule
import uk.co.jakebreen.pokecart.persistence.DatabaseManager
import uk.co.jakebreen.pokecart.persistence.databaseModule
import uk.co.jakebreen.pokecart.ui.cart.cartDialogModule
import uk.co.jakebreen.pokecart.ui.filter.filterDialogModule
import uk.co.jakebreen.pokecart.ui.shop.shopActivityModule

class App: Application() {

    private val databaseManager: DatabaseManager by inject()

    private val exceptionHandler = CoroutineExceptionHandler {_, exception ->
        databaseManager.clearTables()
        Timber.e(exception)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(applicationContext)
            modules(listOf(
                databaseModule,
                apiModule,
                pokemonRepositoryModule,
                shopActivityModule,
                filterRepositoryModule,
                filterDialogModule,
                cartRepositoryModule,
                cartDialogModule))
        }

        initialiseDatabase()
    }

    private fun initialiseDatabase() {
        CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
            databaseManager.onStart()
        }
    }

}