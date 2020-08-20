package uk.co.jakebreen.pokecart.ui.shop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.ShopItemBindingImpl
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemViewModel
import uk.co.jakebreen.pokecart.ui.shop.item.ShopItemViewModelDiffCallback


class ShopAdapter internal constructor(private val viewModels: MutableList<ShopItemViewModel>,
                                       private val clickListener: ShopViewModelClickListener,
                                       private val context: Context):
    RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding = DataBindingUtil.inflate<ShopItemBindingImpl>(LayoutInflater.from(parent.context), R.layout.shop_item, parent, false)
        return ShopViewHolder(binding)
    }

    override fun getItemCount() = viewModels.size

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(viewModels.elementAt(position))
    }

    inner class ShopViewHolder(private val binding: ShopItemBindingImpl): RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ShopItemViewModel) {
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
        fun onShopViewModelClicked(view: View, viewModel: ShopItemViewModel)
    }

}