package com.example.rickandmorty.ui.character

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character (
    val id: Int,
    val name: String,
    val image: String,
    val species: String,
    val status: String,
    val type: String,
    val created: String,
    val episode: List<String>,
    val gender: String,
    val location: Location,
    val origin: Origin
): Parcelable