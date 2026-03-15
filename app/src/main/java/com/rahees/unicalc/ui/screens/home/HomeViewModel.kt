package com.rahees.unicalc.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.unicalc.data.local.ConversionDao
import com.rahees.unicalc.data.local.ConversionEntity
import com.rahees.unicalc.data.local.FavoriteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val conversionDao: ConversionDao
) : ViewModel() {

    val recentConversions: StateFlow<List<ConversionEntity>> = conversionDao
        .getRecentConversions(5)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<FavoriteEntity>> = conversionDao
        .getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
