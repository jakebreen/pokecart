package uk.co.jakebreen.pokecart.ui.filter

import android.R.attr
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.model.type.Type


class FilterChip(ctx: Context, attr: AttributeSet? = null) : Chip(ctx, attr) {

    lateinit var type: Type

    companion object {
        fun from(ctx: Context, type: Type, checked: Boolean): FilterChip {
            val chip = FilterChip(ctx)
            chip.type = type
            chip.text = type.type
            chip.tag = Type.getResourceIdByType(type)
            chip.setEnsureMinTouchTargetSize(false)
            chip.applyResources(ctx, type, checked)
            return chip
        }
    }

    /**
     * Must apply [Chip.isChecked] here in [CustomTarget.onResourceReady] else chip checked
     * state will not be adhered to once resources have loaded
     */
    fun applyResources(ctx: Context, type: Type, checked: Boolean) {
        Glide.with(ctx)
            .asBitmap()
            .load(Type.getResourceDrawableByType(type))
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val palette = Palette.from(resource).generate()
                    palette.dominantSwatch?.also {
                        val states = arrayOf(intArrayOf(attr.state_checked), intArrayOf(-attr.state_checked))
                        val colors = intArrayOf(ColorUtils.setAlphaComponent(it.rgb, 128), android.R.color.white)
                        val stateList = ColorStateList(states, colors)
                        val chipDrawable = ChipDrawable.createFromAttributes(ctx, null, 0, R.style.AppTheme_ChoiceChips)
                        chipDrawable.chipBackgroundColor = stateList
                        setChipDrawable(chipDrawable)
                    }
                    chipIcon = BitmapDrawable(ctx.resources, resource)
                    isChecked = checked
                }

                override fun onLoadCleared(placeholder: Drawable?) { }
            })
    }

}