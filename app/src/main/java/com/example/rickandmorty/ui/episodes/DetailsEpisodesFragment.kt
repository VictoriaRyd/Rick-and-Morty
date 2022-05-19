package com.example.rickandmorty.ui.episodes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.appComponent
import com.example.rickandmorty.databinding.DetailsEpisodesFragmentBinding
import com.example.rickandmorty.factory.EpisodeViewModelFactory
import com.example.rickandmorty.ui.adapters.CharactersAdapter
import com.example.rickandmorty.ui.character.Character
import com.example.rickandmorty.ui.viewModels.EpisodeViewModel
import javax.inject.Inject

private const val KEY_EPISODE = "key.episode"

class DetailsEpisodesFragment(private val episodeId: Int): Fragment(R.layout.details_episodes_fragment){

    private lateinit var binding: DetailsEpisodesFragmentBinding
    private var episode: Episode? = null
    private var adapter = CharactersAdapter()
    private var listCharacters = ArrayList<Character>()

    @Inject
    lateinit var factory: EpisodeViewModelFactory
    private val viewModelEpisode by viewModels<EpisodeViewModel>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailsEpisodesFragmentBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Details Episodes"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (episode == null) {
            viewModelEpisode.getEpisodeId(episodeId)
        }
        observeLiveData()
        init()
        initSwipeRefresh()
    }

    private fun observeLiveData() {
        viewModelEpisode.episodeLiveData.observe(viewLifecycleOwner) {
            it.let {
                episode = it as Episode
                setEpisode(episode)
            }
        }
        viewModelEpisode.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progBarEpisode.apply {
                    visibility = if (it) View.VISIBLE else View.GONE }
            }
        }
        viewModelEpisode.isNoCharacters.observe(viewLifecycleOwner) {
            it.let {
                binding.rcViewEpisode.apply {
                    visibility = if (it) View.GONE else View.VISIBLE }
            }
        }
        viewModelEpisode.isNotEnoughCharactersFound.observe(viewLifecycleOwner) {
            it.let {
                if (it) Toast.makeText(requireContext(),
                    getString(R.string.isNotEnoughCharactersFound), Toast.LENGTH_SHORT).show()
            }
        }
        viewModelEpisode.isNoDataFound.observe(viewLifecycleOwner) {
            it.let{
                if (it) Toast.makeText(requireContext(),
                    getString(R.string.isNoDataFoundEpisode), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setEpisode(episode: Episode?) {
        with(binding) {
            name.text = episode?.name
            episodes.text = episode?.episode
            airDate.text = episode?.air_date
        }
        if (listCharacters.isNullOrEmpty()) {
            getCharactersList(episode)
        }
    }

    private fun getCharactersList(episode: Episode?) {
        episode?.characters?.let { viewModelEpisode.getCharactersId(it) }
        viewModelEpisode.charactersListLiveData.observe(viewLifecycleOwner) {
            it.let {
                listCharacters = it as ArrayList<Character>
                adapter.setCharacters(listCharacters)
            }
        }
    }

    private fun init() {
        with(binding) {
            rcViewEpisode.layoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
            rcViewEpisode.adapter = adapter
            }
        }

    private fun initSwipeRefresh() {
        binding.swRefrDetailEpisode.setOnRefreshListener {
            viewModelEpisode.getEpisodeId(episodeId)
            binding.swRefrDetailEpisode.isRefreshing = false
        }
    }

    companion object {
        fun newInstance(episode: Episode) : DetailsEpisodesFragment {
            return DetailsEpisodesFragment(episode.id).also {
                it.arguments = Bundle() .apply {
                    putInt(KEY_EPISODE, episode.id)
                }
            }
        }
    }
}