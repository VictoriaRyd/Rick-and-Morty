package com.example.rickandmorty.ui.character

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.appComponent
import com.example.rickandmorty.databinding.CharactersFragmentBinding
import com.example.rickandmorty.factory.CharactersViewModelFactory
import com.example.rickandmorty.ui.adapters.CharactersAdapter
import com.example.rickandmorty.ui.viewModels.CharactersViewModel
import javax.inject.Inject

class CharactersFragment : Fragment(R.layout.characters_fragment) {

    private lateinit var binding: CharactersFragmentBinding

    private var page: Int = 1
    private var pages: Int = 1
    private var adapter = CharactersAdapter()
    private var charactersList = ArrayList<Character>()
    private var filter = CharactersFilter()

    @Inject
    lateinit var factory: CharactersViewModelFactory
    val viewModelCharacters by viewModels<CharactersViewModel>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CharactersFragmentBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Characters"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
        init()

        if (charactersList.isNullOrEmpty()) {
            page = 1
            viewModelCharacters.getAllCharacters(page, filter)
        }

        binding.swRefrCharacters.setOnRefreshListener {
            viewModelCharacters.reloadCharacters(page, filter)
            binding.swRefrCharacters.isRefreshing = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_search -> {
                Toast.makeText(context, getString(R.string.search), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.toolbar_filter -> {
                CharactersFilterFragment.newInstance(filter)
                    .show(childFragmentManager, "CharactersDialog")
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    private fun observeLiveData(){
        viewModelCharacters.listCharacters.observe(viewLifecycleOwner) {
            it.let {
                charactersList = it as ArrayList<Character>
                adapter.setCharacters(charactersList)
            }
        }

        viewModelCharacters.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progBarCharacter.apply {
                    visibility = if (it) View.VISIBLE else View.GONE }
            }
        }

        viewModelCharacters.pages.observe(viewLifecycleOwner) {
            this.pages = it
        }
    }

    private fun init(){
        binding.apply {
            rcView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rcView.adapter = adapter
        }
        pagination()
    }

    private fun pagination() {
        binding.rcView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    viewModelCharacters.getAllCharacters(page, filter)
                }
            }
        })
    }

    fun getCharacterFilter(filter: CharactersFilter) {
        this.filter = filter
        viewModelCharacters.getCharacterFilter(filter)
    }

    companion object {
        fun newInstance() = CharactersFragment()
    }
}