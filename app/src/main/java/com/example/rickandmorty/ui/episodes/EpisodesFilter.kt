package com.example.rickandmorty.ui.episodes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodesFilter(
    var name: String? = null,
    var episode: String? = null
): Parcelable