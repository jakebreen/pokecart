package uk.co.jakebreen.pokecart.ui.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.ShopItemBinding


class ShopAdapter internal constructor(private val viewModels: MutableList<ShopItemViewModel>,
                                       private val clickListener: ShopViewModelClickListener):
    RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding = DataBindingUtil.inflate<ShopItemBinding>(LayoutInflater.from(parent.context), R.layout.shop_item, parent, false)
        return ShopViewHolder(binding)
    }

    override fun getItemCount() = viewModels.size

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(viewModels.elementAt(position), clickListener)
    }

    class ShopViewHolder(private val binding: ShopItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ShopItemViewModel, clickListener: ShopViewModelClickListener) {
            binding.viewModel = viewModel
            binding.viewModelClick = clickListener
            binding.executePendingBindings()
            binding.viewModel?.apply {
                setAnimated()
            }
        }
    }

    fun updateAll(updated: List<ShopItemViewModel>) {
        val diffResult = DiffUtil.calculateDiff(
            ShopItemViewModelDiffCallback(
                viewModels,
                updated
            ), false)
        viewModels.clear()
        viewModels.addAll(updated)
        diffResult.dispatchUpdatesTo(this)
    }

    interface ShopViewModelClickListener {
        fun onShopViewModelClicked(id: Int)
    }

}