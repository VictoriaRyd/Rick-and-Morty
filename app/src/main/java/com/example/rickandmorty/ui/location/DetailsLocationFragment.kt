package com.example.rickandmorty.ui.location

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
import com.example.rickandmorty.databinding.DetailsLocationFragmentBinding
import com.example.rickandmorty.factory.LocationViewModelFactory
import com.example.rickandmorty.ui.adapters.CharactersAdapter
import com.example.rickandmorty.ui.character.Character
import com.example.rickandmorty.ui.viewModels.LocationViewModel
import javax.inject.Inject

private const val KEY_LOCATION = "key.location"

class DetailsLocationFragment(private val locationId: Int): Fragment(R.layout.details_location_fragment) {

    private lateinit var binding: DetailsLocationFragmentBinding
    private var location: Locations? = null
    private var adapter = CharactersAdapter()
    private var listCharacters = ArrayList<Character>()
    @Inject
    lateinit var factory: LocationViewModelFactory
    private val viewModelLocation by viewModels<LocationViewModel>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailsLocationFragmentBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Details Location"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (location == null) {
            viewModelLocation.getLocationId(locationId)
        }
        observeLiveData()
        init()
        initSwipeRefresh()
    }

    private fun observeLiveData() {
        viewModelLocation.location.observe(viewLifecycleOwner) {
            it.let {
                location = it as Locations
                updateUI(location)
            }
        }
        viewModelLocation.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progBarLocation.apply {
                    visibility = if (it) View.VISIBLE else View.GONE }
            }
        }
        viewModelLocation.isNoCharacters.observe(viewLifecycleOwner) {
            it.let {
                binding.rcViewLocation.apply {
                    visibility = if (it) View.GONE else View.VISIBLE }
            }
        }
        viewModelLocation.isNotEnoughCharactersFound.observe(viewLifecycleOwner) {
            it.let {
                if (it) Toast.makeText(requireContext(),
                    getString(R.string.isNotEnoughCharactersFound), Toast.LENGTH_SHORT).show()
            }
        }
        viewModelLocation.isNoDataFound.observe(viewLifecycleOwner) {
            it.let{
                if (it) Toast.makeText(requireContext(),
                    getString(R.string.isNoData), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(location: Locations?) {
        with(binding) {
            name.text = location?.name
            type.text =
                if (location?.type?.isEmpty() == true) getString(R.string.unknown) else location?.type
            dimension.text =
                if (location?.dimension?.isEmpty() == true) getString(R.string.unknown) else location?.dimension
        }
        if (listCharacters.isNullOrEmpty()) {
            getCharactersList(location)
        }
    }

    private fun getCharactersList(location: Locations?) {
        location?.residents?.let { viewModelLocation.getCharacterId(it) }
        viewModelLocation.charactersListLiveData.observe(viewLifecycleOwner) {
            it.let {
                listCharacters = it as ArrayList<Character>
                adapter.setCharacters(listCharacters)
            }
        }
    }

    private fun init() {
        with(binding) {
            rcViewLocation.layoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
            rcViewLocation.adapter = adapter
        }
    }

    private fun initSwipeRefresh() {
        binding.swRefrDetailLocation.setOnRefreshListener {
            viewModelLocation.getLocationId(locationId)
            binding.swRefrDetailLocation.isRefreshing = false
        }
    }

    companion object {
        fun newInstance(locations: Locations) : DetailsLocationFragment {
            return DetailsLocationFragment(locations.id).also {
                it.arguments = Bundle() .apply {
                    putInt(KEY_LOCATION, locations.id)
                }
            }
        }
    }
}