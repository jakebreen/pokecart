package uk.co.jakebreen.pokecart.ui.shop

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.shop_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.ShopActivityBindingImpl
import uk.co.jakebreen.pokecart.ui.filter.FilterDialogFragment
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemViewModel


class ShopActivity : AppCompatActivity(), ShopAdapter.ShopViewModelClickListener {

    private val shopViewModel : ShopViewModel by viewModel()

    private lateinit var binding: ShopActivityBindingImpl
    private val shopAdapter = ShopAdapter(mutableListOf(), this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.shop_activity)
        binding.lifecycleOwner = this
        binding.viewModel = shopViewModel
        ivFilterList.setOnClickListener { showFilterDialog() }

        rvShop.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shopAdapter
        }

        shopViewModel.items.observeForever{ showViewModels(it) }
    }

    private fun showViewModels(viewModels: List<ShopItemViewModel>) {
        shopAdapter.updateAll(viewModels)
        rvShop.layoutManager!!.scrollToPosition(0)
    }

    override fun onShopViewModelClicked(view: View, viewModel: ShopItemViewModel) {

    }

    private fun showFilterDialog() {
        val fragmentManager = supportFragmentManager
        FilterDialogFragment().show(fragmentManager, "filter_dialog")
    }

}
