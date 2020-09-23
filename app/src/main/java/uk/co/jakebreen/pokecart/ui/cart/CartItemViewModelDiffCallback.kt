package uk.co.jakebreen.pokecart.ui.cart

import androidx.recyclerview.widget.DiffUtil

class CartItemViewModelDiffCallback(private val oldItemList: List<CartItemViewModel>,
                                    private val newItemList: List<CartItemViewModel>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItemList.size
    }

    override fun getNewListSize(): Int {
        return newItemList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemList[oldItemPosition].id == newItemList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemList[oldItemPosition]
        val newItem = newItemList[newItemPosition]

        return oldItem.name == newItem.name &&
                oldItem.count == newItem.count &&
                oldItem.price == newItem.price
    }
}
