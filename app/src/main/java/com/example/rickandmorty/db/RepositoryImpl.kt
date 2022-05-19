package com.example.rickandmorty.db

import com.example.rickandmorty.api.*
import com.example.rickandmorty.db.dao.CharacterDao
import com.example.rickandmorty.db.dao.EpisodesDao
import com.example.rickandmorty.db.dao.LocationDao
import com.example.rickandmorty.db.mappers.CharacterMapper
import com.example.rickandmorty.db.mappers.EpisodeMapper
import com.example.rickandmorty.db.mappers.LocationMapper
import com.example.rickandmorty.ui.Repository
import com.example.rickandmorty.ui.character.Character
import com.example.rickandmorty.ui.character.CharactersFilter
import com.example.rickandmorty.ui.episodes.Episode
import com.example.rickandmorty.ui.episodes.EpisodesFilter
import com.example.rickandmorty.ui.location.Locations
import com.example.rickandmorty.ui.location.LocationsFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(private val episodesApi: EpisodeApi,
                                         private val charactersApi: CharacterApi,
                                         private val locationsApi: LocationApi,
                                         private val characterDao: CharacterDao,
                                         private val episodeDao: EpisodesDao,
                                         private val locationDao: LocationDao,
                                         private val characterMapper: CharacterMapper,
                                         private val episodeMapper: EpisodeMapper,
                                         private val locationMapper: LocationMapper
): Repository {

    override suspend fun getCharacters(page: Int, filter: CharactersFilter): CharacterList? {
        var result: CharacterList?
        try {
            result = charactersApi.getCharacters(
                page
            ).body()
            result?.results?.forEach { character ->
                characterDao.saveCharacters(characterMapper.mapFromNetworkToDB(character))
            }
        } catch (e: Exception) {
            val characters = characterMapper.mapCharactersFromDBToNetwork(
                characterDao.getFilteredCharacters(
                    filter.name,
                    filter.status,
                    filter.species,
                    filter.type,
                    filter.gender
                )
            )
            result = CharacterList(characters, Result(page))
        }
        return result
    }

    override suspend fun getCharacterId(id: Int): Character? {
        var result: Character? = null
        try {
            result = charactersApi.getCharacterId(id).body()
        } catch (e: Exception) {
            val character = characterDao.getCharacterById(id)
            if (character != null) {
                result = characterMapper.mapFromDBToNetwork(characterDao.getCharacterById(id))
            }
        }
        return result
    }

    override suspend fun getEpisodes(page: Int, filter: EpisodesFilter): EpisodesList? {
        var result: EpisodesList?
        try {
            result = episodesApi.getEpisodes(
                page
            ).body()
            result?.results?.forEach { episode ->
                episodeDao.saveEpisodes(episodeMapper.mapFromNetworkToDB(episode))
            }
        } catch (e: Exception) {
            val episodes = episodeMapper.mapEpisodesFromDBToNetwork(
                episodeDao.getFilteredEpisodes(
                    filter.name,
                    filter.episode
                )
            )
            result = EpisodesList(episodes, Result(page))
        }
        return result
    }

    override suspend fun getEpisodeId(id: Int): Episode? {
        var result: Episode? = null
        try {
            result = episodesApi.getEpisodeId(id).body()
        } catch (e: Exception) {
            val episode = episodeDao.getEpisodeById(id)
            if (episode != null) {
                result = episodeMapper.mapFromDBToNetwork(episode)
            }
        }
        return result
    }

    override suspend fun getLocation(page: Int, filter: LocationsFilter): LocationList? {
        var result: LocationList?
        try {
            result = locationsApi.getLocation(
                page
            ).body()
            result?.results?.forEach { location ->
                locationDao.saveLocations(locationMapper.mapFromNetworkToDB(location))
            }
        } catch (e: Exception) {
            val locations = locationMapper.mupLocationsFromDBToNetwork(
                locationDao.getFilteredLocations(
                    filter.name,
                    filter.type,
                    filter.dimension
                )
            )
            result = LocationList(locations, Result(page))
        }
        return result
    }

    override suspend fun getLocationId(id: Int): Locations? {
        var result: Locations? = null
        try {
            result = locationsApi.getLocationId(id).body()
        } catch (e: Exception) {
            val location = locationDao.getLocationById(id)
            if (location != null) {
                result = locationMapper.mupFromDBToNetwork(location)
            }
        }
        return result
    }

    override suspend fun getLocationFilter(filter: LocationsFilter): LocationList? {
        val result: LocationList? = try {
            locationsApi.getLocationFilter(
                filter.name, filter.type, filter.dimension).body()
        } catch (e: Exception) {
            val locations = locationMapper.mupLocationsFromDBToNetwork(
                locationDao.getFilteredLocations(
                    filter.name,
                    filter.type,
                    filter.dimension
                )
            )
            LocationList(locations, Result(1))
        }
        return result
    }

    override suspend fun getCharacterFilter(filter: CharactersFilter): CharacterList? {
        val result: CharacterList? = try {
            charactersApi.getCharacterFilter(
                filter.name,
                filter.status,
                filter.species,
                filter.type,
                filter.gender
            ).body()
        } catch (e: Exception) {
            val ch = characterDao.getFilteredCharacters(
                filter.name,
                filter.status,
                filter.species,
                filter.type,
                filter.gender
            )
            val characters = characterMapper.mapCharactersFromDBToNetwork(ch)
            CharacterList(characters, Result(1))
        }
        return result
    }

    override suspend fun getEpisodeFilter(filter: EpisodesFilter): EpisodesList? {
        val result: EpisodesList? = try {
            episodesApi.getEpisodeFilter(filter.name, filter.episode).body()
        } catch (e: Exception) {
            val episodes = episodeMapper.mapEpisodesFromDBToNetwork(
                episodeDao.getFilteredEpisodes(
                    filter.name,
                    filter.episode
                )
            )
            EpisodesList(episodes, Result(1))
        }
        return result
    }
}