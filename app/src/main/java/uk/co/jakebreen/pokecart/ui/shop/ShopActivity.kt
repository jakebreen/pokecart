package uk.co.jakebreen.pokecart.ui.shop

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.shop_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.ShopActivityBinding
import uk.co.jakebreen.pokecart.ui.cart.CartDialogFragment
import uk.co.jakebreen.pokecart.ui.filter.FilterDialogFragment


class ShopActivity : AppCompatActivity(), ShopAdapter.ShopViewModelClickListener {

    private val scope = getKoin().getOrCreateScope("shop_scope_id", named<ShopActivity>())
    private val shopViewModel : ShopViewModel by scope.inject()

    private lateinit var binding: ShopActivityBinding
    private val shopAdapter = ShopAdapter(mutableListOf(), this)
    private var shouldResetPosition = false
    private lateinit var tvCartItems: TextView

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

        shopViewModel.observeViewModels().observe(this, Observer {
            showViewModels(it)
        })

        shopViewModel.observeCartItemCount().observe(this, Observer { items ->
            if (::tvCartItems.isInitialized) {
                if (items == 0) {
                    tvCartItems.visibility = View.GONE
                } else {
                    tvCartItems.visibility = View.VISIBLE
                    tvCartItems.text = items.toString()
                }
            }
        })
    }

    private fun showViewModels(viewModels: List<ShopItemViewModel>) {
        shopAdapter.updateAll(viewModels).apply {
            if (shouldResetPosition)
                rvShop.layoutManager?.apply {
                    scrollToPosition(0)
                }.also {
                    shouldResetPosition = false
                }
        }
    }

    fun setShouldResetAdapter(reset: Boolean) {
        shouldResetPosition = reset
    }

    override fun onShopViewModelClicked(id: Int) {
        CoroutineScope(Dispatchers.IO).launch { shopViewModel.addPokemonToCart(id) }
    }

    private fun showFilterDialog() {
        val fragmentManager = supportFragmentManager
        FilterDialogFragment().show(fragmentManager, "filter_dialog")
    }

    private fun showCartDialog() {
        val fragmentManager = supportFragmentManager
        CartDialogFragment().show(fragmentManager, "cart_dialog")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        val menuItem = menu?.findItem(R.id.cart)
        tvCartItems = menuItem?.actionView?.findViewById<View>(R.id.cart_badge) as TextView
        val layout = menuItem.actionView?.findViewById(R.id.cart_layout) as FrameLayout
        layout.setOnClickListener { showCartDialog() }
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
