package com.example.rickandmorty.ui.location

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationsFilter (
    var name: String? = null,
    var type: String? = null,
    var dimension: String? = null
): Parcelable