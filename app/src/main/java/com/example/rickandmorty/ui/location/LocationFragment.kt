package com.example.rickandmorty.ui.location

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
import com.example.rickandmorty.databinding.FragmentLocationBinding
import com.example.rickandmorty.factory.LocationsViewModelFactory
import com.example.rickandmorty.ui.adapters.LocationAdapter
import com.example.rickandmorty.ui.viewModels.LocationsViewModel
import javax.inject.Inject

class LocationFragment: Fragment(R.layout.fragment_location) {

    private lateinit var binding : FragmentLocationBinding
    private var adapter = LocationAdapter()
    private var locationList = ArrayList<Locations>()
    private var page = 1
    private var pages = 1
    private var filter = LocationsFilter()

    @Inject
    lateinit var factory: LocationsViewModelFactory
    val viewModelLocations by viewModels<LocationsViewModel>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Location"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (locationList.isNullOrEmpty()) {
            page = 1
            viewModelLocations.getLocations(page, filter)
        }

        observeLiveData()
        init()
        initSwipeRefresh()
    }

    private fun observeLiveData(){
        viewModelLocations.listLocations.observe(viewLifecycleOwner) {
            it.let {
                locationList = it as ArrayList<Locations>
                adapter.setLocations(locationList)
            }
        }

        viewModelLocations.isLoading.observe(viewLifecycleOwner) {
            it.let {
                binding.progBarLocation.apply {
                    visibility = if (it) View.VISIBLE else View.GONE }
            }
        }

        viewModelLocations.pages.observe(viewLifecycleOwner) {
            this.pages = it
        }
    }

    private fun init(){
        binding.apply {
            locationList.layoutManager =
                StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL)
            locationList.adapter = adapter
        }
        pagination()
    }

    private fun pagination() {
        binding.locationList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) and (page < pages)) {
                    page += 1
                    viewModelLocations.getLocations(page, filter)
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
                Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.toolbar_filter -> {
                LocationFilterFragment.newInstance(filter)
                    .show(childFragmentManager, "LocationsDialog")
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun getLocationFilter(filter: LocationsFilter) {
        viewModelLocations.getLocationFilter(filter)
        this.filter = filter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    private fun initSwipeRefresh() {
        binding.swRefrLocations.setOnRefreshListener {
            viewModelLocations.reloadLocations(page, filter)
            binding.swRefrLocations.isRefreshing = false
        }
    }

    companion object {
        fun newInstance() = LocationFragment()
    }
}