package uk.co.jakebreen.pokecart.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.CartItemBinding

class CartAdapter(private val viewModels: MutableList<CartItemViewModel>,
                  private val clickListener: CartViewModelClickListener
): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = DataBindingUtil.inflate<CartItemBinding>(LayoutInflater.from(parent.context), R.layout.cart_item, parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount() = viewModels.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(viewModels[position], clickListener)
    }

    class CartViewHolder(private val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: CartItemViewModel, clickListener: CartViewModelClickListener) {
            binding.viewModel = viewModel
            binding.viewModelClick = clickListener
            binding.executePendingBindings()
        }
    }

    fun updateAll(updated: List<CartItemViewModel>) {
        val diffResult = DiffUtil.calculateDiff(
            CartItemViewModelDiffCallback(
                viewModels,
                updated
            ), false)
        viewModels.clear()
        viewModels.addAll(updated)
        diffResult.dispatchUpdatesTo(this)
    }

    interface CartViewModelClickListener {
        fun onCartItemIncreaseClicked(id: Int)
        fun onCartItemDecreaseClicked(id: Int)
    }

}