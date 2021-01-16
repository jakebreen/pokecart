package uk.co.jakebreen.pokecart.ui.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import uk.co.jakebreen.pokecart.R
import uk.co.jakebreen.pokecart.databinding.FilterDialogFragmentBinding
import uk.co.jakebreen.pokecart.model.stat.Stat
import uk.co.jakebreen.pokecart.model.type.Type
import uk.co.jakebreen.pokecart.ui.shop.ShopActivity

class FilterDialogFragment: DialogFragment() {

    private val scope = getKoin().getOrCreateScope("filter_scope_id", named<FilterDialogFragment>())
    private val filterViewModel : FilterDialogViewModel by scope.inject()

    private lateinit var binding: FilterDialogFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            setBackgroundDrawableResource(R.drawable.dialog_background)
        }
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
        filterViewModel.observeFilteredTypes().observe(viewLifecycleOwner, Observer { showFilterChips(it) })
        filterViewModel.observeFilteredStats().observe(viewLifecycleOwner, Observer { statsMap ->
            statsMap[Stat.HEALTH]?.also { healthPair ->   showHealthStats(healthPair.first, healthPair.second) }
            statsMap[Stat.ATTACK]?.also { attackPair ->   showAttackStats(attackPair.first, attackPair.second) }
            statsMap[Stat.DEFENSE]?.also { defensePair -> showDefenseStats(defensePair.first, defensePair.second) }
            statsMap[Stat.SPEED]?.also { speedPair ->     showSpeedStats(speedPair.first, speedPair.second) }
        })
        binding.btApplyFilters.setOnClickListener { onSaveFilters() }
    }

    private fun onSaveFilters() {
        val checkedTypes = binding.cgFilterTypes.children
            .filterIsInstance(FilterChip::class.java)
            .map {
                val type = Type.getTypeByResourceId(it.tag as Int)
                Pair(type, it.isChecked)
            }.toMap()

        binding.viewModel?.also {
            it.saveFilters(
                checkedTypes,
                binding.rsFilterHealth.values,
                binding.rsFilterAttack.values,
                binding.rsFilterDefense.values,
                binding.rsFilterSpeed.values)
        }

        (activity as ShopActivity).setShouldResetAdapter(true).also {
            dismiss()
        }
    }

    private fun showFilterChips(types: Map<Type, Boolean>) {
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

}