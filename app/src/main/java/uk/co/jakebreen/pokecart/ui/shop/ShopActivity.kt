package uk.co.jakebreen.pokecart.ui.shop

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.shop_activity.*
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.ShopActivityBindingImpl
import uk.co.jakebreen.pokecart.ui.filter.FilterDialogFragment
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemViewModel


class ShopActivity : AppCompatActivity(), ShopAdapter.ShopViewModelClickListener {

    private val scope = getKoin().getOrCreateScope("shop_scope_id", named<ShopActivity>())

    private val shopViewModel : ShopViewModel by scope.inject()

    private lateinit var binding: ShopActivityBindingImpl
    private val shopAdapter = ShopAdapter(mutableListOf(), this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.shop_activity)
        binding.lifecycleOwner = this
        binding.viewModel = shopViewModel

        setSupportActionBar(binding.root.findViewById(R.id.toolbar_include))

        rvShop.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shopAdapter
        }

        shopViewModel.observeViewModels().observeForever{
            showViewModels(it)
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> {
                showFilterDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
