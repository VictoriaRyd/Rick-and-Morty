package com.example.rickandmorty.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.CharactersItemBinding
import com.example.rickandmorty.ui.character.Character
import com.example.rickandmorty.ui.character.DetailsCharactersFragment
import com.squareup.picasso.Picasso

class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {

    private var listCharacters = ArrayList<Character>()

    class CharactersViewHolder(private val binding: CharactersItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character){
            binding.characterName.text = character.name
            binding.characterGender.text = character.gender
            binding.characterSpecies.text = character.species
            binding.characterStatus.text = character.status
            Picasso.get().load(character.image).into(binding.characterImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CharactersItemBinding.inflate(layoutInflater, parent, false)
        return CharactersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(listCharacters[position])
        val manager = (holder.itemView.context as FragmentActivity).supportFragmentManager
        val id = listCharacters[position].id
        holder.itemView.setOnClickListener {
            manager.beginTransaction().replace(
                R.id.fragment_container,
                DetailsCharactersFragment(id)
            ).addToBackStack(null).commit()
        }
    }

    override fun getItemCount(): Int {
        return listCharacters.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCharacters(character: List<Character>){
        this.listCharacters.clear()
        this.listCharacters.addAll(character)
        notifyDataSetChanged()
    }
}