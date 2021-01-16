package uk.co.jakebreen.pokecart.ui.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.ShopItemBinding


class ShopAdapter internal constructor(private val items: MutableList<ShopItem>,
                                       private val clickListener: ShopViewModelClickListener):
    RecyclerView.Adapter<ShopAdapter.ShopItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val binding = DataBindingUtil.inflate<ShopItemBinding>(LayoutInflater.from(parent.context), R.layout.shop_item, parent, false)
        return ShopItemViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        holder.bind(items.elementAt(position), clickListener)
    }

    class ShopItemViewHolder(private val binding: ShopItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ShopItem, clickListener: ShopViewModelClickListener) {
            binding.viewModel = viewModel
            binding.viewModelClick = clickListener
            binding.executePendingBindings()
            binding.viewModel?.apply {
                setAnimated()
            }
        }
    }

    fun updateAll(updated: List<ShopItem>) {
        val diffResult = DiffUtil.calculateDiff(
            ShopItemDiffCallback(
                items,
                updated
            ), false)
        items.clear()
        items.addAll(updated)
        diffResult.dispatchUpdatesTo(this)
    }

    interface ShopViewModelClickListener {
        fun onShopViewModelClicked(id: Int)
    }

}