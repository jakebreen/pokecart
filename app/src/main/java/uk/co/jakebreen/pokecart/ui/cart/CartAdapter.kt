package uk.co.jakebreen.pokecart.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.CartItemBinding

class CartAdapter(private val items: MutableList<CartItem>,
                  private val clickListener: CartViewModelClickListener
): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = DataBindingUtil.inflate<CartItemBinding>(LayoutInflater.from(parent.context), R.layout.cart_item, parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }

    class CartViewHolder(private val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem, clickListener: CartViewModelClickListener) {
            binding.viewModel = item
            binding.viewModelClick = clickListener
            binding.executePendingBindings()
        }
    }

    fun updateAll(updated: List<CartItem>) {
        val diffResult = DiffUtil.calculateDiff(
            CartItemDiffCallback(
                items,
                updated
            ), false)
        items.clear()
        items.addAll(updated)
        diffResult.dispatchUpdatesTo(this)
    }

    interface CartViewModelClickListener {
        fun onCartItemIncreaseClicked(id: Int)
        fun onCartItemDecreaseClicked(id: Int)
    }

}