package com.example.rickandmorty.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.EpisodesItemBinding
import com.example.rickandmorty.ui.episodes.DetailsEpisodesFragment
import com.example.rickandmorty.ui.episodes.Episode

class EpisodesAdapter : RecyclerView.Adapter<EpisodesAdapter.EpisodesHolder>() {

    private var listEpisodes = ArrayList<Episode>()

    class EpisodesHolder(private var binding: EpisodesItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: Episode){
            binding.episodesName.text = episode.name
            binding.episodeNumber.text = episode.episode
            binding.airDate.text = episode.air_date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EpisodesItemBinding.inflate(layoutInflater, parent, false)
        return EpisodesHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodesHolder, position: Int) {
        holder.bind(listEpisodes[position])
        val manager = (holder.itemView.context as FragmentActivity).supportFragmentManager
        val id = listEpisodes[position].id
        holder.itemView.setOnClickListener {
            manager.beginTransaction().replace(
                R.id.fragment_container,
                DetailsEpisodesFragment(id)
            ).addToBackStack(null).commit()
        }
    }

    override fun getItemCount(): Int {
        return listEpisodes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEpisodes(episode: List<Episode>){
        this.listEpisodes.clear()
        this.listEpisodes.addAll(episode)
        notifyDataSetChanged()
    }
}