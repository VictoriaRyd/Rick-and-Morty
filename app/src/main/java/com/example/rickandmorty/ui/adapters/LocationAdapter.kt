package com.example.rickandmorty.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.LocationItemBinding
import com.example.rickandmorty.ui.location.DetailsLocationFragment
import com.example.rickandmorty.ui.location.Locations

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.LocationHolder>() {

    private var listLocation = ArrayList<Locations>()

    class LocationHolder(private var binding: LocationItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(locations: Locations){
            binding.locationName.text = locations.name
            binding.locationType.text = locations.type
            binding.locationDimension.text = locations.dimension
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LocationItemBinding.inflate(layoutInflater, parent, false)
        return LocationHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationHolder, position: Int) {
        holder.bind(listLocation[position])
        val manager = (holder.itemView.context as FragmentActivity).supportFragmentManager
        val id = listLocation[position].id
        holder.itemView.setOnClickListener {
            manager.beginTransaction().replace(
                R.id.fragment_container,
                DetailsLocationFragment(id)
            ).addToBackStack(null).commit()
        }
    }

    override fun getItemCount(): Int {
        return listLocation.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLocations(locations: List<Locations>){
        this.listLocation.clear()
        this.listLocation.addAll(locations)
        notifyDataSetChanged()
    }
}