package com.example.rickandmorty.ui.character

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FilterCharactersFragmentBinding

class CharactersFilterFragment: DialogFragment() {

    private lateinit var binding: FilterCharactersFragmentBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = FilterCharactersFragmentBinding.inflate(LayoutInflater.from(requireContext()))

        val args = arguments?.getParcelable<CharactersFilter>(FRAGMENT_FILTER_CHARACTERS)
        if (args != null) setFilter(args)

        val builder = AlertDialog.Builder(requireContext())

        val dialog = builder
            .setView(binding.root)
            .setPositiveButton("Submit") { _, _, ->
                val name = if (binding.edName.text.isNotEmpty()) {
                    binding.edName.text.toString()
                } else ""
                val status = if (binding.statusSpin.selectedItem != "Any") {
                    binding.statusSpin.selectedItem.toString()
                } else null
                val species = if (binding.edSpecies.text.isNotEmpty()) {
                    binding.edSpecies.text.toString()
                } else ""
                val type = if (binding.edType.text.isNotEmpty()) {
                    binding.edType.text.toString()
                } else ""
                val gender = if (binding.genderSpin.selectedItem != "Any") {
                    binding.genderSpin.selectedItem as String
                } else null
                val filter = CharactersFilter(name, status, species, type, gender)
                if (isEmpty(filter)) {
                    Toast.makeText(requireContext(),
                        "Filter is empty", Toast.LENGTH_SHORT).show()
                }
                if (isSame(args, filter)) {
                    Toast.makeText(requireContext(),
                        "Same filter", Toast.LENGTH_SHORT).show()
                }
                (parentFragment as CharactersFragment).getCharacterFilter(filter)
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Clear filter and exit") {_, _ ->
                (parentFragment as CharactersFragment).
                getCharacterFilter(CharactersFilter
                    ("", null, "", "", null))
            }
            .create()
        return dialog
    }

    private fun setFilter(args: CharactersFilter) {
        with(binding) {
            edName.setText(args.name)
            statusSpin.setSelection(
                resources.getStringArray(R.array.status).indexOf(args.status)
            )
            edSpecies.setText(args.species)
            edType.setText(args.type)
            genderSpin.setSelection(
                resources.getStringArray(R.array.gender).indexOf(args.gender)
            )
            if (!args.status.isNullOrEmpty()) {
                statusSpin.setSelection(
                    resources.getStringArray(R.array.status).indexOf(args.status)
                )
            } else statusSpin.setSelection(0)
            if (!args.gender.isNullOrEmpty()) {
                genderSpin.setSelection(
                    resources.getStringArray(R.array.gender).indexOf(args.gender)
                )
            } else genderSpin.setSelection(0)
        }
    }

    private fun isEmpty(filter: CharactersFilter): Boolean {
        return filter.name == ""
                && filter.status == null
                && filter.species == ""
                && filter.type == ""
                && filter.gender == null
    }

    private fun isSame(args: CharactersFilter?, filter: CharactersFilter): Boolean {
        return args?.name == filter.name
                && args?.status == filter.status
                && args?.species == filter.species
                && args?.type == filter.type
                && args?.gender == filter.gender
    }

    companion object {
        private const val FRAGMENT_FILTER_CHARACTERS = "FRAGMENT_FILTER_CHARACTERS"
        fun newInstance(filter: CharactersFilter): CharactersFilterFragment {
            val fragment = CharactersFilterFragment()
            val args = Bundle().apply {
                putParcelable(FRAGMENT_FILTER_CHARACTERS, filter)
            }
            fragment.arguments = args
            return fragment
        }
    }
}