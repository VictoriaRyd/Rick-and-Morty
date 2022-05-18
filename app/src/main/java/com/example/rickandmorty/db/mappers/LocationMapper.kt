package com.example.rickandmorty.db.mappers

import com.example.rickandmorty.db.entity.LocationEntity
import com.example.rickandmorty.ui.location.Locations
import javax.inject.Inject

class LocationMapper @Inject constructor() {

    fun mapFromNetworkToDB(location: Locations) : LocationEntity {
        return LocationEntity(
            id = location.id,
            name = location.name,
            type = location.type,
            dimension = location.dimension,
            residents = location.residents
        )
    }

    fun mupFromDBToNetwork(location: LocationEntity) : Locations {
        return Locations(
            id = location.id,
            name = location.name,
            type = location.type,
            dimension = location.dimension,
            residents = location.residents
        )
    }

    fun mupLocationsFromDBToNetwork(locations: List<LocationEntity>) : List<Locations> {
        return locations.map {
            Locations(
                id = it.id,
                name = it.name,
                type = it.type,
                dimension = it.dimension,
                residents = it.residents
            )
        }
    }
}