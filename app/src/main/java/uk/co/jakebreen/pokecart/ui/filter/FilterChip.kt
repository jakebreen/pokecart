package uk.co.jakebreen.pokecart.ui.filter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.chip.Chip
import uk.co.jakebreen.pokecart.model.type.Type


class FilterChip(ctx: Context, attr: AttributeSet? = null) : Chip(ctx, attr) {

    lateinit var type: Type

    companion object {
        fun from(ctx: Context, type: Type, checked: Boolean): FilterChip {
            val chip = FilterChip(ctx)
            chip.type = type
            chip.applyImage(ctx, type)
            chip.text = type.type
            chip.isCheckedIconVisible = true
            chip.isCheckable = true
            chip.isChecked = checked
            chip.tag = Type.getResourceIdByType(type)
            chip.setEnsureMinTouchTargetSize(false)
            return chip
        }
    }

    fun applyImage(ctx: Context, type: Type) {
        Glide.with(ctx)
            .load(Type.getResourceDrawableByType(type))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    chipIcon = resource
                    return false
                }
            }).preload()
    }

}