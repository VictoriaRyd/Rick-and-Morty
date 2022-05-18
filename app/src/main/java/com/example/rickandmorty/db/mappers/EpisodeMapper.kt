package com.example.rickandmorty.db.mappers

import com.example.rickandmorty.db.entity.EpisodesEntity
import com.example.rickandmorty.ui.episodes.Episode
import javax.inject.Inject

class EpisodeMapper @Inject constructor() {

    fun mapFromNetworkToDB(episode: Episode) : EpisodesEntity {
        return EpisodesEntity(
            id = episode.id,
            name = episode.name,
            episode = episode.episode,
            airDate = episode.air_date,
            characters = episode.characters,
        )
    }

    fun mapFromDBToNetwork(episode: EpisodesEntity) : Episode {
        return Episode(
            id = episode.id,
            name = episode.name,
            episode = episode.episode,
            air_date = episode.airDate,
            characters = episode.characters,
        )
    }

    fun mapEpisodesFromDBToNetwork(episodes: List<EpisodesEntity>) : List<Episode> {
        return episodes.map {
            Episode(
                id = it.id,
                name = it.name,
                episode = it.episode,
                air_date = it.airDate,
                characters = it.characters,
            )
        }
    }
}