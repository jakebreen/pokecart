package uk.co.jakebreen.pokecart

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import uk.co.jakebreen.pokecart.api.apiModule
import uk.co.jakebreen.pokecart.model.pokemon.repositoryModule
import uk.co.jakebreen.pokecart.persistence.DatabaseManager
import uk.co.jakebreen.pokecart.persistence.databaseModule
import uk.co.jakebreen.pokecart.ui.filter.filterDialogModule
import uk.co.jakebreen.pokecart.ui.shop.shopModule

class App: Application() {

    private val databaseManager: DatabaseManager by inject()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(applicationContext)
            modules(listOf(
                databaseModule,
                apiModule,
                repositoryModule,
                shopModule,
                filterModule,
                filterDialogModule
            ))
        }

        databaseManager.onStart()
    }

}