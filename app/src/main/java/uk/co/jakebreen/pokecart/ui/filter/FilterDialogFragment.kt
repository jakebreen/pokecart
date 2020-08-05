package uk.co.jakebreen.pokecart.ui.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import org.koin.android.ext.android.inject
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.FilterDialogFragmentBinding
import uk.co.jakebreen.pokecart.model.type.Type

class FilterDialogFragment: DialogFragment() {

    private val presenter: FilterDialogPresenter by inject()

    private lateinit var binding: FilterDialogFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.filter_dialog_fragment, container, false)
        binding.presenter = presenter
        presenter.attach(this)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_background)
        return dialog
    }

    fun onSaveFilters() {
        val checkedTypes = binding.cgFilterTypes.children
            .map { it as FilterChip }
            .map {
                val type = Type.getTypeByResourceId(it.tag as Int)!!
                Pair(type, it.isChecked)
            }.toMap()

        presenter.updateFilters(
            checkedTypes,
            binding.rsFilterHealth.values,
            binding.rsFilterAttack.values,
            binding.rsFilterDefense.values,
            binding.rsFilterSpeed.values)

        dismiss()
    }

    fun showFilterChips(types: Map<Type, Boolean>) {
        binding.cgFilterTypes.apply {
            removeAllViews()
            types.forEach {
                addView(FilterChip.from(context, it.key, it.value))
            }
        }
    }

    fun showHealthStats(min: Float, max: Float) {
        binding.rsFilterHealth.setValues(min, max)
    }

    fun showAttackStats(min: Float, max: Float) {
        binding.rsFilterAttack.setValues(min, max)
    }

    fun showDefenseStats(min: Float, max: Float) {
        binding.rsFilterDefense.setValues(min, max)
    }

    fun showSpeedStats(min: Float, max: Float) {
        binding.rsFilterSpeed.setValues(min, max)
    }

}