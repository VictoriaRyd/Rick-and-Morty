package com.example.rickandmorty.ui.location

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FilterLocationFragmentBinding

class LocationFilterFragment: DialogFragment() {

    private lateinit var binding: FilterLocationFragmentBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FilterLocationFragmentBinding.inflate(LayoutInflater.from(requireContext()))

        val args = arguments?.getParcelable<LocationsFilter>(FRAGMENT_FILTER_LOCATIONS)
        if (args != null) {
            binding.edName.setText(args.name)
            binding.edType.setText(args.type)
            binding.edDimension.setText(args.dimension)
        }

        val builder = AlertDialog.Builder(requireContext())

        val dialog = builder
            .setView(binding.root)
            .setPositiveButton("Submit") {_, _  ->
                val name = if (binding.edName.text.isNotEmpty()) {
                    binding.edName.text.toString()
                } else ""
                val type = if (binding.edType.text.isNotEmpty()) {
                    binding.edType.text.toString()
                } else ""
                val dimension = if (binding.edDimension.text.isNotEmpty()) {
                    binding.edDimension.text.toString()
                } else ""
                val filter = LocationsFilter(name, type, dimension)
                if (isEmpty(filter)) {
                    Toast.makeText(requireContext(),
                        getString(R.string.empty), Toast.LENGTH_SHORT).show()
                }
                if (isSame(args, filter)) {
                    Toast.makeText(requireContext(),
                        getString(R.string.same_filter), Toast.LENGTH_SHORT).show()
                }
                (parentFragment as LocationFragment).getLocationFilter(filter)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .setNeutralButton(getString(R.string.clear_and_exit)) {_, _ ->
                (parentFragment as LocationFragment).getLocationFilter(
                    LocationsFilter("", "", ""))
            }
            .create()
        return dialog
    }

    private fun isEmpty(filter: LocationsFilter): Boolean {
        return filter.name == "" && filter.type == "" && filter.dimension == ""
    }

    private fun isSame(args: LocationsFilter?, filter: LocationsFilter): Boolean {
        return args?.name == filter.name
                && args?.type == filter.type
                && args?.dimension == filter.dimension
    }

    companion object {
        private const val FRAGMENT_FILTER_LOCATIONS ="FRAGMENT_FILTER_LOCATIONS"
        fun newInstance(filter: LocationsFilter): LocationFilterFragment{
            val fragment = LocationFilterFragment()
            val args = Bundle().apply {
                putParcelable(FRAGMENT_FILTER_LOCATIONS, filter)
            }
            fragment.arguments = args
            return fragment
        }
    }
}