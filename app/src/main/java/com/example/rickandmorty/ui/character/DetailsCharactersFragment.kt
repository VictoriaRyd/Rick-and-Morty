package com.example.rickandmorty.ui.character

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
import com.example.rickandmorty.databinding.DetailsCharactersFragmentBinding
import com.example.rickandmorty.factory.CharacterViewModelFactory
import com.example.rickandmorty.ui.adapters.EpisodesAdapter
import com.example.rickandmorty.ui.episodes.Episode
import com.example.rickandmorty.ui.viewModels.CharacterViewModel
import com.squareup.picasso.Picasso
import javax.inject.Inject

private const val KEY_CHARACTER = "key.character"

class DetailsCharactersFragment(private val characterId: Int) : Fragment(R.layout.details_characters_fragment) {

    private lateinit var binding: DetailsCharactersFragmentBinding
    private var character: Character? = null
    private var episodesList = ArrayList<Episode>()
    private var adapter = EpisodesAdapter()

    @Inject
    lateinit var factory: CharacterViewModelFactory
    private val viewModelCharacter by viewModels<CharacterViewModel>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailsCharactersFragmentBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Details Characters"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (character == null) {
            viewModelCharacter.getCharacterById(characterId)
        }
        observeLiveData()
        init()
        initSwipeRefresh()
    }

    private fun observeLiveData() {
        viewModelCharacter.characterLiveData.observe(viewLifecycleOwner) {
            it.let {
                character = it as Character
                setCharacter(character)
            }
        }
        viewModelCharacter.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progBarCharacter.apply {
                    visibility = if (it) View.VISIBLE else View.GONE }
            }
        }
        viewModelCharacter.isNoEpisodes.observe(viewLifecycleOwner) {
            it.let {
                binding.rcView.apply {
                    visibility = if (it) View.GONE else View.VISIBLE }
            }
        }
        viewModelCharacter.isNotEnoughEpisodesFound.observe(viewLifecycleOwner) {
            it.let {
                if (it) Toast.makeText(requireContext(),
                    "Connect to network to receive episodes",
                    Toast.LENGTH_SHORT).show()
            }
        }
        viewModelCharacter.isNoDataFound.observe(viewLifecycleOwner) {
            it.let{
                if (it) Toast.makeText(requireContext(),
                    "No locations found, check filter or/and connect to " +
                            "network for loading more data",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setCharacter(character: Character?){
        Picasso.get().load(character?.image).into(binding.imCharacter)
        with(binding){
                nameCharacter.text = character?.name
                genderCharacter.text = character?.gender
                statusCharacter.text = character?.status
                originCharacter.text = character?.origin?.name
                locationCharacter.text = character?.location?.name
                speciesCharacter.text = character?.species
        }
        if(episodesList.isNullOrEmpty()) {
            getEpisodesList(character)
        }
    }

    private fun getEpisodesList(character: Character?) {
        character?.episode?.let { viewModelCharacter.getEpisodesId(it) }
        viewModelCharacter.episodesListLiveData.observe(viewLifecycleOwner) {
            it.let {
                episodesList = it as ArrayList<Episode>
                adapter.setEpisodes(episodesList)
            }
        }
    }

    private fun init() {
        with(binding) {
            rcView.layoutManager = StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL)
                rcView.adapter = adapter
        }
    }

    private fun initSwipeRefresh(){
        binding.swRefrDetailCharacter.setOnRefreshListener {
            viewModelCharacter.getCharacterById(characterId)
            binding.swRefrDetailCharacter.isRefreshing = false
        }
    }

    companion object {
        fun newInstance(character: Character) : DetailsCharactersFragment {
            return DetailsCharactersFragment(character.id).also {
                it.arguments = Bundle() .apply {
                    getInt(KEY_CHARACTER, character.id)
                }
            }
        }
    }
}