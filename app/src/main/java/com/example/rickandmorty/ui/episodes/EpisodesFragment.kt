package com.example.rickandmorty.ui.episodes

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
import com.example.rickandmorty.databinding.FragmentEpisodesBinding
import com.example.rickandmorty.factory.EpisodesViewModelFactory
import com.example.rickandmorty.ui.adapters.EpisodesAdapter
import com.example.rickandmorty.ui.viewModels.EpisodesViewModel
import javax.inject.Inject

class EpisodesFragment: Fragment(R.layout.fragment_episodes) {

    private lateinit var binding : FragmentEpisodesBinding

    private var adapter = EpisodesAdapter()
    private var episodesList = ArrayList<Episode>()
    private var page = 1
    private var pages = 1
    private var filter = EpisodesFilter()

    @Inject
    lateinit var factory: EpisodesViewModelFactory
    val viewModelEpisodes by viewModels<EpisodesViewModel>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Episodes"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
        init()
        initSwipeRefresh()

        if (episodesList.isNullOrEmpty()) {
            page = 1
            viewModelEpisodes.getEpisodes(page, filter)
        }
    }

    private fun observeLiveData(){
        viewModelEpisodes.listEpisodes.observe(viewLifecycleOwner) {
            it.let {
                episodesList = it as ArrayList<Episode>
                adapter.setEpisodes(episodesList)
            }
        }

        viewModelEpisodes.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progBarEpisode.apply {
                    visibility = if (it) View.VISIBLE else View.GONE }
            }
        }

        viewModelEpisodes.pages.observe(viewLifecycleOwner) {
            this.pages = it
        }
    }

    private fun init(){
        binding.apply {
            episodesList.layoutManager =
                StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL)
            episodesList.adapter = adapter
        }
        pagination()
    }

    private fun pagination() {
        binding.episodesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    viewModelEpisodes.getEpisodes(page, filter)
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbar_search -> {
                Toast.makeText(context, getString(R.string.search), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.toolbar_filter -> {
                EpisodesFilterFragment.newInstance(filter)
                    .show(childFragmentManager, "EpisodesDialog")
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun getEpisodeFilter(filter: EpisodesFilter) {
        viewModelEpisodes.getEpisodeFilter(filter)
        this.filter = filter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    private fun initSwipeRefresh() {
        binding.swRefrEpisodes.setOnRefreshListener {
            viewModelEpisodes.reloadEpisodes(page, filter)
            binding.swRefrEpisodes.isRefreshing = false
        }
    }

    companion object {
        fun newInstance() = EpisodesFragment()
    }
}