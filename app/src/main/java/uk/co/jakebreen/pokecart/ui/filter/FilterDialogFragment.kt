package uk.co.jakebreen.pokecart.ui.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.FilterDialogFragmentBinding
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type

class FilterDialogFragment: DialogFragment() {

    private val filterViewModel : FilterDialogViewModel by viewModel()

    private lateinit var binding: FilterDialogFragmentBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_background)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.filter_dialog_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = filterViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterViewModel.getTypes()?.let { showFilterChips(it) }
        filterViewModel.getStats()?.let { applyStats(it) }
        binding.btApplyFilters.setOnClickListener { onSaveFilters() }
    }

    fun onSaveFilters() {
        val checkedTypes = binding.cgFilterTypes.children
            .map { it as FilterChip }
            .map {
                val type = Type.getTypeByResourceId(it.tag as Int)!!
                Pair(type, it.isChecked)
            }.toMap()

        binding.viewModel!!.saveFilters(
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

    private fun showHealthStats(min: Int, max: Int) {
        binding.rsFilterHealth.setValues(min.toFloat(), max.toFloat())
    }

    private fun showAttackStats(min: Int, max: Int) {
        binding.rsFilterAttack.setValues(min.toFloat(), max.toFloat())
    }

    private fun showDefenseStats(min: Int, max: Int) {
        binding.rsFilterDefense.setValues(min.toFloat(), max.toFloat())
    }

    private fun showSpeedStats(min: Int, max: Int) {
        binding.rsFilterSpeed.setValues(min.toFloat(), max.toFloat())
    }

    private fun applyStats(map: Map<Stat, Pair<Int, Int>>) {
        map.forEach {
            when (it.key) {
                Stat.HEALTH -> showHealthStats(
                    it.value.first,
                    it.value.second
                )
                Stat.ATTACK -> showAttackStats(
                    it.value.first,
                    it.value.second
                )
                Stat.DEFENSE -> showDefenseStats(
                    it.value.first,
                    it.value.second
                )
                Stat.SPEED -> showSpeedStats(
                    it.value.first,
                    it.value.second
                )
            }
        }
    }

}