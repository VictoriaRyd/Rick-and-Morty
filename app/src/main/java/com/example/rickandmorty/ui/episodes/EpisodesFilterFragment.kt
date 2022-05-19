package com.example.rickandmorty.ui.episodes

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FilterEpisodesFragmentBinding

class EpisodesFilterFragment: DialogFragment() {

    private lateinit var binding: FilterEpisodesFragmentBinding
    private var filter = EpisodesFilter()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = FilterEpisodesFragmentBinding.inflate(LayoutInflater.from(requireContext()))

        val args = arguments?.getParcelable<EpisodesFilter>(FRAGMENT_FILTER_EPISODES)
        if (args != null) {
            binding.edName.setText(args.name)
            binding.edEpisode.setText(args.episode)
        }

        val builder = AlertDialog.Builder(requireContext())

        val dialog = builder
            .setView(binding.root)
            .setPositiveButton("Submit") {_, _  ->
                val name = if (binding.edName.text.isNotEmpty()) {
                    binding.edName.text.toString()
                } else ""
                val code = if (binding.edEpisode.text.isNotEmpty()) {
                    binding.edEpisode.text.toString()
                } else ""
                filter = EpisodesFilter(name, code)
                if (isEmpty(filter)) {
                    Toast.makeText(requireContext(),
                        getString(R.string.empty), Toast.LENGTH_SHORT).show()
                }
                if (isSame(args, filter)) {
                    Toast.makeText(requireContext(),
                        getString(R.string.same_filter), Toast.LENGTH_SHORT).show()
                }
                (parentFragment as EpisodesFragment).getEpisodeFilter(filter)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .setNeutralButton(getString(R.string.clear_and_exit)) {_, _ ->
                (parentFragment as EpisodesFragment).getEpisodeFilter(
                    EpisodesFilter("", ""))
            }
            .create()
        return dialog
    }

    private fun isEmpty(filter: EpisodesFilter): Boolean {
        return filter.name == "" && filter.episode == ""
    }

    private fun isSame(args: EpisodesFilter?, filter: EpisodesFilter): Boolean {
        return args?.name == filter.name && args?.episode == filter.episode
    }

    companion object {
        private const val FRAGMENT_FILTER_EPISODES = "FRAGMENT_FILTER_EPISODES"
        fun newInstance(filter: EpisodesFilter): EpisodesFilterFragment {
            val fragment = EpisodesFilterFragment()
            val args = Bundle().apply {
                putParcelable(FRAGMENT_FILTER_EPISODES, filter)
            }
            fragment.arguments = args
            return fragment
        }
    }
}