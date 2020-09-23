package uk.co.jakebreen.pokecart.ui.cart

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import uk.co.jakebreen.pokecart.R

class CartRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): RecyclerView(context, attrs, defStyle) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val maxHeight = MeasureSpec.makeMeasureSpec(context.resources.getDimension(R.dimen.cart_max_height).toInt(), MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, maxHeight)
    }
}