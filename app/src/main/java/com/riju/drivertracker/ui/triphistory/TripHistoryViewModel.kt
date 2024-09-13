package com.riju.drivertracker.ui.triphistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riju.drivertracker.repository.TripHistoryRepository
import com.riju.drivertracker.repository.model.TripDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripHistoryViewModel @Inject constructor(
    private val tripHistoryRepository: TripHistoryRepository
) : ViewModel() {
    private val _tripHistory: MutableStateFlow<List<TripDetails>> = MutableStateFlow(emptyList())
    val tripHistory = _tripHistory.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                _tripHistory.value = tripHistoryRepository.getAllTripHistory() ?: emptyList()
            } catch (e: Exception) {
                _tripHistory.value = emptyList()
            }
        }
    }
}