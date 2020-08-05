package uk.co.jakebreen.pokecart.ui.shop

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.shop_activity.*
import org.koin.android.ext.android.inject
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.ShopActivityBindingImpl
import uk.co.jakebreen.pokecart.ui.filter.FilterDialogFragment


class ShopActivity : AppCompatActivity(), ShopAdapter.ShopViewModelClickListener {

    private val presenter: ShopPresenter by inject()

    private lateinit var binding: ShopActivityBindingImpl
    private val shopAdapter = ShopAdapter(mutableListOf(), this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.shop_activity)
        binding.lifecycleOwner = this
        binding.presenter = presenter

        rvShop.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shopAdapter
        }

        presenter.attach(this)
    }

    fun showPokemon(viewModels: List<ShopViewModel>) {
        shopAdapter.updateAll(viewModels)
        rvShop.layoutManager!!.scrollToPosition(0)
    }

    override fun onShopViewModelClicked(view: View, viewModel: ShopViewModel) {

    }

    fun showFilters() {
        val fragmentManager = supportFragmentManager
        FilterDialogFragment().show(fragmentManager, "filter_dialog")
    }

}
