package uk.co.jakebreen.pokecart.ui.shop.item

import androidx.recyclerview.widget.DiffUtil

class ShopItemViewModelDiffCallback(private val oldItemList: List<ShopItemViewModel>,
                                    private val newItemList: List<ShopItemViewModel>): DiffUtil.Callback() {

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
                oldItem.health == newItem.health &&
                oldItem.attack == newItem.attack &&
                oldItem.defense == newItem.defense &&
                oldItem.speed == newItem.speed &&
                oldItem.typePrimary == newItem.typePrimary &&
                oldItem.typeSecondary == newItem.typeSecondary
    }
}