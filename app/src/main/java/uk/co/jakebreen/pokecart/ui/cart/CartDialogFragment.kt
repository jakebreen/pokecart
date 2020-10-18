package uk.co.jakebreen.pokecart.ui.cart

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.cart_dialog_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.CartDialogFragmentBinding

class CartDialogFragment: DialogFragment(), CartAdapter.CartViewModelClickListener {

    private val scope = getKoin().getOrCreateScope("cart_scope_id", named<CartDialogFragment>())
    private val cartDialogViewModel: CartDialogViewModel by scope.inject()

    private lateinit var binding: CartDialogFragmentBinding
    private val cartAdapter = CartAdapter(mutableListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            setBackgroundDrawableResource(R.drawable.dialog_background)
        }
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.cart_dialog_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = cartDialogViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }

        cartDialogViewModel.observeUpdates().observe(viewLifecycleOwner, Observer {
            it.toList().also { showViewModels(it) }
        })
    }

    private fun showViewModels(items: List<CartItem>) {
        cartAdapter.updateAll(items)
    }

    override fun onCartItemIncreaseClicked(id: Int) {
        CoroutineScope(Dispatchers.IO).launch { cartDialogViewModel.addCartItem(id) }
    }

    override fun onCartItemDecreaseClicked(id: Int) {
        CoroutineScope(Dispatchers.IO).launch { cartDialogViewModel.removeCartItem(id) }
    }
}