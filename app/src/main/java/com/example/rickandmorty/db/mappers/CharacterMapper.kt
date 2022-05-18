package com.example.rickandmorty.db.mappers

import com.example.rickandmorty.db.entity.CharacterEntity
import com.example.rickandmorty.ui.character.Character
import com.example.rickandmorty.ui.character.Location
import com.example.rickandmorty.ui.character.Origin
import javax.inject.Inject

class CharacterMapper @Inject constructor() {

    fun mapFromNetworkToDB(character: Character): CharacterEntity {
        return CharacterEntity(
            id = character.id,
            name = character.name,
            image = character.image,
            species = character.species,
            status = character.status,
            type = character.type,
            created = character.created,
            gender = character.gender,
            locationName = character.location.name,
            locationUrl = character.location.url,
            originName = character.origin.name,
            originUrl = character.origin.url,
            episode = character.episode
        )
    }

    fun mapFromDBToNetwork(character: CharacterEntity?): Character? {
        return character?.let {
            Character(
                id = it.id,
                name = character.name,
                image = character.image,
                species = character.species,
                status = character.status,
                type = character.type,
                created = character.created,
                episode = character.episode,
                gender = character.gender,
                location = Location(character.locationName, character.locationUrl),
                origin = Origin(character.originName, character.originUrl)
            )
        }
    }

    fun mapCharactersFromDBToNetwork(characters: List<CharacterEntity>): List<Character> {
        return characters.map {
            Character(
                id = it.id,
                name = it.name,
                image = it.image,
                species = it.species,
                status = it.status,
                type = it.type,
                created = it.created,
                episode = it.episode,
                gender = it.gender,
                location = Location(it.locationName, it.locationUrl),
                origin = Origin(it.originName, it.originUrl)
            )
        }
    }

}