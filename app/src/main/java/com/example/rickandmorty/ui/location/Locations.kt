package com.example.rickandmorty.ui.location

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Locations(
        val id: Int,
        val name: String,
        val type: String,
        val dimension: String,
        val residents: List<String>
    ): Parcelable
